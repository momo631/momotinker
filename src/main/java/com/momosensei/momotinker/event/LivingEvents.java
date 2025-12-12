package com.momosensei.momotinker.event;


import com.momosensei.momotinker.Momotinker;
import com.momosensei.momotinker.entity.MeteorEntity;
import com.momosensei.momotinker.mobs.StageMeteor;
import com.momosensei.momotinker.network.Channel;
import com.momosensei.momotinker.network.packet.StageMeteorCharge;
import com.momosensei.momotinker.register.MomotinkerBlock;
import com.momosensei.momotinker.register.MomotinkerConfig;
import com.momosensei.momotinker.register.MomotinkerItem;
import com.momosensei.momotinker.util.TeleportEntityManager;
import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.tags.FluidTags;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.animal.IronGolem;
import net.minecraft.world.entity.animal.Sheep;
import net.minecraft.world.entity.animal.Wolf;
import net.minecraft.world.entity.animal.frog.Frog;
import net.minecraft.world.entity.boss.enderdragon.EnderDragon;
import net.minecraft.world.entity.boss.wither.WitherBoss;
import net.minecraft.world.entity.item.FallingBlockEntity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.monster.Slime;
import net.minecraft.world.entity.monster.Witch;
import net.minecraft.world.entity.monster.warden.Warden;
import net.minecraft.world.entity.npc.VillagerTrades;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.item.trading.MerchantOffer;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.SaplingBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.EntityJoinLevelEvent;
import net.minecraftforge.event.entity.living.BabyEntitySpawnEvent;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.event.entity.living.MobSpawnEvent;
import net.minecraftforge.event.entity.player.BonemealEvent;
import net.minecraftforge.event.entity.player.EntityItemPickupEvent;
import net.minecraftforge.event.entity.player.PlayerDestroyItemEvent;
import net.minecraftforge.event.entity.player.PlayerWakeUpEvent;
import net.minecraftforge.event.level.BlockEvent;
import net.minecraftforge.event.village.VillagerTradesEvent;
import net.minecraftforge.eventbus.api.Event;
import slimeknights.tconstruct.library.tools.helper.ModifierUtil;
import slimeknights.tconstruct.library.tools.nbt.ModDataNBT;

import java.util.List;
import java.util.Random;
import java.util.UUID;

import static slimeknights.tconstruct.TConstruct.RANDOM;


public class LivingEvents {
    public LivingEvents() {
        MinecraftForge.EVENT_BUS.addListener(this::OnLivingHurt);
        MinecraftForge.EVENT_BUS.addListener(this::OnEntityDeath);
        MinecraftForge.EVENT_BUS.addListener(this::OnBabyEntitySpawn);
        MinecraftForge.EVENT_BUS.addListener(this::OnBonemeal);
        MinecraftForge.EVENT_BUS.addListener(this::OnPlayerWakeUp);
        MinecraftForge.EVENT_BUS.addListener(this::OnAddCustomTrades);
        MinecraftForge.EVENT_BUS.addListener(this::OnBreakEvent);
        MinecraftForge.EVENT_BUS.addListener(this::OnEntityJoinLevel);
        MinecraftForge.EVENT_BUS.addListener(this::OnPlayerTick);
        MinecraftForge.EVENT_BUS.addListener(this::OnPlayerPickUp);
        MinecraftForge.EVENT_BUS.addListener(this::OnLivingTick);
        MinecraftForge.EVENT_BUS.addListener(this::OnCheckSpawn);
        MinecraftForge.EVENT_BUS.addListener(this::OnPlayerDestroyItem);
    }

    private static final ResourceLocation lusttest = Momotinker.getResource("lusttest");
    private static final ResourceLocation ragetest = Momotinker.getResource("ragetest");

