package org.jcrypt;

import java.math.BigInteger;


public interface ITransaction {
	public BigInteger getValueSentToMe(IWallet w);
	public ITransactionConfidence getConfidence(); 
	public String getHashAsString();
}
