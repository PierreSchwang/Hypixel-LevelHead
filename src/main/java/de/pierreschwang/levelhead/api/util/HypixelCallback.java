package de.pierreschwang.levelhead.api.util;

import com.google.common.util.concurrent.SettableFuture;
import com.google.gson.Gson;
import de.pierreschwang.levelhead.api.HypixelApiResponse;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.Objects;

public abstract class HypixelCallback<T> implements Callback {

    private final SettableFuture<HypixelApiResponse<T>> future;

    public HypixelCallback(SettableFuture<HypixelApiResponse<T>> future) {
        this.future = future;
    }

    @Override
    public void onFailure(@NotNull Call call, @NotNull IOException e) {
        future.setException(e);
    }

    @Override
    public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
        if (response.code() != 200) {
            future.set(HypixelApiResponse.of(response.code(), null));
            return;
        }
        future.set(HypixelApiResponse.of(response.code(), handle(response)));
    }

    public abstract T handle(@NotNull Response response) throws IOException;

    public static <E> HypixelCallback<E> forGson(
            Gson gson,
            SettableFuture<HypixelApiResponse<E>> future,
            Class<E> clazz
    ) {
        return new HypixelCallback<E>(future) {
            @Override
            public E handle(@NotNull Response response) throws IOException {
                return gson.fromJson(Objects.requireNonNull(response.body()).string(), clazz);
            }
        };
    }

}
