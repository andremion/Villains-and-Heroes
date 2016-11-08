package com.andremion.heroes.ui.util;

import android.content.Context;
import android.support.annotation.NonNull;

import com.andremion.heroes.R;
import com.andremion.heroes.api.MarvelException;

import java.io.IOException;

public class StringUtils {

    private StringUtils() {
    }

    public static String getApiErrorMessage(@NonNull Context context, @NonNull Throwable e) {
        if (e instanceof IOException) {
            return context.getString(R.string.connection_error);
        } else if (e instanceof MarvelException) {
            return context.getString(R.string.server_error);
        } else {
            return "";
        }
    }

}
