package org.jcrypt;

import java.math.BigInteger;

public class jcUtils implements IjcUtils {
	
	public String bitcoinValueToFriendlyString(BigInteger value){
		return com.google.bitcoin.core.Utils.bitcoinValueToFriendlyString(value);
	}

	/**
	 * Ensures that an object reference passed as a parameter to the calling
	 * method is not null.
	 * 
	 * @param reference
	 *            an object reference
	 * @return the non-null reference that was validated
	 * @throws NullPointerException
	 *             if {@code reference} is null
	 */
	public <T> T checkNotNull(T reference) {
		if (reference == null) {
			throw new NullPointerException();
		}
		return reference;
	}
	
}