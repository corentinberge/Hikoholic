package com.example.calcul;

import com.google.android.gms.maps.model.LatLng;

public class Point {

    private double lat ;
    private double lon;
    private double alt;
    private String name;

    //COnstructeur du point
    public Point()
    {
        this.lat = 0; //Latitude
        this.lon = 0; //Longitude
        this.alt = 0; //Altitude
        this.name = "nom"; //Son nom

    }

    //Constructeur du point avec paramètres
    public Point(double latInit, double lonInit, double altInit, String nameInit)
    {
        this.lat = latInit; //Latitude
        this.lon = lonInit; //Longitude
        this.alt = altInit; //Altitude
        this.name = nameInit; //Son nom

    }

    //Remplace la latitude du point 
    public void replaceLat( double lat)
    {
        this.lat = lat;
    }

    //Remplace la longitude du point
    public void replaceLon( double lon)
    {
        this.lon = lon;
    }

    //Remplace l'altitude du point
    public void replaceAlt( double alt)
    {
        this.alt = alt;
    }

    //Remplace le nom du point
    public void replaceName( String name)
    {
        this.name = name;
    }

    //Retourne la latitude du point
    public double getLat() { return this.lat; }

    //Retourne la longitude du point
    public double getLon()
    {
        return this.lon;
    }

    //Retourne l'altitude du point
    public double getAlt()
    {
        return this.alt;
    }

    //Retourne un objet de type LatLng avec la latitude et la longitude du point
    public LatLng getLatLng()
    {
        LatLng l = new LatLng(this.lat,this.lon);
        return l;
    }

    //Retourne le nom du point
    public String getName()
    {

        return this.name;
    }

    //Retourne un string comprennant toutes les informations du point
    public String toString(){
        return this.name + " " + this.lat + " " + this.lon + " " + this.alt + "\n";
    }
}