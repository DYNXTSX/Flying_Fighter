package com.example.flyingfighter;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class ShopActivity extends AppCompatActivity {
    RelativeLayout shopP1, shopP2, shopP3, shopP4;
    RelativeLayout unlock2, unlock3, unlock4;
    ImageView home;
    private TextView textDiams;
    private SharedPreferences prefs;
    boolean shop2, shop3, shop4;
    int coins, action;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shop);

        prefs = this.getSharedPreferences("game", Context.MODE_PRIVATE);
        coins = prefs.getInt("diamants", 0);
        textDiams = (TextView) findViewById(R.id.tv_coins);
        textDiams.setText(String.valueOf(coins));

        shopP1 = (RelativeLayout) findViewById(R.id.shopP1);
        shopP2 = (RelativeLayout) findViewById(R.id.shopP2);
        shopP3 = (RelativeLayout) findViewById(R.id.shopP3);
        shopP4 = (RelativeLayout) findViewById(R.id.shopP4);

        unlock2 = (RelativeLayout) findViewById(R.id.unlock2);
        unlock3 = (RelativeLayout) findViewById(R.id.unlock3);
        unlock4 = (RelativeLayout) findViewById(R.id.unlock4);

        home = (ImageView) findViewById(R.id.home);

        final SharedPreferences settings = getSharedPreferences("shop", Context.MODE_PRIVATE);
        action = settings.getInt("ACTION", 1);
        shop2 = settings.getBoolean("SHOP2", false);
        shop3 = settings.getBoolean("SHOP3", false);
        shop4 = settings.getBoolean("SHOP4", false);

        if(shop2){
            unlock2.setVisibility(View.GONE);
        }
        if(shop3){
            unlock3.setVisibility(View.GONE);
        }
        if(shop4){
            unlock4.setVisibility(View.GONE);
        }

        shopP1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                action = 1;

                SharedPreferences.Editor editor = settings.edit();
                editor.putInt("ACTION", action);
                editor.commit();
                startActivity(new Intent(ShopActivity.this, MainActivity.class));
            }
        });

        shopP2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(shop2){
                    action = 2;

                    SharedPreferences.Editor editor = settings.edit();
                    editor.putInt("ACTION", action);
                    editor.commit();
                    startActivity(new Intent(ShopActivity.this, MainActivity.class));
                } else if(coins > 30){
                    shop2 = true;
                    action = 2;
                    coins = coins - 30;

                    textDiams.setText(String.valueOf(coins));
                    unlock2.setVisibility(View.GONE);

                    SharedPreferences.Editor editor2 = prefs.edit();
                    editor2.putInt("diamants", coins);
                    editor2.apply();

                    SharedPreferences.Editor editor = settings.edit();
                    editor.putInt("ACTION", action);
                    editor.putBoolean("SHOP2", shop2);
                    editor.commit();
                    startActivity(new Intent(ShopActivity.this, MainActivity.class));
                } else {
                    Toast.makeText(ShopActivity.this, "pas assez de diamants !", Toast.LENGTH_LONG);
                }
            }
        });

        shopP3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(shop3){
                    action = 3;

                    SharedPreferences.Editor editor = settings.edit();
                    editor.putInt("ACTION", action);
                    editor.commit();
                    startActivity(new Intent(ShopActivity.this, MainActivity.class));
                } else if(coins > 50){
                    shop2 = true;
                    action = 3;
                    coins = coins - 50;

                    textDiams.setText(String.valueOf(coins));
                    unlock3.setVisibility(View.GONE);

                    SharedPreferences.Editor editor2 = prefs.edit();
                    editor2.putInt("diamants", coins);
                    editor2.apply();

                    SharedPreferences.Editor editor = settings.edit();
                    editor.putInt("ACTION", action);
                    editor.putBoolean("SHOP3", shop3);
                    editor.commit();
                    startActivity(new Intent(ShopActivity.this, MainActivity.class));
                } else {
                    Toast.makeText(ShopActivity.this, "pas assez de diamants !", Toast.LENGTH_LONG);
                }
            }
        });

        shopP4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(shop4){
                    action = 4;

                    SharedPreferences.Editor editor = settings.edit();
                    editor.putInt("ACTION", action);
                    editor.commit();
                    startActivity(new Intent(ShopActivity.this, MainActivity.class));
                } else if(coins > 80){
                    shop4 = true;
                    action = 4;
                    coins = coins - 80;

                    textDiams.setText(String.valueOf(coins));
                    unlock4.setVisibility(View.GONE);

                    SharedPreferences.Editor editor2 = prefs.edit();
                    editor2.putInt("diamants", coins);
                    editor2.apply();

                    SharedPreferences.Editor editor = settings.edit();
                    editor.putInt("ACTION", action);
                    editor.putBoolean("SHOP4", shop4);
                    editor.commit();
                    startActivity(new Intent(ShopActivity.this, MainActivity.class));
                } else {
                    Toast.makeText(ShopActivity.this, "pas assez de diamants !", Toast.LENGTH_LONG);
                }
            }
        });

        home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(ShopActivity.this, MainActivity.class));
                finish();
            }
        });
    }
}
