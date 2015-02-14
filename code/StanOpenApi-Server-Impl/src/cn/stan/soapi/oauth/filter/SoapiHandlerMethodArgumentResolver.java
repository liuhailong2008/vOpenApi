package cn.stan.soapi.oauth.filter;

import org.apache.oltu.oauth2.as.request.OAuthRequest;
import org.apache.oltu.oauth2.common.message.OAuthResponse;
import org.springframework.core.MethodParameter;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

public class SoapiHandlerMethodArgumentResolver implements HandlerMethodArgumentResolver{

    public boolean supportsParameter(MethodParameter parameter) {
        Class<?> type = parameter.getParameterType();
        if(type==OAuthRequest.class || type==OAuthResponse.class){
            return true;
        }else{
        	return false;
        }
    }
    
    public Object resolveArgument(MethodParameter parameter, 
            ModelAndViewContainer mavContainer, NativeWebRequest request, 
            WebDataBinderFactory factory) throws Exception {
        //Class<?> type = parameter.getParameterType();
        /*
        if(type==OptimusRequest.class){
            OptimusRequest optimusRequest = (OptimusRequest)ThreadLocalManager
                    .get(ThreadLocalManager.OPTIMUS_REQUEST);
            if(optimusRequest==null){
                optimusRequest = new OptimusRequest((HttpServletRequest)request.getNativeRequest());
                ThreadLocalManager.add(ThreadLocalManager.OPTIMUS_REQUEST, optimusRequest);
            }
            return optimusRequest;
        }
        if(type==OptimusResponse.class){
            OptimusResponse optimusResponse = (OptimusResponse)ThreadLocalManager
                    .get(ThreadLocalManager.OPTIMUS_RESPONSE);
            if(optimusResponse==null){
                optimusResponse = new OptimusResponse();
                ThreadLocalManager.add(ThreadLocalManager.OPTIMUS_RESPONSE, optimusResponse);
            }
            return optimusResponse;
        }*/
        return request;
    }

}
