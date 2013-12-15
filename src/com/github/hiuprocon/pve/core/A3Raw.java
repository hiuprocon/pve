package com.github.hiuprocon.pve.core;

import jp.sourceforge.acerola3d.a3.*;
import javax.media.j3d.*;

//javax.media.j3d.NodeをそのままA3Objectにする
class A3Raw extends A3Object {
    public A3Raw(Node n) {
        super(new A3InitData("com.github.hiuprocon.pve.core.A3Raw"));
        setNode(n);
    }
}
