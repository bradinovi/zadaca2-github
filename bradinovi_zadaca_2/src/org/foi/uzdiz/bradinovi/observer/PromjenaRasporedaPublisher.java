/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.foi.uzdiz.bradinovi.observer;

import java.util.Map;
import org.foi.uzdiz.bradinovi.podaci.Osoba;
import org.foi.uzdiz.bradinovi.podaci.Par;
import org.foi.uzdiz.bradinovi.podaci.Uloga;

/**
 *
 * @author Borna
 */
public class PromjenaRasporedaPublisher extends Subject{

    @Override
    public void Notify(long idOsobe, Uloga stara, Uloga nova) {
        for (Map.Entry<Long,Observer> entry : observers.entrySet()) {
            Observer o = entry.getValue();
            o.update(idOsobe, stara, nova);
        }
    }

   
    
}
