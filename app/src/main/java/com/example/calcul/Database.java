package com.example.calcul;

import android.content.Context;

import com.example.hikoholic.R;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.StringTokenizer;

public class Database {

    //Attributs
    public static ArrayList<Point> Peak;
    public static int which_db;

    //Fonctions

    public Database(){

    }

    //Permet de récupérer de la base de données choisie
    public Database(Context ctx) throws IOException {
        Peak = new ArrayList<Point>();
        Point point;
        double lat,longi,alt;
        String string = new String("");
        StringTokenizer tokenizer;
        InputStream is = null;

        //Sélection de la base de donnée
        if(which_db == 1){
            is = ctx.getResources().openRawResource(R.raw.sommets2600);
        }
        else if (which_db == 2){
            is = ctx.getResources().openRawResource(R.raw.toulouse);
        }

        //Lecture du fichier de base de données
        BufferedReader br = new BufferedReader(new InputStreamReader(is));
        while (true) {
            try {
                if ((string = br.readLine()) == null) break;
            }
            catch (IOException erreur) {
                erreur.printStackTrace();
            }
            tokenizer = new StringTokenizer(string,"; "); //Nos objets sont délimités par des ";"
            lat = Double.parseDouble(tokenizer.nextToken());
            longi = Double.parseDouble(tokenizer.nextToken());
            alt = Double.parseDouble(tokenizer.nextToken());

            //Création d'un point avec les données récupérés et ajout dans la base de données
            point = new Point(lat,longi,alt,tokenizer.nextToken());
            Peak.add(point);
        }

        //Fermeture du fichier
        is.close();
    }

    //Permet d'ajouter un objet dans la base de données
    public void add_Database(Point p){
        Peak.add(p);
    }

    //Retourne l'objet d'indice i de la base de données
    public Point get_Database(int i){
        return Peak.get(i);
    }

    //Permet de supprimer un objet de la base de données
    public void delete_Database(int i){
        Peak.remove(i);
    }

    //Retourne la taille de la base de données
    public int size_Database(){
        return Peak.size();
    }

}
