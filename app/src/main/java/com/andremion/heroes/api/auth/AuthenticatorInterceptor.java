package com.andremion.heroes.api.auth;

import java.io.IOException;

import okhttp3.HttpUrl;
import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

public class AuthenticatorInterceptor implements Interceptor {

    // TODO: Put your private key here
    private static final String PRIVATE_KEY = "###";
    // TODO: Put your public key here
    private static final String PUBLIC_KEY = "###";

    private static final String QUERY_NAME_TIMESTAMP = "ts";
    private static final String QUERY_NAME_APIKEY = "apikey";
    private static final String QUERY_NAME_HASH = "hash";

    @Override
    public Response intercept(Chain chain) throws IOException {

        String ts = String.valueOf(System.currentTimeMillis());
        String hash = HashHelper.generate(ts + PRIVATE_KEY + PUBLIC_KEY);

        Request currentRequest = chain.request();

        HttpUrl url = currentRequest.url().newBuilder()
                .addQueryParameter(QUERY_NAME_TIMESTAMP, ts)
                .addQueryParameter(QUERY_NAME_APIKEY, PUBLIC_KEY)
                .addQueryParameter(QUERY_NAME_HASH, hash).build();

        Request newRequest = currentRequest.newBuilder().url(url).build();

        return chain.proceed(newRequest);
    }
}
