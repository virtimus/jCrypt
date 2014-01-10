package org.jcrypt.ejb;

import javax.ejb.Local;

import org.jcrypt.INetwork;

@Local
public interface WalletLocal extends org.jcrypt.IWallet {
	public void init(INetwork network);
}
