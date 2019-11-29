package org.foi.uzdiz.bradinovi;

import org.foi.uzdiz.bradinovi.builderi.raspored.BuilderRasporeda;
import org.foi.uzdiz.bradinovi.builderi.raspored.GeneratorRasporeda;
import org.foi.uzdiz.bradinovi.builderi.raspored.TjedniRasporedBuilder;
import org.foi.uzdiz.bradinovi.greske.KreiranjeResursaException;
import org.foi.uzdiz.bradinovi.kreatori.KreatorOsobe;
import org.foi.uzdiz.bradinovi.kreatori.KreatorPrograma;
import org.foi.uzdiz.bradinovi.kreatori.KreatorUloge;
import org.foi.uzdiz.bradinovi.kreatori.KreatorVrsta;
import org.foi.uzdiz.bradinovi.podaci.*;
import org.foi.uzdiz.bradinovi.raspored.TjedniRaspored;
import org.foi.uzdiz.bradinovi.upravitelji.UcitavanjeHelper;

import java.io.File;
import java.time.LocalTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import org.foi.uzdiz.bradinovi.greske.ResporedCompositeException;
import org.foi.uzdiz.bradinovi.iteratori.EmisijaVrstaIterator;
import org.foi.uzdiz.bradinovi.iteratori.EmisijeIterator;
import org.foi.uzdiz.bradinovi.observer.PromjenaRasporedaPublisher;
import org.foi.uzdiz.bradinovi.raspored.DanRasporeda;
import org.foi.uzdiz.bradinovi.raspored.ElementRasporeda;
import org.foi.uzdiz.bradinovi.raspored.KomponentaRasporeda;
import org.foi.uzdiz.bradinovi.raspored.SlozenaKomponentaRasporeda;

public class TVKuca extends SlozenaKomponentaRasporeda {

    String datotekaTVKucaPutanja;
    HashMap<Long, Uloga> ulogeSve;
    HashMap<Long, Vrsta> sveVrste;
    
    PromjenaRasporedaPublisher publisher;

    public TVKuca(String naziv) {
        super(null);
    }

    public void init(String datotekaTVKuca, String datotekaOsobe, String datotekaUloge, String datotekaEmisije, String datotekaVrste) throws KreiranjeResursaException {
        System.out.println("Učitavanje počinje...");
        this.datotekaTVKucaPutanja = new File(new File(datotekaTVKuca).getAbsolutePath()).getParent();
        List<TVProgram> programi = null;
        List<Uloga> uloge = null;
        List<Osoba> osobe = null;
        List<Vrsta> vrsteEmisija = null;
        try {
            programi = UcitavanjeHelper.ucitajResurse(datotekaTVKuca, new KreatorPrograma());
            uloge = UcitavanjeHelper.ucitajResurse(datotekaUloge, new KreatorUloge());
            osobe = UcitavanjeHelper.ucitajResurse(datotekaOsobe, new KreatorOsobe());
            vrsteEmisija = UcitavanjeHelper.ucitajResurse(datotekaVrste, new KreatorVrsta());
        } catch (KreiranjeResursaException e) {
            System.out.println(e.getMessage());
            return;
        }
        List<Emisija> emisije = UcitavanjeHelper.ucitajEmisije(datotekaEmisije, uloge, osobe, vrsteEmisija);
        if (emisije.isEmpty()) {
            throw new KreiranjeResursaException("Nema emisija");
        }
        BuilderRasporeda builder = new TjedniRasporedBuilder();
        generirajRasporede(programi, builder, emisije, osobe, uloge);
        sveVrste = new HashMap<>();
        for (Vrsta v : vrsteEmisija) {
            sveVrste.put(v.getId(), v);
        }
        ulogeSve = new HashMap<>();
        for (Uloga u : uloge) {
            ulogeSve.put(u.getId(), u);
        }
        System.out.println("Učitavanje završeno...");
        System.out.println();
    }

    private void generirajRasporede(List<TVProgram> programi, BuilderRasporeda builder, List<Emisija> emisije, List<Osoba> osobe, List<Uloga> uloge) {
        publisher = new PromjenaRasporedaPublisher();
        for (TVProgram program : programi) {
            program.setNazivDatoteke(this.datotekaTVKucaPutanja + "\\" + program.getNazivDatoteke());
            List<String> listaParametara = UcitavanjeHelper.ucitajDatoteku(program.getNazivDatoteke());
            builder.init(program, emisije, listaParametara, osobe, uloge);
            GeneratorRasporeda generatorRasporeda = new GeneratorRasporeda(builder);
            TjedniRaspored rasporedPrograma = generatorRasporeda.make();
            rasporedPrograma.setProgram(program);
            try {
                this.add(rasporedPrograma);
            } catch (ResporedCompositeException ex) { }
            publisher.attach(rasporedPrograma);
        }
    }

