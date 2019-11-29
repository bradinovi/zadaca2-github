package org.foi.uzdiz.bradinovi.builderi.raspored;

import java.util.HashMap;
import java.util.Map;

//https://codingexplained.com/coding/java/enum-to-integer-and-integer-to-enum

public enum Tjedan {
    PON(1){
        @Override
        public String toString() {
            return "PONEDJELJAK";
        }
    },
    UTO(2){
        @Override
        public String toString() {
            return "UTORAK";
        }
    },
    SRI(3){
        @Override
        public String toString() {
            return "SRIJEDA";
        }
    },
    CET(4){
        @Override
        public String toString() {
            return "CETVRTAK";
        }
    },
    PET(5){
        @Override
        public String toString() {
            return "PETAK";
        }
    },
    SUB(6){
        @Override
        public String toString() {
            return "SUBOTA";
        }
    },
    NED(7){
        @Override
        public String toString() {
            return "NEDJELJA";
        }
    }
    ;


    private int value;
    private static Map map = new HashMap<>();

    private Tjedan(int value) {
        this.value = value;
    }

    static {
        for (Tjedan pageType : Tjedan.values()) {
            map.put(pageType.value, pageType);
        }
    }

    public static Tjedan valueOf(int pageType) {
        return (Tjedan) map.get(pageType);
    }

    public int getValue() {
        return value;
    }

}


