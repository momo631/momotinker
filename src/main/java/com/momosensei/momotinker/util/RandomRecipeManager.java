package com.momosensei.momotinker.util;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.momosensei.momotinker.network.Channel;
import com.momosensei.momotinker.network.packet.SyncDataPacket;
import com.momosensei.momotinker.register.MomotinkerItem;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtIo;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.storage.LevelResource;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.server.ServerStartingEvent;
import net.minecraftforge.event.server.ServerStoppingEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.registries.ForgeRegistries;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jetbrains.annotations.NotNull;
import slimeknights.mantle.recipe.ingredient.SizedIngredient;

import java.io.File;
import java.io.InputStreamReader;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

import static com.momosensei.momotinker.Items.tool.revelation_fruit.revelation;
import static com.momosensei.momotinker.Items.tool.revelation_fruit.revelation_limit;
import static com.momosensei.momotinker.Momotinker.getResource;

public class RandomRecipeManager {
    private static final Map<ResourceLocation, List<Map<Integer, List<ItemConfig>>>> RECIPE_OPTIONS = new ConcurrentHashMap<>();
    private static final Map<ResourceLocation, List<SizedIngredient>> CURRENT_INPUTS = new ConcurrentHashMap<>();
    private static final Map<ResourceLocation, Map<Integer, ItemStack>> FIXED_DISPLAYS = new ConcurrentHashMap<>();
    private static final Map<ResourceLocation, ResourceLocation> RECIPE_MODIFIER_MAP = new ConcurrentHashMap<>();
    private static final Random RANDOM = new Random();
    private static final String DATA_FILE = "momotinker_random_recipes.dat";
    private static boolean dataLoaded = false;
    private static final Object LOCK = new Object();
    private static final Logger LOGGER = LogManager.getLogger("RandomRecipeManager");
    static {
        MinecraftForge.EVENT_BUS.register(new ServerEvents());
    }

    public static class ServerEvents {
        @SubscribeEvent
        public void onServerStarting(ServerStartingEvent event) {
            synchronized (LOCK) {
                loadData();
                dataLoaded = true;
            }
        }

        @SubscribeEvent
        public void onServerStopping(ServerStoppingEvent event) {
            saveData();
        }
    }

    public static ResourceLocation getModifierId(ResourceLocation recipeId) {
        synchronized (LOCK) {
            if (!dataLoaded) {
                MinecraftServer server = net.minecraftforge.server.ServerLifecycleHooks.getCurrentServer();
                if (server != null && server.isRunning()) {
                    loadData();
                    dataLoaded = true;
                } else {
                    return null;
                }
            }
            if (RECIPE_MODIFIER_MAP.containsKey(recipeId)) {
                return RECIPE_MODIFIER_MAP.get(recipeId);
            }
            loadOptions(recipeId);
            return RECIPE_MODIFIER_MAP.get(recipeId);
        }
    }

    public static List<SizedIngredient> getCurrentInputs(ResourceLocation recipeId) {
        synchronized (LOCK) {
            return CURRENT_INPUTS.get(recipeId);
        }
    }

    public static Map<Integer, ItemStack> getFixedDisplay(ResourceLocation recipeId) {
        synchronized (LOCK) {
            return FIXED_DISPLAYS.get(recipeId);
        }
    }

    public static void ensureRecipeInitialized(ResourceLocation recipeId) {
        synchronized (LOCK) {
            if (!dataLoaded) {
                MinecraftServer server = net.minecraftforge.server.ServerLifecycleHooks.getCurrentServer();
                if (server != null && server.isRunning()) {
                    loadData();
                    dataLoaded = true;
                } else {
                    return;
                }
            }
            if (!FIXED_DISPLAYS.containsKey(recipeId)) {
                generateNewDisplay(recipeId);
            }
        }
    }

