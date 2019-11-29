package org.foi.uzdiz.bradinovi.kreatori;

import org.foi.uzdiz.bradinovi.podaci.Kljucevi;
import org.foi.uzdiz.bradinovi.podaci.ResursTVKuce;
import org.foi.uzdiz.bradinovi.podaci.TVProgram;

import java.util.HashMap;
import java.util.regex.Pattern;

public class KreatorPrograma extends KreatorResursa {


    public KreatorPrograma() {
        regexText = "(\\d+);\\s*([\\d\\w\\s\\.]+);\\s*(\\d{1,2}:\\d{1,2}:{0,1}\\d{1,2}|\\s*);\\s*(\\d{1,2}:\\d{1,2}|\\s*);\\s*([\\w,\\s-]+\\.txt)";
        regex = Pattern.compile(regexText);
    }

    @Override
    public HashMap<Kljucevi, String> ucitajParametre(String parametri) {
        HashMap<Kljucevi,String> hashMap = new HashMap<>();
        hashMap.put(Kljucevi.IDPROGRAMA,m.group(1).trim());
        hashMap.put(Kljucevi.NAZIVPROGRAMA,m.group(2).trim());
        hashMap.put(Kljucevi.POCETAK,m.group(3).trim());
        hashMap.put(Kljucevi.KRAJ,m.group(4).trim());
        hashMap.put(Kljucevi.NAZIVDATOTEKE,m.group(5).trim());
        return hashMap;
    }

    @Override
    public String dajGresku() {
        return "Greška u zapisu programa";
    }

    @Override
    public ResursTVKuce dajResurs() {
        return new TVProgram();
    }
}
