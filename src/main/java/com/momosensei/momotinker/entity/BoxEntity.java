package com.momosensei.momotinker.entity;


import com.momosensei.momotinker.register.MomotinkerModifiers;
import com.momosensei.momotinker.util.AttackUtil;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraftforge.registries.ForgeRegistries;
import slimeknights.tconstruct.common.TinkerTags;
import slimeknights.tconstruct.library.materials.RandomMaterial;
import slimeknights.tconstruct.library.materials.stats.MaterialStatsId;
import slimeknights.tconstruct.library.tools.definition.ToolDefinition;
import slimeknights.tconstruct.library.tools.definition.ToolDefinitionLoader;
import slimeknights.tconstruct.library.tools.definition.module.material.ToolMaterialHook;
import slimeknights.tconstruct.library.tools.nbt.MaterialNBT;
import slimeknights.tconstruct.library.tools.nbt.ToolStack;
import slimeknights.tconstruct.library.tools.stat.ToolStats;
import slimeknights.tconstruct.library.utils.Util;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;


public class BoxEntity extends Projectile {
    public ToolStack tool;
    public float damage = 0;
    public BoxEntity(EntityType<? extends Projectile> p_37248_, Level p_37249_) {
        super(p_37248_, p_37249_);
    }
    private static final EntityDataAccessor<ItemStack> DATA_TOOL = SynchedEntityData.defineId(BoxEntity.class, EntityDataSerializers.ITEM_STACK);
    private static final EntityDataAccessor<Float> DATA_SPAWN_YAW = SynchedEntityData.defineId(BoxEntity.class, EntityDataSerializers.FLOAT);
    private static final EntityDataAccessor<Float> DATA_SPAWN_PITCH = SynchedEntityData.defineId(BoxEntity.class, EntityDataSerializers.FLOAT);

    @Override
    protected void defineSynchedData() {
        this.entityData.define(DATA_TOOL, ItemStack.EMPTY);
        this.entityData.define(DATA_SPAWN_YAW, 0f);
        this.entityData.define(DATA_SPAWN_PITCH, 0f);
    }
    public void setSpawnRotation(float yaw, float pitch) {
        this.entityData.set(DATA_SPAWN_YAW, yaw);
        this.entityData.set(DATA_SPAWN_PITCH, pitch);
    }

    // 获取召唤时的Yaw
    public float getSpawnYaw() {
        return this.entityData.get(DATA_SPAWN_YAW);
    }

    // 获取召唤时的Pitch
    public float getSpawnPitch() {
        return this.entityData.get(DATA_SPAWN_PITCH);
    }

//    public final Random RANDOM = new Random();
//    public final RandomMaterial randomMaterial = RandomMaterial.random().allowHidden().build();
//    public Map<ResourceLocation, ToolDefinition> getTools() {
//        return ToolDefinitionLoader.getInstance()
//                .getRegisteredToolDefinitions()
//                .stream()
//                .collect(Collectors.toMap(ToolDefinition::getId, Function.identity()));
//    }
//    public ToolDefinition getRandomToolDefinition() {
//        Map<ResourceLocation, ToolDefinition> tools = getTools();
//        if (tools.isEmpty()) {
//            throw new IllegalStateException("No tool definitions available for mod " );
//        }
//        List<ToolDefinition> toolList = new ArrayList<>(tools.values());
//        return toolList.get(RANDOM.nextInt(toolList.size()));
//    }
//    public ToolStack buildTools(Item item,ToolDefinition definition, MaterialNBT materials) {
//        return ToolStack.createTool(item, definition, materials);
//    }
//
//    private ToolStack getRandomTools(MaterialNBT materials) {
//        ToolDefinition definition = getRandomToolDefinition();
//        Item item=ForgeRegistries.ITEMS.getValue(definition.getId());
//        return buildTools(item,definition,materials);
//    }
//    private ToolStack getToolRandomMaterials() {
//        ToolDefinition definition = getRandomToolDefinition();
//        Item item = ForgeRegistries.ITEMS.getValue(definition.getId());
//        List<MaterialStatsId> stats = ToolMaterialHook.stats(definition);
//        return buildTools(item, definition, RandomMaterial.build(stats, Collections.nCopies(stats.size(), randomMaterial), RandomSource.create()));
//    }
    private static final Random RANDOM = new Random();
    private static final RandomMaterial randomMaterial = RandomMaterial.random().allowHidden().build();
    private static final Map<ResourceLocation, ToolDefinition> TOOLS_CACHE = new HashMap<>();
    private static final Map<ResourceLocation, Item> DEFINITION_TO_ITEM_MAP = new HashMap<>();
    private static final List<ToolDefinition> TOOL_DEFINITION_LIST = new ArrayList<>();

    static {
        Map<ResourceLocation, ToolDefinition> tempTools = ToolDefinitionLoader.getInstance()
                .getRegisteredToolDefinitions()
                .stream()
                .collect(Collectors.toMap(ToolDefinition::getId, Function.identity()));

        TOOLS_CACHE.putAll(tempTools);
        TOOL_DEFINITION_LIST.addAll(tempTools.values());

        for (ToolDefinition definition : TOOL_DEFINITION_LIST) {
            Item item = ForgeRegistries.ITEMS.getValue(definition.getId());
            if (item != null && item != Items.AIR) {
                DEFINITION_TO_ITEM_MAP.put(definition.getId(), item);
            }
        }
    }

    public static Map<ResourceLocation, ToolDefinition> getTools() {
        return new HashMap<>(TOOLS_CACHE);
    }

    public static ToolDefinition getRandomToolDefinition() {
        if (TOOL_DEFINITION_LIST.isEmpty()) {
            throw new IllegalStateException("No tool definitions available");
        }
        return TOOL_DEFINITION_LIST.get(RANDOM.nextInt(TOOL_DEFINITION_LIST.size()));
    }

