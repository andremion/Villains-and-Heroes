package com.andremion.heroes.ui.binding;

import android.databinding.BindingAdapter;
import android.widget.ImageView;

import com.andremion.heroes.R;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.Target;

public class CustomBindingAdapter {

    @BindingAdapter("app:imageUrl")
    public static void loadImage(ImageView imageView, String url) {
        Glide.with(imageView.getContext())
                .load(url)
                .override(Target.SIZE_ORIGINAL, Target.SIZE_ORIGINAL)
                .error(R.mipmap.ic_launcher)
                .into(imageView);
    }

} 