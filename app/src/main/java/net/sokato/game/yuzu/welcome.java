package net.sokato.game.yuzu;

import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

public class welcome extends AppCompatActivity {

    private ViewPager WelcomePager;
    private LinearLayout WelcomeLinear;

    private TextView[] dots;

    private SliderAdapter sliderAdapter;

    private Button Back;
    private Button Next;
    private int currentPosition;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);

        final PreferencesManager PM = new PreferencesManager(welcome.this);

        WelcomeLinear = findViewById(R.id.WelcomeLinearLayout);
        WelcomePager = findViewById(R.id.WelcomeViewPager);

        sliderAdapter = new SliderAdapter(this);

        WelcomePager.setAdapter(sliderAdapter);

        Back = findViewById(R.id.ButtonBack);
        Next = findViewById(R.id.ButtonNext);

        addDotsIndicator(0);

        WelcomePager.addOnPageChangeListener(viewListener);

        Back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                WelcomePager.setCurrentItem(currentPosition-1);
            }
        });

        Next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(currentPosition == dots.length-1){
                    Intent GoToNext;
                    if(PM.isFirstLaunch()) {
                        GoToNext = new Intent(welcome.this, Consent.class);
                    }else{
                        GoToNext = new Intent(welcome.this, MainActivity.class);
                    }
                    MusicManager.keepMusicOn();
                    PM.firstLaunchDone();
                    startActivity(GoToNext);
                }else{
                    WelcomePager.setCurrentItem(currentPosition+1);
                }
            }
        });
    }

    public void addDotsIndicator(int position){
        dots = new TextView[4];
        WelcomeLinear.removeAllViews();
        for(int i=0; i<dots.length; i++){

            dots[i] = new TextView(this);
            dots[i].setText(Html.fromHtml("&#8226;"));
            dots[i].setTextSize(35);
            dots[i].setTextColor(getResources().getColor(R.color.Yuzu_1));

            WelcomeLinear.addView(dots[i]);

        }
        if(dots.length > 0) {
            dots[position].setTextColor(getResources().getColor(R.color.blanc));
        }
    }

    ViewPager.OnPageChangeListener viewListener = new ViewPager.OnPageChangeListener() {
        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

        }

        @Override
        public void onPageSelected(int position) {

            addDotsIndicator(position);
            currentPosition = position;

            if(position == 0){
                Back.setEnabled(false);
                //Next.setEnabled(true);
                Back.setVisibility(View.INVISIBLE);
            }else if(position == dots.length-1){
                Next.setText(getResources().getText(R.string.ButtonFinish));
            }else{
                Next.setText(getResources().getString(R.string.ButtonNextText));
                Back.setEnabled(true);
                Next.setEnabled(true);
                Back.setVisibility(View.VISIBLE);
                Next.setVisibility(View.VISIBLE);
            }

        }

        @Override
        public void onPageScrollStateChanged(int state) {

        }
    };

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
