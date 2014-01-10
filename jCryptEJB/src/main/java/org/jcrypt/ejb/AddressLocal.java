package org.jcrypt.ejb;

import org.jcrypt.INetwork;

public interface AddressLocal extends org.jcrypt.IAddress {
	public void init(INetwork network, String adr);
}
