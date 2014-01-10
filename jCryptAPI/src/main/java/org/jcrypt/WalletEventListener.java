package org.jcrypt;

import java.math.BigInteger;
import java.util.List;

/**
 * Convenience implementation of {@link IWalletEventListener}.
 */
public abstract class WalletEventListener implements IWalletEventListener {
    @Override
    public void onCoinsReceived(IWallet wallet, ITransaction tx, BigInteger prevBalance, BigInteger newBalance) {
        onChange();
    }

    @Override
    public void onCoinsSent(IWallet wallet, ITransaction tx, BigInteger prevBalance, BigInteger newBalance) {
        onChange();
    }

    @Override
    public void onReorganize(IWallet wallet) {
        onChange();
    }

    @Override
    public void onTransactionConfidenceChanged(IWallet wallet, ITransaction tx) {
        onChange();
    }

    @Override
    public void onKeysAdded(IWallet wallet, List<IECKey> keys) {
        onChange();
    }

    @Override
    public void onScriptsAdded(IWallet wallet, List<IScript> scripts) {
        onChange();
    }

    @Override
    public void onWalletChanged(IWallet wallet) {
        onChange();
    }

    public void onChange() {
    }
}
