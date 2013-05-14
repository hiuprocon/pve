package com.github.hiuprocon.pve.obj;

import com.github.hiuprocon.pve.core.*;

public class Luggage extends PVEObject {
    LuggagePart luggagePart;

    public Luggage() {
        init();
    }

    @Override
    protected PVEPart[] createParts() {
        luggagePart = new LuggagePart();
        return new PVEPart[] { luggagePart };
    }

    @Override
    protected Constraint[] createConstraints() {
        return new Constraint[0];
    }

    @Override
    protected PVEPart getMainPart() {
        return luggagePart;
    }
}
