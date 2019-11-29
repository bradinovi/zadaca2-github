package org.foi.uzdiz.bradinovi.kreatori;

import org.foi.uzdiz.bradinovi.podaci.Kljucevi;
import org.foi.uzdiz.bradinovi.podaci.ResursTVKuce;
import org.foi.uzdiz.bradinovi.podaci.Vrsta;

import java.util.HashMap;
import java.util.regex.Pattern;

public class KreatorVrsta extends KreatorResursa {

    public KreatorVrsta() {
        regexText = "(^\\d+)\\s*;\\s*([\\w\\s]+);\\s*(0|1);\\s*(\\d+)$";
        regex = Pattern.compile(regexText);
    }

    @Override
    public HashMap<Kljucevi, String> ucitajParametre(String parametri) {
        HashMap<Kljucevi,String> hashMap = new HashMap<>();
        hashMap.put(Kljucevi.IDVRSTEEMISIJE,m.group(1).trim());
        hashMap.put(Kljucevi.NAZIVVRSTEEMISIJE,m.group(2).trim());
        hashMap.put(Kljucevi.REKLAMEEMISIJE,m.group(3).trim());
        hashMap.put(Kljucevi.TRAJANEREKLAMA,m.group(4).trim());
        return hashMap;
    }

    @Override
    public String dajGresku() {
        return "Greška u zapisu vrste emisije";
    }

    @Override
    public ResursTVKuce dajResurs() {
        return new Vrsta();
    }
}
