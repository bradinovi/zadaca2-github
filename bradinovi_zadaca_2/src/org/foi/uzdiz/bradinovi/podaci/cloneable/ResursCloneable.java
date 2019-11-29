package org.foi.uzdiz.bradinovi.podaci.cloneable;

import java.util.Objects;

public abstract class ResursCloneable {
    private long id;

    public ResursCloneable(long id) {
        this.id = id;
    }

    public ResursCloneable() {
        this.id = id;
    }

    public ResursCloneable(ResursCloneable resurs) {
        if(resurs != null){
            this.id = resurs.getId();
        }
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public abstract ResursCloneable clone();

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof ResursCloneable)) return false;
        ResursCloneable shape2 = (ResursCloneable) obj;
        return shape2.id == id;
    }
}