    private void OnLivingHurt(LivingHurtEvent event) {
        boolean configall = MomotinkerConfig.special_acquisition.get();
        boolean configstage = MomotinkerConfig.stage_meteor.get();
        if (!configall)return;
        Entity a = event.getEntity();
        Entity b = event.getSource().getEntity();
        boolean configa = MomotinkerConfig.arriving_at_the_other_shore.get();
        if (configa&&(StageMeteor.getStageFloat()==1||!configstage)){
            if (a instanceof ServerPlayer player && player.getHealth() <= 1) {
                if (event.getAmount() > player.getMaxHealth() && player.isAlive()) {
                    ItemStack a1 = new ItemStack(MomotinkerItem.arriving_at_the_other_shore.get());
                    ModifierUtil.dropItem(player, a1);
                }
            }
        }
        boolean configb = MomotinkerConfig.jealous_notes.get();
        if (configb&&(StageMeteor.getStageFloat()==1||!configstage)) {
            if (a instanceof ServerPlayer player && b instanceof LivingEntity entity && player.getItemBySlot(EquipmentSlot.MAINHAND).is(Items.WRITABLE_BOOK)) {
                if (!player.hasItemInSlot(EquipmentSlot.HEAD) && !player.hasItemInSlot(EquipmentSlot.CHEST) && !player.hasItemInSlot(EquipmentSlot.LEGS) && !player.hasItemInSlot(EquipmentSlot.FEET)) {
                    if (entity.hasItemInSlot(EquipmentSlot.HEAD) || entity.hasItemInSlot(EquipmentSlot.CHEST) || entity.hasItemInSlot(EquipmentSlot.LEGS) || entity.hasItemInSlot(EquipmentSlot.FEET)) {
                        player.setItemSlot(EquipmentSlot.MAINHAND, ItemStack.EMPTY);
                        ItemStack a1 = new ItemStack(MomotinkerItem.jealous_notes.get());
                        player.getInventory().add(a1);
                    }
                }
            }
        }
    }

    private void OnEntityDeath(LivingDeathEvent event) {
        boolean configall = MomotinkerConfig.special_acquisition.get();
        boolean configstage = MomotinkerConfig.stage_meteor.get();
        if (!configall)return;
        if (event.getEntity() instanceof Warden warden) {
            boolean configa = MomotinkerConfig.heartsteel.get();
            if (configa&&(StageMeteor.getStageFloat()==1||!configstage)) {
                if (event.getSource().getEntity() instanceof IronGolem) {
                    ItemStack a = new ItemStack(MomotinkerItem.heartsteel.get());
                    ModifierUtil.dropItem(event.getSource().getEntity(), a);
                }
            }
            boolean configb = MomotinkerConfig.arrogance_proof.get();
            if (configb&&(StageMeteor.getStageFloat()==1||!configstage)) {
                if (event.getSource().getEntity() instanceof ServerPlayer player) {
                    if (!player.hasItemInSlot(EquipmentSlot.HEAD) && !player.hasItemInSlot(EquipmentSlot.CHEST) && !player.hasItemInSlot(EquipmentSlot.LEGS) && !player.hasItemInSlot(EquipmentSlot.FEET)) {
                        ItemStack a = new ItemStack(MomotinkerItem.arrogance_proof.get());
                        ModifierUtil.dropItem(player, a);
                    }
                }
            }
        }
        boolean configc = MomotinkerConfig.interdimensional_crystal.get();
        if (configc) {
            if (event.getEntity() instanceof WitherBoss wither) {
                if (wither.getOnPos().getY() >= 300) {
                    ItemStack a = new ItemStack(MomotinkerItem.interdimensional_crystal.get());
                    ModifierUtil.dropItem(event.getEntity(), a);
                }
            }
        }
        boolean configd = MomotinkerConfig.gluttony_core.get();
        if (configd&&(StageMeteor.getStageFloat()==1||!configstage)) {
            if (event.getEntity() instanceof Slime slime) {
                if (event.getSource().getEntity() instanceof Frog) {
                    int b = RANDOM.nextInt(10);
                    if (b == 1) {
                        ItemStack a = new ItemStack(MomotinkerItem.gluttony_core.get());
                        ModifierUtil.dropItem(event.getSource().getEntity(), a);
                    }
                }
            }
        }
        boolean confige = MomotinkerConfig.rage_stone_statue.get();
        if (confige&&(StageMeteor.getStageFloat()==1||!configstage)) {
            if (event.getSource().getEntity() instanceof Wolf wolf) {
                if (event.getEntity() instanceof Sheep sheep) {
                    ModDataNBT a = ModDataNBT.readFromNBT(event.getSource().getEntity().getPersistentData());
                    if (a.getFloat(ragetest) == 0) {
                        a.putFloat(ragetest, 1);
                    }
                }
            }
            if (event.getEntity() instanceof Wolf wolf) {
                if (event.getSource().getEntity() instanceof ServerPlayer player) {
                    ModDataNBT a = ModDataNBT.readFromNBT(event.getEntity().getPersistentData());
                    if (wolf.isAngry() && a.getFloat(ragetest) == 1) {
                        ItemStack b = new ItemStack(MomotinkerItem.rage_stone_statue.get());
                        ModifierUtil.dropItem(player, b);
                    }
                }
            }
        }
        boolean configf = MomotinkerConfig.compassion_mask.get();
        if (configf&&StageMeteor.getStageFloat()==1) {
            if (event.getEntity() instanceof Witch) {
                if (event.getSource()==event.getEntity().level().damageSources().fall()) {
                    ItemStack a = new ItemStack(MomotinkerItem.compassion_mask.get());
                    ModifierUtil.dropItem(event.getEntity(), a);
                }
            }
        }
        boolean configg = MomotinkerConfig.dragon_jade.get();
        if (configg&&(StageMeteor.getStageFloat()==1||!configstage)) {
            if (event.getEntity() instanceof EnderDragon enderDragon&&isDragonAliveLongEnough(enderDragon)&&enderDragon.level() instanceof ServerLevel level) {
                ItemEntity itemEntity = new ItemEntity(level, enderDragon.getX(), enderDragon.getY(), enderDragon.getZ(), MomotinkerItem.dragon_jade.get().getDefaultInstance());
                itemEntity.setGlowingTag(true);
                itemEntity.setNoGravity(true);
                itemEntity.setPickUpDelay(20);
                level.addFreshEntity(itemEntity);
            }
        }
    }