    public static void generateNewDisplay(ResourceLocation recipeId) {
        List<Map<Integer, List<ItemConfig>>> options = loadOptions(recipeId);
        if (options == null || options.isEmpty()) return;
        synchronized (LOCK) {
            int index = RANDOM.nextInt(options.size());
            Map<Integer, List<ItemConfig>> selectedOption = options.get(index);
            Map<Integer, ItemStack> newDisplays = new HashMap<>();
            List<SizedIngredient> newInputs = new ArrayList<>();
            int maxSlot = selectedOption.keySet().stream().max(Integer::compare).orElse(0);
            for (int slot = 0; slot <= maxSlot; slot++) {
                if (selectedOption.containsKey(slot)) {
                    List<ItemConfig> configs = selectedOption.get(slot);
                    if (!configs.isEmpty()) {
                        ItemConfig selectedConfig = configs.get(RANDOM.nextInt(configs.size()));
                        int count = selectedConfig.getRandomCount();
                        newDisplays.put(slot, new ItemStack(selectedConfig.item(), count));
                        newInputs.add(SizedIngredient.of(Ingredient.of(selectedConfig.item()), count));
                    }
                }
            }
            FIXED_DISPLAYS.put(recipeId, newDisplays);
            CURRENT_INPUTS.put(recipeId, newInputs);
        }
        saveData();
    }

    public static void updateCurrentInputs(ResourceLocation recipeId) {
        synchronized (LOCK) {
            Map<Integer, ItemStack> displays = FIXED_DISPLAYS.get(recipeId);
            if (displays != null) {
                List<SizedIngredient> inputs = new ArrayList<>();
                int maxSlot = displays.keySet().stream().max(Integer::compare).orElse(0);
                for (int slot = 0; slot <= maxSlot; slot++) {
                    if (displays.containsKey(slot)) {
                        ItemStack stack = displays.get(slot);
                        inputs.add(SizedIngredient.of(Ingredient.of(stack.getItem()), stack.getCount()));
                    }
                }
                CURRENT_INPUTS.put(recipeId, inputs);
            }
        }
    }

    private static List<Map<Integer, List<ItemConfig>>> loadOptions(ResourceLocation recipeId) {
        if (RECIPE_OPTIONS.containsKey(recipeId)) {
            return RECIPE_OPTIONS.get(recipeId);
        }
        List<Map<Integer, List<ItemConfig>>> options = new ArrayList<>();
        MinecraftServer server = net.minecraftforge.server.ServerLifecycleHooks.getCurrentServer();
        if (server == null) {
            return options;
        }
        String recipePath = recipeId.getPath();
        String fileName = recipePath.substring(recipePath.lastIndexOf('/') + 1);

        ResourceLocation simplePath = new ResourceLocation(recipeId.getNamespace(),
                "modifier_random/" + fileName + ".json");
        ResourceLocation fullPath = new ResourceLocation(recipeId.getNamespace(),
                "modifier_random/" + recipePath + ".json");

        boolean simpleExists = server.getResourceManager().getResource(simplePath).isPresent();
        boolean fullExists = server.getResourceManager().getResource(fullPath).isPresent();

        ResourceLocation optionsId;
        if (simpleExists) {
            optionsId = simplePath;
        } else if (fullExists) {
            optionsId = fullPath;
        } else {
            if (LOGGER.isDebugEnabled()) {
                LOGGER.debug("No config found for recipe: {}", recipeId);
            }
            return options;
        }
        try {
            var resource = server.getResourceManager().getResource(optionsId);
            if (resource.isEmpty()) {
                return options;
            }
            try (var reader = new InputStreamReader(resource.get().open())) {
                JsonObject json = JsonParser.parseReader(reader).getAsJsonObject();
                if (json.has("modifier_id")) {
                    String modifierIdStr = json.get("modifier_id").getAsString();
                    ResourceLocation modifierId = new ResourceLocation(modifierIdStr);
                    synchronized (LOCK) {
                        RECIPE_MODIFIER_MAP.put(recipeId, modifierId);
                    }
                } else {
                    return options;
                }
                if (json.has("options")) {
                    JsonArray optionsArray = json.getAsJsonArray("options");
                    for (JsonElement optionElement : optionsArray) {
                        JsonObject optionObj = optionElement.getAsJsonObject();
                        if (optionObj.has("slots")) {
                            JsonArray slotsArray = optionObj.getAsJsonArray("slots");
                            Map<Integer, List<ItemConfig>> slotMap = new HashMap<>();
                            for (JsonElement slotElement : slotsArray) {
                                JsonObject slotObj = slotElement.getAsJsonObject();
                                int slotIndex = slotObj.get("slot_index").getAsInt();
                                JsonArray itemsArray = slotObj.getAsJsonArray("items");

                                List<ItemConfig> itemConfigs = new ArrayList<>();
                                for (JsonElement itemElement : itemsArray) {
                                    ItemConfig config = parseItemConfig(itemElement);
                                    if (config != null && config.item != null) {
                                        itemConfigs.add(config);
                                    }
                                }
                                if (!itemConfigs.isEmpty()) {
                                    slotMap.put(slotIndex, itemConfigs);
                                }
                            }
                            if (!slotMap.isEmpty()) {
                                options.add(slotMap);
                            }
                        }
                    }
                } else {
                    return options;
                }
            }
        } catch (Exception e) {
            return options;
        }
        if (!options.isEmpty()) {
            synchronized (LOCK) {
                RECIPE_OPTIONS.put(recipeId, options);
            }
        }
        return options;
    }

