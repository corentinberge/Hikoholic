package com.example.calcul;

public class Distance {

    //Créateur
    public Distance ()
    {

    }

    //Calcule la distance entre 2 points
    private static double calculateDistance(Point A, Point B)
    {

        double distance_AB = 0;

        double lat_A = Math.toRadians((double)A.getLat());
        double lon_A = Math.toRadians((double)A.getLon());
        double lat_B = Math.toRadians((double)B.getLat());
        double lon_B = Math.toRadians((double)B.getLon());

        distance_AB = 6371000*Math.acos(Math.sin(lat_A)*Math.sin(lat_B)+Math.cos(lat_A)*Math.cos(lat_B)*Math.cos(lon_B-lon_A));

        return distance_AB;

    };

    //Retourne la distance entre 2 points
    public static double getDistance(Point A, Point B)
    {
        return calculateDistance( A, B );
    };
}
