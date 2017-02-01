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

package com.andremion.heroes.api;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public abstract class MarvelCallback<T> implements Callback<T> {

    @Override
    public final void onResponse(Call<T> call, Response<T> response) {
        if (response.isSuccessful()) {
            onResult(response.body());
        } else {
            onError(new MarvelException(response.code(), response.message()));
        }
    }

    @Override
    public final void onFailure(Call<T> call, Throwable t) {
        if (!call.isCanceled()) { // Cancellation is not an error because we cancel it
            onError(t);
        }
    }

    public abstract void onResult(T data);

    public abstract void onError(Throwable e);

}