    private static void loadData() {
        MinecraftServer server = net.minecraftforge.server.ServerLifecycleHooks.getCurrentServer();
        if (server == null) return;

        File dataFile = new File(server.getWorldPath(LevelResource.ROOT).toFile(), DATA_FILE);
        if (!dataFile.exists()) return;

        try {
            CompoundTag rootTag = NbtIo.readCompressed(dataFile);

            synchronized (LOCK) {
                FIXED_DISPLAYS.clear();
                RECIPE_MODIFIER_MAP.clear();
                CURRENT_INPUTS.clear();

                if (rootTag.contains("displays")) {
                    CompoundTag displaysTag = rootTag.getCompound("displays");
                    for (String recipeKey : displaysTag.getAllKeys()) {
                        ResourceLocation recipeId = new ResourceLocation(recipeKey);
                        CompoundTag recipeDisplaysTag = displaysTag.getCompound(recipeKey);
                        Map<Integer, ItemStack> slotMap = new HashMap<>();

                        for (String slotKey : recipeDisplaysTag.getAllKeys()) {
                            int slot = Integer.parseInt(slotKey);
                            CompoundTag stackTag = recipeDisplaysTag.getCompound(slotKey);
                            ItemStack stack = ItemStack.of(stackTag);
                            if (!stack.isEmpty()) {
                                slotMap.put(slot, stack);
                            }
                        }

                        if (!slotMap.isEmpty()) {
                            FIXED_DISPLAYS.put(recipeId, slotMap);

                            List<SizedIngredient> inputs = new ArrayList<>();
                            int maxSlot = slotMap.keySet().stream().max(Integer::compare).orElse(0);
                            for (int slot = 0; slot <= maxSlot; slot++) {
                                if (slotMap.containsKey(slot)) {
                                    ItemStack stack = slotMap.get(slot);
                                    inputs.add(SizedIngredient.of(Ingredient.of(stack.getItem()), stack.getCount()));
                                }
                            }
                            CURRENT_INPUTS.put(recipeId, inputs);
                        }
                    }
                }

                if (rootTag.contains("modifiers")) {
                    CompoundTag modifiersTag = rootTag.getCompound("modifiers");
                    for (String recipeKey : modifiersTag.getAllKeys()) {
                        ResourceLocation recipeId = new ResourceLocation(recipeKey);
                        ResourceLocation modifierId = new ResourceLocation(modifiersTag.getString(recipeKey));
                        RECIPE_MODIFIER_MAP.put(recipeId, modifierId);
                    }
                }
            }

            LOGGER.info("Successfully loaded random recipe data");

        } catch (Exception e) {
            LOGGER.error("Error loading data, starting fresh", e);
            synchronized (LOCK) {
                FIXED_DISPLAYS.clear();
                RECIPE_MODIFIER_MAP.clear();
                CURRENT_INPUTS.clear();
            }
        }
    }

    private static void saveData() {
        MinecraftServer server = net.minecraftforge.server.ServerLifecycleHooks.getCurrentServer();
        if (server == null) return;

        File dataFile = new File(server.getWorldPath(LevelResource.ROOT).toFile(), DATA_FILE);

        try {
            CompoundTag rootTag = new CompoundTag();
            CompoundTag displaysTag = new CompoundTag();
            CompoundTag modifiersTag = new CompoundTag();

            synchronized (LOCK) {
                for (Map.Entry<ResourceLocation, Map<Integer, ItemStack>> entry : FIXED_DISPLAYS.entrySet()) {
                    String recipeKey = entry.getKey().toString();
                    CompoundTag recipeDisplaysTag = getCompoundTag(entry);
                    displaysTag.put(recipeKey, recipeDisplaysTag);
                }
                for (Map.Entry<ResourceLocation, ResourceLocation> entry : RECIPE_MODIFIER_MAP.entrySet()) {
                    modifiersTag.putString(entry.getKey().toString(), entry.getValue().toString());
                }
            }

            rootTag.put("displays", displaysTag);
            rootTag.put("modifiers", modifiersTag);

            NbtIo.writeCompressed(rootTag, dataFile);

        } catch (Exception e) {
            LOGGER.error("Error saving data", e);
        }
    }

