package com.example.jason.ftp;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;
import java.util.regex.Pattern;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.Application;
import android.app.DialogFragment;
import android.content.ComponentName;
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
import android.view.KeyEvent;
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
	
	private static Object lock = new Object();

	private TableLayout mainTable;
	TableLayout myLayout;
	AnimationDrawable animationDrawable;
	Animation frombottom;
	Animation fromtop;
	Button playButton;
	Button hsButton;
	Button disableMusic;
	TextView mainTitle;
	Intent svc;
	public boolean playing;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        playing = true;
		//backgroundMusic = Music.get(this);
		Intent intent = getIntent();

		Bundle extras = intent.getExtras();

		svc =new Intent(this, MusicService.class);
		svc.setAction("com.example.jason.ftp.MusicService");
		startService(svc);


		setContentView(R.layout.main);

		myLayout = (TableLayout)findViewById(R.id.bg);

		animationDrawable = (AnimationDrawable)myLayout.getBackground();
		animationDrawable.setEnterFadeDuration(4000);
		animationDrawable.setExitFadeDuration(4000);
		animationDrawable.start();

		playButton = (Button)findViewById(R.id.Play);
		hsButton = (Button)findViewById(R.id.Menu);
		mainTitle = (TextView) findViewById(R.id.mainTitle);
		disableMusic = (Button) findViewById(R.id.disableMusic);

		frombottom = AnimationUtils.loadAnimation(this,R.anim.frombottom);
		fromtop = AnimationUtils.loadAnimation(this,R.anim.fromtop);

		playButton.setAnimation(frombottom);
		hsButton.setAnimation(frombottom);
		mainTitle.setAnimation(fromtop);
		disableMusic.setAnimation(frombottom);

		if(extras !=null){
			if (extras.containsKey("playingValue")) {
				boolean isNew = extras.getBoolean("playingValue", true);
				if(isNew == true){
					disableMusic.setText("Disable Music");
					playing = true;
				}
				else
					stopService(svc);
					disableMusic.setText("Enable Music");
					playing = false;
			}
		}

		disableMusic.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View view)
			{

				if (playing ==true) {
					disableMusic.setText("Enable Music");
					stopService(svc);
					playing =false;
				}
				else if (playing ==false) {
					disableMusic.setText("Disable Music");
					startService(svc);
					playing = true;
				}
			}
		});

       ((Button)findViewById(R.id.Play)).setOnClickListener(new OnClickListener() {

		   @Override
		   public void onClick(View view)
		   {
			   DialogFragment dialog = new PlayDialogFragment();

			   Bundle bundle = new Bundle();
			   bundle.putBoolean("playingValue", playing);
			   dialog.setArguments(bundle);

			   dialog.show(getFragmentManager(), "play");
		   }
	});

		((Button)findViewById(R.id.Menu)).setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent i = new Intent(Manager.this, HSActivity.class);
				i.putExtra("playingValue", playing);
				startActivity(i);


			}


		});
      

    }

	@Override
	public void onResume()
	{
		super.onResume();
	}




	@Override
	public void onDestroy()
	{
		super.onDestroy();
	}

	@Override
	public void onBackPressed() {
		stopService(svc);
		Intent intent = new Intent(Intent.ACTION_MAIN);
		intent.addCategory(Intent.CATEGORY_HOME);
		intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		startActivity(intent);
		finish();
		System.exit(0);
	}


	@Override
	public void onPause() {
		if (isApplicationSentToBackground(this)){
			stopService(svc);
		}
		super.onPause();
	}

	@Override
	public void onStop()
	{
		super.onStop();
	}

	public boolean isApplicationSentToBackground(final Context context) {
		ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
		List<ActivityManager.RunningTaskInfo> tasks = am.getRunningTasks(1);
		if (!tasks.isEmpty()) {
			ComponentName topActivity = tasks.get(0).topActivity;
			if (!topActivity.getPackageName().equals(context.getPackageName())) {
				return true;
			}
		}
		return false;
	}



}