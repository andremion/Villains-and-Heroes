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
