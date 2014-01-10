package org.jcrypt.ejb;

//import  com.google.common.util.concurrent.ListeningExecutorService;
import com.google.common.util.concurrent.ListeningExecutorService;

import org.jcrypt.IExecutor;

public class Executor implements IExecutor {
	
	private ListeningExecutorService nestedExecutor = null;
	
	public Executor(ListeningExecutorService executor){
		this.nestedExecutor = executor;
	}

}
