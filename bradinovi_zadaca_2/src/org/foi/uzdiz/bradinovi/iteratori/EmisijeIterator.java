/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.foi.uzdiz.bradinovi.iteratori;

/**
 *
 * @author Borna
 */
public interface EmisijeIterator<E> {
    public boolean hasNext();
    public E next();
}
