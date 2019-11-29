package org.foi.uzdiz.bradinovi.builderi.raspored;

import org.foi.uzdiz.bradinovi.TVKuca;
import org.foi.uzdiz.bradinovi.builderi.emisija.EmisijaBuilder;
import org.foi.uzdiz.bradinovi.builderi.emisija.EmisijaBuilderImp;
import org.foi.uzdiz.bradinovi.greske.OsobaNePostojiException;
import org.foi.uzdiz.bradinovi.greske.ResporedCompositeException;
import org.foi.uzdiz.bradinovi.greske.UlogaNePostojiException;
import org.foi.uzdiz.bradinovi.podaci.*;
import org.foi.uzdiz.bradinovi.raspored.DanRasporeda;
import org.foi.uzdiz.bradinovi.raspored.ElementRasporeda;
import org.foi.uzdiz.bradinovi.raspored.TjedniRaspored;
import org.foi.uzdiz.bradinovi.upravitelji.UcitavanjeHelper;

import java.time.Duration;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class TjedniRasporedBuilder implements BuilderRasporeda {

    private TjedniRaspored tjedniRaspored;
    private TVProgram program;
    private HashMap<Integer, List<ElementRasporeda>> trenutniRaspored;
    private HashMap<Long, Emisija> emisije = new HashMap<>();
    private Pattern regex;
    List<Osoba> osobe;
    List<Uloga> uloge;

    private String regexZadanDanPocetak = "^\\s*(?<emisija>\\d+)\\s*;\\s*((?<raspon>[1-7]-[1-7])|(?<dan>[1-7])|(?<lista>([1-7],){0,}\\s*([1-7]){0,}))\\s*;\\s*(?<pocetak>\\d{1,2}:\\d{1,2}:{0,1}\\d{1,2})\\s*;{0,1}\\s*(?<uloge>(\\s*(\\d+-\\d+,){0,}\\s*(\\d+-\\d+){0,}))\\s*;{0,1}$";
    private String regexSamoDani = "^(\\d+)\\s*;\\s*(([1-7]-[1-7])|([1-7])|(([1-7],){0,}\\s*([1-7]){0,}));\\s*(?<uloge>(\\s*((\\d+-\\d+),){0,}\\s*(\\d+-\\d+){0,}))$";
    private String regexSamoEmisija = "^(\\d+)\\s*;\\s*(?<uloge>(\\s*((\\d+-\\d+),){0,}\\s*(\\d+-\\d+){0,}));{0,1}$";
    private List<String> listaParametaraFiksneEmisije = new ArrayList<>();
    private List<String> listaParametaraDnevneEmisije = new ArrayList<>();
    private List<String> listaParametaraSlobodneEmisije = new ArrayList<>();

    public TjedniRasporedBuilder(List<Emisija> emisije, TVProgram program, List<String> listaParametara, List<Osoba> osobe, List<Uloga> uloge) {
        init(program, emisije, listaParametara, osobe, uloge);
    }

    public TjedniRasporedBuilder() {

    }

    public void init(TVProgram program1, List<Emisija> emisije1, List<String> listaParametara, List<Osoba> osobe, List<Uloga> uloge) {
        this.osobe = osobe;
        this.uloge = uloge;
        tjedniRaspored = new TjedniRaspored(program1.getNaziv(), program1.getPocetak(), program1.getKraj());
        trenutniRaspored = new HashMap<>();
        for (int i = 1; i <= 7; i++) {
            ArrayList<ElementRasporeda> lista = new ArrayList<>();
            LocalTime kraj = program1.getKraj();
            if (program1.getKraj() == null) {
                kraj = LocalTime.MAX;
            }
            LocalTime pocetak = program1.getPocetak();
            if (program1.getPocetak() == null) {
                pocetak = LocalTime.MAX;
            }
            lista.add(new ElementRasporeda(null, pocetak));
            lista.add(new ElementRasporeda(null, kraj));
            trenutniRaspored.put(i, lista);
        }
        for (Emisija e : emisije1) {
            this.emisije.put(e.getId(), e);
        }
        this.program = program1;
        //List<String> listaParametara = LoaderDatoteka.ucitajDatoteku(program1.getNazivDatoteke());
        if (listaParametara == null) {
            listaParametara = new ArrayList<>();
        }
        listaParametaraFiksneEmisije.clear();
        listaParametaraDnevneEmisije.clear();
        listaParametaraSlobodneEmisije.clear();
        odvojiParametrePoVrstama(listaParametara);
    }

    private void odvojiParametrePoVrstama(List<String> listaParametara) {
        int i = 2;
        for (String parametar : listaParametara) {
            if (parametar.matches(regexZadanDanPocetak)) {
                listaParametaraFiksneEmisije.add(parametar);
            } else if (parametar.matches(regexSamoDani)) {
                listaParametaraDnevneEmisije.add(parametar);
            } else if (parametar.matches(regexSamoEmisija)) {
                listaParametaraSlobodneEmisije.add(parametar);
            } else {
                System.out.println(String.format("[%s] Greška sintakse red: %d", program.getNazivDatoteke(), i));
            }
            i++;
        }
    }

    private boolean dodajElementRasporeduOdredenoVrijeme(List<ElementRasporeda> rasporedDana, ElementRasporeda rasporedItem) {
        for (int i = 1; i < rasporedDana.size(); i++) {
            LocalTime kraj = rasporedItem.getPocetakPrikazivanja().plusMinutes(rasporedItem.getEmisija().getTrajanjeMinuta());
            if (rasporedDana.get(i).getPocetakPrikazivanja().isAfter(rasporedItem.getPocetakPrikazivanja())
                    && (kraj.isBefore(rasporedDana.get(i).getPocetakPrikazivanja())
                    || kraj.equals(rasporedDana.get(i).getPocetakPrikazivanja()))
                    && (rasporedItem.getPocetakPrikazivanja().isAfter(rasporedDana.get(i - 1).getPocetakPrikazivanja())
                    || rasporedItem.getPocetakPrikazivanja().equals(rasporedDana.get(i - 1).getPocetakPrikazivanja()))) {
                rasporedDana.add(i, rasporedItem);
                return true;
            }
        }
        return false;
    }

    private boolean dodajElementRasporeduNeodredenoVrijeme(List<ElementRasporeda> rasporedDana, ElementRasporeda rasporedItem) {
        int index = -1;
        double trajanjeSlobodnogIntrevala;
        for (int i = 0; i < rasporedDana.size() - 1; i++) {
            LocalTime kraj = null;
            if (rasporedDana.get(i).getEmisija() == null) {
                kraj = rasporedDana.get(i).getPocetakPrikazivanja();
            } else {
                kraj = rasporedDana.get(i).getPocetakPrikazivanja().plusMinutes(rasporedDana.get(i).getEmisija().getTrajanjeMinuta());
            }
            trajanjeSlobodnogIntrevala = (double) Duration.between(kraj, rasporedDana.get(i + 1).getPocetakPrikazivanja()).toMinutes();
            if ((trajanjeSlobodnogIntrevala >= rasporedItem.getEmisija().getTrajanjeMinuta())) {
                index = i + 1;
                LocalTime krajPrethodnog;
                if (rasporedDana.get(index - 1).getEmisija() == null) {
                    krajPrethodnog = rasporedDana.get(index - 1).getPocetakPrikazivanja();
                } else {
                    krajPrethodnog = rasporedDana.get(index - 1).getPocetakPrikazivanja().plusMinutes(rasporedDana.get(index - 1).getEmisija().getTrajanjeMinuta());
                }
                rasporedItem.setPocetakPrikazivanja(krajPrethodnog);
                rasporedDana.add(index, rasporedItem);
                return true;
            }
        }
        return false;
    }

    private Emisija dodajDodatneSudionike(Emisija e, String sudionici) throws OsobaNePostojiException, UlogaNePostojiException {
        String[] sudioniciLista = sudionici.split(",");
        List<Par<Osoba, Uloga>> sud = e.deepCopySudionici(e.getSudionici());
        Emisija eNovo = (Emisija) e.clone();
        eNovo.getSudionici().addAll(sud);
        for (String s : sudioniciLista) {
            String[] ulogaosoba = s.split("-");
            long osobaId = Long.parseLong(ulogaosoba[0].trim());
            long ulogaId = Long.parseLong(ulogaosoba[1].trim());
            Osoba o = EmisijaBuilderImp.nadiOsobu(osobaId, osobe);
            Uloga u = EmisijaBuilderImp.nadiUlogu(ulogaId, uloge);
            if (o == null) {
                throw new OsobaNePostojiException("Osoba ne postoji.");
            }
            if (u == null) {
                throw new UlogaNePostojiException("Uloga ne postoji");
            }
            eNovo.addSudionik(o, u);
        }
        return eNovo;
    }

    @Override
    public void promjeniProgram(TVProgram program) {
        List<String> listaParametara = UcitavanjeHelper.ucitajDatoteku(program.getNazivDatoteke());
        odvojiParametrePoVrstama(listaParametara);
        this.program = program;
    }

    @Override
    public void reset() {
        for (int i = 1; i < 7; i++) {
            trenutniRaspored.get(i).clear();
        }
        tjedniRaspored = new TjedniRaspored(program.getNaziv(), program.getPocetak(), program.getKraj());
    }

    @Override
    public BuilderRasporeda rasporediFiksneEmisije() {
        DateTimeFormatter formatterSekunde = DateTimeFormatter.ofPattern("H:m:s");
        DateTimeFormatter formatterSati = DateTimeFormatter.ofPattern("H:m");
        regex = Pattern.compile(regexZadanDanPocetak);
        Matcher m;
        for (String red : listaParametaraFiksneEmisije) {
            m = regex.matcher(red);
            m.matches();
            String id = "";
            String uloge = "";
            try {
                id = m.group("emisija");
                uloge = m.group("uloge");
            } catch (Exception e) {
                e.printStackTrace();
                System.err.println(e.getStackTrace());
            }

            Emisija e = emisije.get(Long.parseLong(id));
            if (e == null) {
                System.err.println("Ne postoji emisija s ID: " + id);
                continue;
            }
            List<Par<Osoba, Uloga>> sud = e.deepCopySudionici(e.getSudionici());
            Emisija emisijaNova = (Emisija) e.clone();
            emisijaNova.getSudionici().addAll(sud);

            if (!uloge.isEmpty()) {
                try {
                    emisijaNova = dodajDodatneSudionike(emisijaNova, uloge);
                } catch (OsobaNePostojiException | UlogaNePostojiException ex) {
                    System.err.println(ex.getMessage());
                }
            }
            String pocetakEmisije = m.group("pocetak");
            DateTimeFormatter formatter = null;
            if (pocetakEmisije.split(":").length == 3) {
                formatter = formatterSekunde;
            } else if ((pocetakEmisije.split(":").length == 2)) {
                formatter = formatterSati;
            }
            ElementRasporeda erNovi = new ElementRasporeda(emisijaNova, LocalTime.parse(pocetakEmisije, formatter));
            String rasponDana = m.group("raspon");
            String dan = m.group("dan");
            String daniLista = m.group("lista");
            int odDana = 0;
            int doDana = 0;
            if (dan != null) {
                odDana = Integer.parseInt(dan);
                doDana = odDana;
            } else if (rasponDana != null) {
                String[] daniEmisije = rasponDana.split("-");
                odDana = Integer.parseInt(daniEmisije[0]);
                doDana = Integer.parseInt(daniEmisije[1]);
            }
            if (daniLista != null) {
                String[] dani = daniLista.split(",");
                for (String d : dani) {
                    List<ElementRasporeda> rasporedDana = trenutniRaspored.get(Integer.parseInt(d));
                    if (!dodajElementRasporeduNeodredenoVrijeme(rasporedDana, erNovi)) {
                        System.out.println(String.format("[%s] Nije moguće dodati Emisija: %s Dan: %s Vrijeme: %s", program.getNaziv(), erNovi.getEmisija().getNaziv(), d, erNovi.getPocetakPrikazivanja().toString()));
                    }
                    trenutniRaspored.put(Integer.parseInt(d), rasporedDana);
                    erNovi = new ElementRasporeda(emisijaNova);
                }
                continue;
            }
            for (int i = odDana; i <= doDana; i++) {
                List<ElementRasporeda> rasporedDana = trenutniRaspored.get(i);
                if (!dodajElementRasporeduOdredenoVrijeme(rasporedDana, erNovi)) {
                    System.out.println(String.format("[%s] Nije moguće dodati Emisija: %s Dan: %d Vrijeme: %s", program.getNaziv(), erNovi.getEmisija().getNaziv(), i, erNovi.getPocetakPrikazivanja().toString()));
                }
                trenutniRaspored.put(i, rasporedDana);
            }

        }
        return this;
    }

    @Override
    public BuilderRasporeda rasporediDnevneEmisije() {
        regex = Pattern.compile(regexSamoDani);
        Matcher m;
        for (String red : listaParametaraDnevneEmisije) {
            m = regex.matcher(red);
            if (!m.matches()) {
                continue;
            };
            String daniLista;
            String dan;
            String rasponDana;
            String id = m.group(1);
            Emisija e = emisije.get(Long.parseLong(id));
            if (e == null) {
                System.err.println("Ne postoji emisija s ID: " + id);
                continue;
            }
            List<Par<Osoba, Uloga>> sud = e.deepCopySudionici(e.getSudionici());
            Emisija emisijaNova = (Emisija) e.clone();
            emisijaNova.getSudionici().addAll(sud);
            String uloge = m.group("uloge");
            if (!uloge.isEmpty()) {
                try {
                    emisijaNova = dodajDodatneSudionike(emisijaNova, uloge);
                } catch (OsobaNePostojiException | UlogaNePostojiException ex) {
                    System.out.println(ex.getMessage());
                }
            }
            ElementRasporeda erNovi = new ElementRasporeda(emisijaNova);
            if (!(m.group(5) == null)) {
                daniLista = m.group(5);
                String[] dani = daniLista.split(",");
                for (String d : dani) {
                    List<ElementRasporeda> rasporedDana = trenutniRaspored.get(Integer.parseInt(d));
                    if (!dodajElementRasporeduNeodredenoVrijeme(rasporedDana, erNovi)) {
                        System.out.println(String.format("[%s] Dnevnu emisiju nije moguće dodati. Emisija: %s Dan: %s", program.getNaziv(), erNovi.getEmisija().getNaziv(), d));
                    }
                    trenutniRaspored.put(Integer.parseInt(d), rasporedDana);
                    erNovi = new ElementRasporeda(emisijaNova);
                }
                continue;
            }
            if (!(m.group(3) == null)) {
                rasponDana = m.group(3);
                String[] daniEmisjie = rasponDana.split("-");
                int odDana = Integer.parseInt(daniEmisjie[0]);
                int doDana = Integer.parseInt(daniEmisjie[1]);
                for (int i = odDana; i <= doDana; i++) {
                    List<ElementRasporeda> rasporedDana = trenutniRaspored.get(i);
                    if (!dodajElementRasporeduNeodredenoVrijeme(rasporedDana, erNovi)) {
                        System.out.println(String.format("[%s] Dnevnu emisiju nije moguće dodati. Emisija: %s Dan: %d", program.getNaziv(), erNovi.getEmisija().getNaziv(), i));
                    }
                    trenutniRaspored.put(i, rasporedDana);
                }
                continue;
            };
            if (!(m.group(4) == null)) {
                dan = m.group(4);
                List<ElementRasporeda> rasporedDana = trenutniRaspored.get(Integer.parseInt(dan));
                if (!dodajElementRasporeduNeodredenoVrijeme(rasporedDana, erNovi)) {
                    System.out.println(String.format("[%s] Dnevnu emisiju nije moguće dodati. Emisija: %s Dan: %s", program.getNaziv(), erNovi.getEmisija().getNaziv(), dan));
                }
                trenutniRaspored.put(Integer.parseInt(dan), rasporedDana);
            }
        }
        return this;
    }

    @Override
    public BuilderRasporeda rasporediSlobodneEmisije() {
        regex = Pattern.compile(regexSamoEmisija);
        Matcher m;
        for (String red : listaParametaraSlobodneEmisije) {
            m = regex.matcher(red);
            if (m.matches()) {
                String id = m.group(1);
                Emisija e = emisije.get(Long.parseLong(id));
                if (e == null) {
                    System.err.println("Ne postoji emisija s ID: " + id);
                    continue;
                }
                List<Par<Osoba, Uloga>> sud = e.deepCopySudionici(e.getSudionici());
                Emisija emisijaNova = (Emisija) e.clone();
                emisijaNova.getSudionici().addAll(sud);

                String uloge = m.group("uloge");
                if (!uloge.isEmpty()) {
                    try {
                        emisijaNova = dodajDodatneSudionike(emisijaNova, uloge);
                    } catch (OsobaNePostojiException | UlogaNePostojiException ex) {
                        System.out.println(ex.getMessage());
                    }
                }
                ElementRasporeda erNovi = new ElementRasporeda(emisijaNova);
                for (int i = 1; i <= 7; i++) {
                    List<ElementRasporeda> rasporedDana = trenutniRaspored.get(i);
                    if (!dodajElementRasporeduNeodredenoVrijeme(rasporedDana, erNovi)) {
                        System.out.println(String.format("[%s] Dnevnu emisiju nije moguće dodati. Emisija: %s Dan: %d", program.getNaziv(), erNovi.getEmisija().getNaziv(), i));
                    }
                    trenutniRaspored.put(i, rasporedDana);
                    erNovi = new ElementRasporeda(emisijaNova);
                }
            }
        }
        return this;
    }

    @Override
    public TjedniRaspored build() {
        TjedniRaspored product = tjedniRaspored;
        for (Integer key : trenutniRaspored.keySet()) {
            DanRasporeda d = new DanRasporeda(Tjedan.valueOf(key).toString());
            trenutniRaspored.get(key).remove(0);
            trenutniRaspored.get(key).remove(trenutniRaspored.get(key).size() - 1);
            for (ElementRasporeda el : trenutniRaspored.get(key)) {
                try {
                    d.add(el);
                } catch (ResporedCompositeException e) {
                    System.err.println(e.getMessage());
                }
            }
            try {
                tjedniRaspored.add(d);
            } catch (ResporedCompositeException e) {
                System.err.println(e.getMessage());
            }
        }
        this.reset();
        return product;
    }

}
