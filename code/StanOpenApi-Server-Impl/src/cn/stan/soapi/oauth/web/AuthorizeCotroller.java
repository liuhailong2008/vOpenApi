package cn.stan.soapi.oauth.web;

import java.io.IOException;
import java.io.UnsupportedEncodingException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.apache.oltu.oauth2.as.issuer.MD5Generator;
import org.apache.oltu.oauth2.as.issuer.OAuthIssuerImpl;
import org.apache.oltu.oauth2.as.request.OAuthAuthzRequest;
import org.apache.oltu.oauth2.as.response.OAuthASResponse;
import org.apache.oltu.oauth2.common.OAuth;
import org.apache.oltu.oauth2.common.exception.OAuthProblemException;
import org.apache.oltu.oauth2.common.exception.OAuthSystemException;
import org.apache.oltu.oauth2.common.message.OAuthResponse;
import org.apache.oltu.oauth2.common.message.types.ResponseType;
import org.apache.oltu.oauth2.common.utils.OAuthUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/api/oauth2")
public class AuthorizeCotroller {
	
	Logger logger = Logger.getLogger(this.getClass());
	//@Autowired
	//private ComRegistService comRegistService;
    
	/**
	 * http://localhost:8080/soapi/api/oauth2/authorize
	 * @throws IOException 
	 * @throws OAuthSystemException 
	 */
	@RequestMapping("/authorize")
	public void authorize(HttpServletRequest request,HttpServletResponse response) 
			throws OAuthProblemException, IOException, OAuthSystemException{
		logger.debug("BEGIN authorize ... ");
		
		OAuthAuthzRequest oauthRequest = null;

        OAuthIssuerImpl oauthIssuerImpl = new OAuthIssuerImpl(new MD5Generator());

        try {
            oauthRequest = new OAuthAuthzRequest(request);
            
            String redirectURI = oauthRequest.getParam(OAuth.OAUTH_REDIRECT_URI);
            
            if (OAuthUtils.isEmpty(redirectURI)) {
                String msg = "OAuth callback url needs to be provided by client.";
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                response.getOutputStream().write(msg.getBytes("UTF-8"));
                return ;
            }
            

            //build response according to response_type
            String responseType = oauthRequest.getParam(OAuth.OAUTH_RESPONSE_TYPE);

            OAuthASResponse.OAuthAuthorizationResponseBuilder builder = OAuthASResponse
                .authorizationResponse(request,HttpServletResponse.SC_FOUND);//302

            if (ResponseType.CODE.toString().equals(responseType)) {
                builder.setCode(oauthIssuerImpl.authorizationCode());
            }
            
            if (ResponseType.TOKEN.toString().equals(responseType)) {
                builder.setAccessToken(oauthIssuerImpl.accessToken());
                builder.setExpiresIn(3600l);
            }

            final OAuthResponse oAuthResponse = builder.location(redirectURI).buildQueryMessage();
            
            buildHttpServletResponse(response,oAuthResponse);
        	return ;

        } catch (OAuthProblemException e) {
        	// 返回错误信息
            String redirectUri = e.getRedirectUri();

            if (OAuthUtils.isEmpty(redirectUri)) {
                String msg = "OAuth callback url needs to be provided by client.";
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                response.getOutputStream().write(msg.getBytes("UTF-8"));
                return ;
            }
			
			final OAuthResponse oAuthResponse = 
					OAuthASResponse.errorResponse(HttpServletResponse.SC_FOUND)
			    .error(e)
			    .location(redirectUri).buildQueryMessage();
            buildHttpServletResponse(response,oAuthResponse);
            return ;	
        }
	}

	protected static void buildHttpServletResponse(HttpServletResponse httpServletResponse,OAuthResponse oAuthResponse) throws UnsupportedEncodingException, IOException{
		httpServletResponse.setStatus(oAuthResponse.getResponseStatus());
		httpServletResponse.setHeader("location", oAuthResponse.getLocationUri());
	}
}
