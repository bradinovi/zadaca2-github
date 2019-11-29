package org.foi.uzdiz.bradinovi.podaci;

import java.util.HashMap;

public class Osoba implements ResursTVKuce {
    long id;
    private String imePrezime;

    public Osoba(Osoba o) {
        id = o.getId();
        imePrezime = o.getImePrezime();
    }

    public Osoba() {
    }

    @Override
    public void postaviVrijednosti(HashMap<Kljucevi, String> parametri) {
        id = Long.parseLong(parametri.get(Kljucevi.IDOSOBE));
        imePrezime = parametri.get(Kljucevi.IMEPREZIMEOSOBE);
    }

    public String getImePrezime() {
        return imePrezime;
    }

    public void setImePrezime(String imePrezime) {
        this.imePrezime = imePrezime;
    }

    @Override
    public boolean equals(Object obj) {
        if(!(obj instanceof Osoba)) return false;
        Osoba o = (Osoba) obj;
        return imePrezime == o.imePrezime && id ==o.id;
    }
    
    
    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }
}
