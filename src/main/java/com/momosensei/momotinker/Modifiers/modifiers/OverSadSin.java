package com.momosensei.momotinker.Modifiers.modifiers;

import com.momosensei.momotinker.Modifiers.momomodifier;
import com.momosensei.momotinker.Momotinker;
import com.momosensei.momotinker.register.MomotinkerModifiers;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import slimeknights.tconstruct.library.modifiers.Modifier;
import slimeknights.tconstruct.library.modifiers.ModifierEntry;
import slimeknights.tconstruct.library.tools.nbt.IToolStackView;
import slimeknights.tconstruct.library.tools.nbt.ModDataNBT;
import slimeknights.tconstruct.library.tools.nbt.ToolStack;


public class OverSadSin extends momomodifier {
    public OverSadSin() {
    }
    public static final ResourceLocation oversadsinpoints = Momotinker.getResource("oversadsinpoints");
    @Override
    public Component onRemoved(IToolStackView iToolStackView, Modifier modifier) {
        iToolStackView.getPersistentData().remove(oversadsinpoints);
        return null;
    }
    @Override
    public void onInventoryTick(IToolStackView iToolStackView, ModifierEntry modifierEntry, Level level, LivingEntity entity, int index, boolean b, boolean b1, ItemStack itemStack) {
        if (entity instanceof Player player) {
            int a = getMainhandModifierlevel(player, MomotinkerModifiers.sacrificetospirit.getId());
            ToolStack tool=ToolStack.from(player.getMainHandItem());
            ModDataNBT data=tool.getPersistentData();
            if (a>0&&player.tickCount%40==0){
                data.putFloat(oversadsinpoints,data.getFloat(oversadsinpoints)+1f+a*0.25f);
                player.hurt(DamageSource.MAGIC,data.getFloat(oversadsinpoints)*data.getFloat(oversadsinpoints)*0.5f);
            }
            if (a==0&&iToolStackView.getPersistentData().getFloat(oversadsinpoints)!=0){
                iToolStackView.getPersistentData().putFloat(oversadsinpoints,0);
            }
        }
    }

}