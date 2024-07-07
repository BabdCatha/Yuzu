package net.sokato.game.yuzu;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.os.Bundle;
import android.text.InputFilter;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import javax.xml.transform.Result;


public class Resultat extends AppCompatActivity {

    boolean StorySaved = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_resultat);
        Resultat.this.setTitle(R.string.Resultat);
        Intent ReceivedIntent = getIntent();
        final String TexteFinal = ReceivedIntent.getStringExtra("TexteFinal");
        StorySaved = ReceivedIntent.getBooleanExtra("IsAlreadySaved", false);
        final FloatingActionButton Lire = (FloatingActionButton) findViewById(R.id.Lire);
        TextView ResultTextView = (TextView) findViewById(R.id.ResultTextView);
        ResultTextView.setText(TexteFinal);

        final SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(this);
        final SharedPreferences.Editor editor = sharedPrefs.edit();

        FloatingActionButton Quitter = (FloatingActionButton) findViewById(R.id.Quitter);
        Quitter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MusicManager.keepMusicOn();
                Intent GoBack = new Intent(Resultat.this, MainActivity.class);
                startActivity(GoBack);
            }
        });

        Lire.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(StorySaved == false) {
                    //New things beginning
                    AlertDialog.Builder alert = new AlertDialog.Builder(Resultat.this);
                    final EditText edittext = new EditText(Resultat.this);
                    edittext.setMaxLines(1);
                    InputFilter[] fArray = new InputFilter[1];
                    fArray[0] = new InputFilter.LengthFilter(20);
                    edittext.setFilters(fArray);
                    alert.setMessage(R.string.EntrerTitre);
                    alert.setView(edittext);

                    alert.setPositiveButton(R.string.save, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            int SavedStories = sharedPrefs.getInt("NumberOfStoriesSaved", 0);
                            SavedStories++;
                            //Log.i("SavedStories", String.valueOf(SavedStories));
                            editor.putInt("NumberOfStoriesSaved", SavedStories).apply();
                            editor.putString(String.valueOf(SavedStories), TexteFinal).apply();
                            editor.putString(String.valueOf(SavedStories)+"title", edittext.getText().toString()).apply();
                            StorySaved = true;
                            Toast.makeText(Resultat.this, R.string.Saved, Toast.LENGTH_LONG).show();
                        }
                    });
                    alert.show();

                    //End of the new things
                }
                else{
                    Toast.makeText(Resultat.this, R.string.AlreadySaved, Toast.LENGTH_LONG).show();
                }
            }
        });

        FloatingActionButton Envoyer = (FloatingActionButton) findViewById(R.id.Envoyer);
        Envoyer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent SendText = new Intent(Intent.ACTION_SEND);
                SendText.setType("text/plain");
                SendText.putExtra(Intent.EXTRA_TEXT, TexteFinal);
                startActivity(SendText);
            }
        });
    }

    @Override
    protected void onPause(){
        super.onPause();
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

    @Override
    public void onBackPressed(){
        Intent MM = new Intent(Resultat.this, MainActivity.class);
        startActivity(MM);
    }

}
