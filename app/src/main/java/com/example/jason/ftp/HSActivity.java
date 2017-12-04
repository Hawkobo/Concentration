package com.example.jason.ftp;

import android.content.Intent;
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
import java.util.Scanner;

public class HSActivity extends AppCompatActivity {

    private TextView[] highScores;
    private Scanner file;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        highScores = new TextView[]{findViewById(R.id.score1), findViewById(R.id.score2), findViewById(R.id.score3), findViewById(R.id.score4), findViewById(R.id.score5)};
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_hs);

        File scores = new File("highscore.txt");

        try {
            if (!scores.exists()) {
                scores.createNewFile();

                FileWriter fw = new FileWriter(scores);

                for (int i = 0; i < highScores.length; i++)
                {
                    highScores[i].setText("ABC");
                    fw.write("ABC\n");
                }

                fw.flush();
                fw.close();
            } else {
                file = new Scanner(scores);

                for (int i = 0; i < highScores.length; i++)
                {
                    highScores[i].setText(file.nextLine());
                }
            }
        }
        catch (FileNotFoundException e) {
            e.getMessage();
        }
        catch (IOException e) {
            e.getMessage();
        }



        ((TextView)findViewById(R.id.score1)).setText("ABC");

        ((Button)findViewById(R.id.menuButton)).setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent i = new Intent(HSActivity.this, Manager.class);
                startActivity(i);

            }


        });
    }

    public boolean isHighScore(int x)
    {
        boolean higher = false;
        String line;
        String[] splitLine;

        for (int i = 0; i < highScores.length; i++)
        {
            line = highScores[i].getText().toString();
            splitLine = line.split(" ");
            if (x > Integer.parseInt(splitLine[1]))
            {
                higher = true;
                break;
            }
        }
        return higher;
    }

    public void updateHighScore(int x, String name) throws IOException
    {
        boolean higher = false;
        int position = 0;

        String line;
        String[] splitLine;

        for (int i = 0; i < highScores.length; i++)
        {
            line = highScores[i].getText().toString();
            splitLine = line.split(" ");
            if (x > Integer.parseInt(splitLine[1]))
            {
                higher = true;
                position = i;
                break;
            }
        }

        if (higher)
        {
            TextView tempT;
            TextView newScore = new TextView(this);
            newScore.setText(name + " " + x);

            for (int i = position; i < highScores.length; i++)
            {
                tempT = highScores[i];
                highScores[i] = newScore;
                newScore = tempT;
            }

            FileWriter fw = new FileWriter(new File("highscore.txt"));

            for (int i = 0; i < highScores.length; i++)
            {
                fw.write(highScores[i].getText().toString() + "\n");
            }

            fw.flush();
            fw.close();
        }
    }
}
