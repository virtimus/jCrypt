package org.jcrypt;

import java.math.BigInteger;

public interface IjcUtils {
	public String bitcoinValueToFriendlyString(BigInteger value);
	public <T> T checkNotNull(T reference);
	
}