package net.sokato.game.yuzu;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.preference.PreferenceManager;
import android.os.Bundle;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class Options extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_options);

        final PreferencesManager PM = new PreferencesManager(Options.this);
        final CheckBox Ads = findViewById(R.id.CheckBoxAds);
        final CheckBox CrashReports = findViewById(R.id.CheckBoxCrash);

        Ads.setChecked(PM.WantsTailoredAds());
        CrashReports.setChecked(PM.SendCrashReports());

        Ads.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PM.SetTailoredAds(Ads.isChecked());
                if(Ads.isChecked()){
                    Toast.makeText(Options.this, R.string.NewAds, Toast.LENGTH_SHORT).show();
                }else{
                    Toast.makeText(Options.this, R.string.NoAds, Toast.LENGTH_SHORT).show();
                }
            }
        });

        CrashReports.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PM.SetCrashReports(CrashReports.isChecked());
                Toast.makeText(Options.this, R.string.PleaseRestart, Toast.LENGTH_SHORT).show();
            }
        });

        TextView Thanks = findViewById(R.id.Thanks);
        Thanks.setText(Html.fromHtml(getString(R.string.Antonis)));
        Thanks.setMovementMethod(LinkMovementMethod.getInstance());

        final SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(this);
        final SharedPreferences.Editor editor = sharedPrefs.edit();

        Typeface tf = Typeface.createFromAsset(getAssets(), "fonts/britannic-bold-t.ttf");

        Button ResetButton = findViewById(R.id.ResetButton);
        ResetButton.setTypeface(tf);
        ResetButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder alert = new AlertDialog.Builder(Options.this);
                alert.setMessage(R.string.DeleteEverything);
                alert.setTitle(R.string.Sure);
                alert.setPositiveButton(R.string.Yes, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        editor.clear().apply();
                    }
                });
                alert.setNegativeButton(R.string.No, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //If the user clicks no we just close the dialog, so this is empty
                    }
                });
                alert.show();
            }
        });

        final CheckBox MusicCheck = findViewById(R.id.MusicCheckBox);
        MusicCheck.setChecked(MusicManager.isMusicActivated());
        MusicCheck.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(MusicCheck.isChecked()){
                    MusicManager.setMusicOn(true, Options.this);
                    MusicManager.iAmIn(Options.this);
                }
                else if(!MusicCheck.isChecked()){
                    MusicManager.setMusicOn(false, Options.this);
                    MusicManager.iAmLeaving();
                }
            }
        });

        SeekBar VolumeSeekBar = findViewById(R.id.SeekBarVolume);
        VolumeSeekBar.setMax(0);
        VolumeSeekBar.setMax(100);
        VolumeSeekBar.setProgress(MusicManager.getCurrentVolume(Options.this));
        VolumeSeekBar.refreshDrawableState();
        VolumeSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                MusicManager.setVolume((float)progress, false);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
            }
        });

        Button showIntro = findViewById(R.id.ShowIntro);
        showIntro.setTypeface(tf);
        showIntro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent ShowIntro = new Intent(Options.this, welcome.class);
                MusicManager.keepMusicOn();
                startActivity(ShowIntro);
            }
        });

    }

    @Override
    protected void onPause(){
        super.onPause();
        MusicManager.saveVolume(Options.this);
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

    public void CrashApp(){

    }
}