    public void dajIzbornik() {
        boolean radi = true;
        Scanner reader = new Scanner(System.in);
        while (radi) {
            int odabir;
            System.out.println();
            System.out.println("    TV KUĆA");
            System.out.println();
            System.out.println("    OPCIJE");
            System.out.println();
            System.out.println("[1] Ispis vremenskog plana");
            System.out.println("[2] Prihodi od reklama");
            System.out.println("[3] Ispis vremenskog plana za određenu vrstu emisije");
            System.out.println("[4] Promjena uloge osobe u emisiji");
            System.out.println("[5] Izlaz    ");
            System.out.println("[6] Full raspored");
            System.out.println();
            System.out.println("Unesite broj opcije: ");
            odabir = reader.nextInt();
            int i = 0;
            switch (odabir) {
                case 1:
                    DanRasporeda danPrikaz = odabirProgramDan();
                    if (danPrikaz != null) {
                        danPrikaz.prikazi();
                    }
                    break;
                case 3:
                    ispisZaVrstu();
                    break;
                case 4:
                    ispisZaPromjene();
                    break;
                case 5:
                    System.out.println("Gasim...");
                    radi = false;
                    break;
                case 6:
                    i = 0;
                    for (KomponentaRasporeda entry : getChildren()) {
                        TjedniRaspored tr = (TjedniRaspored) entry;
                        System.out.println("[" + i + "] " + tr.getProgram().getNaziv());
                        i++;
                    }
                    reader = new Scanner(System.in);
                    System.out.println("Unesite broj programa: ");
                    odabir = reader.nextInt();
                    if (odabir >= this.getChildren().size() || odabir < 0) {
                        System.out.println("Opcija ne postoji.");
                        break;
                    }
                    TjedniRaspored tjedniRaspored = (TjedniRaspored) getChildren().get(odabir);
                    tjedniRaspored.prikazi();
                    break;
                default:
                    System.out.println("Opcija ne postoji.");
                    break;
            }
        }
        reader.close();
    }

    private DanRasporeda odabirProgramDan() {
        int odabir;
        int i = 0;
        for (KomponentaRasporeda entry : getChildren()) {
            TjedniRaspored tr = (TjedniRaspored) entry;
            System.out.println("[" + i + "] " + tr.getProgram().getNaziv());
            i++;
        }
        Scanner reader = new Scanner(System.in);
        System.out.println("Unesite broj programa: ");
        odabir = reader.nextInt();
        if (odabir >= this.getChildren().size() || odabir < 0) {
            System.out.println("Opcija ne postoji.");
            return null;
        }
        TjedniRaspored tjedniRaspored = (TjedniRaspored) getChildren().get(odabir);
        System.out.println("Unesite redni broj dana u tjednu (1-pon,...,7-ned): ");
        odabir = reader.nextInt();
        if (odabir > 7 || odabir < 0) {
            System.out.println("Opcija ne postoji.");
            return null;
        }
        return (DanRasporeda) tjedniRaspored.getChildren().get(odabir - 1);
    }

    private void ispisZaVrstu() {
        System.out.println("");
        System.out.println("Vrste emisija");
        int odabir = 0;
        Scanner reader = new Scanner(System.in);
        for (Map.Entry<Long, Vrsta> entry : sveVrste.entrySet()) {
            Vrsta v = entry.getValue();
            System.out.println("[" + v.getId() + "] " + v.getVrstaEmisije());
        }
        System.out.println("");
        System.out.println("Unesite broj vrste: ");
        odabir = reader.nextInt();

        int i = 0;
        for (KomponentaRasporeda entry : getChildren()) {
            TjedniRaspored tr = (TjedniRaspored) entry;
            for (EmisijaVrstaIterator iter = tr.getVrstaIterator(odabir); iter.hasNext();) {
                ElementRasporeda er = (ElementRasporeda) iter.next();
                er.prikazi();
            }
        }
    }

    private void ispisZaPromjene() {
        System.out.println("");
        System.out.println("Vrste emisija");
        long osoba = 0;
        long ulogaStara = 0;
        long ulogaNova = 0;
        Scanner reader = new Scanner(System.in);
        System.out.println("Unesite ID osobe: ");
        osoba = reader.nextInt();

        reader = new Scanner(System.in);
        System.out.println("Uloge osobe");
        for (Map.Entry<Long, Uloga> entry : dajUlogeOsobe(osoba).entrySet()) {
            Uloga u = entry.getValue();
            System.out.println("[" + u.getId() + "] " + u.getNazivUloge());
        }

        System.out.println("");
        System.out.println("Unesite broj STARE Uloge: ");
        ulogaStara = reader.nextInt();

        System.out.println("Dostupne uloge");
        for (Map.Entry<Long, Uloga> entry : ulogeSve.entrySet()) {
            Uloga u = entry.getValue();
            System.out.println("[" + u.getId() + "] " + u.getNazivUloge());
        }
        System.out.println("");
        System.out.println("Unesite broj NOVE Uloge: ");
        ulogaNova = reader.nextInt();

        publisher.Notify(osoba, ulogeSve.get(ulogaStara), ulogeSve.get(ulogaNova));

    }

    @Override
    public LocalTime vratiTrajanje() {
        return null;
    }

    HashMap<Long, Uloga> dajUlogeOsobe(long idOsobe) {
        HashMap<Long, Uloga> uloge = new HashMap<>();
        for (KomponentaRasporeda komponentaRasporeda : getChildren()) {
            TjedniRaspored tr = (TjedniRaspored) komponentaRasporeda;
            for (EmisijeIterator iter = tr.getIterator(); iter.hasNext();) {
                ElementRasporeda er = (ElementRasporeda) iter.next();
                Emisija e = er.getEmisija();
                for (Par<Osoba, Uloga> s : e.getSudionici()) {
                    if (s.getP1().getId() == idOsobe) {
                        uloge.put(s.getP2().getId(), s.getP2());
                    }
                }
            }
        }
        return uloge;
    }

}
