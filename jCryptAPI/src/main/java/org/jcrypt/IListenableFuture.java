package org.jcrypt;

import java.util.concurrent.Executor;
import java.util.concurrent.Future;
import java.util.concurrent.RejectedExecutionException;


public interface IListenableFuture<V> {
	 public void addCallback(FutureCallback<? super V> callback);
	 //public void addCallback();
	 
	  /**
	   * Registers a listener to be {@linkplain Executor#execute(Runnable) run} on
	   * the given executor.  The listener will run when the {@code Future}'s
	   * computation is {@linkplain Future#isDone() complete} or, if the computation
	   * is already complete, immediately.
	   *
	   * <p>There is no guaranteed ordering of execution of listeners, but any
	   * listener added through this method is guaranteed to be called once the
	   * computation is complete.
	   *
	   * <p>Exceptions thrown by a listener will be propagated up to the executor.
	   * Any exception thrown during {@code Executor.execute} (e.g., a {@code
	   * RejectedExecutionException} or an exception thrown by {@linkplain
	   * MoreExecutors#sameThreadExecutor inline execution}) will be caught and
	   * logged.
	   *
	   * <p>Note: For fast, lightweight listeners that would be safe to execute in
	   * any thread, consider {@link MoreExecutors#sameThreadExecutor}. For heavier
	   * listeners, {@code sameThreadExecutor()} carries some caveats.  For
	   * example, the listener may run on an unpredictable or undesirable thread:
	   *
	   * <ul>
	   * <li>If the input {@code Future} is done at the time {@code addListener} is
	   * called, {@code addListener} will execute the listener inline.
	   * <li>If the input {@code Future} is not yet done, {@code addListener} will
	   * schedule the listener to be run by the thread that completes the input
	   * {@code Future}, which may be an internal system thread such as an RPC
	   * network thread.
	   * </ul>
	   *
	   * Also note that, regardless of which thread executes the listener, all
	   * other registered but unexecuted listeners are prevented from running
	   * during its execution, even if those listeners are to run in other
	   * executors.
	   *
	   * <p>This is the most general listener interface. For common operations
	   * performed using listeners, see {@link
	   * com.google.common.util.concurrent.Futures}. For a simplified but general
	   * listener interface, see {@link
	   * com.google.common.util.concurrent.Futures#addCallback addCallback()}.
	   *
	   * @param listener the listener to run when the computation is complete
	   * @param executor the executor to run the listener in
	   * @throws NullPointerException if the executor or listener was null
	   * @throws RejectedExecutionException if we tried to execute the listener
	   *         immediately but the executor rejected it.
	   */
	  void addListener(Runnable listener, IExecutor executor);	 
}
