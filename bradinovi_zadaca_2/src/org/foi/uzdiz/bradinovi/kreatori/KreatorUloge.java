package org.foi.uzdiz.bradinovi.kreatori;

import org.foi.uzdiz.bradinovi.podaci.Kljucevi;
import org.foi.uzdiz.bradinovi.podaci.ResursTVKuce;
import org.foi.uzdiz.bradinovi.podaci.Uloga;


import java.util.HashMap;
import java.util.regex.Pattern;

public class KreatorUloge extends KreatorResursa{

    public KreatorUloge() {
        regexText = "(^\\d+)\\s*;\\s(.+)$";
        regex = Pattern.compile(regexText);
    }

    @Override
    public HashMap<Kljucevi, String> ucitajParametre(String parametri) {
        HashMap<Kljucevi,String> hashMap = new HashMap<>();
        hashMap.put(Kljucevi.IDULOGE,m.group(1).trim());
        hashMap.put(Kljucevi.IMEULOGE,m.group(2).trim());
        return hashMap;
    }

    @Override
    public String dajGresku() {
        return "Greška u zapisu uloge";
    }

    @Override
    public ResursTVKuce dajResurs() {
        return new Uloga();
    }
}
