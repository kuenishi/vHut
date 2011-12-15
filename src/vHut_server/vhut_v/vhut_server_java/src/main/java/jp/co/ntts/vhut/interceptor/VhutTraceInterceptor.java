/*
 * Copyright 2011 NTT Software Corporation.
 * All Rights Reserved.
 */
package jp.co.ntts.vhut.interceptor;

import jp.co.ntts.vhut.util.VhutLogger;

import org.aopalliance.intercept.MethodInvocation;
import org.seasar.framework.aop.interceptors.TraceInterceptor;

/**
 * <p>vHut仕様のトレースログを出力するインターセプター.
 * <br>
 * <p>{@link VhutLogger} を用いてトランザクションの開始と終了を出力する。
 * 例外がある場合は、スタックトレースを出力する。
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
public class VhutTraceInterceptor extends TraceInterceptor {

    /**
     * デフォルトシリアル.
     */
    private static final long serialVersionUID = 1L;

    private static final VhutLogger logger = VhutLogger.getLogger(VhutTraceInterceptor.class);

    /**
     * 開始ログ（正常）のメッセージコード.
     */
    private String startInfoMessageCode = "ISRVS0011";
    /**
     * 終了ログ（正常）のメッセージコード.
     */
    private String endInfoMessageCode = "ISRVS0012";
    /**
     * 終了ログ（エラー）のメッセージコード.
     */
    private String endErrorMessageCode = "ESRVS0012";


    /**
     * @param startInfoMessageCode the startInfoMessageCode to set
     */
    public void setStartInfoMessageCode(String startInfoMessageCode) {
        this.startInfoMessageCode = startInfoMessageCode;
    }

    /**
     * @return the startInfoMessageCode
     */
    public String getStartInfoMessageCode() {
        return startInfoMessageCode;
    }

    /**
     * @param endInfoMessageCode the endInfoMessageCode to set
     */
    public void setEndInfoMessageCode(String endInfoMessageCode) {
        this.endInfoMessageCode = endInfoMessageCode;
    }

    /**
     * @return the endInfoMessageCode
     */
    public String getEndInfoMessageCode() {
        return endInfoMessageCode;
    }

    /**
     * @param endErrorMessageCode the endErrorMessageCode to set
     */
    public void setEndErrorMessageCode(String endErrorMessageCode) {
        this.endErrorMessageCode = endErrorMessageCode;
    }

    /**
     * @return the endErrorMessageCode
     */
    public String getEndErrorMessageCode() {
        return endErrorMessageCode;
    }

    /* (non-Javadoc)
     * @see org.aopalliance.intercept.MethodInterceptor#invoke(org.aopalliance.intercept.MethodInvocation)
     */
    @Override
    public Object invoke(MethodInvocation invocation) throws Throwable {
        if (!logger.isInfoEnabled()) {
            return invocation.proceed();
        }
        String transactionId = VhutLogger.createTransactionId();

        final StringBuffer buf = new StringBuffer(100);
        buf.append(getTargetClass(invocation).getName());
        buf.append("#")
            .append(invocation.getMethod()
                .getName())
            .append("(");
        final Object[] args = invocation.getArguments();
        if (args != null && args.length > 0) {
            for (int i = 0; i < args.length; ++i) {
                appendObject(buf, args[i]).append(", ");
            }
            buf.setLength(buf.length() - 2);
        }
        buf.append(")");
        Object ret = null;
        Throwable cause = null;
        //        logger.debug("BEGIN " + buf);
        logger.start(transactionId, startInfoMessageCode, new Object[]{ buf.toString() });
        try {
            ret = invocation.proceed();
            buf.append(" : ");
            appendObject(buf, ret);
        } catch (final Throwable t) {
            //            buf.append(" Throwable:").append(t);
            cause = t;
        }
        //        logger.debug("END " + buf);
        if (cause == null) {
            logger.end(transactionId, endInfoMessageCode, new Object[]{ buf.toString() });
            return ret;
        } else {
            logger.end(transactionId, endErrorMessageCode, new Object[]{ buf.toString() }, cause);
        }
        throw cause;
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
