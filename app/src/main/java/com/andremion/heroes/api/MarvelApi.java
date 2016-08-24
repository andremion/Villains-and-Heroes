package com.andremion.heroes.api;

import com.andremion.heroes.api.auth.AuthenticatorInterceptor;
import com.andremion.heroes.api.data.CharacterVO;
import com.andremion.heroes.api.data.ComicVO;
import com.andremion.heroes.api.data.SeriesVO;
import com.andremion.heroes.api.data.StoryVO;
import com.andremion.heroes.api.json.CharacterDataWrapper;
import com.andremion.heroes.api.json.ComicDataWrapper;
import com.andremion.heroes.api.json.SeriesDataWrapper;
import com.andremion.heroes.api.json.StoryDataWrapper;
import com.andremion.heroes.api.util.DataParser;

import java.io.IOException;

import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public class MarvelApi {

    public static final int MAX_FETCH_LIMIT = 100;

    private static final String BASE_URL = "http://gateway.marvel.com/v1/public/";
    private static MarvelApi sMarvelApi;
    private final MarvelService mService;

    private MarvelApi() {

        OkHttpClient client = new OkHttpClient.Builder()
                .addInterceptor(new AuthenticatorInterceptor()).build();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .client(client)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        mService = retrofit.create(MarvelService.class);
    }

    public static MarvelApi getInstance() {
        if (sMarvelApi == null) {
            sMarvelApi = new MarvelApi();
        }
        return sMarvelApi;
    }

    public MarvelResult<CharacterVO> listCharacters(int offset) throws IOException, MarvelException {
        Response<CharacterDataWrapper> response = mService.listCharacters(offset, MAX_FETCH_LIMIT).execute();
        if (response.isSuccessful()) {
            return DataParser.parse(response.body());
        } else {
            throw new MarvelException(response.code(), response.message());
        }
    }

    public MarvelResult<ComicVO> listComics(long characterId, int offset) throws IOException, MarvelException {
        Response<ComicDataWrapper> response = mService.listComics(characterId, offset, MAX_FETCH_LIMIT).execute();
        if (response.isSuccessful()) {
            return DataParser.parse(response.body());
        } else {
            throw new MarvelException(response.code(), response.message());
        }
    }

    public MarvelResult<SeriesVO> listSeries(long characterId, int offset) throws IOException, MarvelException {
        Response<SeriesDataWrapper> response = mService.listSeries(characterId, offset, MAX_FETCH_LIMIT).execute();
        if (response.isSuccessful()) {
            return DataParser.parse(response.body());
        } else {
            throw new MarvelException(response.code(), response.message());
        }
    }

    public MarvelResult<StoryVO> listStories(long characterId, int offset) throws IOException, MarvelException {
        Response<StoryDataWrapper> response = mService.listStories(characterId, offset, MAX_FETCH_LIMIT).execute();
        if (response.isSuccessful()) {
            return DataParser.parse(response.body());
        } else {
            throw new MarvelException(response.code(), response.message());
        }
    }

    private interface MarvelService {

        @GET("characters")
        Call<CharacterDataWrapper> listCharacters(
                @Query("offset") int offset,
                @Query("limit") int limit);

        @GET("characters/{characterId}/comics")
        Call<ComicDataWrapper> listComics(
                @Path("characterId") long characterId,
                @Query("offset") int offset,
                @Query("limit") int limit);

        @GET("characters/{characterId}/series")
        Call<SeriesDataWrapper> listSeries(
                @Path("characterId") long characterId,
                @Query("offset") int offset,
                @Query("limit") int limit);

        @GET("characters/{characterId}/stories")
        Call<StoryDataWrapper> listStories(
                @Path("characterId") long characterId,
                @Query("offset") int offset,
                @Query("limit") int limit);
    }

}
