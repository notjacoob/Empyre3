package io.empyre.armor;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import io.empyre.Empyre;
import io.empyre.util.ChatUtil;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.attribute.Attribute;
import org.bukkit.inventory.ItemFactory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class ArmorPiece {

    public static ArmorPiece fromJSON(JsonObject object, String parent) {
        Appendage a = Appendage.valueOf(object.get("appendage").getAsString());
        int modelDataKey=object.get("modelDataKey").getAsInt();
        int genericStat=object.get("genericStat").getAsInt();
        ArmorType type=ArmorType.valueOf(object.get("armorType").getAsString());
        JsonElement meta = object.get("meta");
        if (meta != null) {
            JsonElement title = meta.getAsJsonObject().get("title");
            JsonElement lore = meta.getAsJsonObject().get("lore");
            if (title != null && lore != null) {
                return new ArmorPiece(parent, a, modelDataKey, genericStat, type, title.getAsString(), jsonArrayToStringArray(lore.getAsJsonArray()));
            } else if (title == null && lore != null) {
                return new ArmorPiece(parent, a, modelDataKey, genericStat, type, jsonArrayToStringArray(lore.getAsJsonArray()));
            } else if (title != null){
                return new ArmorPiece(parent, a, modelDataKey, genericStat, type, title.getAsString());
            } else {
                return new ArmorPiece(parent, a, modelDataKey, genericStat, type);
            }
        } else {
            return new ArmorPiece(parent, a, modelDataKey, genericStat, type);
        }
    }

    private static List<String> jsonArrayToStringArray(JsonArray arr) {
        List<String> list = new ArrayList<>();
        arr.forEach(el -> {
            list.add(el.getAsString());
        });
        return list;
    }

    private final Appendage app;
    private final int modelDataKey;
    private final int genericStat;
    private final ArmorType type;
    private final String parent;
    private String title;
    private List<String> lore;
    public ArmorPiece(String parent, Appendage app, int modelDataKey, int genericStat, ArmorType type, String title) {
        this(parent, app, modelDataKey, genericStat, type);
        this.title=title;
        this.lore= Collections.emptyList();
    }
    public ArmorPiece(String parent, Appendage app, int modelDataKey, int genericStat, ArmorType type) {
        this.app=app;
        this.parent=parent;
        this.modelDataKey=modelDataKey;
        this.genericStat=genericStat;
        this.type=type;
        this.lore=Collections.emptyList();
        this.title=app.withType(type).name();
    }
    public ArmorPiece(String parent, Appendage app, int modelDataKey, int genericStat, ArmorType type, List<String> lore) {
        this(parent, app, modelDataKey, genericStat, type);
        this.lore=lore;
        this.title=app.withType(type).name();
    }
    public ArmorPiece(String parent, Appendage app, int modelDataKey, int genericStat, ArmorType type, String title, List<String> lore) {
        this(parent, app, modelDataKey, genericStat, type);
        this.title=title;
        this.lore=lore;
    }
    public Appendage getAppendage() {
        return app;
    }
    public int getModelDataKey() {
        return modelDataKey;
    }
    public int getGenericStat() {
        return genericStat;
    }
    public String getTitle() {
        return ChatUtil.cl(title);
    }
    public List<String> getLore() {
        return lore.stream().map(ChatUtil::cl).collect(Collectors.toList());
    }
    public ArmorType getType() {
        return type;
    }
    private JsonArray listToJsonArray(List<String> list) {
        JsonArray arr = new JsonArray();
        list.forEach(el -> arr.add(new JsonPrimitive(el)));
        return arr;
    }
    public ItemStack toStack() {
        ItemStack stack = new ItemStack(app.withType(type));
        ItemMeta meta = stack.getItemMeta();
        meta.setCustomModelData(modelDataKey);
        meta.setDisplayName(ChatUtil.cl(title));
        meta.setLore(lore.stream().map(ChatUtil::cl).collect(Collectors.toList()));
        meta.getPersistentDataContainer().set(new NamespacedKey(Empyre.getPlugin(), "armor_set"), PersistentDataType.STRING, parent);
        stack.setItemMeta(meta);
        return stack;
    }
    public JsonObject toJson() {
        JsonObject obj = new JsonObject();
        obj.addProperty("appendage", app.name());
        obj.addProperty("modelDataKey", modelDataKey);
        obj.addProperty("genericStat", genericStat);
        obj.addProperty("armorType", type.name());
        JsonObject meta = new JsonObject();
        meta.addProperty("title", title);
        meta.add("lore", listToJsonArray(lore));
        obj.add("meta", meta);
        return obj;
    }
}
