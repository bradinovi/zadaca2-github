package org.foi.uzdiz.bradinovi.kreatori;

import org.foi.uzdiz.bradinovi.podaci.Kljucevi;
import org.foi.uzdiz.bradinovi.podaci.Osoba;
import org.foi.uzdiz.bradinovi.podaci.ResursTVKuce;

import java.util.HashMap;
import java.util.regex.Pattern;

public class KreatorOsobe extends KreatorResursa{

    public KreatorOsobe() {
        regexText = "(^\\d+); (.+)$";
        regex = Pattern.compile(regexText);
    }

    @Override
    public HashMap<Kljucevi, String> ucitajParametre(String parametri) {
        HashMap<Kljucevi,String> hashMap = new HashMap<>();
        hashMap.put(Kljucevi.IDOSOBE,m.group(1).trim());
        hashMap.put(Kljucevi.IMEPREZIMEOSOBE,m.group(2).trim());
        return hashMap;
    }

    @Override
    public String dajGresku() {
        return "Greška u zapisu osobe";
    }

    @Override
    public ResursTVKuce dajResurs() {
        return new Osoba();
    }
}
