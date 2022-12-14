package com.example.flyingfighter;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.Image;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.Calendar;

/**
 * Classe [MainActivity]
 * @description classe du menu principal au démarrage du jeu
 *
 */
public class MainActivity extends AppCompatActivity {

    private boolean isMute;
    private SharedPreferences prefs;
    private ImageView back;
    private TextView textDiams;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_main);
        changeBg();

        prefs = this.getSharedPreferences("game", Context.MODE_PRIVATE);
        int diamants = prefs.getInt("diamants", 0);
        textDiams = (TextView) findViewById(R.id.diamants_txt);
        textDiams.setText(String.valueOf(diamants));


        ImageView playButton = findViewById(R.id.play);

        playButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, GameActivity.class));
            }
        });

        ImageView shopBushoton = findViewById(R.id.shop);
        shopBushoton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, ShopActivity.class));
            }
        });



    }

    /**
     * Cette méthode va permettre de changer le background en fonction de l'heure de la journée.
     */
    protected void changeBg(){
        Calendar calendar = Calendar.getInstance();
        int hour24hrs = calendar.get(Calendar.HOUR_OF_DAY);
        back = (ImageView) findViewById(R.id.background);
        if(hour24hrs >= 22 || hour24hrs < 5){
            back.setImageResource(R.drawable.background7);
        }else if(hour24hrs < 9){
            back.setImageResource(R.drawable.background1);
        }else if(hour24hrs < 13){
            back.setImageResource(R.drawable.background2);
        }else if(hour24hrs < 18){
            back.setImageResource(R.drawable.background3);
        }else if(hour24hrs < 19){
            back.setImageResource(R.drawable.background4);
        }else if(hour24hrs < 20){
            back.setImageResource(R.drawable.background5);
        }else{
            back.setImageResource(R.drawable.background6);
        }
    }
}


