package org.jcrypt.ejb;

import javax.ejb.Stateless;

import java.math.BigInteger;
import java.util.List;
import java.util.ArrayList;



import org.jcrypt.IAddress;
import org.jcrypt.IECKey;
import org.jcrypt.INetwork;
import org.jcrypt.IPeerGroup;
import org.jcrypt.IWalletEventListener;
import org.jcrypt.InsufficientMoneyException;

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

	@Override
	public List<IECKey> getKeys() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public SendResult sendCoins(IPeerGroup broadcaster, IAddress to,
			BigInteger value) throws InsufficientMoneyException {
		// TODO Auto-generated method stub
		return null;
	}
}
