package org.foi.uzdiz.bradinovi.podaci;

import java.util.HashMap;

public class Uloga implements ResursTVKuce {

    long id;

    public Uloga() {
    }
    private String nazivUloge;

    public Uloga(Uloga u) {
        id = u.getId();
        nazivUloge = u.getNazivUloge();

    }

    @Override
    public void postaviVrijednosti(HashMap<Kljucevi, String> parametri) {
        id = Long.parseLong(parametri.get(Kljucevi.IDULOGE));
        nazivUloge = parametri.get(Kljucevi.IMEULOGE);
    }

    public String getNazivUloge() {
        return nazivUloge;
    }

    public void setNazivUloge(String nazivUloge) {
        this.nazivUloge = nazivUloge;
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof Uloga)) {
            return false;
        }
        Uloga u = (Uloga) obj;
        return nazivUloge.equals(u.nazivUloge) && id == u.id;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }
}
