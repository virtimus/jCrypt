package org.jcrypt;

public interface INetwork {

	public NetworkTypeEnum getNetworkType();
	
	public IAddress newAddress(String adr);
	public void connectToLocalHost();
	public void startAndWait() throws Exception;
	public void stopAndWait() throws Exception;	
	public IWallet newWallet();
	public INetworkParams getParams();
	public IPeerGroup getPeerGroup();
	
	
}
