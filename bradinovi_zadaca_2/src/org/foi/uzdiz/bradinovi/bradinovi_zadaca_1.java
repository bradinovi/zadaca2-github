package org.foi.uzdiz.bradinovi;

import org.foi.uzdiz.bradinovi.greske.KreiranjeResursaException;
import org.foi.uzdiz.bradinovi.podaci.*;

import java.nio.file.InvalidPathException;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;

public class bradinovi_zadaca_1 {

    public static boolean isPathValid(String path) {
        try {
            Paths.get(path);
        } catch (InvalidPathException ex) {
            return false;
        }
        return true;
    }

    public static void main(String[] args) {
        final String dir = System.getProperty("user.dir");
        System.out.println("Current dir = " + dir);
        List<String> list = Arrays.asList(args);
        String[] obavezniArgumenti = { "-t", "-e", "-o","-u", "-v" };
        if(!(list.contains("-t")&&list.contains("-e")&&list.contains("-o")&&list.contains("-u")
                &&list.contains("-v"))){
            System.out.println("Niste zadali sve potrebne argumente");
            return;
        }
        if(list.size()!=10){
            System.out.println("Niste zadali sve potrebne parametre argumentima");
            return;
        }

        String datotekaTVKuca = "";
        String datotekaEmisije  = "";
        String datotekaOsobe  = "";
        String datotekaUloge  = "";
        String datotekaVrste  = "";
        String parametar = "";
        for (int i = 0; i < list.size(); i = i + 2) {
            parametar = args[i+1];
            if(args[i].equals("-t")&&!isPathValid(parametar)){
                System.out.println("Parametar argumenta -t nije putanja datoteke");
            }
            else if(args[i].equals("-e")&&!isPathValid(parametar)){
                System.out.println("Parametar argumenta -e nije putanja datoteke");
            }else if(args[i].equals("-o")&&!isPathValid(parametar)){
                System.out.println("Parametar argumenta -o nije putanja datoteke");
            }
            else if(args[i].equals("-u")&&!isPathValid(parametar)){
                System.out.println("Parametar argumenta -u nije putanja datoteke");
            }
            else if(args[i].equals("-v")&&!isPathValid(parametar)){
                System.out.println("Parametar argumenta -v nije putanja datoteke");
            }
        }
        for (int i = 0; i < list.size(); i = i + 2) {
            parametar = args[i+1];
            if(args[i].equals("-t")){
                datotekaTVKuca = parametar;
            }
            else if(args[i].equals("-e")){
                datotekaEmisije = parametar;
            }else if(args[i].equals("-v")){
                datotekaVrste = parametar;
            }
            else if(args[i].equals("-o")){
                datotekaOsobe = parametar;
            }
            else if(args[i].equals("-u")){
                datotekaUloge = parametar;
            }
        }

        TVKuca tvKuca = new TVKuca("Nova TV");
        try {
            tvKuca.init(datotekaTVKuca,datotekaOsobe,datotekaUloge,datotekaEmisije, datotekaVrste);
        } catch (KreiranjeResursaException e) {
            System.out.println(e.getMessage());
        }
        tvKuca.dajIzbornik();

    }


}


