package com.example.UPSHolic;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.Marker;

//Classe permettant l'affichage de fenêtre lors d'un clic sur un marker
public class InfoWindow implements GoogleMap.InfoWindowAdapter {

    private final View mWindow;
    private Context mContext;

    //Constructeur de la fenêtre
    public InfoWindow(Context context) {
        mContext = context;
        mWindow = LayoutInflater.from(context).inflate(R.layout.custom_info, null);
    }

    //Déclare les informations que doit prendre la fenêtre et sur quelle page s'afficher
    private void WindowText(Marker marker, View view){

        String title = marker.getTitle();
        TextView tvTitle = (TextView) view.findViewById(R.id.title);

        if(!title.equals("")){
            tvTitle.setText(title);
        }

        String snippet = marker.getSnippet();
        TextView tvSnippet = (TextView) view.findViewById(R.id.snippet);

        if(!snippet.equals("")){
            tvSnippet.setText(snippet);
        }
    }

    //Retourne les informations de l'affichage
    @Override
    public View getInfoWindow(Marker marker) {
        WindowText(marker, mWindow);
        return mWindow;
    }

    //Retourne les informations de l'objet (fonction obligatoire avec implements)
    @Override
    public View getInfoContents(Marker marker) {
        WindowText(marker, mWindow);
        return mWindow;
    }
}
