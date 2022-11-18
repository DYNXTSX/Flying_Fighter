package com.example.flyingfighter;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.media.Image;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.Calendar;


public class MainActivity extends AppCompatActivity {

    private boolean isMute;
    private ImageView back;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_main);
        changeBg();

        ImageView playButton = findViewById(R.id.play);

        playButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, GameActivity.class));
            }
        });

        /*
        Button b = findViewById(R.id.button1);
        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText simpleEditText = (EditText) findViewById(R.id.editText1);
                TextView tv = findViewById(R.id.textView1);

                if (simpleEditText.getText().length() == 0) {
                    tv.setText(getApplicationContext().getString(R.string.text_view_1));
                } else {
                    tv.setText(simpleEditText.getText().toString());
                    simpleEditText.setText("");
                }
            }
        });

        TextView highscoreTxt = findViewById(R.id.highScoretext);
        SharedPreferences prefs = getSharedPreferences("game", MODE_PRIVATE);
        highscoreTxt.setText("Highscore : " + prefs.getInt("highscore", 0));

        prefs.getBoolean("isMute", false);

        ImageView volumeCtrl = findViewById(R.id.volumeCtrl);

        if (isMute) {
            volumeCtrl.setImageResource(R.drawable.ic_baseline_volume_off_24);
        } else {
            volumeCtrl.setImageResource(R.drawable.ic_baseline_volume_up_24);
        }

        volumeCtrl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isMute = !isMute;
                if (isMute)
                    volumeCtrl.setImageResource(R.drawable.ic_baseline_volume_off_24);
                else
                    volumeCtrl.setImageResource(R.drawable.ic_baseline_volume_up_24);

                SharedPreferences.Editor editor = prefs.edit();
                editor.putBoolean("isMute", isMute);
                editor.apply();
            }
        });*/
    }

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


