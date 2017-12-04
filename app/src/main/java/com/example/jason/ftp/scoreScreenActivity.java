package com.example.jason.ftp;

import android.app.DialogFragment;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;

import com.airbnb.lottie.LottieAnimationView;

public class scoreScreenActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_score_screen);

        ((Button)findViewById(R.id.scoreMenuButton)).setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view)
            {
                Intent i = new Intent(scoreScreenActivity.this, Manager.class);
                startActivity(i);
            }
        });

    }

    @Override
    public void onBackPressed() {
        Intent i = new Intent(scoreScreenActivity.this, Manager.class);
        startActivity(i);
    }

}
