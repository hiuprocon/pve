package com.github.hiuprocon.pve.core;

import com.github.hiuprocon.pve.core.*;
import com.bulletphysics.collision.shapes.*;
import javax.vecmath.*;
import jp.sourceforge.acerola3d.a3.*;
import java.net.URL;
import java.awt.image.BufferedImage;

//Cameraを表すクラス
public class Camera extends PVEPart {
    double scale;
    String a3url;
    int width;
    int height;

    public Camera(Type type, double mass, int width, int height) {
        this(type,mass,width,height,0.01);
    }
    public Camera(Type type, double mass, int width, int height, double scale) {
        this(type, mass, width,height,scale, "x-res:///res/SonyZ1U.wrl");
    }

    public Camera(Type type, double mass, int width, int height, double scale, String a3url) {
        super(type, mass, a3url);
        this.width = width;
        this.height = height;
        this.scale = scale;
        init();
        a3.setScale(10*scale);
    }

    @Override
    protected A3Object makeA3Object(String a3url) {
        A3VideoCamera c = new A3VideoCamera(width,height);
        try {
            URL url = new URL(a3url);
            c.addNode(Util.loadVRML_B(url));
        } catch(Exception e) {e.printStackTrace();}
        return c;
    }

    // 立方体の剛体を作る
    public CollisionShape makeCollisionShape() {
        return new BoxShape(new Vector3f((float)scale/2,(float)scale/2,(float)scale/2));
    }

    public void renderOffscreenBuffer(BufferedImage image) {
        ((A3VideoCamera)a3).renderOffscreenBuffer(image);
    }
}
