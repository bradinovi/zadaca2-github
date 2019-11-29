package org.foi.uzdiz.bradinovi.raspored;

import org.foi.uzdiz.bradinovi.podaci.Emisija;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import org.foi.uzdiz.bradinovi.podaci.Osoba;
import org.foi.uzdiz.bradinovi.podaci.Par;
import org.foi.uzdiz.bradinovi.podaci.Uloga;

public class ElementRasporeda implements KomponentaRasporeda {
    private Emisija emisija;
    private LocalTime pocetakPrikazivanja;

    public ElementRasporeda(Emisija e, LocalTime pk) {
        emisija = e;
        pocetakPrikazivanja = pk;
    }

    public ElementRasporeda(Emisija e) {
        emisija = e;
    }

    @Override
    public void prikazi() {
        if(emisija != null){           
            String p = this.pocetakPrikazivanja.format(DateTimeFormatter.ofPattern("HH:mm:ss"));
            LocalTime kraj = this.pocetakPrikazivanja.plusMinutes(this.emisija.getTrajanjeMinuta());
            String k = kraj.format(DateTimeFormatter.ofPattern("HH:mm:ss"));
            System.out.println(String.format("%s - %s  %s %s",p ,k,emisija.getNaziv(),emisija.getVrsta().getVrstaEmisije()));
            System.out.println(dajSudionike());
        }

    }

    @Override
    public LocalTime vratiTrajanje() {
        return LocalTime.of(0,0,0).plusMinutes(emisija.getTrajanjeMinuta());
    }

    public Emisija getEmisija() {
        return emisija;
    }

    public void setEmisija(Emisija emisija) {
        this.emisija = emisija;
    }

    public LocalTime getPocetakPrikazivanja() {
        return pocetakPrikazivanja;
    }

    public void setPocetakPrikazivanja(LocalTime pocetakPrikazivanja) {
        this.pocetakPrikazivanja = pocetakPrikazivanja;
    }
    
    String dajSudionike(){
        String tekst = "";
        for (Par<Osoba,Uloga> s : emisija.getSudionici()) {
            tekst += String.format("                   [%s] %s - %s \n",s.getP1().getId(), s.getP1().getImePrezime(), s.getP2().getNazivUloge());
        }      
        return tekst;
    }
}
