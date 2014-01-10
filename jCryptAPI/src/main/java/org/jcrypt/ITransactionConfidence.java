package org.jcrypt;


public interface ITransactionConfidence {
	 public IListenableFuture<ITransaction> getDepthFuture(final int depth);
}
