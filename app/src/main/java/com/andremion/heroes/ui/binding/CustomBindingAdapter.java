package com.andremion.heroes.ui.binding;

import android.databinding.BindingAdapter;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.widget.ImageView;

import com.andremion.heroes.R;
import com.bumptech.glide.Glide;

public class CustomBindingAdapter {

    @BindingAdapter(value = {"imageUrl", "listener"}, requireAll = false)
    public static void loadImage(@NonNull ImageView imageView, @Nullable String url, @Nullable ImageLoadingListener listener) {
        Glide.with(imageView.getContext().getApplicationContext())
                .load(url)
                .listener(listener)
                .error(R.drawable.ic_no_image)
                .into(imageView);
    }

}