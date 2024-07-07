package net.sokato.game.yuzu;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;

import androidx.appcompat.app.AppCompatActivity;

public class Consent extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_consent);

        final PreferencesManager PM = new PreferencesManager(Consent.this);

        Typeface tf = Typeface.createFromAsset(getAssets(), "fonts/britannic-bold-t.ttf");
        final Typeface mt = Typeface.createFromAsset(getAssets(), "fonts/montserrat_regular.ttf");

        Button Finish = findViewById(R.id.FinishConsent);
        Finish.setTypeface(tf);

        final CheckBox Ads = findViewById(R.id.AdsCheckBox);
        final CheckBox Crash = findViewById(R.id.AcceptCheckBox);

        Finish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PM.SetTailoredAds(Ads.isChecked());
                PM.SetCrashReports(Crash.isChecked());

                Intent Finish = new Intent(Consent.this, MainActivity.class);
                MusicManager.keepMusicOn();
                startActivity(Finish);
            }
        });

    }

    @Override
    protected void onPause(){
        super.onPause();
        MusicManager.saveVolume(Consent.this);
        MusicManager.iAmLeaving();
    }

    @Override
    protected void onResume(){
        super.onResume();
        MusicManager.iAmIn(this);
    }

    @Override
    protected void onDestroy(){
        super.onDestroy();
    }
}
