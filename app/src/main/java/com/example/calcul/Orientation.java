package com.example.calcul;

public class Orientation {

    public Orientation ()
    {

    }

    //Calcule l'angle entre 2 points
    private static double calculateAngle(Point A, Point B)
    {

        double lon_A = (double) A.getLon();
        double lon_B = (double) B.getLon();

        double lat_A = Math.toRadians((double) A.getLat());
        double lat_B = Math.toRadians((double) B.getLat());

        double longDiff= Math.toRadians(lon_B-lon_A);
        double y= Math.sin(longDiff)*Math.cos(lat_B);
        double x=Math.cos(lat_A)*Math.sin(lat_B)-Math.sin(lat_A)*Math.cos(lat_B)*Math.cos(longDiff);

        return (Math.toDegrees(Math.atan2(y, x))+360)%360;

    }

    //Calcule l'orientation entre 2 points
    private String calculateOrientation (Point A, Point B)
    {

        String Orientation = null;
        double angle = getAngle(A, B);

        if (22.5 <= angle &&  angle < 67.5)
        {
            Orientation = "Nord Est";
        }
        else if ( 67.5 <= angle && angle < 112.5 )
        {
            Orientation = "Est";
        }
        else if ( 112.5 <= angle && angle < 157.5 )
        {
            Orientation = "Sud Est";
        }
        else if ( 157.5 <= angle && angle < 202.5 )
        {
            Orientation = "Sud";
        }
        else if ( 202.5 <= angle && angle < 247.5 )
        {
            Orientation = "Sud Ouest";
        }
        else if ( 247.5 <= angle && angle < 292.5 )
        {
            Orientation = "Ouest";
        }
        else if ( 292.5 <= angle && angle < 337 )
        {
            Orientation = "Nord Ouest";
        }
        else if ( (0 <= angle && angle < 22.5) || (337 <= angle && angle <= 380) )
        {
            Orientation = "Nord";
        }
        return Orientation ;
    }

    //Retourne l'orientation entre 2 points
    public String getOrientation (Point A, Point B)
    {
        return calculateOrientation(A, B);
    }

    //Retourne l'angle entre 2 points
    public static double getAngle(Point A, Point B)
    {
        return calculateAngle(A, B);
    }

}