    private static @NotNull CompoundTag getCompoundTag(Map.Entry<ResourceLocation, Map<Integer, ItemStack>> entry) {
        CompoundTag recipeDisplaysTag = new CompoundTag();
        for (Map.Entry<Integer, ItemStack> slotEntry : entry.getValue().entrySet()) {
            ItemStack stack = slotEntry.getValue();
            if (!stack.isEmpty()) {
                CompoundTag stackTag = new CompoundTag();
                stack.save(stackTag);
                recipeDisplaysTag.put(String.valueOf(slotEntry.getKey()), stackTag);
            }
        }
        return recipeDisplaysTag;
    }

    public record ItemConfig(Item item, int minCount, int maxCount) {
        public int getRandomCount() {
                if (minCount == maxCount) return minCount;
                return RANDOM.nextInt(maxCount - minCount + 1) + minCount;
            }
        }

    private static ItemConfig parseItemConfig(JsonElement element) {
        try {
            if (element.isJsonObject()) {
                JsonObject obj = element.getAsJsonObject();
                if (obj.has("id")) {
                    String itemId = obj.get("id").getAsString();
                    Item item = ForgeRegistries.ITEMS.getValue(new ResourceLocation(itemId));

                    if (item != null) {
                        int minCount = obj.has("min") ? obj.get("min").getAsInt() : 1;
                        int maxCount = obj.has("max") ? obj.get("max").getAsInt() : Math.min(64, item.getMaxStackSize());

                        if (minCount <= 0) minCount = 1;
                        if (maxCount < minCount) maxCount = minCount;
                        if (maxCount > item.getMaxStackSize()) maxCount = item.getMaxStackSize();

                        return new ItemConfig(item, minCount, maxCount);
                    }
                }
            }
        } catch (Exception e) {
            LOGGER.error("Error parsing item config: {}", element, e);
        }
        return null;
    }
    public static void refreshAllClientDisplays(ServerPlayer player) {
        synchronized (LOCK) {
            for (ResourceLocation recipeId : FIXED_DISPLAYS.keySet()) {
                Map<Integer, ItemStack> displays = FIXED_DISPLAYS.get(recipeId);
                if (displays != null) {
                    int stage = getPlayerStage(player,recipeId);
                    SyncDataPacket packet = new SyncDataPacket(recipeId, stage, displays);
                    Channel.sendToPlayer(packet, player);
                }
            }
        }
    }

    public static void refreshClientDisplay(ServerPlayer player,ResourceLocation recipeId) {
        synchronized (LOCK) {
            Map<Integer, ItemStack> displays = FIXED_DISPLAYS.get(recipeId);
            if (displays != null) {
                int stage = getPlayerStage(player,recipeId);
                SyncDataPacket packet = new SyncDataPacket(recipeId, stage, displays);
                Channel.sendToPlayer(packet, player);
            }
        }
    }
    public static String getShortId(ResourceLocation recipeId) {
        String path = recipeId.getPath();
        return path.substring(path.lastIndexOf('/') + 1);
    }

    public static int getPlayerStage(ServerPlayer player, ResourceLocation recipeId) {
        CompoundTag tag = player.getPersistentData().getCompound(Player.PERSISTED_NBT_TAG);
        ResourceLocation resourceKey = getResource(getShortId(recipeId));
        return tag.getInt(resourceKey.toString());
    }

    public static void resetAllPlayersStage(ResourceLocation recipeId) {
        MinecraftServer server = net.minecraftforge.server.ServerLifecycleHooks.getCurrentServer();
        if (server == null) return;

        String key = getResource(getShortId(recipeId)).toString();
        for (ServerPlayer player : server.getPlayerList().getPlayers()) {
            CompoundTag tag = player.getPersistentData().getCompound(Player.PERSISTED_NBT_TAG);
            tag.remove(key);
            player.getPersistentData().put(Player.PERSISTED_NBT_TAG, tag);
        }
    }

    public static ItemStack getRevelationFruit(String string,int i){
        ItemStack stack = new ItemStack(MomotinkerItem.revelation_fruit.get());
        stack.getOrCreateTag().putString(revelation,string);
        stack.getOrCreateTag().putInt(revelation_limit,i);
        return stack;
    }
}
