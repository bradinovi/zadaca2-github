package org.foi.uzdiz.bradinovi.raspored;

import java.time.LocalTime;

public class DanRasporeda extends SlozenaKomponentaRasporeda {

    private String naziv;

    public DanRasporeda(String val) {
        super(null);
        naziv = val;
    }

    @Override
    public void prikazi() {
        System.out.println(naziv);
        super.prikazi();
    }

    @Override
    public LocalTime vratiTrajanje() {
        LocalTime trajanjePoDanima = LocalTime.of(0, 0, 0);
        System.out.println();
        System.out.println(naziv);
        System.out.println();
        for (KomponentaRasporeda c : children) {
            trajanjePoDanima = trajanjePoDanima.plusHours(c.vratiTrajanje().getHour());
            trajanjePoDanima = trajanjePoDanima.plusMinutes(c.vratiTrajanje().getMinute());
        }
        return trajanjePoDanima;
    }

}
