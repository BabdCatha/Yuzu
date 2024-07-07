package net.sokato.game.yuzu;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewDebug;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import java.nio.file.FileStore;
import java.util.ArrayList;
import java.util.List;

public class AddPlayers extends AppCompatActivity {

    Button AjouterJoueur;
    Button OldButton;
    public int NombreDeJoueurs = 1;
    List<String> playerNames = new ArrayList<String>();
    boolean NomCorrects = true;
    boolean FirstDeleted = false;
    int GarbageID = 57234;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_players);
        Toolbar toolbar = (Toolbar) findViewById(R.id.AddPlayerToolbar);
        setSupportActionBar(toolbar);

        AddPlayers.this.setTitle(R.string.AJActivity);

        FloatingActionButton AddPlayerFAB = findViewById(R.id.AddPlayerFAB);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Typeface tf = Typeface.createFromAsset(getAssets(), "fonts/britannic-bold-t.ttf");
        AjouterJoueur = findViewById(R.id.AddPlayerButton);
        AjouterJoueur.setTypeface(tf);
        final Typeface mt = Typeface.createFromAsset(getAssets(), "fonts/montserrat_regular.ttf");

        RecyclerView recyclerView = findViewById(R.id.AddPlayerRecyclerView);
        final ArrayList<AddPlayerRow> population = new ArrayList<>();
        population.add(new AddPlayerRow());

        final AddPlayerAdapter Adapter = new AddPlayerAdapter(this, population);
        recyclerView.setAdapter(Adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));

        AjouterJoueur.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                population.add(new AddPlayerRow());
                Adapter.notifyDataSetChanged();
            }
        });

        AddPlayerFAB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                NombreDeJoueurs = Adapter.getItemCount();
                String[] playerNamesArray = new String[NombreDeJoueurs];
                for(int i = 0; i<NombreDeJoueurs; i++){
                    playerNamesArray[i] = Adapter.editModelArrayList.get(i).getEditTextValue();
                    if(playerNamesArray[i].equals("")){
                        NomCorrects = false;
                        Adapter.editModelArrayList.get(i).setError(getResources().getString(R.string.NomVide));
                    }
                }

                if (NombreDeJoueurs < 2) {
                    Snackbar.make(view, R.string.PasAssezJoueurs, Snackbar.LENGTH_LONG).setAction("Action", null).show();
                } else {
                    //String[] playerNamesArray = new String[playerNames.size()];
                    //playerNames.toArray(playerNamesArray);
                    Intent LaunchGame = new Intent(AddPlayers.this, Jouer.class);
                    LaunchGame.putExtra("playerNamesArray", playerNamesArray);
                    LaunchGame.putExtra("NombreDeJoueurs", NombreDeJoueurs);
                    LaunchGame.putExtra("Tour", 0);
                    LaunchGame.putExtra("Texte", "");
                    LaunchGame.putExtra("LastPhrase", R.string.welcome);
                    if (NomCorrects) { //We only launch the next activity if all player names are correct
                        MusicManager.keepMusicOn();
                        startActivity(LaunchGame);
                    } else {
                        playerNames.clear(); //We need to clear the array to prevent correct names to be stored multiple times
                        NomCorrects = true; //We reset the flag
                    }
                }
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

}
