package org.foi.uzdiz.bradinovi.podaci;

import org.foi.uzdiz.bradinovi.podaci.cloneable.ResursCloneable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Emisija extends ResursCloneable {
    private String naziv;
    private int trajanjeMinuta;
    private List<Par<Osoba,Uloga>> sudionici;
    private Vrsta vrsta;

    public Emisija() {
        super();
        sudionici = new ArrayList<>();
    }

    public Emisija(Emisija target) {
        super(target);
        sudionici = new ArrayList<>();
        if(target != null){
            this.naziv = target.getNaziv();
            this.trajanjeMinuta = target.getTrajanjeMinuta();
            this.sudionici = deepCopySudionici(sudionici);
            this.vrsta = target.getVrsta();
        }
    }

    
    public String getNaziv() {
        return naziv;
    }

    public void setNaziv(String naziv) {
        this.naziv = naziv;
    }

    public int getTrajanjeMinuta() {
        return trajanjeMinuta;
    }

    public void setTrajanjeMinuta(int trajanjeMinuta) {
        this.trajanjeMinuta = trajanjeMinuta;
    }

    public boolean addSudionik(Osoba o, Uloga u){
        for (Par<Osoba,Uloga> uo: sudionici) {
            if(uo.getP1().getId() == o.getId() && uo.getP2().getId() == u.getId())
                return false;
        }
        sudionici.add(new Par<Osoba,Uloga>(o,u));
        return true;
    }

    public boolean removeSudionik(Osoba o, Uloga u){
        for (int i = 0; i < sudionici.size(); i++) {
            Par<Osoba,Uloga>  uo = sudionici.get(i);
            if(uo.getP1().getId() == o.getId() && uo.getP2().getId() == u.getId()){
                sudionici.remove(i);
                return true;
            }
        }
        return false;
    }

    public List<Par<Osoba, Uloga>> getSudionici() {
        return sudionici;
    }


    @Override
    public ResursCloneable clone() {
        return new Emisija(this);
    }

    @Override
    public boolean equals(Object obj) {
        if(!(obj instanceof Emisija) || !super.equals(obj)) return false;
        Emisija em = (Emisija) obj;
        return em.getNaziv().equals(naziv) && em.getTrajanjeMinuta() == trajanjeMinuta && usporediListeSudionika(em.getSudionici());
    }

    private boolean usporediListeSudionika(List<Par<Osoba,Uloga>> sudionici2){
        if(sudionici.size()!=sudionici2.size()) return false;
        int[] check = new int[sudionici.size()];
        int i = 0;
        for (Par<Osoba,Uloga> sudionik2 :sudionici2) {
            Osoba s2o = sudionik2.getP1();
            Uloga s2u = sudionik2.getP2();
            for (Par<Osoba,Uloga> sudionik :sudionici) {
                Osoba s1o = sudionik.getP1();
                Uloga s1u = sudionik.getP2();
                if(s2o.equals(s1o) && s2u.equals(s1u)){
                    check[i] = 1;
                    i++;
                }
            }

        }
        return Arrays.stream(check).distinct().count() == 1 && check.length == sudionici.size();
    }

    public Vrsta getVrsta() {
        return vrsta;
    }

    public void setVrsta(Vrsta vrsta) {
        this.vrsta = vrsta;
    }
    
    public List<Par<Osoba,Uloga>> deepCopySudionici(List<Par<Osoba, Uloga>> sudionici){
        List<Par<Osoba,Uloga>> copy = new ArrayList<>();
        for (Par<Osoba, Uloga> s : sudionici) {
            Par par = new Par(new Osoba(s.getP1()), new Uloga(s.getP2()));         
            copy.add(par);
        }
        return copy;
    }

   
}   
