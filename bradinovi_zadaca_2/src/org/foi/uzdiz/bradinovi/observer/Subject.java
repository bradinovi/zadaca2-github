/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.foi.uzdiz.bradinovi.observer;

import java.util.HashMap;
import java.util.List;
import org.foi.uzdiz.bradinovi.podaci.Osoba;
import org.foi.uzdiz.bradinovi.podaci.Par;
import org.foi.uzdiz.bradinovi.podaci.Uloga;



/**
 *
 * @author Borna
 */
public abstract class Subject {
   protected HashMap<Long,Observer> observers;
   public abstract void Notify(long idOsobe, Uloga stara, Uloga nova); 

    public Subject() {
        this.observers = new HashMap<>();
    }
   
   public void attach(Observer o){
       observers.put(o.getID(), o);
   };
   public void detach(Observer o){
       observers.remove(o.getID());
   };   
}
