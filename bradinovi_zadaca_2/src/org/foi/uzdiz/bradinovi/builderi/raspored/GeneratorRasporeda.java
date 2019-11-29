package org.foi.uzdiz.bradinovi.builderi.raspored;

import org.foi.uzdiz.bradinovi.podaci.Emisija;
import org.foi.uzdiz.bradinovi.raspored.TjedniRaspored;

import java.time.LocalTime;
import java.util.List;

public class GeneratorRasporeda {

    BuilderRasporeda builder;

    List<Emisija> emisije;
    LocalTime pocetak;
    LocalTime kraj;

    public GeneratorRasporeda(BuilderRasporeda builder) {
        this.builder = builder;

    }

    public TjedniRaspored make(){
        return builder.rasporediFiksneEmisije().rasporediDnevneEmisije().rasporediSlobodneEmisije().build();
    }

}
