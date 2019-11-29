package org.foi.uzdiz.bradinovi.upravitelji;

import org.foi.uzdiz.bradinovi.builderi.emisija.EmisijaBuilder;
import org.foi.uzdiz.bradinovi.builderi.emisija.EmisijaBuilderImp;
import org.foi.uzdiz.bradinovi.builderi.emisija.EmisijaDirector;
import org.foi.uzdiz.bradinovi.greske.KreiranjeResursaException;
import org.foi.uzdiz.bradinovi.podaci.Emisija;
import org.foi.uzdiz.bradinovi.podaci.Osoba;
import org.foi.uzdiz.bradinovi.podaci.ResursTVKuce;
import org.foi.uzdiz.bradinovi.podaci.Uloga;
import org.foi.uzdiz.bradinovi.greske.PogreskaZapisResursaException;
import org.foi.uzdiz.bradinovi.kreatori.KreatorResursa;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import org.foi.uzdiz.bradinovi.podaci.Vrsta;

public class UcitavanjeHelper {

    public static List<String> ucitajDatoteku(String putanja) {
        List<String> redovidatoteke = null;
        try {
            redovidatoteke = Files.readAllLines(Paths.get(putanja));
            redovidatoteke.remove(0);
            for (int i = 0; i < redovidatoteke.size(); i++) {
                if (redovidatoteke.get(i).isEmpty()) {
                    redovidatoteke.remove(i);
                }
            }
        } catch (IOException e) {
            System.err.println(e.getMessage() + " ne postoji.");            
        }
        return redovidatoteke;
    }

    public static <T> List<T> ucitajResurse(String nazivDatoteke, KreatorResursa kreatorResursa) throws KreiranjeResursaException {
        List<String> redovidatoteke = ucitajDatoteku(nazivDatoteke);
        if (redovidatoteke == null) {
            throw new KreiranjeResursaException("Datoteka ne postoji: " + nazivDatoteke);
        }
        if (redovidatoteke.isEmpty()) {
            throw new KreiranjeResursaException("Datoteka prazna: " + nazivDatoteke);
        }
        List<T> listaUloga = new ArrayList<>();
        int i = 1;
        for (String red : redovidatoteke) {
            i++;
            ResursTVKuce resursTVKuce = null;
            try {
                resursTVKuce = kreatorResursa.stvoriResurs(red);
            } catch (PogreskaZapisResursaException e) {
                System.out.println(e.getMessage() + " Red broj: " + i);
            }
            if (resursTVKuce == null) {
                continue;
            }
            listaUloga.add((T) resursTVKuce);
        }
        return listaUloga;
    }

    public static List<Emisija> ucitajEmisije(String nazivDatoteke, List<Uloga> sveUloge, List<Osoba> sveOsobe, List<Vrsta> vrste) {
        List<String> redovidatoteke = ucitajDatoteku(nazivDatoteke);        
        List<Emisija> emisije = new ArrayList<>();
        if(redovidatoteke == null) return emisije;
        int i = 2;
        for (String red : redovidatoteke) {
            EmisijaBuilder builder = new EmisijaBuilderImp();
            try {
                EmisijaDirector ed = new EmisijaDirector(builder, red, sveUloge, sveOsobe, vrste);
                Emisija e = ed.make();
                emisije.add(e);
            } catch (Exception e) {
                System.out.println(e.getMessage() + " Red broj: " + i);
            }
            i++;

        }
        return emisije;
    }

}
