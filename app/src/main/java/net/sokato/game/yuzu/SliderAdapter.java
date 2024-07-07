package net.sokato.game.yuzu;

import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.viewpager.widget.PagerAdapter;

public class SliderAdapter extends PagerAdapter {

    private Context context;
    LayoutInflater layoutInflater;

    public SliderAdapter(Context context){
        this.context = context;
    }

    //Arrays
    public int[] slide_images ={
            R.drawable.lemon_greeting,
            R.drawable.slide_1_img,
            R.drawable.slide_2_img,
            R.drawable.slide_3_img
    };

    public int[] headings ={
            R.string.Heading_1,
            R.string.Heading_2,
            R.string.Heading_3,
            R.string.Heading_4
    };

    public int[] texts ={
            R.string.Text_1,
            R.string.Text_2,
            R.string.Text_3,
            R.string.Text_4
    };

    @Override
    public int getCount(){
        return slide_images.length;
    }

    @Override
    public boolean isViewFromObject(View view, Object o){
        return view == (RelativeLayout) o;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position){

        layoutInflater = (LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
        View view = layoutInflater.inflate(R.layout.slider_layout, container, false);

        ImageView slideImage = view.findViewById(R.id.ImageSlider);
        TextView slideHeading = view.findViewById(R.id.HeadingTextView);
        TextView slideDescription = view.findViewById(R.id.MainTextView);

        Typeface montserrat = Typeface.createFromAsset(context.getAssets(), "fonts/montserrat_regular.ttf");
        slideDescription.setTypeface(montserrat);
        slideHeading.setTypeface(montserrat);

        slideImage.setImageResource(slide_images[position]);
        slideHeading.setText(context.getResources().getString(headings[position]));
        slideDescription.setText(context.getResources().getString(texts[position]));

        container.addView(view);

        return view;

    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object){

        container.removeView((RelativeLayout)object);

    }

}
