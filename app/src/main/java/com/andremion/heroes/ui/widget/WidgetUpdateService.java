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

package com.andremion.heroes.ui.widget;

import android.app.IntentService;
import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProviderInfo;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.support.annotation.NonNull;
import android.util.Log;
import android.widget.RemoteViews;

import com.andremion.heroes.R;
import com.andremion.heroes.api.MarvelApi;
import com.andremion.heroes.api.MarvelException;
import com.andremion.heroes.api.MarvelResult;
import com.andremion.heroes.api.data.CharacterVO;
import com.andremion.heroes.ui.character.view.CharacterActivity;
import com.bumptech.glide.Glide;

import java.io.IOException;
import java.util.List;
import java.util.Random;
import java.util.concurrent.ExecutionException;

public class WidgetUpdateService extends IntentService {

    private static final String TAG = WidgetUpdateService.class.getSimpleName();
    private static final String EXTRA_WIDGET_IDS = WidgetUpdateService.class.getPackage().getName() + ".extra.WIDGET_IDS";

    public static void start(@NonNull Context context, @NonNull int[] appWidgetIds) {
        Intent intent = new Intent(context, WidgetUpdateService.class);
        intent.putExtra(EXTRA_WIDGET_IDS, appWidgetIds);
        context.startService(intent);
    }

    public WidgetUpdateService() {
        super(TAG);
    }

    @Override
    protected void onHandleIntent(Intent intent) {

        MarvelApi marvelApi = MarvelApi.getInstance();

        // Fetch the total of characters.
        int totalCharacters = 1;
        try {
            totalCharacters = marvelApi.getTotalOfCharacters();
        } catch (IOException | MarvelException e) {
            Log.w(TAG, "Problem loading total of characters", e);
        }

        Random randomNumberGenerator = new Random();

        AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(this);
        // Update just the widgets that needs to be updated.
        int[] appWidgetIds = intent.getIntArrayExtra(EXTRA_WIDGET_IDS);

        for (int id : appWidgetIds) {

            AppWidgetProviderInfo appWidgetInfo = appWidgetManager.getAppWidgetInfo(id);
            int width = appWidgetInfo.minWidth;
            int height = appWidgetInfo.minHeight;
            int randomNumber = randomNumberGenerator.nextInt(totalCharacters);

            try {
                // Fetch the character
                MarvelResult<CharacterVO> result = marvelApi.getCharacter(randomNumber);
                List<CharacterVO> entries = result.getEntries();
                if (entries.isEmpty()) {
                    Log.w(TAG, "No character found");
                } else {

                    CharacterVO character = entries.get(0);

                    // Load character info into widget view
                    RemoteViews views = new RemoteViews(getPackageName(), R.layout.widget_character);
                    Bitmap image = Glide.with(getApplicationContext())
                            .load(character.getImage())
                            .asBitmap()
                            .into(width, height)
                            .get();
                    views.setImageViewBitmap(R.id.image, image);
                    views.setTextViewText(R.id.title, character.getName());

                    // Open CharacterActivity when click on widget view
                    PendingIntent pendingIntent = CharacterActivity.getPendingIntent(this, character, id);
                    views.setOnClickPendingIntent(R.id.widget, pendingIntent);

                    // Update the widget
                    appWidgetManager.updateAppWidget(id, views);
                }

            } catch (IOException | MarvelException e) {
                Log.w(TAG, "Problem loading character", e);
            } catch (InterruptedException | ExecutionException e) {
                Log.w(TAG, "Problem loading image", e);
            }
        }
    }
}

