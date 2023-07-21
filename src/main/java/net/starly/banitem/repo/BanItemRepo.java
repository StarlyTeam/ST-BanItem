package net.starly.banitem.repo;

import com.google.common.reflect.TypeToken;
import com.google.gson.*;
import net.starly.banitem.BanItem;
import net.starly.banitem.manager.BanItemManager;
import net.starly.banitem.util.EncodeUtil;
import net.starly.core.jb.util.Pair;
import org.bukkit.World;
import org.bukkit.inventory.ItemStack;

import java.io.File;
import java.io.IOException;
import java.io.Reader;
import java.io.Writer;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.*;

public class BanItemRepo {

    private static BanItemRepo instance;
    public static BanItemRepo getInstance() {
        if (instance == null) instance = new BanItemRepo();
        return instance;
    }

    private final File dataFile;
    private final Gson gson = new GsonBuilder().setPrettyPrinting().create();

    private BanItemRepo() {
        dataFile = new File(BanItem.getInstance().getDataFolder(),"data.json");
        if (!dataFile.exists()) {
            try {
                dataFile.createNewFile();
                saveData();
            } catch (IOException exception) { exception.printStackTrace(); }
        }
    }

    public void saveData() {
        try (Writer writer = Files.newBufferedWriter(dataFile.toPath(), StandardCharsets.UTF_8)) {
            JsonObject json = new JsonObject();
            JsonArray jsonArray = new JsonArray();

            BanItemManager.getInstance().getBanItemList().forEach((uuid, items) -> {
                if (uuid == null) return;
                items.forEach(item -> {
                    if (item == null) return;
                    JsonElement itemElement = gson.toJsonTree(new Pair<>(uuid.toString(), EncodeUtil.encode(item)));
                    jsonArray.add(itemElement);
                });
            });

            json.add("banItems", jsonArray);
            gson.toJson(json, writer);

        } catch (IOException exception) { exception.printStackTrace(); }
    }

    public void loadData() {
        try (Reader reader = Files.newBufferedReader(dataFile.toPath())) {
            JsonObject json = gson.fromJson(reader, JsonObject.class);

            if (json == null) return;

            if (json.has("banItems")) {

                HashMap<UUID, List<ItemStack>> itemMap = new HashMap<>();

                JsonArray gachaTypeArray = json.getAsJsonArray("banItems");
                gachaTypeArray.forEach(jsonElement -> {
                    Pair<String, String> pair = gson.fromJson(jsonElement, new TypeToken<Pair<String,String>>() {}.getType());
                    if (pair == null) return;

                    UUID uuid = UUID.fromString(pair.getFirst());
                    ItemStack itemStack = EncodeUtil.decode(pair.getSecond(), ItemStack.class);

                    if (itemStack == null) return;

                    List<ItemStack> itemList;
                    if (itemMap.containsKey(uuid)) itemList = itemMap.get(uuid);
                    else itemList = new ArrayList<>();

                    itemList.add(itemStack);
                    itemMap.put(uuid,itemList);
                });

                BanItemManager.getInstance().init(itemMap);
            }
        } catch (IOException exception) { exception.printStackTrace(); }
    }

}