    private void OnBabyEntitySpawn(BabyEntitySpawnEvent event) {
        boolean configall = MomotinkerConfig.special_acquisition.get();
        boolean configstage = MomotinkerConfig.stage_meteor.get();
        if (!configall)return;
        boolean config = MomotinkerConfig.lust_mirror.get();
        if (config&&(StageMeteor.getStageFloat()==1||!configstage)) {
            if (event.getParentA() != null && event.getParentB() != null && event.getChild() != null) {
                ModDataNBT a = ModDataNBT.readFromNBT(event.getParentA().getPersistentData());
                ModDataNBT b = ModDataNBT.readFromNBT(event.getParentB().getPersistentData());
                if (a.getFloat(lusttest) <= 4 && b.getFloat(lusttest) <= 4) {
                    a.putFloat(lusttest, a.getFloat(lusttest) + 1);
                    b.putFloat(lusttest, b.getFloat(lusttest) + 1);
                }
                ItemStack c = new ItemStack(MomotinkerItem.lust_mirror.get());
                if (a.getFloat(lusttest) >= 3) {
                    ModifierUtil.dropItem(event.getParentA(), c);
                }
                if (b.getFloat(lusttest) >= 3) {
                    ModifierUtil.dropItem(event.getParentA(), c);
                }
            }
        }
    }

    private void OnBonemeal(BonemealEvent event) {
        boolean configall = MomotinkerConfig.special_acquisition.get();
        boolean configstage = MomotinkerConfig.stage_meteor.get();
        if (!configall)return;
        boolean config = MomotinkerConfig.spirit_visage.get();
        if (config&&(StageMeteor.getStageFloat()==1||!configstage)) {
            if (event.getBlock().getBlock() instanceof SaplingBlock) {
                int b = RANDOM.nextInt(50);
                if (b == 1) {
                    ItemStack a = new ItemStack(MomotinkerItem.spirit_visage.get());
                    ModifierUtil.dropItem(event.getEntity(), a);
                }
            }
        }
    }

