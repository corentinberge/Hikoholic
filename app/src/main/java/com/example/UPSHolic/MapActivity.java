package com.example.UPSHolic;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import com.example.calcul.Database;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import android.view.View;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

//Classe qui permet de récupérer l'affichage de la carte
public class MapActivity extends AppCompatActivity implements OnMapReadyCallback, GoogleMap.OnInfoWindowClickListener {

    private GoogleMap mMap;
    private SupportMapFragment mapFragment;
    private FusedLocationProviderClient fusedLocationProviderClient;
    private TextView tv_map_title,tv_map_subtitle;
    private Switch sw_list;

    
    //Créé l'affichage dès que le switch est enclenché
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        
        super.onCreate(savedInstanceState);
        setContentView(R.layout.map); //Récupérer de l'affichage de la map
        
        //Permet la création d'une carte avec une API Google
        mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.fr_map);
        mapFragment.getMapAsync(this);

        //Permet de récupérer les permissions du téléphone et prévient des erreurs
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        
        //Initialisation des variables d'affichages avec leurs IDs
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
        tv_map_title = findViewById(R.id.tv_title_map);
        tv_map_subtitle = findViewById(R.id.tv_subtitle_map);
        sw_list = findViewById(R.id.sw_list);
        
        //Changement du nom du titre en fonction de la base de données choisie
        if(Database.whichDB == 1){
            tv_map_title.setText("Hikoholic");
            tv_map_subtitle.setText("Addicted to hiking");
        }
        else if(Database.whichDB == 2){
            tv_map_title.setText("Metroholic");
            tv_map_subtitle.setText("Addicted to taking metro");
        }
        
        //Récupération d'un appuie sur le switch pour terminer l'affichage et retourner au listing
        sw_list.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!sw_list.isChecked()) {
                    finish();
                }
            }
        });
    }

    //Initialisation de la carte
    @Override
    public void onMapReady(GoogleMap googleMap) {
        
        mMap = googleMap;
        
        //Permet d'ajouter des markers en parcourant la base de données
        for (int i = 0; i < ListActivity.db.size_Database(); i++) {
            mMap.addMarker(new MarkerOptions()
                    .position(ListActivity.db.get_Database(i).getLatLng())
                    .title(ListActivity.db.get_Database(i).getName())
                    .snippet("Latitude : " + ListActivity.db.get_Database(i).getLat() + "\nLongitude : " + ListActivity.db.get_Database(i).getLon() + "\nAltitude : " + ListActivity.db.get_Database(i).getAlt()));
            mMap.animateCamera(CameraUpdateFactory.zoomTo(18.0f));
            mMap.moveCamera(CameraUpdateFactory.newLatLng(ListActivity.db.get_Database(i).getLatLng()));
        }

        //Ajout d'une fenêtre d'information si on clique sur un marker
        MarkerOptions mark = new MarkerOptions().position(ListActivity.me.getLatLng()).icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE)).title("Ma position");
        mMap.addMarker(mark);
        mMap.setInfoWindowAdapter(new InfoWindow(MapActivity.this));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(ListActivity.me.getLatLng()));
    }

    //Récupère les informations du point et les affiche dans la fenêtre d'information
    @Override
    public void onInfoWindowClick(Marker marker){
        Toast.makeText(this,"Info Window is ", Toast.LENGTH_LONG).show();
    }
}
