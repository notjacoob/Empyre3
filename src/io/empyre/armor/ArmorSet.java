package io.empyre.armor;

import com.google.common.io.Files;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import io.empyre.io.Data;

import java.io.*;
import java.util.HashMap;
import java.util.Map;

public class ArmorSet {

    public static ArmorSet load(String key) throws IOException {
        File file = new File(Data.PATH + "/armor/" + key + ".json");
        BufferedReader r = new BufferedReader(new InputStreamReader(new FileInputStream(file)));
        StringBuilder sb = new StringBuilder();
        String line;
        while ((line = r.readLine()) != null) {
            sb.append(line);
        }
        r.close();
        System.out.println(sb.toString());
        JsonObject obj = new JsonParser().parse(sb.toString()).getAsJsonObject();
        return new ArmorSet(obj);
    }

    private static HashMap<String, ArmorSet> sets = new HashMap<>();
    public static Map<String, ArmorSet> getSets() {
        return sets;
    }
    private ArmorPiece HELMET;
    private ArmorPiece CHESTPLATE;
    private ArmorPiece LEGGINGS;
    private ArmorPiece BOOTS;
    private final String KEY;

    private ArmorSet() {KEY="thisisneverused";}
    private ArmorSet(JsonObject obj) {
        KEY = obj.get("key").getAsString();
        HELMET = ArmorPiece.fromJSON(obj.getAsJsonObject("helmet"), KEY);
        CHESTPLATE = ArmorPiece.fromJSON(obj.getAsJsonObject("chestplate"), KEY);
        LEGGINGS = ArmorPiece.fromJSON(obj.getAsJsonObject("leggings"), KEY);
        BOOTS = ArmorPiece.fromJSON(obj.getAsJsonObject("boots"), KEY);
        sets.put(this.KEY, this);
    }
    public ArmorPiece getHelmet() {
        return HELMET;
    }
    public ArmorPiece getChestplate() {
        return CHESTPLATE;
    }
    public ArmorPiece getLeggings() {
        return LEGGINGS;
    }
    public ArmorPiece getBoots() {
        return BOOTS;
    }
    public String getKey() {
        return KEY;
    }

}
