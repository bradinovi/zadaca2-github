package org.foi.uzdiz.bradinovi.podaci;

import org.foi.uzdiz.bradinovi.raspored.TjedniRaspored;

import java.time.LocalTime;
import java.util.HashMap;

public class TVProgram implements ResursTVKuce {
    long id;
    private String naziv;
    private LocalTime pocetak;
    private LocalTime kraj;
    private String nazivDatoteke;
    private TjedniRaspored tjedniRaspored;

    @Override
    public void postaviVrijednosti(HashMap<Kljucevi, String> parametri) {
        id = Long.parseLong(parametri.get(Kljucevi.IDPROGRAMA));
        naziv = parametri.get(Kljucevi.NAZIVPROGRAMA);
        pocetak = stringToTime(parametri.get(Kljucevi.POCETAK));
        kraj = stringToTime(parametri.get(Kljucevi.KRAJ));
        nazivDatoteke = parametri.get(Kljucevi.NAZIVDATOTEKE);
    }

    private LocalTime stringToTime(String time){
        if(time.isEmpty()) return null;
        String[] t = time.trim().split(":");
        int sekunde = 0;
        if(t.length>2) sekunde = Integer.parseInt(t[2]);
        return LocalTime.of(Integer.parseInt(t[0]),Integer.parseInt(t[1]),sekunde);
    }

    public String getNaziv() {
        return naziv;
    }

    public void setNaziv(String naziv) {
        this.naziv = naziv;
    }


    public String getNazivDatoteke() {
        return nazivDatoteke;
    }

    public void setNazivDatoteke(String nazivDatoteke) {
        this.nazivDatoteke = nazivDatoteke;
    }

    public LocalTime getPocetak() {
        return pocetak;
    }

    public void setPocetak(LocalTime pocetak) {
        this.pocetak = pocetak;
    }

    public LocalTime getKraj() {
        return kraj;
    }

    public void setKraj(LocalTime kraj) {
        this.kraj = kraj;
    }

    public TjedniRaspored getTjedniRaspored() {
        return tjedniRaspored;
    }

    public void setTjedniRaspored(TjedniRaspored tjedniRaspored) {
        this.tjedniRaspored = tjedniRaspored;
    }
    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }
}