    private void OnPlayerWakeUp(PlayerWakeUpEvent event) {
        if (event.getEntity() instanceof ServerPlayer player && !player.level().isClientSide) {
            if (player.level().dimension().location().toString().equals("momotinker:mountains_memory") && player.isSleepingLongEnough()) {
                String s = "minecraft:overworld";
                TeleportEntityManager.teleportEntityToDimension(player, s, player.getX(), player.getY(), player.getZ());
            }
        }
        boolean configall = MomotinkerConfig.special_acquisition.get();
        if (!configall) return;
        boolean configstage = MomotinkerConfig.stage_meteor.get();
        boolean config = MomotinkerConfig.lazy_grail.get();
        if (config && (StageMeteor.getStageFloat() == 1 || !configstage)) {
            if (event.getEntity() instanceof ServerPlayer player) {
                if (player.getEffect(MobEffects.MOVEMENT_SLOWDOWN) != null && player.getEffect(MobEffects.WEAKNESS) != null) {
                    if (player.isSleepingLongEnough() && player.hasEffect(MobEffects.MOVEMENT_SLOWDOWN) && player.hasEffect(MobEffects.WEAKNESS)) {
                        ItemStack a = new ItemStack(MomotinkerItem.lazy_grail.get());
                        ModifierUtil.dropItem(event.getEntity(), a);
                    }
                }
            }
        }
    }

    private void OnAddCustomTrades(VillagerTradesEvent event) {
        boolean configall = MomotinkerConfig.special_acquisition.get();
        boolean configstage = MomotinkerConfig.stage_meteor.get();
        if (!configall)return;
        boolean config = MomotinkerConfig.greedy_contract.get();
        if (config&&(StageMeteor.getStageFloat()==1||!configstage)) {
            if (event.getType() != null) {
                Int2ObjectMap<List<VillagerTrades.ItemListing>> trades = event.getTrades();
                ItemStack a = new ItemStack(MomotinkerItem.greedy_contract.get());
                int villagerLevel = 5;
                trades.get(villagerLevel).add((trader, rand) -> new MerchantOffer(
                        new ItemStack(Items.EMERALD_BLOCK, 16), a, 1, 0, 0.1f));
            }
        }
    }

    private void OnBreakEvent(BlockEvent.BreakEvent event) {
        boolean configall = MomotinkerConfig.special_acquisition.get();
        boolean configstage = MomotinkerConfig.stage_meteor.get();
        if (!configall)return;
        Player player = event.getPlayer();
        boolean config = MomotinkerConfig.dimensional_prism.get();
        if (config&&(StageMeteor.getStageFloat()==1||!configstage)) {
            if (player != null && event.getState().is(Blocks.GLASS)) {
                int a = RANDOM.nextInt(10000);
                if (player.getUseItem().isEnchanted() && player.getUseItem().getEnchantmentLevel(Enchantments.SILK_TOUCH) != 0) {
                    return;
                }
                if (a <618) {
                    ItemStack b = new ItemStack(MomotinkerItem.dimensional_prism.get());
                    ModifierUtil.dropItem(player, b);
                }
            }
        }
    }

