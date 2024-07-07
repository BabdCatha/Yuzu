package net.sokato.game.yuzu;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.RectShape;
import android.preference.PreferenceManager;
import android.provider.ContactsContract;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.TypedValue;
import android.view.DragEvent;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class SavedStories extends AppCompatActivity {

    int ViewSelected = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_saved_stories);
        final SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(this);
        final SharedPreferences.Editor editor = sharedPrefs.edit();
        DisplayMetrics metrics = getApplicationContext().getResources().getDisplayMetrics();
        float dp56 = 56f;
        float dp16 = 16f;
        float px56 = metrics.density * dp56;
        float px16 = metrics.density * dp16;
        final int pixelsValue56 = (int) (px56+0.5f);
        final int pixelsValue16 = (int) (px16+0.5f);

        final RelativeLayout RelativeLayout;
        RelativeLayout = (RelativeLayout) findViewById(R.id.SavedLayout);

        final int SavedStories = sharedPrefs.getInt("NumberOfStoriesSaved", 0);
        //Log.i("SavedStories", String.valueOf(SavedStories));

        if(SavedStories == 0){

            TextView Notextview = new TextView(SavedStories.this);
            android.widget.RelativeLayout.LayoutParams NotextParams = new android.widget.RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            NotextParams.addRule(RelativeLayout.CENTER_VERTICAL);
            Notextview.setText(R.string.NoStorySaved);
            Notextview.setTextSize(TypedValue.COMPLEX_UNIT_SP, 15);
            Notextview.setGravity(Gravity.CENTER);
            RelativeLayout.addView(Notextview, NotextParams);
        }

        for(int it = 1; it<=SavedStories; it++){
            //Log.i("Test", Integer.toString(it));
            final String Ident = Integer.toString(it);

            final TextView txtview = new TextView(SavedStories.this);
            txtview.setId(4000+it);
            android.widget.RelativeLayout.LayoutParams txtparams = new android.widget.RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, pixelsValue56);
            if(it!=1){
                txtparams.addRule(RelativeLayout.BELOW, 3999+it);
            }
            final int ID = txtview.getId();
            txtparams.setMargins( 0, 0, 0, 0);
            txtview.setText(sharedPrefs.getString(Ident+"title", "Error"));
            txtview.setTextSize(TypedValue.COMPLEX_UNIT_SP, 30);
            txtview.setGravity(Gravity.CENTER);
            RelativeLayout.addView(txtview, txtparams);

            final Button DeleteButton = new Button(SavedStories.this);
            DeleteButton.setId(5000+it);
            DeleteButton.setBackgroundResource(R.drawable.ic_delete);
            android.widget.RelativeLayout.LayoutParams DeleteButtonParams = new android.widget.RelativeLayout.LayoutParams(pixelsValue56, pixelsValue56);
            DeleteButtonParams.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
            if(it!=1){
                DeleteButtonParams.addRule(RelativeLayout.BELOW, 4999+it);
            }
            final int ID2 = DeleteButton.getId();
            RelativeLayout.addView(DeleteButton, DeleteButtonParams);
            DeleteButton.setVisibility(View.INVISIBLE);

            DeleteButton.setOnClickListener(new View.OnClickListener() { //OnclickListener for the button
                @Override
                public void onClick(View v) {
                    //Log.i("ButtonTest", "Sucessful");
                    editor.remove(Integer.toString(ID2-5000)).apply();
                    editor.remove(Integer.toString(ID2-5000)+"title").apply();
                    int number = sharedPrefs.getInt("NumberOfStoriesSaved", -2);
                    number--;
                    editor.putInt("NumberOfStoriesSaved", number).apply();

                    DeleteButton.setVisibility(View.GONE);
                    txtview.setVisibility(View.GONE);
                    ViewSelected = 0;

                    for(int counter = ID2-5000; counter<=number; counter++){
                        String text = sharedPrefs.getString(Integer.toString(counter+1), "Error"); //to get the old stories one place up
                        editor.putString(Integer.toString(counter), text).apply();
                        String title = sharedPrefs.getString(Integer.toString(counter+1)+"title", "Error");
                        editor.putString(Integer.toString(counter)+"title", title).apply();


                    }

                    if(number == 0){

                        TextView Notextview = new TextView(SavedStories.this);
                        android.widget.RelativeLayout.LayoutParams NotextParams = new android.widget.RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                        NotextParams.addRule(RelativeLayout.CENTER_VERTICAL);
                        Notextview.setText(R.string.NoStorySaved);
                        Notextview.setTextSize(TypedValue.COMPLEX_UNIT_SP, 15);
                        Notextview.setGravity(Gravity.CENTER);
                        RelativeLayout.addView(Notextview, NotextParams);
                    }
                }
            });

            txtview.setBackground(getResources().getDrawable(R.drawable.border_ss));

            txtview.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent launchResult = new Intent(SavedStories.this, Resultat.class);
                    launchResult.putExtra("TexteFinal", sharedPrefs.getString(Ident, Integer.toString(R.string.TextError)));
                    launchResult.putExtra("IsAlreadySaved", true);
                    MusicManager.keepMusicOn();
                    startActivity(launchResult);
                }
            });

            txtview.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    //Log.i("LongPressRecorded", "Success");
                    if(ViewSelected == 0){
                        ObjectAnimator animationDroite = ObjectAnimator.ofFloat(txtview, "translationX", -pixelsValue56);
                        animationDroite.setDuration(500);
                        animationDroite.addListener(new Animator.AnimatorListener() {
                            @Override
                            public void onAnimationStart(Animator animation) {

                            }

                            @Override
                            public void onAnimationEnd(Animator animation) {
                                Button Delete;
                                Delete = findViewById(ID2);
                                Delete.setVisibility(View.VISIBLE);
                            }

                            @Override
                            public void onAnimationCancel(Animator animation) {

                            }

                            @Override
                            public void onAnimationRepeat(Animator animation) {

                            }
                        });
                        animationDroite.start();
                        ViewSelected = ID;
                    }
                    else if(ViewSelected == ID){
                        ObjectAnimator animationGauche = ObjectAnimator.ofFloat(txtview, "translationX", 0);
                        animationGauche.setDuration(500);
                        animationGauche.start();
                        ViewSelected = 0;
                        Button Delete;
                        Delete = findViewById(ID2);
                        Delete.setVisibility(View.INVISIBLE);
                    }

                    return true;
                }
            });

        }
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