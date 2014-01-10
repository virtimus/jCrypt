package org.jcrypt.ejb;

import javax.ejb.Stateless;

import java.util.List;
import java.util.ArrayList;


import org.jcrypt.INetwork;
import org.jcrypt.IWalletEventListener;

@Stateless
public class WalletBean implements WalletLocal {
	
	private List<IWalletEventListener> listeners = new ArrayList<IWalletEventListener>();

	private INetwork network = null;

	public void init(INetwork network){
		this.network = network;
		//empty initialisation ?
		//TODO meber wallet
	}
	
	public void addEventListener(IWalletEventListener listener){
		listeners.add(listener);
	}
}
