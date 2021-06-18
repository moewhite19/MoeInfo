package cn.whiteg.moeInfo.utils;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.serializer.gson.GsonComponentSerializer;
import org.bukkit.ChatColor;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;
import org.jetbrains.annotations.Nullable;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ComponentUtils {
    private static Field jsonValueField;

    static {
        try{
            jsonValueField = JsonPrimitive.class.getDeclaredField("value");
            jsonValueField.setAccessible(true);
        }catch (NoSuchFieldException e){
            e.printStackTrace();
        }
    }

    //从字符串加载
    public static Component ofString(String str) {
        if (str == null || str.isEmpty()) return Component.empty();
        str = ChatColor.translateAlternateColorCodes('&',str);
        if (str.startsWith("{") && str.endsWith("}"))
            try{
                return GsonComponentSerializer.gson().deserialize(str);
            }catch (Exception ignored){
            }
        return Component.text(str);
    }

    //从配置文件加载
    public static Component ofYml(Object obj) {
        if (obj instanceof String){
            return ofString((String) obj);
        } else if (obj instanceof ConfigurationSection){
            JsonElement gson = ymlToGson(obj);
            return GsonComponentSerializer.gson().deserializeFromTree(gson);
        } else if (obj instanceof Number){
            return Component.text(String.valueOf(obj));
        } else if (obj instanceof Boolean){
            return Component.text(String.valueOf(obj));
        }
        return Component.empty();
    }

    //yml转json
    public static JsonElement ymlToGson(Object o) {
        if (o instanceof ConfigurationSection){
            ConfigurationSection section = (ConfigurationSection) o;
            JsonObject jsonObject = new JsonObject();
            for (String key : section.getKeys(false)) {
                @Nullable Object obj = section.get(key);
                jsonObject.add(key,ymlToGson(obj));
            }
        } else if (o instanceof List){
            List<?> list = (List<?>) o;
            JsonArray jsonArray = new JsonArray();
            for (Object item : list) {
                jsonArray.add(ymlToGson(item));
            }
        } else if (o instanceof String) return new JsonPrimitive((String) o);
        else if (o instanceof Number) return new JsonPrimitive(((Number) o));
        else if (o instanceof Boolean) return new JsonPrimitive(((Boolean) o));
        return new JsonPrimitive("theUnknownObject:" + o);
    }

    //json转yaml
    public static Object gsonToYaml(JsonElement jsonElement) {
        if (jsonElement instanceof JsonPrimitive){
            try{
                return jsonValueField.get(jsonElement);
            }catch (IllegalAccessException e){
                return null;
            }
        } else if (jsonElement instanceof JsonObject){
            JsonObject object = (JsonObject) jsonElement;
            var yml = new YamlConfiguration();
            for (Map.Entry<String, JsonElement> entry : object.entrySet()) {
                yml.set(entry.getKey(),gsonToYaml(entry.getValue()));
            }
            return yml;
        } else if (jsonElement instanceof JsonArray){
            JsonArray element = (JsonArray) jsonElement;
            ArrayList<Object> list = new ArrayList<>(element.size());
            for (JsonElement item : element) {
                list.add(gsonToYaml(item));
            }
            return list;
        }
        return null;
    }
}
