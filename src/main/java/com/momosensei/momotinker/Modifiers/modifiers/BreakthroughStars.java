package com.momosensei.momotinker.Modifiers.modifiers;


import com.momosensei.momotinker.Modifiers.momomodifier;
import com.momosensei.momotinker.Momotinker;
import com.momosensei.momotinker.register.MomotinkerConfig;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import slimeknights.tconstruct.library.modifiers.ModifierEntry;
import slimeknights.tconstruct.library.tools.nbt.IToolStackView;
import slimeknights.tconstruct.library.tools.nbt.ModDataNBT;

import static com.momosensei.momotinker.register.MomotinkerItem.divine_punishment_spear;

public class BreakthroughStars extends momomodifier {
    public BreakthroughStars() {
    }
    public static final ResourceLocation breakthroughstar = Momotinker.getResource("breakthroughstar");

    @Override
    public boolean isNoLevels() {
        return true;
    }

    @Override
    public void onInventoryTick(IToolStackView tool, ModifierEntry modifierEntry, Level level, LivingEntity entity, int index, boolean b, boolean b1, ItemStack itemStack) {
        if (entity instanceof ServerPlayer player && player.getItemBySlot(EquipmentSlot.MAINHAND).is(divine_punishment_spear.get()) && player.level instanceof ServerLevel serverLevel) {
            ModDataNBT a =tool.getPersistentData();
            int c = (int) (player.totalExperience*0.05F);
            int d = MomotinkerConfig.breakthroughstar_limit.get();
            if (c<d) {
                a.putInt(breakthroughstar,c);
            }
            if (c>d){
                a.putInt(breakthroughstar,d);
            }
        }
    }
}