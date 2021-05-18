package com.example.hikoholic;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.appcompat.widget.Toolbar;
import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.widget.CheckBox;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;
import com.example.calcul.Database;
import com.example.calcul.Distance;
import com.example.calcul.MapComparator;
import com.example.calcul.Point;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.view.View;
import android.widget.Switch;
import com.google.android.gms.location.LocationResult;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

//Affichage sous forme de liste des objets de la base de données
public class ListActivity extends AppCompatActivity implements SensorEventListener {

    //Variables utiles au programme
    private static final int permission_location = 22;
    private SensorManager mSensorManager;
    private Sensor mRotationV, mAccelerometer, mMagnetometer;
    private final float[] mLastAccelerometer = new float[3];
    private float[] mLastMagnetometer = new float[3];
    private boolean mLastAccelerometerSet = false;
    private boolean mLastMagnetometerSet = false;
    private List<Point> Peak;
    private Map<Double,String> dist;

    public static Point me;
    public static Database db;
    public static int mode = 0;
    int mAzimuth;
    float[] rMat = new float[9];
    float[] orientation = new float[3];
    boolean haveSensor = false, haveSensor2 = false;
    TextView tv_orientation,tv_text,tv_list_title,tv_list_subtitle;
    Switch sw_maps;
    Toolbar Bar;
    CheckBox cb_location;
    FusedLocationProviderClient LocationProvider;// fonction par default de l'API de google
    LocationRequest locationRequest;
    LocationCallback locationCallback;
    ListView peakFront;
    SimpleAdapter Adapter;
    
    
    //Création de l'interface lors de la sélection de la base de données dans la page d'accueil
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        setTheme(R.style.Theme_Hikoholic);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.listing); //Page de listing

        //Creation de la database depuis un fichier txt
        db = new Database();
        try {
            db = new Database(ListActivity.this);
        } catch (IOException e) {
            e.printStackTrace();
        }

        //Initialisation des variables de l'interface avec leurs IDs
        peakFront = findViewById(R.id.sw_bangle);
        cb_location = findViewById(R.id.cb_locationupdates);
        sw_maps = findViewById(R.id.sw_map);
        tv_orientation = findViewById(R.id.tv_orientation);
        tv_text = findViewById(R.id.tv_peak);
        tv_list_title = findViewById(R.id.tv_title_list);
        tv_list_subtitle = findViewById(R.id.tv_subtitle_list);

        //Changement de titre de ToolBar pour notifier quelle base de données est utilisée
        Bar = findViewById(R.id.MyToolBar);
        setSupportActionBar(Bar);
        //Base de donnée des sommets
        if(Database.which_db == 1){
            tv_list_title.setText("Hikoholic");
            tv_list_subtitle.setText("Addicted to hiking");
            tv_text.setText("Liste des sommets :");
        }
        //Base de données des métros
        else if(Database.which_db == 2){
            tv_list_title.setText("Metroholic");
            tv_list_subtitle.setText("Addicted to taking metro");
            tv_text.setText("Liste des metros :");
        }

        //Permet de récupérer la localisation
        mSensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        locationRequest = new LocationRequest();
        locationRequest.setInterval(5000);// request chaque 30000 ms
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);//to use GPS methode
        locationCallback = new LocationCallback() {

            @Override
            public void onLocationResult(@NonNull LocationResult locationResult) {
                super.onLocationResult(locationResult);
                Location location = locationResult.getLastLocation();
                UpdatePointMe(location);
                Start_Azimuth();
            }

        };

        //Détection d'un changement sur le clic de la case a cocher
        cb_location.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //Si la case est cochée, sélection des sommets juste en face de nous
                if (cb_location.isChecked()) {
                    // location tracking on
                    startLocationUpdates();
                    mode = 1;
                    if(Database.which_db == 1){
                        cb_location.setText("Sommets en face de nous"); //Mode sommets
                    }
                    else {
                        cb_location.setText("Metros en face de nous"); //Mode métros
                    }
                } else {
                    // no tracking
                    startLocationUpdates();
                    mode = 0;
                    if(Database.which_db == 1){
                        cb_location.setText("Tous les sommets à 20km"); //Mode sommets
                    }
                    else {
                        cb_location.setText("Tous les metros à 20km"); //Mode métros
                    }
                }
            }
        });

        //Détection d'un appuie sur le switch pour passer en mode map
        sw_maps.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Si le switch est actif, passage en mode map
                if (sw_maps.isChecked()){
                    startActivity(new Intent(ListActivity.this,MapActivity.class));
                    sw_maps.setChecked(false);
                }
            }
        });
        
        //Récupération de la localisation
        updateGPS();
    }


    //Permet de démarrer la localition en temps continu de la géolocalisation
    private void startLocationUpdates() {
        // c est gerer par android studio not of my business
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        LocationProvider.requestLocationUpdates(locationRequest, locationCallback, null);
        updateGPS();

    }

    //Permet de récupérer les informations si l'utilisateur a donné les droits nécessaires à l'application
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        switch (requestCode) {
            case permission_location:
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {//si la permission et le request son bien
                    updateGPS();
                } else {
                    Toast.makeText(this, "we need a permission to continue", Toast.LENGTH_SHORT).show();
                    finish();//comme le exit() en C
                }
                break;
        }
    }


    //Mets à jour la position GPS si les droits sont attribués
    private void updateGPS() {
        LocationProvider = LocationServices.getFusedLocationProviderClient(ListActivity.this);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {

            LocationProvider.getLastLocation().addOnSuccessListener(this, new OnSuccessListener<Location>() {
                @Override
                public void onSuccess(Location location) { //ca donne la valeur de location

                    // on a la permissions
                    UpdatePointMe(location);
                }
            });
        } else {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                // test si on a la bonne version du l'operating systemes
                requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, permission_location);
            }
        }

    }

    //Récupère la localisation dans un point 
    private void UpdatePointMe(Location location) {
        
        me = new Point(location.getLatitude(),location.getLongitude(),location.getAltitude(),"Moi");
    }


    //Permet de récupérer la modification du gyroscope du téléphone
    @Override
    public void onSensorChanged(SensorEvent event) {

        if (event.sensor.getType() == Sensor.TYPE_ROTATION_VECTOR) {
            SensorManager.getRotationMatrixFromVector(rMat, event.values);
            mAzimuth = (int) (Math.toDegrees(SensorManager.getOrientation(rMat, orientation)[0]) + 360) % 360;
        }

        if (event.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
            System.arraycopy(event.values, 0, mLastAccelerometer, 0, event.values.length);
            mLastAccelerometerSet = true;
        } else if (event.sensor.getType() == Sensor.TYPE_MAGNETIC_FIELD) {
            System.arraycopy(event.values, 0, mLastMagnetometer, 0, event.values.length);
            mLastMagnetometerSet = true;
        }
        if (mLastAccelerometerSet && mLastMagnetometerSet) {
            SensorManager.getRotationMatrix(rMat, null, mLastAccelerometer, mLastMagnetometer);
            SensorManager.getOrientation(rMat, orientation);
            mAzimuth = (int) (Math.toDegrees(SensorManager.getOrientation(rMat, orientation)[0]) + 360) % 360;
        }

        mAzimuth = Math.round(mAzimuth);

        String where = "NW";

        if (mAzimuth >= 350 || mAzimuth <= 10)
            where = "N";
        if (mAzimuth < 350 && mAzimuth > 280)
            where = "NW";
        if (mAzimuth <= 280 && mAzimuth > 260)
            where = "W";
        if (mAzimuth <= 260 && mAzimuth > 190)
            where = "SW";
        if (mAzimuth <= 190 && mAzimuth > 170)
            where = "S";
        if (mAzimuth <= 170 && mAzimuth > 100)
            where = "SE";
        if (mAzimuth <= 100 && mAzimuth > 80)
            where = "E";
        if (mAzimuth <= 80 && mAzimuth > 10)
            where = "NE";
        
        Peak = com.example.calcul.PeakInFront.search_Peak(me,db.Peak,mAzimuth,mode);

        double distance;
        ArrayList<Map<String, String>> item = new ArrayList<Map<String, String>>();
        
        //test si la liste est vide et prévient des potentielles erreurs
        if(Peak.size()==0){
            Map<String, Object> map = new HashMap<String, Object>();
            map.put("name","Attention");
            map.put("distance","Il n'y a pas de sommets à proximité ou en face de vous !");
        }
        else{
            for(int i=0;i<Peak.size();i++){
                Map<String, String> map = new HashMap<String, String>();
                map.put("name",Peak.get(i).getName());
                distance = Distance.getDistance(me,Peak.get(i));
                DecimalFormat f = new DecimalFormat();
                f.setMaximumFractionDigits(0);
                map.put("distance",f.format(distance));
                item.add(map);
            }
        }

        //Permet de trier la liste de manière croissante 
        Collections.sort(item, new MapComparator("distance"));
        for(int i=0;i<item.size();i++){
            Map<String, String> tmp = item.get(i);
            Map<String, String> map = new HashMap<String, String>();
            map.put("name",tmp.get("name"));
            map.put("distance",tmp.get("distance") + " m");
            item.set(i,map);
        }

        //Mis en place des données dans la zone de listing 
        Adapter = new SimpleAdapter(ListActivity.this,item, android.R.layout.simple_list_item_2,new String[]{"name","distance"}, new int[]{android.R.id.text1,android.R.id.text2});
        peakFront.setAdapter(Adapter);
        tv_orientation.setText(mAzimuth + "° " + where);
    }

    //Fonction obligatoire avec l'implements mais non utilisé
    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) { }

    //Permet de lancer la détection du mouvement du gyroscope
    private void Start_Azimuth() {

        if (mSensorManager.getDefaultSensor(Sensor.TYPE_ROTATION_VECTOR) == null) {
            if ((mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER) == null) || (mSensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD) == null)) {
            }
            else {
                mAccelerometer = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
                mMagnetometer = mSensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD);
                haveSensor = mSensorManager.registerListener(this, mAccelerometer, SensorManager.SENSOR_DELAY_UI);
                haveSensor2 = mSensorManager.registerListener(this, mMagnetometer, SensorManager.SENSOR_DELAY_UI);
            }
        }
        else{
            mRotationV = mSensorManager.getDefaultSensor(Sensor.TYPE_ROTATION_VECTOR);
            haveSensor = mSensorManager.registerListener(this, mRotationV, SensorManager.SENSOR_DELAY_UI);
        }
    }


}