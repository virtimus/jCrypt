package org.jcrypt.ejb;

import javax.ejb.Stateless;
import org.jcrypt.INetwork;

@Stateless
public class AddressBean implements AddressLocal {
	private INetwork network = null;
	private String adr = null;
	
	public void init(INetwork network, String adr){
		this.network = network;
		this.adr = adr;
	}
}
