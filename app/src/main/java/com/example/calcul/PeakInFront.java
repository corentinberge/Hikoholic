package com.example.calcul;

import java.util.ArrayList;
import java.util.List;

public class PeakInFront {

    //Retourne une liste de points de la base de données
    public static List<Point> search_Peak(Point me, List<Point> list,double azimuth, int mode){
        List<Point> Front = new ArrayList<>();
        double angle;
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
                angle = Orientation.getAngle(me,list.get(i));
                //On cherche les sommets visibles entre -20° et 20°
                if(angle > azimuth-20 && angle < azimuth+20){
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
