package de.pierreschwang.levelhead.api.adapter;

import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;

import java.io.IOException;
import java.util.UUID;
import java.util.regex.Pattern;

public class UuidJsonAdapter extends TypeAdapter<UUID> {

    private static final Pattern UUID_TO_DASHES = Pattern.compile("(\\w{8})(\\w{4})(\\w{4})(\\w{4})(\\w{12})");

    @Override
    public void write(JsonWriter jsonWriter, UUID uuid) throws IOException {
        jsonWriter.value(uuid.toString());
    }

    @Override
    public UUID read(JsonReader jsonReader) throws IOException {
        String uuidValue = jsonReader.nextString();
        if (uuidValue.contains("-")) {
            return UUID.fromString(uuidValue);
        }
        return UUID.fromString(UUID_TO_DASHES.matcher(uuidValue).replaceAll("$1-$2-$3-$4-$5"));
    }

}