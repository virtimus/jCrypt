package org.jcrypt;

import java.math.BigInteger;
import java.util.List;


public interface IWallet {
	
	public void addEventListener(IWalletEventListener listener);
	public List<IECKey> getKeys();	
	
	public class SendResult {
        /** The coin transaction message that moves the money. */
        public ITransaction tx;
        /** A future that will complete once the tx message has been successfully broadcast to the network. */
        public IListenableFuture<ITransaction> broadcastComplete;		
	}

    public SendResult sendCoins(IPeerGroup broadcaster, IAddress to, BigInteger value) throws InsufficientMoneyException;

}
