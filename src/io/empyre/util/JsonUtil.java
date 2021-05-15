package io.empyre.util;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.JsonPrimitive;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class JsonUtil {
    public static List<String> jsonArrayToStringArray(JsonArray arr) {
        List<String> list = new ArrayList<>();
        arr.forEach(el -> {
            list.add(el.getAsString());
        });
        return list;
    }
    public static JsonArray listToJsonArray(List<String> list) {
        JsonArray arr = new JsonArray();
        list.forEach(el -> arr.add(new JsonPrimitive(el)));
        return arr;
    }
    public static JsonObject loadJsonFile(File f) throws IOException {
        BufferedReader r = new BufferedReader(new InputStreamReader(new FileInputStream(f)));
        JsonObject obj = new JsonParser().parse(r).getAsJsonObject();
        r.close();
        return obj;
    }

}
