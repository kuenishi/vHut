/*
 * Copyright 2011 NTT Software Corporation.
 * All Rights Reserved.
 */
package jp.co.ntts.vhut.interceptor;

import javax.persistence.NoResultException;

import jp.co.ntts.vhut.exception.AbstractVhutException;
import jp.co.ntts.vhut.exception.AbstractVhutRuntimeException;
import jp.co.ntts.vhut.exception.ConfigRuntimeException;
import jp.co.ntts.vhut.exception.DBNoRecordRuntimeException;
import jp.co.ntts.vhut.exception.DBStateRuntimeException;
import jp.co.ntts.vhut.exception.DBStillReferencedRuntimeException;
import jp.co.ntts.vhut.exception.InputRuntimeException;
import jp.co.ntts.vhut.exception.InternalRuntimeException;
import jp.co.ntts.vhut.exception.NotJoinedRuntimeException;

import org.aopalliance.intercept.MethodInvocation;
import org.seasar.framework.aop.interceptors.ThrowsInterceptor;
import org.seasar.framework.exception.SRuntimeException;

/**
 * vHutの共通エラー処理を行うインターセプター.
 * Service, Logic, Commandの処理をインターセプトし、
 * 未分類のRuntimeExceptionを下記のvHutのRuntimeExceptionに変換します.
 * <ul>
 * <li> Null
 * </ul>
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
public class VhutExceptionInterceptor extends ThrowsInterceptor {

    /**
     * シリアル.
     */
    private static final long serialVersionUID = 6453706882225593793L;

    /**
     * モジュール名
     */
    public String moduleName;


    /**
     * ConfigRuntimeException.
     * @param e 例外
     * @param invocation 実行
     */
    public void handleThrowable(ConfigRuntimeException e, MethodInvocation invocation) {
        updateMessageCode(e, "1000");
        throw e;
    }

    /**
     * DBStateRuntimeException.
     * @param e 例外
     * @param invocation 実行
     */
    public void handleThrowable(DBStateRuntimeException e, MethodInvocation invocation) {
        updateMessageCode(e, "2001");
        throw e;
    }

    /**
     * DBNoRecordRuntimeException.
     * @param e 例外
     * @param invocation 実行
     */
    public void handleThrowable(DBNoRecordRuntimeException e, MethodInvocation invocation) {
        updateMessageCode(e, "2002");
        throw e;
    }

    /**
     * NoResultException.
     * @param e 例外
     * @param invocation 実行
     */
    public void handleThrowable(NoResultException e, MethodInvocation invocation) {
        throw new DBNoRecordRuntimeException(getMessageCode("2002"), e);
    }

    /**
     * DBStillReferencedRuntimeException.
     * @param e 例外
     * @param invocation 実行
     */
    public void handleThrowable(DBStillReferencedRuntimeException e, MethodInvocation invocation) {
        updateMessageCode(e, "2003");
        throw e;
    }

    /**
     * InputRuntimeException.
     * @param e 例外
     * @param invocation 実行
     */
    public void handleThrowable(InputRuntimeException e, MethodInvocation invocation) {
        updateMessageCode(e, "3000");
        throw e;
    }

    /**
     * InternalRuntimeException.
     * @param e 例外
     * @param invocation 実行
     */
    public void handleThrowable(InternalRuntimeException e, MethodInvocation invocation) {
        updateMessageCode(e, "4000");
        throw e;
    }

    /**
     * NotJoinedRuntimeException.
     * @param e 例外
     * @param invocation 実行
     */
    public void handleThrowable(NotJoinedRuntimeException e, MethodInvocation invocation) {
        updateMessageCode(e, "4001");
        throw e;
    }

    /**
     * @see org.aopalliance.intercept.MethodInterceptor#invoke(org.aopalliance.intercept.MethodInvocation)
     */
    public Object invoke(MethodInvocation invocation) throws Throwable {
        try {
            return super.invoke(invocation);
        } catch (AbstractVhutException e) {
            //vHut系の検査例外
            throw e;
        } catch (AbstractVhutRuntimeException e) {
            //vHut系の非検査例外
            throw e;
        } catch (SRuntimeException e) {
            //Seasar系の非検査例外
            throw new InternalRuntimeException(getMessageCode("6000"), e);
        } catch (RuntimeException e) {
            //その他の非検査例外
            throw new InternalRuntimeException(getMessageCode("5000"), e);
        }
    }

    /**
     * @param e
     * @param number
     */
    private void updateMessageCode(AbstractVhutRuntimeException e, String number) {
        if (e.getMessageCode() == null) {
            e.setMessageCode(getMessageCode(number));
        }
    }

    private String getMessageCode(String number) {
        return "E" + moduleName + number.toString();
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
