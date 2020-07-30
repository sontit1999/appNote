package com.example.notesmasteer.base;

import android.graphics.Color;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.ColorRes;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.databinding.BindingAdapter;

import com.bumptech.glide.Glide;

public class BindingUntil {
    @BindingAdapter({"bind:imageUrl"})
    public static void loadImage(ImageView view, String imageUrl) {
        Glide
                .with(view.getContext())
                .load(imageUrl)
                .into(view);

    }
    @BindingAdapter({"bind:setbground"})
    public static void setbg(ConstraintLayout view, int setbground) {
        if(setbground == 0){
            view.setBackgroundColor(Color.WHITE);
        }else{
            view.setBackgroundColor(Color.parseColor("#AF593E"));
        }
    }
    @BindingAdapter({"bind:iscomplete"})
    public static void iscomplete(CheckBox view, int iscomplete) {
        if(iscomplete == 0){
            view.setVisibility(View.VISIBLE);
        }else{
            view.setVisibility(View.GONE);
        }
    }
}
