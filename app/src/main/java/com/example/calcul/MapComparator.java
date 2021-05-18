package com.example.calcul;

import java.util.Comparator;
import java.util.Map;

//Classe permettant l'ajout d'info pour les marqueurs de la carte
public class MapComparator implements Comparator<Map<String, String>>
{
    private final String key;
    
    //L'élément à comparer
    public MapComparator(String key)
    {
        this.key = key;
    }

    //Compare l'élément à comparer avec le second élément
    public int compare(Map<String, String> first,
                       Map<String, String> second)
    {
        // TODO: Null checking, both for maps and values
        String firstValue = first.get(key);
        String secondValue = second.get(key);
        return firstValue.compareTo(secondValue);
    }
}

