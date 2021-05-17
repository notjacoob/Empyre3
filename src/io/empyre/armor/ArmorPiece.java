package io.empyre.armor;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import io.empyre.Empyre;
import io.empyre.util.ChatUtil;
import io.empyre.util.JsonUtil;
import org.bukkit.NamespacedKey;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeModifier;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class ArmorPiece {

    public static ArmorPiece fromJSON(JsonObject object, ArmorSet parent) {
        Appendage a = Appendage.valueOf(object.get("appendage").getAsString());
        int modelDataKey=object.get("modelDataKey").getAsInt();
        int genericStat=object.get("genericStat").getAsInt();
        int health = object.get("health").getAsInt();
        ArmorType type=ArmorType.valueOf(object.get("armorType").getAsString());
        JsonElement meta = object.get("meta");
        if (meta != null) {
            JsonElement title = meta.getAsJsonObject().get("title");
            JsonElement lore = meta.getAsJsonObject().get("lore");
            if (title != null && lore != null) {
                return new ArmorPiece(parent, a, modelDataKey, genericStat, type, health, title.getAsString(), JsonUtil.jsonArrayToStringArray(lore.getAsJsonArray()));
            } else if (title == null && lore != null) {
                return new ArmorPiece(parent, a, modelDataKey, genericStat, type, health,  JsonUtil.jsonArrayToStringArray(lore.getAsJsonArray()));
            } else if (title != null){
                return new ArmorPiece(parent, a, modelDataKey, genericStat, type, health,  title.getAsString());
            } else {
                return new ArmorPiece(parent, a, modelDataKey, genericStat,type, health);
            }
        } else {
            return new ArmorPiece(parent, a, modelDataKey, genericStat, type, health);
        }
    }


    private final Appendage app;
    private final int modelDataKey;
    private final int genericStat;
    private final int healthIncrease;
    private final ArmorType type;
    private final ArmorSet parent;
    private String title;
    private List<String> lore;
    public ArmorPiece(ArmorSet parent, Appendage app, int modelDataKey, int genericStat, ArmorType type, int healthIncrease, String title) {
        this(parent, app, modelDataKey, genericStat, type, healthIncrease);
        this.title=title;
        this.lore= Collections.emptyList();
    }
    public ArmorPiece(ArmorSet parent, Appendage app, int modelDataKey, int genericStat, ArmorType type, int healthIncrease) {
        this.app=app;
        this.parent=parent;
        this.modelDataKey=modelDataKey;
        this.genericStat=genericStat;
        this.type=type;
        this.healthIncrease=healthIncrease;
        this.lore=Collections.emptyList();
        this.title=app.withType(type).name();
    }
    public ArmorPiece(ArmorSet parent, Appendage app, int modelDataKey, int genericStat, ArmorType type, int healthIncrease, List<String> lore) {
        this(parent, app, modelDataKey, genericStat, type, healthIncrease);
        this.lore=lore;
        this.title=app.withType(type).name();
    }
    public ArmorPiece(ArmorSet parent, Appendage app, int modelDataKey, int genericStat, ArmorType type, int healthIncrease, String title, List<String> lore) {
        this(parent, app, modelDataKey, genericStat, type, healthIncrease);
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
    public int getHealthIncrease() {
        return healthIncrease;
    }
    public List<String> getLore() {
        return lore.stream().map(ChatUtil::cl).collect(Collectors.toList());
    }
    public ArmorType getType() {
        return type;
    }
    public ItemStack toStack() {
        List<String> finalLore = new ArrayList<>();
        ItemStack stack = new ItemStack(app.withType(type));
        ItemMeta meta = stack.getItemMeta();
        meta.addAttributeModifier(Attribute.GENERIC_ARMOR, new AttributeModifier(this.app.generic, genericStat, AttributeModifier.Operation.ADD_NUMBER));
        meta.setCustomModelData(modelDataKey);
        meta.setDisplayName(ChatUtil.cl(title));
        if (genericStat > 0) {
            finalLore.add(ChatUtil.cl("&b+" + genericStat + " armor"));
        }
        if (this.healthIncrease > 0) {
            finalLore.add(ChatUtil.cl("&c+" + this.healthIncrease + " health"));
        }
        finalLore.addAll(lore);
        meta.setLore(finalLore.stream().map(ChatUtil::cl).collect(Collectors.toList()));
        meta.getPersistentDataContainer().set(new NamespacedKey(Empyre.getPlugin(), "armor_set"), PersistentDataType.STRING, parent.getKey());
        stack.setItemMeta(meta);
        stack.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
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
        meta.add("lore", JsonUtil.listToJsonArray(lore));
        obj.add("meta", meta);
        return obj;
    }
}
