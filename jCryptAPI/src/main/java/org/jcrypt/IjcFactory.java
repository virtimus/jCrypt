package org.jcrypt;

import java.io.File;
import java.math.BigInteger;

public interface IjcFactory {
	
	public INetwork newNetwork(NetworkTypeEnum nType, File directory);
	
	public IExecutor getExecutor(String name);
	
	public IjcUtils getUtils();
	
	//public IMoreExecutors getMoreExecutors();
	
}
