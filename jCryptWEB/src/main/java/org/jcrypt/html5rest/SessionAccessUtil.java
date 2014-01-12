package org.jcrypt.html5rest;

import javax.servlet.http.HttpSession;
import java.security.Principal;


public class SessionAccessUtil {

	private static final ThreadLocal<HttpSession> threadLocal = new ThreadLocal<HttpSession>();
	private static final ThreadLocal<Principal> principal = new ThreadLocal<Principal>();;
	private static boolean serviceMode = false;
	
	public static void setSession(HttpSession session) {
		threadLocal.set(session);
	}
	
	public static HttpSession getSession() {
		return threadLocal.get();
	}
	
	public static Principal getPrincipal(){
		return principal.get();
	}

	public static void setPrincipal(Principal p){
		principal.set(p);
	} 
	
	public static boolean getServiceMode(){
		String sv = null;
		if (getSession()!=null)
			sv = ((String)getSession().getAttribute("org.jcrypt.ServiceMode"));
		if (sv!=null)
			return sv.equals("1");
		else
			return false;
	}
	
	public static void setServiceMode(boolean n){
		getSession().setAttribute("org.jcrypt.ServiceMode",(n)?"1":"0");
	}
	
}
