package org.foi.uzdiz.bradinovi.podaci;

import java.util.HashMap;

public class Vrsta implements ResursTVKuce {
    long id;
    String vrstaEmisije;
    boolean reklame;
    int trajanjeReklamaMax;

    @Override
    public void postaviVrijednosti(HashMap<Kljucevi, String> parametri) {
        id = Long.parseLong(parametri.get(Kljucevi.IDVRSTEEMISIJE));
        vrstaEmisije = parametri.get(Kljucevi.NAZIVVRSTEEMISIJE);
        reklame = parametri.get(Kljucevi.REKLAMEEMISIJE).equals("1");
        trajanjeReklamaMax = Integer.parseInt(parametri.get(Kljucevi.TRAJANEREKLAMA));
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getVrstaEmisije() {
        return vrstaEmisije;
    }

    public void setVrstaEmisije(String vrstaEmisije) {
        this.vrstaEmisije = vrstaEmisije;
    }

    public boolean isReklame() {
        return reklame;
    }

    public void setReklame(boolean reklame) {
        this.reklame = reklame;
    }

    public int getTrajanjeReklamaMax() {
        return trajanjeReklamaMax;
    }

    public void setTrajanjeReklamaMax(int trajanjeReklamaMax) {
        this.trajanjeReklamaMax = trajanjeReklamaMax;
    }

}
