/*
 * Copyright 2011 NTT Software Corporation.
 * All Rights Reserved.
 */
package jp.co.ntts.vhut.interceptor;

import java.lang.annotation.Annotation;
import java.util.Map;

import jp.co.ntts.vhut.annotation.Auth;
import jp.co.ntts.vhut.dto.VhutUserDto;
import jp.co.ntts.vhut.entity.Right;
import jp.co.ntts.vhut.exception.AuthorizationException;
import jp.co.ntts.vhut.util.VhutLogger;

import org.aopalliance.intercept.MethodInvocation;
import org.seasar.framework.aop.interceptors.AbstractInterceptor;
import org.seasar.framework.container.SingletonS2Container;
import org.seasar.framework.util.tiger.AnnotationUtil;

/**
 * 認証情報を付与・検証するインターセプター
 *
 * @version 1.0.0
 * @author NTT Software Corporation.
 *
 * <!--
 * $Date: 2011-11-28 19:50:40 +0900 (月, 28 11 2011) $
 * $Revision: 949 $
 * $Author: NTT Software Corporation. $
 * -->
 */
public class AuthenticationInterceptor extends AbstractInterceptor {

    private static final VhutLogger logger = VhutLogger.getLogger(AbstractInterceptor.class);

    /**
     * シリアル.
     */
    private static final long serialVersionUID = 9185739220619816289L;


    /* (non-Javadoc)
     * @see org.aopalliance.intercept.MethodInterceptor#invoke(org.aopalliance.intercept.MethodInvocation)
     */
    @Override
    public Object invoke(MethodInvocation invocation) throws Throwable {
        logger.debug("intercepted");
        Annotation[] annotations = invocation.getMethod()
            .getAnnotations();
        for (Annotation annotation : annotations) {
            if (annotation.annotationType()
                .getName()
                .equals(Auth.class.getName())) {
                logger.debug("find Auth annotation");
                VhutUserDto vhutUserDto = SingletonS2Container.getComponent(VhutUserDto.class);
                Map<String, Object> props = AnnotationUtil.getProperties(annotation);
                Right right = (Right) props.get("right");
                if (vhutUserDto.isAuthorized(right)) {
                    return invocation.proceed();
                } else {
                    Exception e = new AuthorizationException(vhutUserDto.getAccount(), right.name());
                    logger.log(e);
                    throw e;
                }
            } else {
                return invocation.proceed();
            }
        }
        return invocation.proceed();
    }

}


/**
 * =====================================================================
 * 
 *    Copyright 2011 NTT Sofware Corporation
 * 
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 * 
 *        http://www.apache.org/licenses/LICENSE-2.0
 * 
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 * 
 * =====================================================================
 */
