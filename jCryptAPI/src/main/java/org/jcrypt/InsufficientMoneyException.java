package org.jcrypt;

import java.math.BigInteger;

/**
 * Thrown to indicate that you don't have enough money available to perform the requested operation.
 */
public class InsufficientMoneyException extends Exception {

	private static final long serialVersionUID = 1L;
	/** Contains the number of satoshis that would have been required to complete the operation. */
	
    public final BigInteger missing;

    protected InsufficientMoneyException() {
        this.missing = null;
    }

    public InsufficientMoneyException(BigInteger missing) {
        this(missing, "Insufficient money,  missing " + missing + " satoshis");
    }

    public InsufficientMoneyException(BigInteger missing, String message) {
        super(message);
        this.missing = missing; //checkNotNull(missing);
    }

}
