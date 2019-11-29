/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.foi.uzdiz.bradinovi.observer;

import org.foi.uzdiz.bradinovi.podaci.Osoba;
import org.foi.uzdiz.bradinovi.podaci.Par;
import org.foi.uzdiz.bradinovi.podaci.Uloga;

/**
 *
 * @author Borna
 */
public interface Observer {

    public void update(long idOsobe, Uloga stara, Uloga nova);
    public long getID();
}
