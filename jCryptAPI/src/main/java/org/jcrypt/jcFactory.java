package org.jcrypt;

import java.io.File;

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
		return null;
	}

	public static INetwork newNetwork(NetworkTypeEnum nType, File directory){
		return getImpl().newNetwork(nType, directory);
	}
	
}
