package net.sokato.game.yuzu;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Handler;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends AppCompatActivity {

    Button NouvellePartie;
    Button Saved;
    Button Auteur;
    Button RemoveAds;
    ImageView Citron1;
    ImageView Citron2;
    ImageView Citron3;
    ImageView YuzuImage;
    TextView YuzuText;
    Animation shakeAnimation;
    Random random = new Random();
    Timer timer = new Timer();
    ImageView[] CitronListe = new ImageView[3];
    int YuzuImageSize;
    int orientation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        MusicManager.setMusicParameters(MainActivity.this); //to set the music values to the user choice

        PreferencesManager PrefsManager = new PreferencesManager(MainActivity.this);
        if(PrefsManager.isFirstLaunch()){
            MusicManager.keepMusicOn();
            Intent WelcomeIntent = new Intent(MainActivity.this, welcome.class);
            startActivity(WelcomeIntent);
        }

        Citron1 = (ImageView) findViewById(R.id.Citron1);
        Citron2 = (ImageView) findViewById(R.id.Citron2);
        Citron3 = (ImageView) findViewById(R.id.Citron3);
        CitronListe[0] = Citron1;
        CitronListe[1] = Citron2;
        CitronListe[2] = Citron3;

        YuzuImage = findViewById(R.id.YuzuImage);
        YuzuText = findViewById(R.id.YuzuText);
        orientation = this.getResources().getConfiguration().orientation;

        YuzuImage.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                YuzuImage.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                YuzuImageSize = YuzuImage.getWidth();
                RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(YuzuImageSize, YuzuImageSize);
                if (orientation == Configuration.ORIENTATION_PORTRAIT){
                    lp.addRule(RelativeLayout.RIGHT_OF, R.id.GreetingText);
                    lp.addRule(RelativeLayout.ABOVE, R.id.ButtonLayout);
                }
                else{
                    lp.addRule(RelativeLayout.RIGHT_OF, R.id.ButtonLayout);
                }
                lp.addRule(RelativeLayout.ALIGN_PARENT_TOP);
                YuzuImage.setLayoutParams(lp);
                YuzuText.setLayoutParams(lp);
            }
        });

        shakeAnimation = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.shake_animation);
        TimerTask task = new MyTimerTask(CitronListe, random, shakeAnimation);

        timer.schedule(task, 0, 4000l);

        Typeface tf = Typeface.createFromAsset(getAssets(), "fonts/britannic-bold-t.ttf");
        Typeface mt = Typeface.createFromAsset(getAssets(), "fonts/montserrat_regular.ttf");

        NouvellePartie = (Button) findViewById(R.id.NouvellePartie);
        NouvellePartie.setTypeface(tf);
        NouvellePartie.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MusicManager.keepMusicOn();
                Intent launchAddPlayer = new Intent(MainActivity.this, AddPlayers.class);
                startActivity(launchAddPlayer);
            }
        });

        Saved = (Button) findViewById(R.id.Saved);
        Saved.setTypeface(tf);
        Saved.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MusicManager.keepMusicOn();
                Intent launchSavedStories = new Intent(MainActivity.this, SavedStories.class);
                startActivity(launchSavedStories);
            }
        });

        Auteur = (Button) findViewById(R.id.Auteur);
        Auteur.setTypeface(tf);
        Auteur.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MusicManager.keepMusicOn();
                Intent launchOptions = new Intent(MainActivity.this, Options.class);
                startActivity(launchOptions);
            }
        });

        RemoveAds = (Button) findViewById(R.id.RemoveAds);
        RemoveAds.setTypeface(mt);

        //MobileAds.initialize(MainActivity.this, "ca-app-pub-7683252153346403~2055259808"); //causes long loading times

    }

    @Override
    protected void onPause(){
        super.onPause();
        MusicManager.iAmLeaving();
        //Log.e("LOG", "onPause: Mainactivity");
    }

    @Override
    protected void onResume(){
        super.onResume();
        MusicManager.iAmIn(this);
        MusicManager.ResetVolume();
        //Log.e("LOG", "onResume: Mainactivity");
    }

    @Override
    protected void onDestroy(){
        super.onDestroy();
        //Log.e("LOG", "onDestroy: Mainactivity");
    }

    @Override
    public void onBackPressed(){
        if(Build.VERSION.SDK_INT >= 21){
            finishAndRemoveTask();
        }else{
            finishAffinity();
        }
    }
}

class MyTimerTask extends TimerTask{
    private ImageView[] CitronListe;
    private Random random;
    private ImageView Citron;
    private int index;
    private Animation shakeAnim;
    final Handler handler = new Handler();

    public MyTimerTask(ImageView[] CitronListe, Random random, Animation shakeAnim){
        this.CitronListe = CitronListe;
        this.random = random;
        this.shakeAnim = shakeAnim;
    }

    @Override
    public void run(){
        index = random.nextInt(3);
        Citron = CitronListe[index];

        handler.post(new Runnable() {
            @Override
            public void run() {
                Citron.startAnimation(shakeAnim);
            }
        });
    }
}