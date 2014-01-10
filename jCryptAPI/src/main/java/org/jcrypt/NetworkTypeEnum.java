package org.jcrypt;

import java.io.Serializable;


public enum NetworkTypeEnum implements Serializable, IEnumDescription {
	
    BitcoinProd("Bitcoin - production"),
    BitcoinTest("Bitcoin - test3network"),
    BitcoinRegTest("Bitcoin - regression tests network");
    
    private static final long serialVersionUID = 1L;
    
    private String description = null;
    
    private NetworkTypeEnum(String description) {
        this.description = description;
    }
    
    public String getDescription() {
        return description;
    }    
   
}