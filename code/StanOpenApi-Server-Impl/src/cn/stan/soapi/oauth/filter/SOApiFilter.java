
package cn.stan.soapi.oauth.filter;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

import org.apache.log4j.Logger;


/**
 * 分布式Session过滤器抽象实现类
 */
public class SOApiFilter implements Filter {
	
	Logger logger = Logger.getLogger(this.getClass());
    
    private String encoding = "UTF-8"; 

    /**
     * 初始化
     */
    public void init(FilterConfig filterConfig) throws ServletException {
        this.encoding = filterConfig.getInitParameter("encoding"); 
        logger.info("Stan Open Api Filter starting ... ");
    }
    
    /**
    *
    */
    public void doFilter(ServletRequest request, ServletResponse response,
            FilterChain chain) throws IOException, ServletException {
        if (this.encoding != null){
            request.setCharacterEncoding(this.encoding);
        }
        if(logger.isDebugEnabled()){
        	logger.debug("Stan Open Api do filter ... ");
        	logger.debug(
        			String.format("remote addr:%s,remoteHost:%s,remote port:%s;local addr:%s,local name:%s,local port:%s,path:%s", 
        			request.getRemoteAddr(),request.getRemoteHost(),request.getRemotePort(),
        			request.getLocalAddr(),request.getLocalName(),request.getLocalPort(),
        			request.getServletContext().getContextPath()
        					));
        }
       // HttpServletRequest req = (HttpServletRequest) request;
        //HttpServletResponse res = (HttpServletResponse) response;
       
       // request = new ContainerRequestWrapper(request, response, sessionManager);
        //    chain.doFilter(request, response);
        
    }

   /**
    * 销毁
    */
   public void destroy() {
	   logger.info("Stan Open Api Filter be destroyed . ");
   }
   
}
