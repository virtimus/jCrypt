package org.jcrypt.html5rest;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
//import javax.servlet.http.HttpServletResponse;
import org.apache.commons.logging.Log;

/**
 * Implementation of interface {@link javax.servlet.Filter} handling http actions on web container level
 * 
 * @see javax.servlet.Filter
 */
public class SessionAccessFilter implements Filter {

    private static final Log log = org.apache.commons.logging.LogFactory.getLog(SessionAccessFilter.class);

    /**
     * Required init method {@link javax.servlet.Filter}
     *
     * @see javax.servlet.Filter#init(javax.servlet.FilterConfig)
     *
     * @param arg0 FilterConfig 
     * @throws ServletException
     */
    public void init(FilterConfig arg0) throws ServletException {
    }

    /**
     * Required destroy method {@link javax.servlet.Filter}
     *
     * @see javax.servlet.Filter#destroy()
     */
    public void destroy() {
    }

    /**
     * Required doFilter method {@link javax.servlet.Filter}
     *
     * @see javax.servlet.Filter#doFilter(javax.servlet.ServletRequest, javax.servlet.ServletResponse, javax.servlet.FilterChain)
     *
     * @param request ServletRequest
     * @param response ServletResponse
     * @param chain FilterChain
     * @throws java.io.IOException
     * @throws javax.servlet.ServletException
     */
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        if (request instanceof HttpServletRequest) {
            if (((HttpServletRequest) request).isRequestedSessionIdValid()) {
                SessionAccessUtil.setSession(((HttpServletRequest) request).getSession());
            } else {
                SessionAccessUtil.setSession(null);
            }
        }

        /* set Principal used then for accesing EJB components */
        if (request instanceof HttpServletRequest &&
                (SessionAccessUtil.getPrincipal() != null)) {
        	SessionAccessUtil.setPrincipal(((HttpServletRequest) request).getUserPrincipal());
        }
        chain.doFilter(request, response);

    }
}
