package de.pierreschwang.levelhead.api;

import okhttp3.*;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;

public class HypixelApiAuthenticator implements Interceptor {

    private final HypixelTokenProvider tokenProvider;

    public HypixelApiAuthenticator(HypixelTokenProvider tokenProvider) {
        this.tokenProvider = tokenProvider;
    }

    @NotNull
    @Override
    public Response intercept(@NotNull Chain chain) throws IOException {
        Request request = tokenProvider.getApiToken()
                .map(token -> chain.request().newBuilder().addHeader("API-Key", token).build())
                .orElse(null);
        if (request == null) {
            return chain.proceed(chain.request());
        }
        return chain.proceed(request);
    }

}