    public void OnEntityJoinLevel(EntityJoinLevelEvent event) {
        if (event.getEntity() instanceof FallingBlockEntity fallingBlock) {
            if (fallingBlock.getBlockState().getBlock() == Blocks.ANVIL
                    ||fallingBlock.getBlockState().getBlock() ==Blocks.CHIPPED_ANVIL
                    ||fallingBlock.getBlockState().getBlock() ==Blocks.DAMAGED_ANVIL) {
                // 检测铁砧下落
                fallingBlock.addTag("falling_anvil");
            }
        }
        if (event.getEntity() instanceof EnderDragon enderDragon) {
            long spawnTime = event.getLevel().getGameTime();
            String s = "if_dragon_jade";
            enderDragon.getPersistentData().putLong(s,spawnTime);
        }
    }
    public boolean isDragonAliveLongEnough(EnderDragon dragon) {
        CompoundTag nbt = dragon.getPersistentData();
        String s = "if_dragon_jade";
        if (!nbt.contains(s)) return false;
        long spawnTime = nbt.getLong(s);
        long currentTime = dragon.level().getGameTime();
        return (currentTime - spawnTime) >= 24000;
    }
    private void convertItem(ItemEntity itemEntity, Item targetItem) {
        Level level = itemEntity.level();
        itemEntity.discard();
        ItemEntity newItem = new ItemEntity(level, itemEntity.getX(), itemEntity.getY(), itemEntity.getZ(), new ItemStack(targetItem));
        level.addFreshEntity(newItem);
    }
    private void OnPlayerTick(TickEvent.PlayerTickEvent event) {
        if (event.phase != TickEvent.Phase.START) return;
        boolean configall = MomotinkerConfig.special_acquisition.get();
        if (!configall)return;
        boolean config = MomotinkerConfig.meteor_nucleus.get();
        Player player1=event.player;
        Random random = new Random();
        if (player1.level() instanceof ServerLevel level&&player1 instanceof ServerPlayer player) {
            boolean configa = MomotinkerConfig.devouring_demon_gold.get();
            boolean configstage = MomotinkerConfig.stage_meteor.get();
            if (configa&&(StageMeteor.getStageFloat()==1||!configstage)) {
                double range = 30.0;
                AABB area = new AABB(player.getX() - range,
                        player.getY() - range,
                        player.getZ() - range,
                        player.getX() + range,
                        player.getY() + range,
                        player.getZ() + range);
                for (FallingBlockEntity anvil : level.getEntitiesOfClass(FallingBlockEntity.class, area
                        , entity -> true)) {
                    if ((anvil.getBlockState().getBlock() == Blocks.ANVIL||anvil.getBlockState().getBlock() ==Blocks.CHIPPED_ANVIL
                            ||anvil.getBlockState().getBlock() ==Blocks.DAMAGED_ANVIL )&& anvil.getTags().contains("falling_anvil")) {
                        AABB hitbox = anvil.getBoundingBox().inflate(0.2);
                        for (ItemEntity itemEntity : level.getEntitiesOfClass(ItemEntity.class, hitbox)) {
                            if (!itemEntity.getItem().isEnchanted()) {
                                return;
                            }
                            if (itemEntity.getItem().getAllEnchantments().size() >= 5) {
                                convertItem(itemEntity, MomotinkerItem.devouring_demon_gold.get().getDefaultInstance().getItem()); // 转换物品
                            }
                        }
                    }
                }
            }


            UUID playerId = player.getUUID();
            CompoundTag tag = player.getPersistentData().getCompound(Player.PERSISTED_NBT_TAG);
            String a = "meteor_nucleus_unlock";
            boolean meteorUnlocked = tag.getBoolean(a);
            float currentStageFloat = StageMeteor.getStageFloat(playerId);
            if (meteorUnlocked && currentStageFloat != 1) {
                Channel.sendToPlayer(new StageMeteorCharge(1), player);
                StageMeteor.setStageFloat(playerId, 1);
            } else if (!meteorUnlocked && currentStageFloat != 0) {
                Channel.sendToPlayer(new StageMeteorCharge(0), player);
                StageMeteor.setStageFloat(playerId, 0);
            }


            if (config) {
                int time = MomotinkerConfig.meteor_time_limit.get();
                int probability = MomotinkerConfig.meteor_probability_limit.get();
                if (level.getGameTime() % time == 0 && random.nextInt(100) <= probability) {
                    MeteorSummon(level,player);
                }
            }
        }
    }
    private void MeteorSummon(ServerLevel level,ServerPlayer player) {
        Random random = new Random();
        CompoundTag tag = player.getPersistentData().getCompound(Player.PERSISTED_NBT_TAG);
        String a = "meteor_nucleus_unlock";
        boolean meteorUnlocked = tag.getBoolean(a);
        if (meteorUnlocked) {
            double angle = random.nextDouble() * 2 * Math.PI;
            double distance = 80 + random.nextDouble() * 100;

            float meteorX = (float) (player.getX() + Math.cos(angle) * distance);
            float meteorZ = (float) (player.getZ() + Math.sin(angle) * distance);

            MeteorEntity entity = new MeteorEntity(level, meteorX, player.getY() + 80, meteorZ, new Vec3(0, 0, 0));
            entity.setExplosionPower((byte) (random.nextInt(55) + 25));
            level.addFreshEntity(entity);
            player.sendSystemMessage(Component.translatable("item.momotinker.tooltip.meteor_nucleus5").withStyle(ChatFormatting.GOLD));
        }
    }

