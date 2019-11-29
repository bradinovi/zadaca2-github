package org.foi.uzdiz.bradinovi.kreatori;

import org.foi.uzdiz.bradinovi.podaci.Kljucevi;
import org.foi.uzdiz.bradinovi.podaci.ResursTVKuce;
import org.foi.uzdiz.bradinovi.greske.PogreskaZapisResursaException;

import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public abstract class KreatorResursa {
    public String regexText;
   
    public abstract HashMap<Kljucevi,String> ucitajParametre(String parametri);
    public abstract String dajGresku();
    public abstract ResursTVKuce dajResurs();

    protected Matcher m;
    protected Pattern regex;


    public boolean provjeriParametre(String parametriTekst) {
        m = regex.matcher(parametriTekst);
        return m.matches();
    }


    public ResursTVKuce stvoriResurs(String parametriTekst) throws PogreskaZapisResursaException {
        if(!provjeriParametre(parametriTekst)){
            throw new PogreskaZapisResursaException(dajGresku());
        }
        HashMap<Kljucevi,String> parametri = ucitajParametre(parametriTekst);
        ResursTVKuce resurs = dajResurs();
        resurs.postaviVrijednosti(parametri);
        return resurs;
    }



}
