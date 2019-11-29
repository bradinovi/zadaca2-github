package org.foi.uzdiz.bradinovi.builderi.emisija;

import org.foi.uzdiz.bradinovi.TVKuca;
import org.foi.uzdiz.bradinovi.podaci.Emisija;
import org.foi.uzdiz.bradinovi.podaci.Osoba;
import org.foi.uzdiz.bradinovi.podaci.Uloga;
import org.foi.uzdiz.bradinovi.greske.OsobaNePostojiException;
import org.foi.uzdiz.bradinovi.greske.UlogaNePostojiException;
import org.foi.uzdiz.bradinovi.podaci.Vrsta;

import java.util.List;

public class EmisijaBuilderImp implements EmisijaBuilder {
    private Emisija emisija;
    List<Vrsta> vrste;
    public EmisijaBuilderImp() {
        this.emisija = new Emisija();
    }

    @Override
    public EmisijaBuilder dodajId(long id) {
        emisija.setId(id);
        return this;
    }

    @Override
    public EmisijaBuilder dodajNaziv(String naziv) {
        emisija.setNaziv(naziv);
        return this;
    }

    @Override
    public EmisijaBuilder dodajTrajanje(int trajanje) {
        emisija.setTrajanjeMinuta(trajanje);
        return this;
    }

    @Override
    public EmisijaBuilder dodjeliSudionike(List<Uloga> sveUloge, List<Osoba> sveOsobe, String sudionici) throws OsobaNePostojiException, UlogaNePostojiException {
        
        String[] sudioniciLista = sudionici.split(",");
        for (String s: sudioniciLista
             ) {
            String[] ulogaosoba = s.split("-");
            long osobaId = Long.parseLong(ulogaosoba[0].trim());
            long ulogaId = Long.parseLong(ulogaosoba[1].trim());
            Osoba o = nadiOsobu(osobaId, sveOsobe);
            Uloga u = nadiUlogu(ulogaId, sveUloge);
            if(o == null) throw new OsobaNePostojiException("Osoba ne postoji.");
            if(u == null) throw new UlogaNePostojiException("Uloga ne postoji");
            emisija.addSudionik(o,u);
        }
        return this;
    }

    @Override
    public EmisijaBuilder dodajVrstu(String idVrste, List<Vrsta> vrste) {
        this.vrste = vrste;
        emisija.setVrsta(dajVrstuEmisijePremaID(Long.parseLong(idVrste)));
        return this;
    }

    public static Osoba nadiOsobu(long id, List<Osoba> lista){
        for (Osoba o: lista) {
            if(o.getId() == id)
                return  o;
        }
        return null;
    }

    public static Uloga nadiUlogu(long id, List<Uloga> lista){
        for (Uloga u: lista) {
            if(u.getId() == id)
                return  u;
        }
        return null;
    }

    @Override
    public Emisija build() {
        return emisija;
    }

    private Vrsta dajVrstuEmisijePremaID(long id){
        for (Vrsta vrsta: vrste) {
            if(vrsta.getId() == id) return vrsta;
        }
        return null;
    }
}
