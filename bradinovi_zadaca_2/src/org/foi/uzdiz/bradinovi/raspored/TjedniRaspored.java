package org.foi.uzdiz.bradinovi.raspored;

import java.awt.Container;
import org.foi.uzdiz.bradinovi.podaci.TVProgram;
import org.foi.uzdiz.bradinovi.greske.ResporedCompositeException;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import org.foi.uzdiz.bradinovi.iteratori.ContainerEmisijeIterator;
import org.foi.uzdiz.bradinovi.iteratori.ContainterEmisijaVrstaIterator;
import org.foi.uzdiz.bradinovi.iteratori.EmisijaVrstaIterator;
import org.foi.uzdiz.bradinovi.iteratori.EmisijeIterator;
import org.foi.uzdiz.bradinovi.observer.Observer;
import org.foi.uzdiz.bradinovi.podaci.Emisija;
import org.foi.uzdiz.bradinovi.podaci.Osoba;
import org.foi.uzdiz.bradinovi.podaci.Par;
import org.foi.uzdiz.bradinovi.podaci.Uloga;

public class TjedniRaspored extends SlozenaKomponentaRasporeda implements ContainerEmisijeIterator, ContainterEmisijaVrstaIterator, Observer {

    LocalTime pocetakEmitiranja;
    LocalTime krajEmitiranja;
    TVProgram program;

    public TjedniRaspored(String nazivPrograma, LocalTime pocetakEmitiranja, LocalTime krajEmitiranja) {
        super(null);
        this.pocetakEmitiranja = pocetakEmitiranja;
        this.krajEmitiranja = krajEmitiranja;
        if (krajEmitiranja == null) {
            this.krajEmitiranja = LocalTime.MAX;
        }
        if (pocetakEmitiranja == null) {
            this.pocetakEmitiranja = LocalTime.MIN;
        }
    }

    @Override
    public void prikazi() {
        System.out.println(String.format("%s  Emitiranje: %s - %s", " ", pocetakEmitiranja, krajEmitiranja));
        super.prikazi();
    }

    @Override
    public void add(KomponentaRasporeda c) throws ResporedCompositeException {
        if (children.size() >= 7) {
            throw new ResporedCompositeException("Tjedan ima max 7 dana");
        }
        super.add(c);
    }

    @Override
    public LocalTime vratiTrajanje() {
        return null;
    }

    

    public LocalTime getPocetakEmitiranja() {
        return pocetakEmitiranja;
    }

    public LocalTime getKrajEmitiranja() {
        return krajEmitiranja;
    }

    public TVProgram getProgram() {
        return program;
    }

    public void setProgram(TVProgram program) {
        this.program = program;
    }

    @Override
    public EmisijeIterator getIterator() {
        return new ElementiRasporedaIterator(this);
    }

    @Override
    public EmisijaVrstaIterator getVrstaIterator(long vrsta) {
        return new ElementiRasporedaVrstaIterator(this, vrsta);
    }

    // Observer --- START ----
    @Override
    public void update(long idOsobe, Uloga stara, Uloga nova) {
        int i = 0;
        for (EmisijeIterator iter = getIterator(); iter.hasNext();) {
            ElementRasporeda er = (ElementRasporeda) iter.next();
            Emisija e = er.getEmisija();
            i = 0;
            for (Par<Osoba, Uloga> s : e.getSudionici()) {
                if (s.getP1().getId() == idOsobe && s.getP2().getId() == stara.getId()) {
                    Par<Osoba, Uloga> p = e.getSudionici().get(i);
                    p.setP2(nova);          
                }
                i++;
            }
        }
    }  

    @Override
    public long getID() {
        return program.getId();
    }
    
   
    
    // Observer --- END ----
    private class ElementiRasporedaIterator implements EmisijeIterator<Object> {

        TjedniRaspored raspored;
        int indexDan = 0;
        int indexElement = 0;

        public ElementiRasporedaIterator(TjedniRaspored raspored) {
            this.raspored = raspored;
        }

        @Override
        public boolean hasNext() {
            int pozDan = indexDan;
            int pozElem = indexElement;
            while (raspored.getChildren().size() > pozDan) {
                if (((DanRasporeda) raspored.getChildren().get(pozDan)).getChildren().size() > pozElem) {
                    return true;
                }
                pozDan++;
                pozElem = 0;
            }
            return false;
        }

        @Override
        public Object next() {
            ElementRasporeda elem = null;
            if (hasNext()) {
                List<KomponentaRasporeda> er = ((DanRasporeda) raspored.getChildren().get(indexDan)).getChildren();
                if (er.isEmpty()) {
                    indexDan++;
                } else {
                    elem = (ElementRasporeda) er.get(indexElement);
                    if (er.size() - 1 == indexElement) {
                        indexDan++;
                        indexElement = 0;
                    } else {
                        indexElement++;
                    }

                }
            }
            return elem;
        }

    }

    private class ElementiRasporedaVrstaIterator implements EmisijaVrstaIterator<Object> {

        TjedniRaspored raspored;
        int indexDan = 0;
        int indexElement = 0;

        int noviDan = 0;
        int noviElem = 0;
        long idVrste;

        public ElementiRasporedaVrstaIterator(TjedniRaspored raspored, long idVrste) {
            this.raspored = raspored;
            this.idVrste = idVrste;
        }

        @Override
        public boolean hasNext() {
            int pozDan = indexDan;
            int pozElem = indexElement;
            while (pozDan < raspored.getChildren().size()) {
                DanRasporeda dan = ((DanRasporeda) raspored.getChildren().get(pozDan));
                while (pozElem < dan.getChildren().size()) {
                    ElementRasporeda er = (ElementRasporeda) dan.getChildren().get(pozElem);
                    if (er.getEmisija().getVrsta().getId() == idVrste) {
                        noviDan = pozDan;
                        noviElem = pozElem;
                        return true;
                    }
                    pozElem++;
                }
                pozDan++;
                pozElem = 0;
            }
            return false;
        }

        @Override
        public Object next() {
            if (hasNext()) {
                indexDan = noviDan;
                indexElement = noviElem;
                List<KomponentaRasporeda> er = ((DanRasporeda) raspored.getChildren().get(indexDan)).getChildren();
                ElementRasporeda elem = (ElementRasporeda) er.get(noviElem);
                indexElement++;
                return elem;
            }
            return null;
        }
    }
}
