package org.foi.uzdiz.bradinovi.builderi.raspored;

import java.util.List;
import org.foi.uzdiz.bradinovi.podaci.Emisija;
import org.foi.uzdiz.bradinovi.podaci.Osoba;
import org.foi.uzdiz.bradinovi.podaci.TVProgram;
import org.foi.uzdiz.bradinovi.podaci.Uloga;
import org.foi.uzdiz.bradinovi.raspored.TjedniRaspored;

public interface BuilderRasporeda {
    public void promjeniProgram(TVProgram program);
    public void reset();
    public void init(TVProgram program, List<Emisija> emisije,List<String> listaParametara,  List<Osoba> osobe, List<Uloga> uloge);
    public BuilderRasporeda rasporediFiksneEmisije();
    public BuilderRasporeda rasporediDnevneEmisije();
    public BuilderRasporeda rasporediSlobodneEmisije();
    TjedniRaspored build();
    
}