    public static ToolStack buildTools(Item item, ToolDefinition definition, MaterialNBT materials) {
        return ToolStack.createTool(item, definition, materials);
    }

    public static ToolStack getRandomTools(MaterialNBT materials) {
        ToolDefinition definition = getRandomToolDefinition();
        Item item = DEFINITION_TO_ITEM_MAP.get(definition.getId());
        if (item == null) {
            throw new IllegalStateException("No item found for tool definition: " + definition.getId());
        }
        return buildTools(item, definition, materials);
    }

    public static ToolStack getToolRandomMaterials() {
        ToolDefinition definition = getRandomToolDefinition();
        Item item = DEFINITION_TO_ITEM_MAP.get(definition.getId());
        if (item == null) {
            throw new IllegalStateException("No item found for tool definition: " + definition.getId());
        }
        List<MaterialStatsId> stats = ToolMaterialHook.stats(definition);
        return buildTools(item, definition, RandomMaterial.build(stats, Collections.nCopies(stats.size(), randomMaterial), RandomSource.create()));
    }

    public void setToolTag(ToolStack tool1,ToolStack tool2){
        tool2.setUpgrades(tool1.getUpgrades());
        tool2.setDamage(tool1.getDamage());
        tool2.getPersistentData().copyFrom(tool1.getPersistentData().getCopy());
    }

    public ToolStack getToolStack() {
        if (this.tool == null) {
            if (!this.level.isClientSide()) {
                if (!(this.getOwner() instanceof Player player)) {
                    this.tool = ToolStack.from(ItemStack.EMPTY);
                } else {
                    ToolStack originalStack = ToolStack.from(player.getMainHandItem());
                    ToolStack resultStack = null;

                    // 重试逻辑：最多尝试10次
                    int maxAttempts = 10;
                    for (int i = 0; i < maxAttempts; i++) {
                        if (originalStack.getModifierLevel(MomotinkerModifiers.laomochuji.get()) > 0) {
                            resultStack = getToolRandomMaterials();
                        } else {
                            MaterialNBT materials = originalStack.getMaterials();
                            resultStack = getRandomTools(materials);
                        }

                        if (isValidToolStack(resultStack)) {
                            break;
                        }

                        if (i == maxAttempts - 1) {
                            resultStack = originalStack;
                        }
                    }

                    setToolTag(originalStack, resultStack);
                    this.tool = resultStack;

                    this.entityData.set(DATA_TOOL, this.tool.createStack());
                }
            } else {
                ItemStack syncedStack = this.entityData.get(DATA_TOOL);
                if (!syncedStack.isEmpty()) {
                    this.tool = ToolStack.from(syncedStack);
                } else {
                    this.tool = ToolStack.from(ItemStack.EMPTY);
                }
            }
        }
        return this.tool;
    }
    private boolean isValidToolStack(ToolStack toolStack) {
        if (toolStack == null) return false;
        if (toolStack.getStats().get(ToolStats.ATTACK_DAMAGE)==0) return false;
        if (!toolStack.hasTag(TinkerTags.Items.INTERACTABLE_RIGHT)) return false;
        ItemStack itemStack = toolStack.createStack();
        return !itemStack.isEmpty() && itemStack.getItem() != Items.AIR;
    }
    public ItemStack getItem() {
        if (this.level.isClientSide()) {
            return this.entityData.get(DATA_TOOL);
        }
        ToolStack toolStack = getToolStack();
        return toolStack != null ? toolStack.createStack() : ItemStack.EMPTY;
    }

    @Override
    public void tick() {
        super.tick();
        if (this.tickCount >= 300) {
            this.discard();
            return;
        }
        Entity entity = this.getOwner();
        if (entity == null) {
            return;
        }
        for (int dx = -1; dx <= 1; dx++) {
            for (int dz = -1; dz <= 1; dz++) {
                if (!level.hasChunk(this.chunkPosition().x + dx, this.chunkPosition().z + dz)) {
                    this.discard();
                    return;
                }
            }
        }

        ToolStack tool =ToolStack.from(getItem());
        if (entity instanceof Player player && this.level instanceof ServerLevel serverLevel) {
            List<LivingEntity> ls0 = serverLevel.getEntitiesOfClass(LivingEntity.class, this.getBoundingBox().inflate(1.5));
            for (LivingEntity targets : ls0) {
                if (targets != this.getOwner() && targets != null) {
                    AttackUtil.attackEntity(tool, player, InteractionHand.MAIN_HAND, targets, () -> 1, true, Util.getSlotType(InteractionHand.MAIN_HAND), tool.getStats().get(ToolStats.ATTACK_DAMAGE) + 1, 1f, false, true, true, true);
                }
            }
//            if (this.tickCount == 1) {
//                sendDebugMessages(tool, player);
//            }
        }
    }
//    private void sendDebugMessages(ToolStack tool,Player player) {
//        player.sendSystemMessage(Component.literal("=== 工具调试信息 ==="));
//        player.sendSystemMessage(Component.literal("this.tool: " + tool.getItem().getDescriptionId()));
//        player.sendSystemMessage(Component.literal("this.tool修饰符: " + tool.getModifierList()));
//        player.sendSystemMessage(Component.literal("this.tool攻击伤害: " + tool.getStats().get(ToolStats.ATTACK_DAMAGE)));
//    }
    @Override
    public boolean ignoreExplosion() {
        return true;
    }

    @Override
    public boolean isPushable() {
        return false;
    }

    @Override
    public void push(Entity entity) {
    }

    @Override
    public void push(double x, double y, double z) {
    }
}
