package com.momosensei.momotinker.test.testd;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.storage.LevelResource;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.server.ServerStoppingEvent;
import net.minecraftforge.fml.loading.FMLEnvironment;
import net.minecraftforge.registries.ForgeRegistries;
import slimeknights.mantle.recipe.ingredient.SizedIngredient;

import java.io.*;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class RandomRecipeManager {
    private static final Map<ResourceLocation, List<Map<Integer, List<Item>>>> RECIPE_OPTIONS = new ConcurrentHashMap<>();
    private static final Map<ResourceLocation, List<SizedIngredient>> CURRENT_INPUTS = new ConcurrentHashMap<>();
    private static final Map<ResourceLocation, Map<Integer, ItemStack>> FIXED_DISPLAYS = new ConcurrentHashMap<>();
    private static final Random RANDOM = new Random();
    private static final String DATA_FILE = "momotinker_random_recipes.dat";
    private static boolean dataLoaded = false;
    private static final Object LOCK = new Object();

    static {
        if (!FMLEnvironment.dist.isClient()) {
            MinecraftForge.EVENT_BUS.addListener((ServerStoppingEvent event) -> saveData());
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
                loadData();
                dataLoaded = true;
            }

            if (!FIXED_DISPLAYS.containsKey(recipeId)) {
                generateNewDisplay(recipeId);
            }
        }
    }

    public static void generateNewDisplay(ResourceLocation recipeId) {
        List<Map<Integer, List<Item>>> options = loadOptions(recipeId);
        if (options == null || options.isEmpty()) return;

        synchronized (LOCK) {
            int index = RANDOM.nextInt(options.size());
            Map<Integer, List<Item>> selectedOption = options.get(index);

            Map<Integer, ItemStack> newDisplays = new HashMap<>();
            List<SizedIngredient> newInputs = new ArrayList<>();

            int maxSlot = selectedOption.keySet().stream().max(Integer::compare).orElse(0);

            for (int slot = 0; slot <= maxSlot; slot++) {
                if (selectedOption.containsKey(slot)) {
                    List<Item> items = selectedOption.get(slot);
                    if (!items.isEmpty()) {
                        Item selectedItem = items.get(RANDOM.nextInt(items.size()));
                        int maxStackSize = selectedItem.getMaxStackSize();
                        int count = RANDOM.nextInt(Math.min(64, maxStackSize)) + 1;

                        newDisplays.put(slot, new ItemStack(selectedItem, count));
                        newInputs.add(SizedIngredient.of(Ingredient.of(selectedItem), count));
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

    private static List<Map<Integer, List<Item>>> loadOptions(ResourceLocation recipeId) {
        if (RECIPE_OPTIONS.containsKey(recipeId)) {
            return RECIPE_OPTIONS.get(recipeId);
        }

        List<Map<Integer, List<Item>>> options = new ArrayList<>();
        MinecraftServer server = net.minecraftforge.server.ServerLifecycleHooks.getCurrentServer();

        if (server != null) {
            ResourceLocation optionsId = new ResourceLocation(recipeId.getNamespace(),
                    "modifier_random/" + recipeId.getPath() + ".json");

            try {
                var resource = server.getResourceManager().getResource(optionsId);
                if (resource.isPresent()) {
                    try (var reader = new InputStreamReader(resource.get().open())) {
                        JsonObject json = JsonParser.parseReader(reader).getAsJsonObject();
                        if (json.has("options")) {
                            JsonArray optionsArray = json.getAsJsonArray("options");
                            for (JsonElement optionElement : optionsArray) {
                                JsonObject optionObj = optionElement.getAsJsonObject();
                                if (optionObj.has("slots")) {
                                    JsonArray slotsArray = optionObj.getAsJsonArray("slots");
                                    Map<Integer, List<Item>> slotMap = new HashMap<>();

                                    for (JsonElement slotElement : slotsArray) {
                                        JsonObject slotObj = slotElement.getAsJsonObject();
                                        int slotIndex = slotObj.get("slot_index").getAsInt();
                                        JsonArray itemsArray = slotObj.getAsJsonArray("items");

                                        List<Item> items = new ArrayList<>();
                                        for (JsonElement itemElement : itemsArray) {
                                            String itemId = itemElement.getAsString();
                                            Item item = ForgeRegistries.ITEMS.getValue(new ResourceLocation(itemId));
                                            if (item != null) items.add(item);
                                        }

                                        if (!items.isEmpty()) {
                                            slotMap.put(slotIndex, items);
                                        }
                                    }

                                    if (!slotMap.isEmpty()) {
                                        options.add(slotMap);
                                    }
                                }
                            }
                        }
                    }
                }
            } catch (Exception e) {
            }
        }

        if (!options.isEmpty()) {
            RECIPE_OPTIONS.put(recipeId, options);
        }

        return options;
    }

    private static void loadData() {
        MinecraftServer server = net.minecraftforge.server.ServerLifecycleHooks.getCurrentServer();
        if (server == null) return;

        File dataFile = new File(server.getWorldPath(LevelResource.ROOT).toFile(), DATA_FILE);
        if (!dataFile.exists()) return;

        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(dataFile))) {
            @SuppressWarnings("unchecked")
            Map<ResourceLocation, Map<Integer, ItemStack>> loaded = (Map<ResourceLocation, Map<Integer, ItemStack>>) ois.readObject();

            synchronized (LOCK) {
                FIXED_DISPLAYS.clear();
                FIXED_DISPLAYS.putAll(loaded);

                CURRENT_INPUTS.clear();
                for (var entry : loaded.entrySet()) {
                    List<SizedIngredient> inputs = new ArrayList<>();
                    int maxSlot = entry.getValue().keySet().stream().max(Integer::compare).orElse(0);
                    for (int slot = 0; slot <= maxSlot; slot++) {
                        if (entry.getValue().containsKey(slot)) {
                            ItemStack stack = entry.getValue().get(slot);
                            inputs.add(SizedIngredient.of(Ingredient.of(stack.getItem()), stack.getCount()));
                        }
                    }
                    CURRENT_INPUTS.put(entry.getKey(), inputs);
                }
            }
        } catch (Exception e) {
        }
    }

    private static void saveData() {
        MinecraftServer server = net.minecraftforge.server.ServerLifecycleHooks.getCurrentServer();
        if (server == null) return;

        File dataFile = new File(server.getWorldPath(LevelResource.ROOT).toFile(), DATA_FILE);
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(dataFile))) {
            synchronized (LOCK) {
                oos.writeObject(new HashMap<>(FIXED_DISPLAYS));
            }
        } catch (Exception e) {
        }
    }

    public static void clearCache() {
        synchronized (LOCK) {
            RECIPE_OPTIONS.clear();
            CURRENT_INPUTS.clear();
            FIXED_DISPLAYS.clear();
            dataLoaded = false;
        }
    }
}
