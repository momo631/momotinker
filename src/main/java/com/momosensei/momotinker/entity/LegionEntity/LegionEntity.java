package com.momosensei.momotinker.entity.LegionEntity;


import com.momosensei.momotinker.register.MomotinkerModifiers;
import com.momosensei.momotinker.register.MomotinkerTags;
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
import net.minecraft.world.entity.MoverType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
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
import slimeknights.tconstruct.tools.TinkerTools;
import slimeknights.tconstruct.tools.ToolDefinitions;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

import static com.momosensei.momotinker.entity.LegionEntity.OrbitDefenseSystem.interceptProjectiles;
import static com.momosensei.momotinker.entity.MomotinkerEntitiesMove.*;


public class LegionEntity extends Projectile {
    public ToolStack tool;
    public float damage = 0;
    public LegionEntity(EntityType<? extends Projectile> p_37248_, Level p_37249_) {
        super(p_37248_, p_37249_);
    }

    private static final EntityDataAccessor<ItemStack> DATA_TOOL = SynchedEntityData.defineId(LegionEntity.class, EntityDataSerializers.ITEM_STACK);
    private static final EntityDataAccessor<Float> DATA_SPAWN_YAW = SynchedEntityData.defineId(LegionEntity.class, EntityDataSerializers.FLOAT);
    private static final EntityDataAccessor<Float> DATA_SPAWN_PITCH = SynchedEntityData.defineId(LegionEntity.class, EntityDataSerializers.FLOAT);
    private static final EntityDataAccessor<Integer> DATA_FORM = SynchedEntityData.defineId(LegionEntity.class, EntityDataSerializers.INT);

    @Override
    protected void defineSynchedData() {
        this.entityData.define(DATA_TOOL, ItemStack.EMPTY);
        this.entityData.define(DATA_SPAWN_YAW, 0f);
        this.entityData.define(DATA_SPAWN_PITCH, 0f);
        this.entityData.define(DATA_FORM, 0);
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

    public void setForm(int form) {
        this.entityData.set(DATA_FORM, form);
    }

    public int getForm() {
        return this.entityData.get(DATA_FORM);
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
        if (tool2.getModifierLevel(MomotinkerModifiers.key_to_ruin.getId())!=1) {
            tool2.addModifier(MomotinkerModifiers.key_to_ruin.getId(), 1);
        }
    }

    public ToolStack getToolStack() {
        if (this.tool == null) {
            if (!this.level().isClientSide()) {
                if (!(this.getOwner() instanceof Player player)) {
                    this.tool = ToolStack.from(ItemStack.EMPTY);
                } else {
                    ToolStack originalStack = ToolStack.from(player.getMainHandItem());
                    ToolStack resultStack = null;
                    MaterialNBT materials = originalStack.getMaterials();

                    int maxAttempts = 20;
                    for (int i = 0; i < maxAttempts; i++) {
                        if (originalStack.getModifierLevel(MomotinkerModifiers.laomochuji.get()) > 0) {
                            resultStack = getToolRandomMaterials();
                        } else {
                            resultStack = getRandomTools(materials);
                        }

                        if (isValidToolStack(resultStack)) {
                            break;
                        }

                        if (i == maxAttempts - 1) {
                            resultStack = ToolStack.from(ToolStack.createTool(TinkerTools.sword.get(), ToolDefinitions.SWORD, materials).createStack());
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
        if (toolStack == null
                ||toolStack.getStats().get(ToolStats.ATTACK_DAMAGE)==0
                ||(!toolStack.hasTag(TinkerTags.Items.INTERACTABLE_RIGHT))
                ||toolStack.hasTag(MomotinkerTags.Items.LEGION)) return false;
        ItemStack itemStack = toolStack.createStack();
        return !itemStack.isEmpty() && itemStack.getItem() != Items.AIR;
    }
    public ItemStack getItem() {
        if (this.level().isClientSide()) {
            return this.entityData.get(DATA_TOOL);
        }
        ToolStack toolStack = getToolStack();
        return toolStack != null ? toolStack.createStack() : ItemStack.EMPTY;
    }

    @Override
    public void tick() {
        super.tick();
        int discardTime = switch (getForm()) {
            case 1 -> 160;
            case 2 -> 240;
            default -> 120;
        };
        if (this.tickCount >= discardTime) {
            this.discard();
            return;
        }
        if (!isInLoadedChunk(this)) {
            this.discard();
            return;
        }
        Entity entity = this.getOwner();
        if (entity == null) {
            return;
        }
        if (!this.level().isClientSide) {
            updateEntityIgnoredTargets(this);
            if (getForm()!=2) {
                LivingEntity target = findNearestTargetWithTransfer(this, entity, 20);
                double speed = 1.2;
                float spawnYaw = this.getSpawnYaw();
                float spawnPitch = this.getSpawnPitch();
                Vec3 movementVector = calculateMovementVector(spawnYaw, spawnPitch);
                if (target == null || getForm() == 0) {
                    speed=1.6;
                    this.setDeltaMovement(movementVector.scale(speed));
                } else if (getForm() == 1) {
                    if (this.tickCount > 5) {
                        moveTowardsTargetWithTransfer(this, target, 1.8, 1.5);
                    } else {
                        this.setDeltaMovement(movementVector.scale(speed));
                    }
                }
            }else{
                if (entity.isAlive()) {
                    //circularMotionNew(BoxEntity.class,this,entity,entity,2,2,3,0.05);
                    circularMotion(this,entity,2.5,0.15);
                    interceptProjectiles(entity,this);
                }
            }
            updateRotation();
            super.move(MoverType.SELF, this.getDeltaMovement());
        }

        ToolStack tool =ToolStack.from(getItem());
        float multiplier = 0.8f;
        float a = 1.5f;
        if (getForm()==2||getForm()==1){
            multiplier=0.2f;
            if (getForm()==2) {
                a = 0.75f;
            }
        }
        if (entity instanceof Player player && this.level() instanceof ServerLevel serverLevel) {
            List<LivingEntity> ls0 = serverLevel.getEntitiesOfClass(LivingEntity.class, this.getBoundingBox().inflate(a));
            for (LivingEntity targets : ls0) {
                if (targets != this.getOwner() && targets != null) {
                    AttackUtil.attackEntity(tool, player, InteractionHand.MAIN_HAND, targets, () -> 1, true, Util.getSlotType(InteractionHand.MAIN_HAND), tool.getStats().get(ToolStats.ATTACK_DAMAGE)+1, multiplier, false, true, true, true);
                }
            }
            if (player.isDeadOrDying())this.discard();
        }
    }

    @Override
    public void remove(Entity.RemovalReason reason) {
        super.remove(reason);
        cleanupEntityData(this);
    }

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

    @Override
    public boolean isNoGravity() {
        return true;
    }

    @Override
    protected void checkInsideBlocks() {
    }

    @Override
    public boolean canCollideWith(Entity entity) {
        return false;
    }

    @Override
    public boolean canBeCollidedWith() {
        return false;
    }
}
