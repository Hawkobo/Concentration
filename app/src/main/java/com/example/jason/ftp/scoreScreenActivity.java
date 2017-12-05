package com.example.jason.ftp;

import android.app.AlertDialog;
import android.app.DialogFragment;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.InputFilter;
import android.text.InputType;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.airbnb.lottie.LottieAnimationView;

public class scoreScreenActivity extends AppCompatActivity {

    int score;
    private AlertDialog.Builder builder;
    private EditText input;
    private String m_Text = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_score_screen);

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

                if (0==0)
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
                        }
                    });
                    builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.cancel();
                        }
                    });

                    builder.show();
                }

            }
        });

    }

}