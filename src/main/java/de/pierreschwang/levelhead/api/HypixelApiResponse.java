package de.pierreschwang.levelhead.api;

public class HypixelApiResponse<T> {

    private final boolean limitReached;
    private final boolean invalidApiKey;
    private final T response;

    public HypixelApiResponse(boolean limitReached, boolean invalidApiKey, T response) {
        this.limitReached = limitReached;
        this.invalidApiKey = invalidApiKey;
        this.response = response;
    }

    public boolean isLimitReached() {
        return limitReached;
    }

    public boolean isInvalidApiKey() {
        return invalidApiKey;
    }

    public T getResponse() {
        return response;
    }

    public static <Data> HypixelApiResponse<Data> of(int status, Data data) {
        if (status == 403) {
            return new HypixelApiResponse<>(false, true, null);
        }
        if (status == 429) {
            return new HypixelApiResponse<>(true, false, null);
        }
        return new HypixelApiResponse<>(false, false, data);
    }

}