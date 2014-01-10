package org.jcrypt.ejb;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.concurrent.TimeUnit;
import java.lang.Override;

import javax.ejb.Stateless;

import org.jcrypt.IAddress;
import org.jcrypt.INetworkParams;
import org.jcrypt.IPeerGroup;
import org.jcrypt.IWallet;
import org.jcrypt.NetworkTypeEnum;
import org.jcrypt.jcFactory;
import org.jcrypt.jcFactoryImpl;

import com.google.bitcoin.core.BlockChain;
import com.google.bitcoin.core.CheckpointManager;
import com.google.bitcoin.core.DownloadListener;
import com.google.bitcoin.core.ECKey;
import com.google.bitcoin.core.NetworkParameters;
import com.google.bitcoin.core.PeerAddress;
import com.google.bitcoin.core.PeerEventListener;
import com.google.bitcoin.core.PeerGroup;
import com.google.bitcoin.core.Wallet;
import com.google.bitcoin.kits.WalletAppKit;
import com.google.bitcoin.net.discovery.DnsDiscovery;
import com.google.bitcoin.params.MainNetParams;
import com.google.bitcoin.params.RegTestParams;
import com.google.bitcoin.params.TestNet3Params;
import com.google.bitcoin.store.BlockStoreException;
import com.google.bitcoin.store.SPVBlockStore;
import com.google.bitcoin.store.WalletProtobufSerializer;
import com.google.common.util.concurrent.FutureCallback;
import com.google.common.util.concurrent.Futures;
import com.google.common.util.concurrent.Service.State;

@Stateless
public class NetworkBean implements NetworkLocal {
	
	private NetworkTypeEnum networkType = null;
	private File directory = null;
	private String filePrefix = "jCrypt";
    private volatile File walletFile;
    private volatile SPVBlockStore store;
    private InputStream checkpoints;
    private volatile BlockChain chain;
    private volatile PeerGroup peerGroup;
    private String userAgent, version; 
    private volatile Wallet wallet;    
    private boolean useAutoSave = true;    
    private PeerAddress[] peerAddresses;  
    private boolean blockingStartup = true;    
    private PeerEventListener downloadListener; 
    private boolean autoStop = true;    
    
	
	
	private NetworkParameters params;

