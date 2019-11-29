package org.foi.uzdiz.bradinovi.raspored;

import org.foi.uzdiz.bradinovi.greske.ResporedCompositeException;

import java.util.ArrayList;
import java.util.List;

public abstract class SlozenaKomponentaRasporeda implements KomponentaRasporeda {

    List<KomponentaRasporeda> children = new ArrayList<>();
    KomponentaRasporeda parent;
    
    public SlozenaKomponentaRasporeda(KomponentaRasporeda parent) {
        this.parent = parent;
    }
    
    public SlozenaKomponentaRasporeda() {
        
    }

    public void add(KomponentaRasporeda c) throws ResporedCompositeException {
        children.add(c) ;
    }
    
    public List<KomponentaRasporeda> getChildren(){
        return children;
    }

    public void prikazi() {
        for (int i = 0; i < children.size(); i++) {
            children.get(i).prikazi();
        }
    }

}
