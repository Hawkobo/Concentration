package com.example.jason.ftp;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;
import java.util.regex.Pattern;

import android.app.Activity;
import android.app.DialogFragment;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.Drawable;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.constraint.ConstraintLayout;
import android.text.util.Linkify;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.AdapterView.OnItemSelectedListener;

public class Manager extends Activity {
    private static int ROW_COUNT = -1;
	private static int COL_COUNT = -1;
	private Context context;
	private Drawable backImage;
	private int [] [] cards;
	private List<Drawable> images;
	private Card firstCard;
	private Card seconedCard;
	private Music backgroundMusic;
	
	private static Object lock = new Object();
	
	int turns;
	private TableLayout mainTable;
	TableLayout myLayout;
	AnimationDrawable animationDrawable;
	Animation frombottom;
	Animation fromtop;
	Button playButton;
	Button hsButton;
	TextView mainTitle;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
		backgroundMusic = Music.get(this);

		setContentView(R.layout.main);

		myLayout = (TableLayout)findViewById(R.id.bg);

		animationDrawable = (AnimationDrawable)myLayout.getBackground();
		animationDrawable.setEnterFadeDuration(4000);
		animationDrawable.setExitFadeDuration(4000);
		animationDrawable.start();

		playButton = (Button)findViewById(R.id.Play);
		hsButton = (Button)findViewById(R.id.Menu);
		mainTitle = (TextView) findViewById(R.id.mainTitle);

		frombottom = AnimationUtils.loadAnimation(this,R.anim.frombottom);
		fromtop = AnimationUtils.loadAnimation(this,R.anim.fromtop);

		playButton.setAnimation(frombottom);
		hsButton.setAnimation(frombottom);
		mainTitle.setAnimation(fromtop);

       ((Button)findViewById(R.id.Play)).setOnClickListener(new OnClickListener() {

		   @Override
		   public void onClick(View view)
		   {
			   DialogFragment dialog = new PlayDialogFragment();

			   dialog.show(getFragmentManager(), "play");
		   }
	});

		((Button)findViewById(R.id.Menu)).setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent i = new Intent(Manager.this, HSActivity.class);
				startActivity(i);


			}


		});
      


    }


}