package org.jcrypt;

import java.io.File;
import java.math.BigInteger;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.jcrypt.INetwork;
import org.jcrypt.NetworkTypeEnum;
import org.jcrypt.ejb.NetworkLocal;
import org.jcrypt.ejb.MoreExecutorsLocal;



public class jcFactoryImpl implements IjcFactory {
	private static final Log log = LogFactory.getLog(jcFactory.class);
	
	private static jcUtils jcUtils = null;
	
	private static Object doLocalLookup(String context, String name) throws NamingException {
		InitialContext iniCtx = new InitialContext();
		Context ejbCtx = (Context) iniCtx.lookup(context);
		return ejbCtx.lookup(name);		
	}	
	
	private static Object lookupLocal(String context,String name, boolean silent) {
		log.debug("[lookupLocal] context:"+context+", name:"+name);
		Object ref = null;
		try{
			ref = doLocalLookup(context,name);
		} catch (NamingException x){
			if (!silent){
				x.printStackTrace();
			}
		}
		return ref;
	}	
	
	public static Object lookupLocal(String shortName){
		String context = "java:app/jCryptEJB-1.0-SNAPSHOT/";
		String name = shortName+"Bean!org.jcrypt.ejb."+shortName+"Local";
		return lookupLocal(context, name, false);
	}	
	
	@Override
	public INetwork newNetwork(NetworkTypeEnum nType, File directory){
		NetworkLocal nLoc = (NetworkLocal)lookupLocal("Network");
		nLoc.init(nType,directory);
		return (INetwork)nLoc;
	}

	@Override
	public IExecutor getExecutor(String name){
		//TODO static init on arraylist/map by name?
		MoreExecutorsLocal meLoc = (MoreExecutorsLocal)lookupLocal("MoreExecutors");
		//meLoc.init(this);
		return (IExecutor)meLoc.getExecutor(name);
	}
	
	@Override
	public IjcUtils getUtils(){
		if (jcUtils == null){
			jcUtils = new jcUtils();
		}
		return jcUtils;
	}
	

}
