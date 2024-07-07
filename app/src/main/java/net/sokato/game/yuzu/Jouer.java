package net.sokato.game.yuzu;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatEditText;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.ViewCompat;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

public class Jouer extends AppCompatActivity {

    EditText OldEdit;
    EditText FirstEditText;
    TextView LastPhraseTextView;
    int NombreDePhrases = 1;
    String NewText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_jouer);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        OldEdit = (EditText) findViewById(R.id.FirstEditText);
        FirstEditText = (EditText) findViewById(R.id.FirstEditText);
        LastPhraseTextView = (TextView) findViewById(R.id.LastPhraseView);

        DisplayMetrics metrics = getApplicationContext().getResources().getDisplayMetrics();
        float dp56 = 56f;
        float dp16 = 16f;
        float px56 = metrics.density * dp56;
        float px16 = metrics.density * dp16;
        final int pixelsValue56 = (int) (px56+0.5f);
        final int pixelsValue16 = (int) (px16+0.5f);

        Intent ReceivedIntent = getIntent();
        final String[] playerNamesArray = ReceivedIntent.getStringArrayExtra("playerNamesArray");
        final int NombredeJoueurs = ReceivedIntent.getIntExtra("NombreDeJoueurs", 0);
        final int Tour = ReceivedIntent.getIntExtra("Tour", 0);
        int joueur = Tour%NombredeJoueurs;
        int lastJoueur = (Tour-1)%NombredeJoueurs;
        String LastPhrase = ReceivedIntent.getStringExtra("LastPhrase");
        final String Texte = ReceivedIntent.getStringExtra("Texte");

        if(Tour != 0){
            LastPhraseTextView.setText(LastPhrase);
        }
        else{
            LastPhraseTextView.setTextColor(Color.BLACK);
            LastPhraseTextView.setText(R.string.welcome);
        }
        Jouer.this.setTitle(playerNamesArray[joueur]);

        Button Terminer = (Button) findViewById(R.id.Terminer);

        Typeface tf = Typeface.createFromAsset(getAssets(), "fonts/britannic-bold-t.ttf");
        Terminer.setTypeface(tf);
        final Typeface mt = Typeface.createFromAsset(getAssets(), "fonts/montserrat_regular.ttf");

        if(Tour<NombredeJoueurs){
            Terminer.setVisibility(View.GONE);
        }

        FloatingActionButton NextPlayer = (FloatingActionButton) findViewById(R.id.NextPlayer);
        NextPlayer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(NombreDePhrases < 2) {
                    Snackbar.make(view, R.string.PasAssezDeTexte, Snackbar.LENGTH_LONG).setAction("Action", null).show();
                }
                else{
                    NewText = Texte + FirstEditText.getText().toString();
                    NewText += CompleteSentence(FirstEditText.getText().toString());
                    for(int it = 2; it<=NombreDePhrases; it++){
                        EditText edt = new EditText(Jouer.this);
                        edt = (EditText) findViewById(3000+it);
                        NewText += edt.getText().toString();
                        NewText += CompleteSentence(edt.getText().toString());
                    }
                    int NextTour =Tour + 1;
                    EditText edt = (EditText) findViewById(3000+NombreDePhrases);
                    Intent NextPlayer = new Intent(Jouer.this, Jouer.class);
                    NextPlayer.putExtra("playerNamesArray",playerNamesArray);
                    NextPlayer.putExtra("NombreDeJoueurs", NombredeJoueurs);
                    NextPlayer.putExtra("Tour", NextTour);
                    NextPlayer.putExtra("Texte", NewText);
                    NextPlayer.putExtra("LastPhrase", edt.getText().toString());
                    MusicManager.keepMusicOn();
                    if(edt.getText().toString().matches("")){
                        edt.setError(getResources().getString(R.string.LastPhraseEmpty));
                    }else{
                        startActivity(NextPlayer);
                    }

                }
            }
        });

        FloatingActionButton AddText = (FloatingActionButton) findViewById(R.id.AddText);
        AddText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                NombreDePhrases++;
                RelativeLayout RelativeLayout = (RelativeLayout) findViewById(R.id.JouerLayout);
                AppCompatEditText edt = new AppCompatEditText(Jouer.this);
                ColorStateList clste = ColorStateList.valueOf(getResources().getColor(R.color.EditTextColor));
                ViewCompat.setBackgroundTintList(edt, clste);
                edt.setId(3000+NombreDePhrases);
                edt.setTypeface(mt);
                edt.setHint(R.string.NouvellePhrase);
                android.widget.RelativeLayout.LayoutParams edtparams = new android.widget.RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                edtparams.addRule(RelativeLayout.BELOW, OldEdit.getId());
                RelativeLayout.addView(edt, edtparams);
                OldEdit = edt;
                ScrollView scrollView = findViewById(R.id.scrollPlay);
                scrollView.fullScroll(View.FOCUS_DOWN);
            }
        });

        Terminer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!FirstEditText.getText().toString().matches("")){
                    NewText = Texte + FirstEditText.getText().toString();
                    NewText += CompleteSentence(FirstEditText.getText().toString());
                    for(int it = 2; it<=NombreDePhrases; it++){
                        EditText edt = new EditText(Jouer.this);
                        edt = (EditText) findViewById(3000+it);
                        NewText += edt.getText().toString();
                        NewText += CompleteSentence(edt.getText().toString());
                    }
                }
                MusicManager.keepMusicOn();
                Intent ShowResult = new Intent(Jouer.this, Resultat.class);
                if(!FirstEditText.getText().toString().matches("")){
                    ShowResult.putExtra("TexteFinal", NewText);
                }else {
                    ShowResult.putExtra("TexteFinal", Texte);
                }
                startActivity(ShowResult);
            }
        });

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
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

    String CompleteSentence(String sentence) {
        if (sentence.endsWith(".") || sentence.endsWith("!") || sentence.endsWith("?")) {
            return " ";
        } else if (sentence.endsWith(" ")) {
            return "";
        }else if(sentence.matches("")){
            return "";
        }else{
            return ". ";
        }
    }

    @Override
    public void onBackPressed(){
        new AlertDialog.Builder(this)
                .setTitle(R.string.Really)
                .setMessage(R.string.Quit)
                .setNegativeButton(R.string.No, null)
                .setPositiveButton(R.string.Yes, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent MM = new Intent(Jouer.this, MainActivity.class);
                        startActivity(MM);
                    }
                }).create().show();
    }

}
