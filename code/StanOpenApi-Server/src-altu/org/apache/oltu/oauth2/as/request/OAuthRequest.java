/**
 *       Copyright 2010 Newcastle University
 *
 *          http://research.ncl.ac.uk/smart/
 *
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.apache.oltu.oauth2.as.request;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;

import org.apache.oltu.oauth2.common.OAuth;
import org.apache.oltu.oauth2.common.exception.OAuthProblemException;
import org.apache.oltu.oauth2.common.exception.OAuthSystemException;
import org.apache.oltu.oauth2.common.utils.OAuthUtils;
import org.apache.oltu.oauth2.common.validators.OAuthValidator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * The Abstract OAuth request for the Authorization server.
 */
public abstract class OAuthRequest {

    private Logger log = LoggerFactory.getLogger(OAuthRequest.class);

    protected HttpServletRequest request;
    /**
     * 从validators中取得实例。
     */
    protected OAuthValidator<HttpServletRequest> validator;
    /**
     * Key为授权类型。
     */
    protected Map<String, Class<? extends OAuthValidator<HttpServletRequest>>> validators =
        new HashMap<String, Class<? extends OAuthValidator<HttpServletRequest>>>();
	/**
	 * 转化之后，验证。
	 * @param request
	 * @throws OAuthSystemException
	 * @throws OAuthProblemException
	 */
    public OAuthRequest(HttpServletRequest request) throws OAuthSystemException, OAuthProblemException {
        this.request = request;
        validate();
    }

    public OAuthRequest() {
    }
    /**
     * 
     * @throws OAuthSystemException
     * @throws OAuthProblemException 验证失败，设定了redirectUri，接下来，需要返回错误消息
     */
    protected void validate() throws OAuthSystemException, OAuthProblemException {
        try {
            validator = initValidator();//抽象方法，留给子类实现
            validator.validateMethod(request);
            validator.validateContentType(request);
            validator.validateRequiredParameters(request);
            validator.validateClientAuthenticationCredentials(request);
        } catch (OAuthProblemException e) {
            try {
                String redirectUri = request.getParameter(OAuth.OAUTH_REDIRECT_URI);
                if (!OAuthUtils.isEmpty(redirectUri)) {
                    e.setRedirectUri(redirectUri);
                }
            } catch (Exception ex) {
//                if (log.isDebugEnabled()) {
//                    log.debug("Cannot read redirect_url from the request: {}", new String[] {ex.getMessage()});
//                }
                if (log.isErrorEnabled()) {
                    log.error("Cannot read redirect_url from the request: {}", new String[] {ex.getMessage()});
                }
            }

            throw e;
        }

    }

    protected abstract OAuthValidator<HttpServletRequest> initValidator() throws OAuthProblemException,
        OAuthSystemException;

    public String getParam(String name) {
        return request.getParameter(name);
    }

    public String getClientId() {
        String[] creds = OAuthUtils.decodeClientAuthenticationHeader(request.getHeader(OAuth.HeaderType.AUTHORIZATION));
        if (creds != null) {
            return creds[0];
        }
        return getParam(OAuth.OAUTH_CLIENT_ID);
    }

    public String getRedirectURI() {
        return getParam(OAuth.OAUTH_REDIRECT_URI);
    }
    /**
     * 对Header中authorization Base64解压缩后，得到密码（Basic base64(client_secret:this_is_secret)）。
     * @return
     */
    public String getClientSecret() {
        String[] creds = OAuthUtils.decodeClientAuthenticationHeader(request.getHeader(OAuth.HeaderType.AUTHORIZATION));
        if (creds != null) {
            return creds[1];
        }
        return getParam(OAuth.OAUTH_CLIENT_SECRET);
    }

    /**
     * 对Header中authorization Base64解压缩后，字符串中包含“:”，则返回true。
     * @return
     */
    public boolean isClientAuthHeaderUsed() {
        return OAuthUtils.decodeClientAuthenticationHeader(request.getHeader(OAuth.HeaderType.AUTHORIZATION)) != null;
    }
    /**
     * 多个Scope是空格分开的。
     * @return
     */
    public Set<String> getScopes() {
        String scopes = getParam(OAuth.OAUTH_SCOPE);
        return OAuthUtils.decodeScopes(scopes);
    }

}
