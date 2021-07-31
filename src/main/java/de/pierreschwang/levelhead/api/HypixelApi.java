package de.pierreschwang.levelhead.api;

import com.google.common.util.concurrent.ListenableFuture;
import com.google.common.util.concurrent.SettableFuture;
import com.google.gson.Gson;
import de.pierreschwang.levelhead.api.data.HypixelPlayerData;
import de.pierreschwang.levelhead.api.util.HypixelCallback;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.Objects;
import java.util.UUID;

public class HypixelApi {

    private static final String API_HOST = "api.hypixel.net";
    private static final String API_SCHEME = "https";

    private final Gson gson;
    private final OkHttpClient httpClient;

    public HypixelApi(HypixelTokenProvider tokenProvider) {
        this.gson = new Gson();
        this.httpClient = new OkHttpClient.Builder()
                .addInterceptor(new HypixelApiAuthenticator(tokenProvider))
                .build();
    }

    public ListenableFuture<HypixelApiResponse<HypixelPlayerData>> getPlayerData(UUID playerId) {
        SettableFuture<HypixelApiResponse<HypixelPlayerData>> result = SettableFuture.create();
        Request request = new Request.Builder().get()
                .url(new HttpUrl.Builder()
                        .scheme(API_SCHEME).host(API_HOST).addPathSegment("player")
                        .addQueryParameter("uuid", playerId.toString())
                        .build()
                ).build();
        httpClient.newCall(request).enqueue(HypixelCallback.forGson(gson, result, HypixelPlayerData.class));
        return result;
    }

}
