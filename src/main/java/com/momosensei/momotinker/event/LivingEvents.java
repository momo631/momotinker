package com.momosensei.momotinker.event;


import com.momosensei.momotinker.Momotinker;
import com.momosensei.momotinker.entity.MeteorEntity;
import com.momosensei.momotinker.register.MomotinkerConfig;
import com.momosensei.momotinker.register.MomotinkerItem;
import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import net.minecraft.ChatFormatting;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.animal.IronGolem;
import net.minecraft.world.entity.animal.Sheep;
import net.minecraft.world.entity.animal.Wolf;
import net.minecraft.world.entity.animal.frog.Frog;
import net.minecraft.world.entity.boss.wither.WitherBoss;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.monster.Slime;
import net.minecraft.world.entity.monster.warden.Warden;
import net.minecraft.world.entity.npc.VillagerTrades;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.item.trading.MerchantOffer;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.SaplingBlock;
import net.minecraft.world.phys.Vec2;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.item.ItemEvent;
import net.minecraftforge.event.entity.living.BabyEntitySpawnEvent;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.event.entity.player.BonemealEvent;
import net.minecraftforge.event.entity.player.EntityItemPickupEvent;
import net.minecraftforge.event.entity.player.SleepingTimeCheckEvent;
import net.minecraftforge.event.level.BlockEvent;
import net.minecraftforge.event.village.VillagerTradesEvent;
import slimeknights.tconstruct.library.tools.helper.ModifierUtil;
import slimeknights.tconstruct.library.tools.nbt.ModDataNBT;

import java.util.List;
import java.util.Random;

import static slimeknights.tconstruct.TConstruct.RANDOM;


public class LivingEvents {
    public LivingEvents() {
        MinecraftForge.EVENT_BUS.addListener(this::livinghurtevent);
        MinecraftForge.EVENT_BUS.addListener(this::onEntityDeath);
        MinecraftForge.EVENT_BUS.addListener(this::onBabyEntitySpawnEvent);
        MinecraftForge.EVENT_BUS.addListener(this::onBonemealEvent);
        MinecraftForge.EVENT_BUS.addListener(this::onSleepingTimeCheckEvent);
        MinecraftForge.EVENT_BUS.addListener(this::addCustomTrades);
        MinecraftForge.EVENT_BUS.addListener(this::onItemEvent);
        MinecraftForge.EVENT_BUS.addListener(this::onFallVoidEvent);
        MinecraftForge.EVENT_BUS.addListener(this::playertick);
        MinecraftForge.EVENT_BUS.addListener(this::playerpickup);
    }

    private static final ResourceLocation lusttest = Momotinker.getResource("lusttest");
    private static final ResourceLocation ragetest = Momotinker.getResource("ragetest");

