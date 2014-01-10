package org.jcrypt.ejb;

import java.io.File;

import javax.ejb.Local;

import org.jcrypt.NetworkTypeEnum;

@Local
public interface NetworkLocal extends org.jcrypt.INetwork{
	public void init(NetworkTypeEnum nType, File directory);
}
