package com.example.calcul;

import java.util.ArrayList;
import java.util.List;

public class PeakInFront {

    //Retourne une liste de points de la base de données
    public static List<Point> searchPeak(Point me, List<Point> list, double cap, int mode){
        List<Point> Front = new ArrayList<>();
        double azimuth;
        double distance;

        //Si on souhaite tous les sommets à 20km
        if(mode == 0){
            for(int i =0;i<list.size();i++){
                distance = Distance.getDistance(me,list.get(i));
                //20km = 20000m
                if(distance < 20000){
                    Front.add(list.get(i));
                }
            }
        }

        //Si on souhaite tous les sommets à 20km en face de nous
        else if(mode == 1){
            for(int i =0;i<list.size();i++){
                azimuth = Orientation.getAzimuth(me,list.get(i));
                //On cherche les sommets visibles entre -20° et 20°
                if(azimuth > cap-20 && azimuth < cap+20){
                    distance = Distance.getDistance(me,list.get(i));
                    //20km = 20000m
                    if(distance < 20000){
                        Front.add(list.get(i));
                    }
                }
            }
        }

        return Front;
    }

}
