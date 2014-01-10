package org.jcrypt.ejb;

import javax.ejb.Stateless;

import org.jcrypt.IExecutor;
import org.jcrypt.INetwork;

import com.google.common.util.concurrent.MoreExecutors;
import com.google.common.util.concurrent.ListeningExecutorService;

@Stateless
public class MoreExecutorsBean implements MoreExecutorsLocal {

	private INetwork network = null;
	
	public void init(INetwork network){
		this.network = network;
		//empty initialisation ?
		//TODO meber wallet
	}	
	
	public IExecutor getExecutor(String name){
		if ("sameThread".equals(name)){
			return new org.jcrypt.ejb.Executor(MoreExecutors.sameThreadExecutor());
		}
		return null;
	}

	
}
