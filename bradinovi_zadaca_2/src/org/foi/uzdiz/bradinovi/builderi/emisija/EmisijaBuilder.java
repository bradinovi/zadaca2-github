package org.foi.uzdiz.bradinovi.builderi.emisija;

import org.foi.uzdiz.bradinovi.podaci.Emisija;
import org.foi.uzdiz.bradinovi.podaci.Osoba;
import org.foi.uzdiz.bradinovi.podaci.Uloga;
import org.foi.uzdiz.bradinovi.greske.OsobaNePostojiException;
import org.foi.uzdiz.bradinovi.greske.UlogaNePostojiException;

import java.util.List;
import org.foi.uzdiz.bradinovi.podaci.Vrsta;

public interface EmisijaBuilder {

    EmisijaBuilder dodajId(long id);

    EmisijaBuilder dodajNaziv(String naziv);

    EmisijaBuilder dodajTrajanje(int trajanje);

    EmisijaBuilder dodjeliSudionike(List<Uloga> sveUloge, List<Osoba> sveOsobe, String sudionici) throws OsobaNePostojiException, UlogaNePostojiException;

    EmisijaBuilder dodajVrstu(String idVrste, List<Vrsta> vrste);

    Emisija build();
}
