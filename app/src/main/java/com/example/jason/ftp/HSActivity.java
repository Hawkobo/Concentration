package com.example.jason.ftp;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.AssetManager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.util.Scanner;

public class HSActivity extends AppCompatActivity {

    private final int NUM_OF_SCORES = 5;
    private InputStream is;
    private Scanner kb;
    private AssetManager am;
    private String[] highScores;
    private int[] HSInts;
    private int newScore;
    private TextView score1;
    private TextView score2;
    private TextView score3;
    private TextView score4;
    private TextView score5;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hs);

        highScores = new String[NUM_OF_SCORES];
        HSInts = new int[NUM_OF_SCORES];
        score1 = (TextView) findViewById(R.id.score1);
        score2 = (TextView) findViewById(R.id.score2);
        score3 = (TextView) findViewById(R.id.score3);
        score4 = (TextView) findViewById(R.id.score4);
        score5 = (TextView) findViewById(R.id.score5);
        newScore = getIntent().getIntExtra("score", 0);

        try {
            am = this.getAssets();
            is = am.open("scores.txt");
            kb = new Scanner(is);
        }
        catch(IOException e){
            e.getMessage();
        }

        for (int i = 0; i < highScores.length; i++)
        {
            highScores[i] = kb.nextLine();
            HSInts[i] = Integer.parseInt(highScores[i].replaceAll("[\\D]", ""));
        }

        score1.setText(highScores[0]);
        score2.setText(highScores[1]);
        score3.setText(highScores[2]);
        score4.setText(highScores[3]);
        score5.setText(highScores[4]);

        ((Button)findViewById(R.id.menuButton)).setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent i = new Intent(HSActivity.this, Manager.class);
                startActivity(i);

            }


        });
    }
}
