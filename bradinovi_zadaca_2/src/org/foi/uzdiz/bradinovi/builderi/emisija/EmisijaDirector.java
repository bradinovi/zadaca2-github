package org.foi.uzdiz.bradinovi.builderi.emisija;

import org.foi.uzdiz.bradinovi.podaci.Emisija;
import org.foi.uzdiz.bradinovi.podaci.Kljucevi;
import org.foi.uzdiz.bradinovi.podaci.Osoba;
import org.foi.uzdiz.bradinovi.podaci.Uloga;
import org.foi.uzdiz.bradinovi.greske.KriviParametriEmisijeException;
import org.foi.uzdiz.bradinovi.greske.OsobaNePostojiException;
import org.foi.uzdiz.bradinovi.greske.UlogaNePostojiException;

import java.util.HashMap;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.foi.uzdiz.bradinovi.podaci.Vrsta;

public class EmisijaDirector {
    private EmisijaBuilder builder;
    private HashMap<Kljucevi,String> parametri;
    private Matcher m;
    private List<Uloga> sveUloge;
    private List<Osoba> sveOsobe;
    private List<Vrsta> sveVrste;

    public EmisijaDirector(EmisijaBuilder builder, String parametriTekst, List<Uloga> sveUloge, List<Osoba> sveOsobe, List<Vrsta> sveVrste) throws Exception {
        if(!provjeriParametre(parametriTekst))
            throw new KriviParametriEmisijeException("Greška u parametrima emisije");
        pripremiParametre();
        this.sveOsobe = sveOsobe;
        this.sveUloge = sveUloge;
        this.builder = builder;
        this.sveVrste = sveVrste;
    }

    public void promjeniBuilder(EmisijaBuilder builder) {
        this.builder = builder;
    }

    public Emisija make() throws OsobaNePostojiException, UlogaNePostojiException {
        builder = builder.dodajId(Long.parseLong(parametri.get(Kljucevi.IDEMISIJE)))
                .dodajNaziv(parametri.get(Kljucevi.NAZIVEMISIJE))
                .dodajTrajanje(Integer.parseInt(parametri.get(Kljucevi.TRAJANJEEMISIJE)))
                .dodajVrstu(parametri.get(Kljucevi.VRSTAEMISIJE),this.sveVrste);
        String sudionici = parametri.get(Kljucevi.SUDIONICIEMISIJE);
        if(!sudionici.isEmpty())
                builder.dodjeliSudionike(this.sveUloge, this.sveOsobe,sudionici);
        return builder.build();
    }

    private boolean provjeriParametre(String parametri){
        String regexTekst = "^(\\d+\\s*);\\s*(.+)\\s*;s*(\\d+)\\s*;\\s*(\\d+)\\s*;(\\s*((\\d+-\\d+),){0,}\\s*(\\d+-\\d+){0,})$";
        Pattern regex;
        regex = Pattern.compile(regexTekst);
        m = regex.matcher(parametri);
        return m.matches();
    }

    private void pripremiParametre(){
        HashMap<Kljucevi,String> hashMap = new HashMap<>();
        hashMap.put(Kljucevi.IDEMISIJE,m.group(1).trim());
        hashMap.put(Kljucevi.NAZIVEMISIJE,m.group(2).trim());
        hashMap.put(Kljucevi.VRSTAEMISIJE,m.group(3).trim());
        hashMap.put(Kljucevi.TRAJANJEEMISIJE,m.group(4).trim());
        hashMap.put(Kljucevi.SUDIONICIEMISIJE,m.group(5).trim());
        parametri = hashMap;
    }
}
