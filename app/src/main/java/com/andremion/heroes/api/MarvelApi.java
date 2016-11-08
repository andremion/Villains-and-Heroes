package com.andremion.heroes.api;

import com.andremion.heroes.BuildConfig;
import com.andremion.heroes.api.auth.AuthenticatorInterceptor;
import com.andremion.heroes.api.data.CharacterVO;
import com.andremion.heroes.api.data.SectionVO;
import com.andremion.heroes.api.json.CharacterDataContainer;
import com.andremion.heroes.api.json.CharacterDataWrapper;
import com.andremion.heroes.api.json.SectionDataWrapper;
import com.andremion.heroes.api.util.DataParser;

import java.io.IOException;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import okhttp3.logging.HttpLoggingInterceptor.Level;
import retrofit2.Call;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public class MarvelApi {

    public static final int MAX_FETCH_LIMIT = 20;

    private static final String BASE_URL = "http://gateway.marvel.com/v1/public/";
    private static MarvelApi sMarvelApi;
    private final MarvelService mService;
    private Call<CharacterDataWrapper> mLastSearchCall;

    private MarvelApi() {

        AuthenticatorInterceptor authenticator = new AuthenticatorInterceptor();

        HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
        logging.setLevel(BuildConfig.DEBUG ? Level.BODY : Level.BASIC);

        OkHttpClient client = new OkHttpClient.Builder()
                .addInterceptor(authenticator)
                .addInterceptor(logging)
                .build();

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
        Response<CharacterDataWrapper> response = mService.listCharacters(null, offset, MAX_FETCH_LIMIT).execute();
        if (response.isSuccessful()) {
            return DataParser.parse(response.body());
        } else {
            throw new MarvelException(response.code(), response.message());
        }
    }

    public void listCharacters(int offset, MarvelCallback<CharacterDataWrapper> callback) {
        mService.listCharacters(null, offset, MAX_FETCH_LIMIT).enqueue(callback);
    }

    public void searchCharacters(String query, MarvelCallback<CharacterDataWrapper> callback) {
        if (mLastSearchCall != null && !mLastSearchCall.isCanceled()) {
            mLastSearchCall.cancel();
        }
        mLastSearchCall = mService.listCharacters(query, /* offset */ 0, MAX_FETCH_LIMIT);
        mLastSearchCall.enqueue(callback);
    }

    public int getTotalOfCharacters() throws IOException, MarvelException {
        int limit = 1;
        Response<CharacterDataWrapper> response = mService.listCharacters(null, /* offset */ 0, limit).execute();
        if (response.isSuccessful()) {
            CharacterDataContainer dataContainer = response.body().data;
            if (dataContainer != null) {
                return dataContainer.total;
            }
            return limit;
        } else {
            throw new MarvelException(response.code(), response.message());
        }
    }

    public MarvelResult<CharacterVO> getCharacter(int offset) throws IOException, MarvelException {
        Response<CharacterDataWrapper> response = mService.listCharacters(null, offset, /* limit */ 1).execute();
        if (response.isSuccessful()) {
            return DataParser.parse(response.body());
        } else {
            throw new MarvelException(response.code(), response.message());
        }
    }

    public MarvelResult<SectionVO> listComics(long characterId, int offset) throws IOException, MarvelException {
        Response<SectionDataWrapper> response = mService.listComics(characterId, offset, MAX_FETCH_LIMIT).execute();
        if (response.isSuccessful()) {
            return DataParser.parse(response.body());
        } else {
            throw new MarvelException(response.code(), response.message());
        }
    }

    public void listComics(long characterId, int offset, MarvelCallback<SectionDataWrapper> callback) {
        mService.listComics(characterId, offset, MAX_FETCH_LIMIT).enqueue(callback);
    }

    public MarvelResult<SectionVO> listSeries(long characterId, int offset) throws IOException, MarvelException {
        Response<SectionDataWrapper> response = mService.listSeries(characterId, offset, MAX_FETCH_LIMIT).execute();
        if (response.isSuccessful()) {
            return DataParser.parse(response.body());
        } else {
            throw new MarvelException(response.code(), response.message());
        }
    }

    public void listSeries(long characterId, int offset, MarvelCallback<SectionDataWrapper> callback) {
        mService.listSeries(characterId, offset, MAX_FETCH_LIMIT).enqueue(callback);
    }

    public MarvelResult<SectionVO> listStories(long characterId, int offset) throws IOException, MarvelException {
        Response<SectionDataWrapper> response = mService.listStories(characterId, offset, MAX_FETCH_LIMIT).execute();
        if (response.isSuccessful()) {
            return DataParser.parse(response.body());
        } else {
            throw new MarvelException(response.code(), response.message());
        }
    }

    public void listStories(long characterId, int offset, MarvelCallback<SectionDataWrapper> callback) {
        mService.listStories(characterId, offset, MAX_FETCH_LIMIT).enqueue(callback);
    }

    public MarvelResult<SectionVO> listEvents(long characterId, int offset) throws IOException, MarvelException {
        Response<SectionDataWrapper> response = mService.listEvents(characterId, offset, MAX_FETCH_LIMIT).execute();
        if (response.isSuccessful()) {
            return DataParser.parse(response.body());
        } else {
            throw new MarvelException(response.code(), response.message());
        }
    }

    public void listEvents(long characterId, int offset, MarvelCallback<SectionDataWrapper> callback) {
        mService.listEvents(characterId, offset, MAX_FETCH_LIMIT).enqueue(callback);
    }

    private interface MarvelService {

        @GET("characters")
        Call<CharacterDataWrapper> listCharacters(
                @Query("nameStartsWith") String query,
                @Query("offset") int offset,
                @Query("limit") int limit);

        @GET("characters/{characterId}/comics")
        Call<SectionDataWrapper> listComics(
                @Path("characterId") long characterId,
                @Query("offset") int offset,
                @Query("limit") int limit);

        @GET("characters/{characterId}/series")
        Call<SectionDataWrapper> listSeries(
                @Path("characterId") long characterId,
                @Query("offset") int offset,
                @Query("limit") int limit);

        @GET("characters/{characterId}/stories")
        Call<SectionDataWrapper> listStories(
                @Path("characterId") long characterId,
                @Query("offset") int offset,
                @Query("limit") int limit);

        @GET("characters/{characterId}/events")
        Call<SectionDataWrapper> listEvents(
                @Path("characterId") long characterId,
                @Query("offset") int offset,
                @Query("limit") int limit);
    }

}
