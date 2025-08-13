package com.momosensei.momotinker.Modifiers.modifiers;

import com.momosensei.momotinker.Modifiers.momomodifier;
import com.momosensei.momotinker.Momotinker;
import com.momosensei.momotinker.register.MomotinkerModifiers;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.monster.Slime;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import slimeknights.tconstruct.library.modifiers.Modifier;
import slimeknights.tconstruct.library.modifiers.ModifierEntry;
import slimeknights.tconstruct.library.tools.context.EquipmentContext;
import slimeknights.tconstruct.library.tools.nbt.IToolStackView;
import slimeknights.tconstruct.library.tools.nbt.ModDataNBT;
import slimeknights.tconstruct.library.tools.nbt.ToolStack;
import slimeknights.tconstruct.tools.TinkerModifiers;
import slimeknights.tconstruct.tools.modifiers.slotless.OverslimeModifier;

import java.util.Random;


public class SlimeRhythmA extends momomodifier {
    public SlimeRhythmA() {
        MinecraftForge.EVENT_BUS.addListener(this::livinghurtevent);
    }

    public static final ResourceLocation rhythmaobbsa = Momotinker.getResource("rhythmaobbsa");
    public static final ResourceLocation rhythmaobbsb = Momotinker.getResource("rhythmaobbsb");

    public static final ResourceLocation rhythmapoints = Momotinker.getResource("rhythmapoints");

    @Override
    public Component onRemoved(IToolStackView iToolStackView, Modifier modifier) {
        iToolStackView.getPersistentData().remove(rhythmaobbsa);
        iToolStackView.getPersistentData().remove(rhythmaobbsb);
        iToolStackView.getPersistentData().remove(rhythmapoints);
        return null;
    }
    @Override
    public void onInventoryTick(IToolStackView tool, ModifierEntry modifier, Level world, LivingEntity entity, int index, boolean isSelected, boolean isCorrectSlot, ItemStack stack) {
        ModDataNBT a = tool.getPersistentData();
        a.putInt(rhythmaobbsa,a.getInt(rhythmaobbsa)+1);
        if (a.getInt(rhythmaobbsa)<0){
            a.putInt(rhythmaobbsa,0);
        }
        if (a.getInt(rhythmaobbsb)<0){
            a.putInt(rhythmaobbsb,0);
        }
        if (a.getInt(rhythmapoints)<0){
            a.putInt(rhythmapoints,0);
        }
    }

    @Override
    public void onAttacked(IToolStackView tool, ModifierEntry modifier, EquipmentContext context, EquipmentSlot slotType, DamageSource source, float amount, boolean isDirectDamage) {
        if (source.getEntity() != null) {
            ModDataNBT a = tool.getPersistentData();
            OverslimeModifier overslime = TinkerModifiers.overslime.get();
            ModifierEntry entry = tool.getModifier(TinkerModifiers.overslime.getId());

            if (a.getInt(rhythmaobbsa)!=0){
                int b=Math.abs(a.getInt(rhythmaobbsa)-a.getInt(rhythmaobbsb));
                if (b<=6) {
                    int c = 1;
                    if (source.getEntity() instanceof Slime){
                        Random random=new Random();
                        c=random.nextInt(10);
                    }
                    if (c==1) {
                        a.putInt(rhythmapoints, a.getInt(rhythmapoints) + 1);
                        if (entry.getLevel() > 0 && overslime.getShield(tool) < overslime.getShieldCapacity(tool, entry)) {
                            overslime.addOverslime(tool, entry, (int) (1 + Math.floor(getArmorModifierlevel(context.getEntity(), MomotinkerModifiers.slimerhythma.getId()) * 0.25f + 0.25f * a.getInt(rhythmapoints))));
                        }
                    }
                }else {
                    a.putInt(rhythmapoints,0);
                }
                a.putInt(rhythmaobbsb, a.getInt(rhythmaobbsa));
                a.putInt(rhythmaobbsa, 0);
            }
            if (a.getInt(rhythmapoints)>=24){
                if (context.getEntity().level() instanceof ServerLevel level){
                    level.sendParticles(ParticleTypes.ITEM_SLIME, context.getEntity().getX(), context.getEntity().getY()+context.getEntity().getBbHeight()*0.6f, context.getEntity().getZ(), 4, 0.5, 0.5, 0.5, 2);
                }
            }
        }
    }
    private ModifierEntry getOverSlime(ToolStack tool){
        return tool.getModifier(TinkerModifiers.overslime.getId());
    }
    private int getRhythmPoints(ToolStack tool){
        return tool.getPersistentData().getInt(rhythmapoints);
    }
    private boolean getRhythmPointsTure(ToolStack tool1,ToolStack tool2,ToolStack tool3,ToolStack tool4){
        return getRhythmPoints(tool1) > 0 || getRhythmPoints(tool2) > 0 || getRhythmPoints(tool3) > 0 || getRhythmPoints(tool4) > 0;
    }
    private void livinghurtevent(LivingHurtEvent event) {
        LivingEntity a = event.getEntity();
        OverslimeModifier overslime = TinkerModifiers.overslime.get();
        if (a instanceof Player player) {
            ToolStack tool3 = ToolStack.from(player.getItemBySlot(EquipmentSlot.HEAD));
            ToolStack tool4 = ToolStack.from(player.getItemBySlot(EquipmentSlot.CHEST));
            ToolStack tool5 = ToolStack.from(player.getItemBySlot(EquipmentSlot.LEGS));
            ToolStack tool6 = ToolStack.from(player.getItemBySlot(EquipmentSlot.FEET));
            
            int d = getArmorModifierlevel(player, MomotinkerModifiers.slimerhythma.getId());
            if (d > 0&&getRhythmPointsTure(tool3,tool4,tool5,tool6)) {
                int b1 = (int) Math.floor(event.getAmount());
                int b3;int b4;int b5;int b6;
                if (b1>0&&getOverSlime(tool3).getLevel() > 0&&overslime.getShield(tool3) >0) {
                    b3 = overslime.getShield(tool3);
                    b1-=b3;
                    if (b1>0) {
                        overslime.addOverslime(tool3, getOverSlime(tool3), -b3);
                    }else overslime.addOverslime(tool3, getOverSlime(tool3), -b1);
                }
                if (b1>0&&getOverSlime(tool4).getLevel() > 0&&overslime.getShield(tool4) >0) {
                    b4 = overslime.getShield(tool4);
                    b1-=b4;
                    if (b1>0) {
                        overslime.addOverslime(tool4, getOverSlime(tool4), -b4);
                    }else overslime.addOverslime(tool4, getOverSlime(tool4), -b1);
                }
                if (b1>0&&getOverSlime(tool5).getLevel() > 0&&overslime.getShield(tool5) >0) {
                    b5 = overslime.getShield(tool5);
                    b1-=b5;
                    if (b1>0) {
                        overslime.addOverslime(tool5, getOverSlime(tool5), -b5);
                    }else overslime.addOverslime(tool5, getOverSlime(tool5), -b1);
                }
                if (b1>0&&getOverSlime(tool6).getLevel() > 0&&overslime.getShield(tool6) >0) {
                    b6 = overslime.getShield(tool6);
                    b1-=b6;
                    if (b1>0) {
                        overslime.addOverslime(tool6, getOverSlime(tool6), -b6);
                    }else overslime.addOverslime(tool6, getOverSlime(tool6), -b1);
                }
                event.setAmount(Math.max(b1, 0));
            }
        }
    }
}