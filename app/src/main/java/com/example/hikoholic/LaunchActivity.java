package com.example.hikoholic;

import android.content.Intent;
import android.os.Bundle;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toolbar;
import androidx.appcompat.app.AppCompatActivity;
import com.example.calcul.Database;

//Classe permettant l'affichage de la page d'accueil
public class LaunchActivity extends AppCompatActivity {

    RadioGroup select;
    RadioButton rb_sommet,rb_metro;
    Toolbar tb_map,tb_list;
    TextView tv_text;

    //Creation de l'affichage lors du démarrage de l'application
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        setTheme(R.style.Theme_Hikoholic);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.launch); //Page de lancement

        //Initialisation des variables d'affichage avec leurs IDs
        select = findViewById(R.id.rg_select);
        rb_sommet = findViewById(R.id.rb_sommet);
        rb_metro = findViewById(R.id.rb_metro);
        tb_list = findViewById(R.id.tb_list);
        tb_map = findViewById(R.id.tb_map);
        tv_text = findViewById(R.id.tv_peak);

        //Capture d'un changement sur le RadioGroup (quelle base de données est choisies) 
        select.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if(rb_sommet.isChecked()){
                    Database.which_db = 1; //Sommets
                }
                else if (rb_metro.isChecked()){
                    Database.which_db = 2; //Métros
                }
                startActivity(new Intent(LaunchActivity.this, ListActivity.class)); //Lancement de l'activité listing et pause de l'activité accueil
            }
        });
    }
}