	public void init(NetworkTypeEnum nType, File directory){
		networkType = nType;
		this.directory = directory;
        if (NetworkTypeEnum.BitcoinTest.equals(nType)) {
            params = TestNet3Params.get();
            filePrefix = "jCrypt-bitcoin-testnet";
        } else if (NetworkTypeEnum.BitcoinRegTest.equals(nType)) {
            params = RegTestParams.get();
            filePrefix = "jCrypt-bitcoin-regtest";
        } else if (NetworkTypeEnum.BitcoinProd.equals(nType)) {
            params = MainNetParams.get();
            filePrefix = "jCrypt-bitcoin-mainnet";
        } else {
        	//TODO! unknown network exception
        }		
		
	}	

	
    protected void startUp() throws Exception {
        // Runs in a separate thread.
        if (!directory.exists()) {
            if (!directory.mkdir()) {
                throw new IOException("Could not create named directory.");
            }
        }
        FileInputStream walletStream = null;
        try {
            File chainFile = new File(directory, filePrefix + ".spvchain");
            boolean chainFileExists = chainFile.exists();
            walletFile = new File(directory, filePrefix + ".wallet");
            boolean shouldReplayWallet = walletFile.exists() && !chainFileExists;

            store = new SPVBlockStore(params, chainFile);
            if (!chainFileExists && checkpoints != null) {
                // Ugly hack! We have to create the wallet once here to learn the earliest key time, and then throw it
                // away. The reason is that wallet extensions might need access to peergroups/chains/etc so we have to
                // create the wallet later, but we need to know the time early here before we create the BlockChain
                // object.
                long time = Long.MAX_VALUE;
                if (walletFile.exists()) {
                    Wallet wallet = new Wallet(params);
                    FileInputStream stream = new FileInputStream(walletFile);
                    new WalletProtobufSerializer().readWallet(WalletProtobufSerializer.parseToProto(stream), wallet);
                    time = wallet.getEarliestKeyCreationTime();
                }
                CheckpointManager.checkpoint(params, checkpoints, store, time);
            }
            chain = new BlockChain(params, store);
            peerGroup = new PeerGroup(params, chain);
            if (this.userAgent != null)
                peerGroup.setUserAgent(userAgent, version);
            if (walletFile.exists()) {
                walletStream = new FileInputStream(walletFile);
                wallet = new Wallet(params);
                addWalletExtensions(); // All extensions must be present before we deserialize
                new WalletProtobufSerializer().readWallet(WalletProtobufSerializer.parseToProto(walletStream), wallet);
                if (shouldReplayWallet)
                    wallet.clearTransactions(0);
            } else {
                wallet = new Wallet(params);
                wallet.addKey(new ECKey());
                addWalletExtensions();
            }
            if (useAutoSave) wallet.autosaveToFile(walletFile, 1, TimeUnit.SECONDS, null);
            // Set up peer addresses or discovery first, so if wallet extensions try to broadcast a transaction
            // before we're actually connected the broadcast waits for an appropriate number of connections.
            if (peerAddresses != null) {
                for (PeerAddress addr : peerAddresses) peerGroup.addAddress(addr);
                peerAddresses = null;
            } else {
                peerGroup.addPeerDiscovery(new DnsDiscovery(params));
            }
            chain.addWallet(wallet);
            peerGroup.addWallet(wallet);
            onSetupCompleted();

            if (blockingStartup) {
                peerGroup.startAndWait();
                // Make sure we shut down cleanly.
                installShutdownHook();
                // TODO: Be able to use the provided download listener when doing a blocking startup.
                final DownloadListener listener = new DownloadListener();
                peerGroup.startBlockChainDownload(listener);
                listener.await();
            } else {
                Futures.addCallback(peerGroup.start(), new FutureCallback<State>() {
                    @Override
                    public void onSuccess(State result) {
                        final PeerEventListener l = downloadListener == null ? new DownloadListener() : downloadListener;
                        peerGroup.startBlockChainDownload(l);
                    }

                    @Override
                    public void onFailure(Throwable t) {
                        throw new RuntimeException(t);
                    }
                });
            }
        } catch (BlockStoreException e) {
            throw new IOException(e);
        } finally {
            if (walletStream != null) walletStream.close();
        }
    }	
	

    protected void shutDown() throws Exception {
        // Runs in a separate thread.
        try {
            peerGroup.stopAndWait();
            wallet.saveToFile(walletFile);
            store.close();

            peerGroup = null;
            wallet = null;
            store = null;
            chain = null;
        } catch (BlockStoreException e) {
            throw new IOException(e);
        }
    }    
    
    
    /**
     * <p>Override this to load all wallet extensions if any are necessary.</p>
     *
     * <p>When this is called, chain(), store(), and peerGroup() will return the created objects, however they are not
     * initialized/started</p>
     */
    protected void addWalletExtensions() throws Exception { }
    
    /**
     * This method is invoked on a background thread after all objects are initialised, but before the peer group
     * or block chain download is started. You can tweak the objects configuration here.
     */
    protected void onSetupCompleted() { }  
    
    
    private void installShutdownHook() {
        if (autoStop) Runtime.getRuntime().addShutdownHook(new Thread() {
            @Override public void run() {
                try {
                    NetworkBean.this.stopAndWait();
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }
        });
    }    
    
    
    
	
	public NetworkTypeEnum getNetworkType(){
		return networkType;
	}
	
	public IAddress newAddress(String adr){
		AddressLocal aLoc = (AddressLocal)jcFactoryImpl.lookupLocal("Address");
		aLoc.init(this, adr);
		return (IAddress)aLoc;
	}

	public IWallet newWallet(){
		WalletLocal wLoc = (WalletLocal)jcFactoryImpl.lookupLocal("Wallet");
		wLoc.init(this);
		return (IWallet)wLoc;		
	}	
	
	public IPeerGroup getPeerGroup(){
		//TODO! implement
		return null;
	}
	
	public INetworkParams getParams(){
		//TODO! implement
		return null;		
	}
	
	public void startAndWait() throws Exception {
		//TODO! implement notifiers
		startUp();
	}

	public void stopAndWait() throws Exception {
		//TODO! implement notifiers
		shutDown();
	}	
	
	public void connectToLocalHost(){
		//TODO! implement
	}
	
}
