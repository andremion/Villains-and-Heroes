package com.andremion.heroes.ui.binding;

import android.databinding.BindingAdapter;
import android.widget.ImageView;

import com.andremion.heroes.R;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.RequestListener;

public class CustomBindingAdapter {

    @BindingAdapter(value = {"imageUrl", "listener"}, requireAll = false)
    public static void loadImage(ImageView imageView, String url, RequestListener<String, GlideDrawable> listener) {
        Glide.with(imageView.getContext().getApplicationContext())
                .load(url)
                .listener(listener)
                .error(R.mipmap.ic_launcher)
                .into(imageView);
    }

}