package com.momosensei.momotinker.event;


/*
public class LivingEvents {
    public LivingEvents(){
        MinecraftForge.EVENT_BUS.addListener(this::livinghurtevent);
        MinecraftForge.EVENT_BUS.addListener(EventPriority.LOWEST,this::onEntityDeath);
        MinecraftForge.EVENT_BUS.addListener(EventPriority.LOWEST,this::onBabyEntitySpawnEvent);
        MinecraftForge.EVENT_BUS.addListener(EventPriority.LOWEST,this::onBonemealEvent);
        MinecraftForge.EVENT_BUS.addListener(EventPriority.LOWEST,this::onSleepingTimeCheckEvent);
    }
    private static final ResourceLocation lusttest = Momotinker.getResource("lusttest");
    private static final ResourceLocation ragetest = Momotinker.getResource("ragetest");

    private void livinghurtevent(LivingHurtEvent event) {
        Entity a = event.getEntity();
        Entity b = event.getSource().getEntity();
        if (a instanceof ServerPlayer player&&player.getHealth() <= 1){
            if (event.getAmount()>player.getMaxHealth()&&player.isAlive()){
                ItemStack a1 = new ItemStack(MomotinkerItem.arriving_at_the_other_shore.get());
                ModifierUtil.dropItem(player, a1);
            }
        }
        if (a instanceof ServerPlayer player&&b instanceof LivingEntity entity&&player.getItemBySlot(EquipmentSlot.MAINHAND).is(Items.WRITABLE_BOOK)) {
            if (!player.hasItemInSlot(EquipmentSlot.HEAD) && !player.hasItemInSlot(EquipmentSlot.CHEST) && !player.hasItemInSlot(EquipmentSlot.LEGS) && !player.hasItemInSlot(EquipmentSlot.FEET)) {
                if (entity.hasItemInSlot(EquipmentSlot.HEAD) ||entity.hasItemInSlot(EquipmentSlot.CHEST) ||entity.hasItemInSlot(EquipmentSlot.LEGS) ||entity.hasItemInSlot(EquipmentSlot.FEET)){
                    player.setItemSlot(EquipmentSlot.MAINHAND, ItemStack.EMPTY);
                    ItemStack a1 = new ItemStack(MomotinkerItem.jealous_notes.get());
                    player.getInventory().add(a1);
                }
            }
        }
    }

    private void onEntityDeath(LivingDeathEvent event) {
        if (event.getEntity() instanceof Warden warden) {
            if (event.getSource().getEntity() instanceof IronGolem){
                ItemStack a = new ItemStack(MomotinkerItem.heartsteel.get());
                ModifierUtil.dropItem(event.getSource().getEntity(),a);
            }
            if (event.getSource().getEntity() instanceof ServerPlayer player){
                if (!player.hasItemInSlot(EquipmentSlot.HEAD)&&!player.hasItemInSlot(EquipmentSlot.CHEST)&&!player.hasItemInSlot(EquipmentSlot.LEGS)&&!player.hasItemInSlot(EquipmentSlot.FEET)){
                    ItemStack a = new ItemStack(MomotinkerItem.arrogance_proof.get());
                    ModifierUtil.dropItem(player,a);
                }
            }
        }
        if (event.getEntity() instanceof WitherBoss wither) {
            if (wither.getOnPos().getY() >= 300) {
                ItemStack a = new ItemStack(MomotinkerItem.interdimensional_crystal.get());
                ModifierUtil.dropItem(event.getEntity(), a);
            }
        }
        if(event.getEntity() instanceof Slime slime){
            if (event.getSource().getEntity() instanceof Frog){
                int b = RANDOM.nextInt(4);
                if (b==1) {
                    ItemStack a = new ItemStack(MomotinkerItem.gluttony_core.get());
                    ModifierUtil.dropItem(event.getSource().getEntity(), a);
                }
            }
        }
        if (event.getSource().getEntity() instanceof Wolf wolf){
            if (event.getEntity() instanceof Sheep sheep){
                ModDataNBT a = ModDataNBT.readFromNBT(event.getSource().getEntity().getPersistentData());
                if (a.getFloat(ragetest)==0) {
                    a.putFloat(ragetest, 1);
                }
            }
        }
        if (event.getEntity() instanceof Wolf wolf){
            if (event.getSource().getEntity() instanceof ServerPlayer player) {
                ModDataNBT a = ModDataNBT.readFromNBT(event.getEntity().getPersistentData());
                if (wolf.isAngry()&&a.getFloat(ragetest)==1){
                    ItemStack b = new ItemStack(MomotinkerItem.rage_stone_statue.get());
                    ModifierUtil.dropItem(player, b);
                }
            }
        }
    }

    private void onBabyEntitySpawnEvent(BabyEntitySpawnEvent event) {
        if (event.getParentA()!=null&&event.getParentB()!=null&&event.getChild()!=null){
            ModDataNBT a = ModDataNBT.readFromNBT(event.getParentA().getPersistentData());
            ModDataNBT b = ModDataNBT.readFromNBT(event.getParentB().getPersistentData());
            if (a.getFloat(lusttest)<=4&&b.getFloat(lusttest)<=4) {
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
    private void onBonemealEvent(BonemealEvent event) {
        if (event.getBlock().getBlock() instanceof SaplingBlock){
            int b = RANDOM.nextInt(10);
            if (b==1) {
                ItemStack a = new ItemStack(MomotinkerItem.spirit_visage.get());
                ModifierUtil.dropItem(event.getEntity(), a);
            }
        }
    }
    private void onSleepingTimeCheckEvent(SleepingTimeCheckEvent event) {
        if (event.getEntity() instanceof ServerPlayer player) {
            if (player.getEffect(MobEffects.MOVEMENT_SLOWDOWN)!=null&&player.getEffect(MobEffects.WEAKNESS)!=null) {
                if (player.isSleepingLongEnough() && player.hasEffect(MobEffects.MOVEMENT_SLOWDOWN) && player.hasEffect(MobEffects.WEAKNESS)) {
                    ItemStack a = new ItemStack(MomotinkerItem.lazy_grail.get());
                    ModifierUtil.dropItem(event.getEntity(), a);
                }
            }
        }
    }
}*/