package cn.stan.soapi.oauth.web;

import java.io.IOException;
import java.io.UnsupportedEncodingException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.apache.oltu.oauth2.as.issuer.MD5Generator;
import org.apache.oltu.oauth2.as.issuer.OAuthIssuer;
import org.apache.oltu.oauth2.as.issuer.OAuthIssuerImpl;
import org.apache.oltu.oauth2.as.request.OAuthTokenRequest;
import org.apache.oltu.oauth2.as.response.OAuthASResponse;
import org.apache.oltu.oauth2.common.OAuth;
import org.apache.oltu.oauth2.common.error.OAuthError;
import org.apache.oltu.oauth2.common.exception.OAuthProblemException;
import org.apache.oltu.oauth2.common.exception.OAuthSystemException;
import org.apache.oltu.oauth2.common.message.OAuthResponse;
import org.apache.oltu.oauth2.common.message.types.GrantType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import cn.stan.soapi.oauth.model.TokenValidateBo;
import cn.stan.soapi.oauth.service.TokenService;

@Controller
@RequestMapping("/api/oauth2")
public class TokenCotroller {
	
	public static final String INVALID_CLIENT_DESCRIPTION = "Client authentication failed (e.g., unknown client, no client authentication included, or unsupported authentication method).";
	
	Logger logger = Logger.getLogger(this.getClass());
	@Autowired
	private TokenService tokenService;
    
	@RequestMapping("/access_token")
	public void token(HttpServletRequest request,HttpServletResponse response) 
			throws OAuthProblemException, OAuthSystemException, UnsupportedEncodingException, IOException{
		logger.debug("BEGIN token ... ");
		
		OAuthTokenRequest oauthRequest  = null;
		OAuthResponse 	  oAuthResponse = null;

        OAuthIssuer oauthIssuerImpl = new OAuthIssuerImpl(new MD5Generator());

        try {
            oauthRequest = new OAuthTokenRequest(request);
            TokenValidateBo bo = this.tokenService.getTokenValidateBo(oauthRequest.getClientId());
            
            // 1.1. 查验client id是否合法
            if (bo==null) {
            	oAuthResponse =
                    OAuthASResponse.errorResponse(HttpServletResponse.SC_BAD_REQUEST)
                        .setError(OAuthError.TokenResponse.INVALID_CLIENT)
                        .setErrorDescription(INVALID_CLIENT_DESCRIPTION)
                        .buildJSONMessage();
                buildHttpServletResponse(response,oAuthResponse);
                return ;
            }

            // 1.2. 查验client secret是否合法
            if (!bo.validateClientSecret(oauthRequest.getClientSecret())) {
            	oAuthResponse =
                    OAuthASResponse.errorResponse(HttpServletResponse.SC_UNAUTHORIZED)
                        .setError(OAuthError.TokenResponse.UNAUTHORIZED_CLIENT)
                        .setErrorDescription(INVALID_CLIENT_DESCRIPTION)
                        .buildJSONMessage();
            	buildHttpServletResponse(response,oAuthResponse);
                return ;
            }

            // 2. 依据不同授权模式进行校验
            String grantType = oauthRequest.getParam(OAuth.OAUTH_GRANT_TYPE);
            // 暂时支持两种：
            // 2.1. 授权码模式
            if (GrantType.AUTHORIZATION_CODE.toString().equals(grantType)) {
            	
                if (!bo.validateAuthCode(oauthRequest.getParam(OAuth.OAUTH_CODE))) {
                	oAuthResponse =
                	   OAuthASResponse
                        .errorResponse(HttpServletResponse.SC_BAD_REQUEST)
                        .setError(OAuthError.TokenResponse.INVALID_GRANT)
                        .setErrorDescription("invalid authorization code")
                        .buildJSONMessage();
                	buildHttpServletResponse(response,oAuthResponse);
                	return ;
                }
            }
            // 2.2. 密码模式
            else if (GrantType.PASSWORD.toString().equals(grantType)) {
            	
                if (!bo.validateUsernameAndPassword(oauthRequest.getUsername(),
                		oauthRequest.getPassword())) {
                	oAuthResponse = OAuthASResponse
                        .errorResponse(HttpServletResponse.SC_BAD_REQUEST)
                        .setError(OAuthError.TokenResponse.INVALID_GRANT)
                        .setErrorDescription("invalid username or password")
                        .buildJSONMessage();
                	buildHttpServletResponse(response,oAuthResponse);
                	return ;
                }
            } 

            // 3. 发放令牌 access_token
            oAuthResponse = OAuthASResponse
                .tokenResponse(HttpServletResponse.SC_OK)
                .setAccessToken(oauthIssuerImpl.accessToken())
                .setExpiresIn("3600") // TODO expires_in改为在配置文件中配置
                .buildJSONMessage();
            buildHttpServletResponse(response,oAuthResponse);
        	return ;

        } catch (OAuthProblemException e) {
        	oAuthResponse = OAuthASResponse.errorResponse(HttpServletResponse.SC_BAD_REQUEST).error(e)
                .buildJSONMessage();
            buildHttpServletResponse(response,oAuthResponse);
        	return ;
        }
	}
	
	protected static void buildHttpServletResponse(HttpServletResponse httpServletResponse,OAuthResponse oAuthResponse) throws UnsupportedEncodingException, IOException{
		httpServletResponse.setStatus(oAuthResponse.getResponseStatus());
		httpServletResponse.getOutputStream().write(oAuthResponse.getBody().getBytes("UTF-8"));
	}
}
