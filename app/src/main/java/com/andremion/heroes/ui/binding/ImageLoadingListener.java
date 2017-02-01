/*
 * Copyright (c) 2017. André Mion
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.andremion.heroes.ui.binding;

import android.support.annotation.NonNull;

import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;

public abstract class ImageLoadingListener implements RequestListener<String, GlideDrawable> {

    @Override
    public final boolean onResourceReady(GlideDrawable resource, String model, Target<GlideDrawable> target, boolean isFromMemoryCache, boolean isFirstResource) {
        onSuccess();
        return false;
    }

    @Override
    public final boolean onException(Exception e, String model, Target<GlideDrawable> target, boolean isFirstResource) {
        onFailed(e);
        return false;
    }

    public abstract void onSuccess();

    public abstract void onFailed(@NonNull Exception e);
}
