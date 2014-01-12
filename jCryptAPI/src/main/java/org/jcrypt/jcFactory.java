package org.jcrypt;

import java.io.File;
import java.lang.reflect.Constructor;

public abstract class jcFactory /*implements IjcFactory*/ {
	
	private static IjcFactory factoryImpl = null;
	
	private static IjcFactory getImpl(){
		if (factoryImpl == null){
			factoryImpl = buildFactory();
		}
		return factoryImpl;
	}
	
	private static IjcFactory buildFactory() {
		// TODO Auto-generated method stub
		// should default to jcFactoryImpl instance - but dynamically loaded
		Object jaxbObject = null;
		try {
			Class jaxbObjectClass = null;
			jaxbObjectClass = Class.forName("org.jcrypt.jcFactoryImpl");
			@SuppressWarnings({ "rawtypes", "unchecked" })
			Constructor constructor = jaxbObjectClass.getConstructor();
			jaxbObject = constructor.newInstance();
		} catch (Exception ex) {
			ex.printStackTrace();
			//handleJBONotPresent("[loadJAXBObject] Exception: "+ ex.getMessage(),ex);
		}		
		
		return (IjcFactory)jaxbObject;
	}

	public static INetwork newNetwork(NetworkTypeEnum nType, File directory){
		return getImpl().newNetwork(nType, directory);
	}
	
	public static IjcUtils getUtils(){
		return getImpl().getUtils();
	}
	
	public static IExecutor getExecutor(String name){
		return getImpl().getExecutor(name);
	}
	
}
