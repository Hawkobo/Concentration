package com.example.jason.ftp;

import android.app.AlertDialog;
import android.app.DialogFragment;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.AssetManager;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.InputFilter;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.airbnb.lottie.LottieAnimationView;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.util.Scanner;

public class scoreScreenActivity extends AppCompatActivity {

    private File file;

    private final int NUM_OF_SCORES = 5;
    private InputStreamReader isr;
    private OutputStreamWriter osw;
    private BufferedReader br;
    private FileOutputStream fos;
    private FileInputStream fis;
    private String contents;
    private Scanner kb;
    private String[] highScores;
    private int[] HSInts;
    private int score;
    private AlertDialog.Builder builder;
    private EditText input;
    private String m_Text = "";
    private String newEntry;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_score_screen);

        highScores = new String[NUM_OF_SCORES];
        HSInts = new int[NUM_OF_SCORES];

        file = new File(this.getFilesDir(), "scores.txt");

        try {
            if(file.length() == 0 || !file.exists())
            {
                fos = new FileOutputStream(file);
                fos.write("ABC...5\nABC...4\nABC...3\nABC...2\nABC...1".getBytes());
            }

            fis = new FileInputStream(file);

            int length = (int) file.length();
            byte[] bytes = new byte[length];
            fis.read(bytes);
            contents = new String(bytes);
            kb = new Scanner(contents);
        }
        catch(IOException e){
            e.getMessage();
        }

        for (int i = 0; i < highScores.length; i++)
        {
            highScores[i] = kb.nextLine();
            HSInts[i] = Integer.parseInt(highScores[i].replaceAll("[\\D]", ""));
        }

        score = getIntent().getIntExtra("score", 0);
        TextView scoreLabel = findViewById(R.id.scoreLabel);
        Bundle extras = getIntent().getExtras();
        StringBuilder sb = new StringBuilder();
        sb.append(extras.getInt("score"));

        scoreLabel.setText(sb.toString());
        builder = new AlertDialog.Builder(this);
        input = new EditText(this);

        ((Button)findViewById(R.id.scoreMenuButton)).setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view)
            {
                if (score >= HSInts[HSInts.length - 1])
                {

                    builder.setTitle("    New High Score! Enter your name: \n               Limit: Three characters");

                    // Set up the input

                    // Specify the type of input expected; this, for example, sets the input as a password, and will mask the text
                    input.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PERSON_NAME);
                    input.setFilters(new InputFilter[] { new InputFilter.LengthFilter(3) });
                    builder.setView(input);

                    // Set up the buttons
                    builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            m_Text = input.getText().toString();
                            newEntry = m_Text + "..." + score;
                            try { updateHighScore(newEntry, score); } catch (IOException e) { e.getMessage(); }
                            startActivity(new Intent(scoreScreenActivity.this, Manager.class));
                        }
                    });
                    builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.cancel();
                            startActivity(new Intent(scoreScreenActivity.this, Manager.class));
                        }
                    });

                    builder.show();
                }
                else
                {
                    startActivity(new Intent(scoreScreenActivity.this, Manager.class));
                }

            }
        });
    }

    private void updateHighScore(String newEntry, int score) throws IOException
    {
        int count = 0; boolean found = false;
        while (!found && count < highScores.length)
        {
            if (score >= HSInts[count])
            {
                String temp;
                for (int i = count; i < highScores.length; i++)
                {
                    temp = highScores[i];
                    highScores[i] = newEntry;
                    newEntry = temp;
                }
                StringBuilder sb = new StringBuilder();
                for (int i = 0; i < highScores.length; i++)
                {
                    if (i == highScores.length - 1)
                        sb.append(highScores[i]);
                    else
                        sb.append(highScores[i] + "\n");
                }
                fos = new FileOutputStream(file);
                fos.write(sb.toString().getBytes());
                found = true;
            }
            count++;
        }

        fos.close();
        fis.close();
    }
}