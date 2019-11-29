/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.foi.uzdiz.bradinovi.decorator;

/**
 *
 * @author Borna
 */
public class DecoratorBase implements DecoratorComponent{
    private DecoratorComponent wrappee;

    public DecoratorBase(DecoratorComponent wrappee) {
        this.wrappee = wrappee;
    }

    @Override
    public String dajString() {
        return wrappee.dajString();     
    }
    
}
