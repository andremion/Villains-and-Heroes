package com.andremion.heroes.ui.binding;

import android.databinding.BindingAdapter;
import android.widget.ImageView;

import com.andremion.heroes.R;
import com.bumptech.glide.Glide;

public class CustomBindingAdapter {

    @BindingAdapter("imageUrl")
    public static void loadImage(ImageView imageView, String url) {
        Glide.with(imageView.getContext())
                .load(url)
                .centerCrop()
//                .override(Target.SIZE_ORIGINAL, Target.SIZE_ORIGINAL)
                .error(R.mipmap.ic_launcher)
                .into(imageView);
    }

} 