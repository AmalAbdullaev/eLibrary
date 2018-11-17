package com.youngbrains.application.service;

import java.util.HashMap;

public class Transliterator extends HashMap<Character, String> {
    private static Transliterator INSTANCE;

    private Transliterator() {
        put(':', "_");
        put(' ', "_");
        put('а', "a");
        put('б', "b");
        put('в', "v");
        put('г', "g");
        put('д', "d");
        put('е', "ye");
        put('ё', "ye");
        put('ж', "zh");
        put('з', "z");
        put('и', "i");
        put('й', "y");
        put('к', "k");
        put('л', "l");
        put('м', "m");
        put('н', "n");
        put('о', "o");
        put('п', "p");
        put('р', "r");
        put('с', "s");
        put('т', "t");
        put('у', "u");
        put('ф', "f");
        put('х', "kh");
        put('ц', "ts");
        put('ч', "ch");
        put('ш', "sh");
        put('щ', "shch");
        put('ъ', "");
        put('ы', "y");
        put('ь', "");
        put('э', "e");
        put('ю', "yu");
        put('я', "ya");
    }

    public static Transliterator getInstance() {
        if (INSTANCE == null)
            INSTANCE = new Transliterator();
        return INSTANCE;
    }

    public String transliterate(String message) {
        message = message.toLowerCase();
        StringBuilder result = new StringBuilder();
        for (int i = 0; i < message.length(); i++) {
            char symbol = message.charAt(i);
            String latinSymbol = get(symbol);
            if (latinSymbol != null)
                result.append(latinSymbol);
            else
                result.append(symbol);
        }
        return result.toString();
    }
}