    private void livinghurtevent(LivingHurtEvent event) {
        boolean configall = MomotinkerConfig.special_acquisition.get();
        if (!configall)return;
        Entity a = event.getEntity();
        Entity b = event.getSource().getEntity();
        boolean configa = MomotinkerConfig.arriving_at_the_other_shore.get();
        if (configa) {
            if (a instanceof ServerPlayer player && player.getHealth() <= 1) {
                if (event.getAmount() > player.getMaxHealth() && player.isAlive()) {
                    ItemStack a1 = new ItemStack(MomotinkerItem.arriving_at_the_other_shore.get());
                    ModifierUtil.dropItem(player, a1);
                }
            }
        }
        boolean configb = MomotinkerConfig.jealous_notes.get();
        if (configb) {
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

    private void onEntityDeath(LivingDeathEvent event) {
        boolean configall = MomotinkerConfig.special_acquisition.get();
        if (!configall)return;
        if (event.getEntity() instanceof Warden warden) {
            boolean configa = MomotinkerConfig.heartsteel.get();
            if (configa) {
                if (event.getSource().getEntity() instanceof IronGolem) {
                    ItemStack a = new ItemStack(MomotinkerItem.heartsteel.get());
                    ModifierUtil.dropItem(event.getSource().getEntity(), a);
                }
            }
            boolean configb = MomotinkerConfig.arrogance_proof.get();
            if (configb) {
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
        if (configd) {
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
        if (confige) {
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
    }

    private void onBabyEntitySpawnEvent(BabyEntitySpawnEvent event) {
        boolean configall = MomotinkerConfig.special_acquisition.get();
        if (!configall)return;
        boolean config = MomotinkerConfig.lust_mirror.get();
        if (config) {
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

    private void onBonemealEvent(BonemealEvent event) {
        boolean configall = MomotinkerConfig.special_acquisition.get();
        if (!configall)return;
        boolean config = MomotinkerConfig.spirit_visage.get();
        if (config) {
            if (event.getBlock().getBlock() instanceof SaplingBlock && event.getEntity() instanceof ServerPlayer) {
                int b = RANDOM.nextInt(50);
                if (b == 1) {
                    ItemStack a = new ItemStack(MomotinkerItem.spirit_visage.get());
                    ModifierUtil.dropItem(event.getEntity(), a);
                }
            }
        }
    }

    private void onSleepingTimeCheckEvent(SleepingTimeCheckEvent event) {
        boolean configall = MomotinkerConfig.special_acquisition.get();
        if (!configall)return;
        boolean config = MomotinkerConfig.lazy_grail.get();
        if (config) {
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

    private void addCustomTrades(VillagerTradesEvent event) {
        boolean configall = MomotinkerConfig.special_acquisition.get();
        if (!configall)return;
        boolean config = MomotinkerConfig.greedy_contract.get();
        if (config) {
            if (event.getType() != null) {
                Int2ObjectMap<List<VillagerTrades.ItemListing>> trades = event.getTrades();
                ItemStack a = new ItemStack(MomotinkerItem.greedy_contract.get());
                int villagerLevel = 5;
                trades.get(villagerLevel).add((trader, rand) -> new MerchantOffer(
                        new ItemStack(Items.EMERALD_BLOCK, 16), a, 1, 0, 0.1f));
            }
        }
    }

    private void onItemEvent(BlockEvent.BreakEvent event) {
        boolean configall = MomotinkerConfig.special_acquisition.get();
        if (!configall)return;
        Player player = event.getPlayer();
        boolean config = MomotinkerConfig.dimensional_prism.get();
        if (config) {
            if (player != null && event.getState().is(Blocks.GLASS)) {
                int a = RANDOM.nextInt(10);
                if (player.getUseItem().isEnchanted() && player.getUseItem().getEnchantmentLevel(Enchantments.SILK_TOUCH) != 0) {
                    return;
                }
                if (a == 1) {
                    ItemStack b = new ItemStack(MomotinkerItem.dimensional_prism.get());
                    ModifierUtil.dropItem(player, b);
                }
            }
        }
    }

    private void onFallVoidEvent(ItemEvent event) {
        boolean configall = MomotinkerConfig.special_acquisition.get();
        if (!configall)return;
        ItemEntity entity = event.getEntity();
        Level level = event.getEntity().level;
        boolean config = MomotinkerConfig.devouring_demon_gold.get();
        if (config) {
            if (entity != null && entity.getOnPos().getY() < level.getMinBuildHeight()) {
                if (!entity.getItem().isEnchanted()) {
                    return;
                }
                if (entity.getItem().getAllEnchantments().size() >= 5) {
                    entity.setItem(MomotinkerItem.devouring_demon_gold.get().getDefaultInstance());
                }
            }
        }
    }

    private void playertick(TickEvent.PlayerTickEvent event) {
        boolean configall = MomotinkerConfig.special_acquisition.get();
        if (!configall)return;
        boolean config = MomotinkerConfig.meteor_nucleus.get();
        if (config) {
            if (event.player.getLevel() instanceof ServerLevel level && level.getGameTime() % 2000 == 0) {
                Player player = event.player;
                Random random = new Random();
                CompoundTag tag = player.getPersistentData().getCompound(Player.PERSISTED_NBT_TAG);
                String a = "meteor_nucleus_unlock";
                if (random.nextInt(5) == 0 && tag.getBoolean(a)) {

                    Vec2 pos1 = new Vec2((float) (player.getX() + random.nextInt(240) - random.nextInt(240)), (float) (player.getZ() + random.nextInt(240) - random.nextInt(240)));
                    Vec2 pos2 = new Vec2((float) (player.getX() + random.nextInt(100) - random.nextInt(100)), (float) (player.getZ() + random.nextInt(100) - random.nextInt(100)));
                    Vec2 pos = new Vec2((float) Math.pow(Math.pow(pos1.x,2)-Math.pow(pos2.x,2), (double) 1 /2), (float) Math.pow(Math.pow(pos1.y,2)-Math.pow(pos2.y,2), (double) 1 /2));
                    if ((pos.x<100)||(pos.y<100)){
                        return;
                    }
                    EntitySpawnEvent event1 = new EntitySpawnEvent(new Vec3(pos.x, player.getY() + 150, pos.y));
                    //EntitySpawnEvent event1 = new EntitySpawnEvent(new Vec3(player.getX(), player.getY() + 150, player.getZ()));
                    MinecraftForge.EVENT_BUS.post(event1);
                    if (!event1.isCanceled()) {
                        MeteorEntity entity = new MeteorEntity(level, pos.x, player.getY() + 150, pos.y, new Vec3(0, 0, 0));
                        //MeteorEntity entity = new MeteorEntity(level, player.getX(), player.getY() + 150, player.getZ(), new Vec3(random.nextFloat() * 0.5, random.nextFloat() * 2.5 - 1.5, random.nextFloat() * 0.5));
                        entity.setExplosionPower((byte) (random.nextInt(55) + 25));
                        level.addFreshEntity(entity);
                        player.sendSystemMessage(Component.translatable("momotinker.item.tooltip.meteor_nucleus5").withStyle(ChatFormatting.GOLD));
                    }
                }
            }
        }
    }

    private void playerpickup(EntityItemPickupEvent event) {
        boolean configall = MomotinkerConfig.special_acquisition.get();
        if (!configall)return;
        boolean config = MomotinkerConfig.meteor_nucleus.get();
        if (config) {
            if (event.getEntity() instanceof ServerPlayer player) {
                CompoundTag tag = player.getPersistentData().getCompound(Player.PERSISTED_NBT_TAG);
                String a = "meteor_nucleus_unlock";
                if (event.getItem().getItem().is(MomotinkerItem.interdimensional_crystal.get()) && !tag.getBoolean(a)) {
                    player.sendSystemMessage(Component.translatable("momotinker.item.tooltip.interdimensional_crystal3").withStyle(ChatFormatting.GOLD));
                    player.getPersistentData().getBoolean(a);
                    tag.putBoolean(a, true);
                    player.getPersistentData().put(Player.PERSISTED_NBT_TAG, tag);
                }
            }
        }
    }
}