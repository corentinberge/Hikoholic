package com.example.calcul;

import java.util.*;

public class NearestPoint
{

    //Trouve le point le plus proche de soit par rapport à la base de données
    private static Point searchPoint ( Point myPosition, List <Point> listPoints )
    {
        //Permet de ne pas faire de sortie de liste
        if(listPoints.size() == 0){
            return null;
        }
        
        Distance distance = new Distance();
        double bestDistance = distance.getDistance( myPosition , listPoints.get(0));
        int nbPoints = listPoints.size();
        int winningIndex = 0;

        //Parcourt de la liste
        for(int i=0; i<nbPoints; i++)
        {

            if (distance.getDistance( myPosition , listPoints.get(i)) < bestDistance)
            {
                bestDistance = distance.getDistance( myPosition , listPoints.get(i));
                winningIndex = i;
            }

        }

        return listPoints.get(winningIndex);

    };

    //Retourne le point le plus proche
    public Point get( Point myPosition, List <Point> listPoints)
    {
        return searchPoint (myPosition, listPoints);
    }


}