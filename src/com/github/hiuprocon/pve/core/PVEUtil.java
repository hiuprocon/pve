package com.github.hiuprocon.pve.core;

import jp.sourceforge.acerola3d.a3.*;

public class PVEUtil {
	static A3Object errorA3;
	static {
		try {
			errorA3 = new VRML("x-res:///jp/sourceforge/acerola3d/resources/error.wrl");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
    public static A3Object loadA3(String url) {
    	A3Object a3 = null;
    	try {
        	if (url.endsWith("a3")||url.endsWith("A3")) {
        		a3 = new Action3D(url);
        	} else if (url.endsWith("wrl")||url.endsWith("WRL")) {
        		a3 = new VRML(url);
        	}
    	} catch (Exception e) {
    		e.printStackTrace();
    	}
    	if (a3!=null)
    	    return a3;
    	else
    		return errorA3;
    }
}