    private void OnPlayerPickUp(EntityItemPickupEvent event) {
        boolean configall = MomotinkerConfig.special_acquisition.get();
        if (!configall)return;
        boolean config = MomotinkerConfig.meteor_nucleus.get();
        if (config) {
            if (event.getEntity() instanceof ServerPlayer player) {
                CompoundTag tag = player.getPersistentData().getCompound(Player.PERSISTED_NBT_TAG);
                String a = "meteor_nucleus_unlock";
                if (event.getItem().getItem().is(MomotinkerItem.interdimensional_crystal.get()) && !tag.getBoolean(a)) {
                    player.sendSystemMessage(Component.translatable("item.momotinker.tooltip.interdimensional_crystal3").withStyle(ChatFormatting.GOLD));
                    player.getPersistentData().getBoolean(a);
                    tag.putBoolean(a, true);
                    player.getPersistentData().put(Player.PERSISTED_NBT_TAG, tag);
                    if (player.level instanceof ServerLevel level){
                        MeteorSummon(level,player);
                    }
                }
            }
        }
    }
    private void OnLivingTick(TickEvent.LevelTickEvent event) {
        if (event.phase != TickEvent.Phase.END || event.level.isClientSide) return;
        boolean configall = MomotinkerConfig.special_acquisition.get();
        if (!configall)return;
        boolean config = MomotinkerConfig.lightning_strike_wood.get();
        if (config) {
            for (Player player : event.level.players()) {
                List<Entity> list = event.level.getEntitiesOfClass(Entity.class, player.getBoundingBox().inflate(200));
                for (Entity entity : list) {
                    if (entity instanceof LightningBolt bolt) {
                        Vec3 vec3 = bolt.position();
                        BlockPos strikePos = new BlockPos((int) vec3.x, (int) (vec3.y - 1.0E-6), (int) vec3.z);
                        for (int x = -1; x <= 1; x++) {
                            for (int y = -3; y <= 4; y++) {
                                for (int z = -1; z <= 1; z++) {
                                    BlockPos targetPos = strikePos.offset(x, y, z); // 计算目标方块的坐标
                                    BlockState blockState = event.level.getBlockState(targetPos); // 获取该位置的方块状态
                                    Block block = blockState.getBlock(); // 获取方块对象
                                    if (block == MomotinkerBlock.jujube_wood_log.get()) {
                                        event.level.setBlock(targetPos, MomotinkerBlock.lightning_strike_wood.get().defaultBlockState(), 3);
                                    }
                                    if (block == MomotinkerBlock.jujube_wood_leaves.get()) {
                                        event.level.setBlock(targetPos, Blocks.AIR.defaultBlockState(), 3);
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
        boolean configa = MomotinkerConfig.mountain_river_paintings.get();
        if (configa&&StageMeteor.getStageFloat()==1) {
            Random random=new Random();
            for (Player player : event.level.players()) {
                List<ItemEntity> list = event.level.getEntitiesOfClass(ItemEntity.class, player.getBoundingBox().inflate(200));
                for (ItemEntity entity : list) {
                    if (entity.getItem().is(Items.MAP)&&player.tickCount%20==0 &&random.nextInt(20)==0 &&entity.level().getFluidState(entity.blockPosition()).is(FluidTags.WATER)){
                        entity.getItem().setCount(entity.getItem().getCount()-1);
                        ItemStack b = new ItemStack(MomotinkerItem.mountain_river_paintings.get().getDefaultInstance().getItem());
                        ModifierUtil.dropItem(entity, b);
                    }
                }
            }
        }
    }

    private void OnCheckSpawn(MobSpawnEvent.SpawnPlacementCheck event) {
        if (event.getLevel().getLevel().dimension().location().toString().equals("momotinker:mountains_memory")) {
            if (event.getSpawnType() == MobSpawnType.NATURAL) {
                event.setResult(Event.Result.DENY);
            }
        }
    }
    private void OnPlayerDestroyItem(PlayerDestroyItemEvent event) {
        boolean configall = MomotinkerConfig.special_acquisition.get();
        if (!configall)return;
        boolean config = MomotinkerConfig.immortal_weiqi.get();
        if (config&&StageMeteor.getStageFloat()==1) {
            if (!isMidnightToNoonStrict(event.getEntity().level().getDayTime()))return;
            Random random=new Random();
            if (random.nextInt(4)!=0)return;
            if (event.getOriginal().is(Items.WOODEN_AXE)) {
                ItemStack b = new ItemStack(MomotinkerItem.immortal_weiqi.get().getDefaultInstance().getItem());
                ModifierUtil.dropItem(event.getEntity(), b);
            }
        }
    }
    public static boolean isMidnightToNoonStrict(long minecraftTime) {
        long time = minecraftTime % 24000;
        if (time < 0) time += 24000;
        return (time < 6000)||(time > 18000);
    }
}