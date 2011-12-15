/*
 * Copyright 2011 NTT Software Corporation.
 * All Rights Reserved.
 */
package jp.co.ntts.vhut.driver;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FilterOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.StringReader;
import java.io.StringWriter;

import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.TransformerFactoryConfigurationError;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;

import jp.co.ntts.vhut.VhutModule;
import jp.co.ntts.vhut.util.VhutLogger;

import org.apache.commons.httpclient.methods.RequestEntity;
import org.apache.commons.io.IOUtils;
import org.apache.xmlrpc.XmlRpcException;
import org.apache.xmlrpc.XmlRpcRequest;
import org.apache.xmlrpc.client.XmlRpcClient;
import org.apache.xmlrpc.client.XmlRpcClientException;
import org.apache.xmlrpc.client.XmlRpcCommonsTransport;
import org.apache.xmlrpc.client.XmlRpcCommonsTransportFactory;
import org.apache.xmlrpc.client.XmlRpcHttpClientConfig;
import org.apache.xmlrpc.client.XmlRpcTransport;
import org.apache.xmlrpc.util.XmlRpcIOException;
import org.xml.sax.SAXException;

/**
 * <p>Apache XML-RPCクライアントにデータロギングさせるための、TransportFactory拡張.
 * <br>
 * <p>ベースとなっているXmlRpcCommonsTransportFactoryはApache XML-RPCの標準的なトランスポートです.
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
public class VhutXmlRpcCommonsTransportFactory extends XmlRpcCommonsTransportFactory {

    /**
     * vhutのLoggerインスタンス
     */
    private static final VhutLogger logger = VhutLogger.getLogger(VhutXmlRpcCommonsTransportFactory.class);

    /**
     * Maximum number of allowed redirects.
     */
    private static final int MAX_REDIRECT_ATTEMPTS = 100;

    private LoggingTransport transport;

    private VhutModule module;


    //    private final Logger logger = LoggerFactory.getLogger(getClass());

    /**
     * コンストラクタ.
     * @param pClient XML-RPC クライアント
     * @param module vHutモジュールタイプ
     */
    public VhutXmlRpcCommonsTransportFactory(XmlRpcClient pClient, VhutModule module) {
        super(pClient);
        this.module = module;
    }

    @Override
    public XmlRpcTransport getTransport() {
        transport = new LoggingTransport(this, module);
        return transport;
    }


    /**
     * <p>Apache XML-RPCクライアントにデータロギングさせるための、Transport拡張.
     * <br>
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
    private class LoggingTransport extends XmlRpcCommonsTransport {

        /**
         * vhutのLoggerインスタンス
         */
        private final VhutLogger logger = VhutLogger.getLogger(LoggingTransport.class);

        private final String parseError = "parse_error";

        private XmlRpcHttpClientConfig config;

        private String transactionId;

        private VhutModule module;

        private int contentLength = -1;


        @Override
        protected void setContentLength(int pLength) {
            super.setContentLength(pLength);
            contentLength = pLength;
        }

        public LoggingTransport(VhutXmlRpcCommonsTransportFactory pFactory, VhutModule module) {
            super(pFactory);
            this.module = module;
        }

        /* (non-Javadoc)
         * @see org.apache.xmlrpc.client.XmlRpcCommonsTransport#initHttpHeaders(org.apache.xmlrpc.XmlRpcRequest)
         */
        @Override
        protected void initHttpHeaders(XmlRpcRequest pRequest) throws XmlRpcClientException {
            super.initHttpHeaders(pRequest);
            transactionId = "undefined";
            if (pRequest.getParameterCount() > 0) {
                Object param = pRequest.getParameter(0);
                if (param != null && param instanceof String) {
                    transactionId = (String) pRequest.getParameter(0);
                }
            }
            config = (XmlRpcHttpClientConfig) pRequest.getConfig();
        }

        /**
         * XmlRpcCommonsTransportがXMLを書き込む処理.
         * リクエスト送信後にログを書き込みます.
         */
        @Override
        protected void writeRequest(final ReqWriter pWriter) throws XmlRpcException {
            try {
                method.setRequestEntity(new RequestEntity() {
                    public boolean isRepeatable() {
                        return true;
                    }

                    public void writeRequest(OutputStream pOut) throws IOException {
                        try {
                            /* Make sure, that the socket is not closed by replacing it with our
                             * own BufferedOutputStream.
                             */
                            OutputStream ostream;
                            if (isUsingByteArrayOutput(config)) {
                                // No need to buffer the output.
                                ostream = new FilterOutputStream(pOut) {
                                    public void close() throws IOException {
                                        flush();
                                    }
                                };
                            } else {
                                ostream = new BufferedOutputStream(pOut) {
                                    public void close() throws IOException {
                                        flush();
                                    }
                                };
                            }
                            pWriter.write(ostream);
                        } catch (XmlRpcException e) {
                            throw new XmlRpcIOException(e);
                        } catch (SAXException e) {
                            throw new XmlRpcIOException(e);
                        }
                    }

                    public long getContentLength() {
                        return contentLength;
                    }

                    public String getContentType() {
                        return "text/xml";
                    }
                });

                // REQUESTログ（正常）書き込み処理
                if (logger.isInfoEnabled()) {
                    ByteArrayOutputStream bos = null;
                    String data = parseError;
                    try {
                        bos = new ByteArrayOutputStream();
                        method.getRequestEntity()
                            .writeRequest(bos);
                        data = toPrettyXml(bos.toString());
                    } catch (IOException e) {
                        //throw new XmlRpcException(e.getMessage(), e);
                        //なにもしない
                    } catch (TransformerFactoryConfigurationError e) {
                        //なにもしない
                    } catch (TransformerException e) {
                        //なにもしない
                    } finally {
                        IOUtils.closeQuietly(bos);
                    }
                    logger.request(transactionId, String.format("I%s1100", module), new Object[]{ config.getServerURL(), data });
                }

                try {
                    int redirectAttempts = 0;
                    for (;;) {
                        client.executeMethod(method);
                        if (!isRedirectRequired()) {
                            break;
                        }
                        if (redirectAttempts++ > MAX_REDIRECT_ATTEMPTS) {
                            throw new XmlRpcException("Too many redirects.");
                        }
                        resetClientForRedirect();
                    }
                } catch (XmlRpcIOException e) {
                    Throwable t = e.getLinkedException();
                    if (t instanceof XmlRpcException) {
                        throw (XmlRpcException) t;
                    } else {
                        throw new XmlRpcException("Unexpected exception: " + t.getMessage(), t);
                    }
                } catch (IOException e) {
                    throw new XmlRpcException("I/O error while communicating with HTTP server: " + e.getMessage(), e);
                }
            } catch (XmlRpcException e) {
                //REQUESTログ（接続失敗）書き出し
                logger.request(transactionId, String.format("E%s1100", module), new Object[]{ config.getServerURL() }, e);
                throw e;
            }
        }

        /**
         * XmlRpcCommonsTransportがサーバからの応答をInputStreamに変換する処理.
         * 変換後に同じデータをログに書き込みます.
         */
        @Override
        protected InputStream getInputStream() throws XmlRpcException {
            InputStream istream = super.getInputStream();
            if (logger.isInfoEnabled()) {
                BufferedReader reader = null;
                String data = parseError;
                try {
                    reader = new BufferedReader(new InputStreamReader(istream));
                    String line = null;
                    StringBuilder respBuf = new StringBuilder();
                    while ((line = reader.readLine()) != null) {
                        respBuf.append(line);
                    }
                    data = toPrettyXml(respBuf.toString());
                } catch (IOException e) {
                    logger.reply(transactionId, String.format("E%s1300", module), new Object[]{}, e);
                    throw new XmlRpcException(e.getMessage(), e);
                } catch (TransformerFactoryConfigurationError e) {
                    // ないもしない
                } catch (TransformerException e) {
                    // なにもしない
                } finally {
                    IOUtils.closeQuietly(reader);
                }
                logger.reply(transactionId, String.format("I%s1200", module), new Object[]{ data });
                return new ByteArrayInputStream(data.getBytes());
            } else {
                return istream;
            }
        }

        /**
         * XMLを成形します.
         * @param xml XMLデータ
         * @return 成形後のXML文字列
         * @throws TransformerFactoryConfigurationError
         * @throws TransformerException
         */
        private String toPrettyXml(String xml) throws TransformerFactoryConfigurationError, TransformerException {
            Transformer transformer = TransformerFactory.newInstance()
                .newTransformer();
            transformer.setOutputProperty(OutputKeys.INDENT, "yes");
            transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "2");
            StreamResult result = new StreamResult(new StringWriter());
            StreamSource source = new StreamSource(new StringReader(xml));
            transformer.transform(source, result);
            return result.getWriter()
                .toString();
        }

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
