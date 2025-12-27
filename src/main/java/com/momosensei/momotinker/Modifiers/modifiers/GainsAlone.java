package com.momosensei.momotinker.Modifiers.modifiers;

import com.momosensei.momotinker.Modifiers.momomodifier;
import com.momosensei.momotinker.Momotinker;
import com.momosensei.momotinker.register.MomotinkerModifiers;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.event.level.BlockEvent;
import org.jetbrains.annotations.NotNull;
import slimeknights.tconstruct.library.modifiers.ModifierEntry;
import slimeknights.tconstruct.library.tools.nbt.IToolContext;
import slimeknights.tconstruct.library.tools.nbt.IToolStackView;
import slimeknights.tconstruct.library.tools.nbt.ModDataNBT;
import slimeknights.tconstruct.library.tools.nbt.ToolStack;
import slimeknights.tconstruct.library.tools.stat.ModifierStatsBuilder;
import slimeknights.tconstruct.library.tools.stat.ToolStats;

import javax.annotation.Nullable;
import java.util.Iterator;
import java.util.List;


public class GainsAlone extends momomodifier {
    public GainsAlone() {
        MinecraftForge.EVENT_BUS.addListener(this::onEntityDeath);
        MinecraftForge.EVENT_BUS.addListener(this::onBreakBlockEvent);
    }
    public static final ResourceLocation gainsalonepoints = Momotinker.getResource("gainsalonepoints");

    @Override
    public boolean isNoLevels() {
        return true;
    }
    //    @Override
//    public Component onRemoved(IToolStackView iToolStackView, Modifier modifier) {
//        iToolStackView.getPersistentData().remove(gainsalonepoints);
//        return null;
//    }
    @Nullable
    @Override
    public Component requirementsError(ModifierEntry entry) {
        return Component.translatable("recipe.momotinker.modifier.gainsalone");
    }

    @Override
    public @NotNull List<ModifierEntry> displayModifiers(ModifierEntry entry) {
        return List.of(new ModifierEntry(MomotinkerModifiers.overweightingwealthsin.getId(), 1));
    }

    @Override
    public Component validate(IToolStackView tool, ModifierEntry modifier) {
        if (tool.getModifierLevel(MomotinkerModifiers.overweightingwealthsin.getId()) > 0
                && tool.getModifierLevel(MomotinkerModifiers.eternalanger.getId()) == 0
                && tool.getModifierLevel(MomotinkerModifiers.slackatmosphere.getId()) == 0
                && tool.getModifierLevel(MomotinkerModifiers.thepinnacleofarrogance.getId()) == 0
                && tool.getModifierLevel(MomotinkerModifiers.filledwithhunger.getId()) == 0
                && tool.getModifierLevel(MomotinkerModifiers.resentmentknives.getId()) == 0
                && tool.getModifierLevel(MomotinkerModifiers.forbiddenfruit.getId()) == 0
                && tool.getModifierLevel(MomotinkerModifiers.compassionateeverything.getId()) == 0) {
            return null;
        }
        return requirementsError(modifier);
    }

    @Override
    public float getRepairFactor(IToolStackView tool, ModifierEntry entry, float factor) {
        int c = tool.getPersistentData().getInt(gainsalonepoints);
        float a = (float) Math.pow(0.5, c);
        return factor*a;
    }
    @Override
    public int onDamageTool(IToolStackView tool, ModifierEntry modifier, int amount, @org.jetbrains.annotations.Nullable LivingEntity livingEntity) {
        int c = tool.getPersistentData().getInt(gainsalonepoints);
        int a = (int) Math.floor(Math.pow(1.2, c));
        return amount*a;
    }
    @Override
    public void processLoot(IToolStackView tool, ModifierEntry modifier, List<ItemStack> list, LootContext context) {
        if (!tool.isBroken()&&getRemainingDurability(tool)>1) {
            if (!list.isEmpty()
                    && context.getParamOrNull(LootContextParams.BLOCK_STATE) != null
                    && !context.getParamOrNull(LootContextParams.BLOCK_STATE).isAir()
                    && context.hasParam(LootContextParams.BLOCK_STATE)) {
                Iterator<ItemStack> iterator = list.iterator();
                while (iterator.hasNext()) {
                    int c1 = tool.getPersistentData().getInt(gainsalonepoints);
                    int a = (int) Math.floor(Math.pow(2, c1 + 1));
                    ItemStack stack = iterator.next();
                    stack.setCount(stack.getCount() * a);
                }
            }
        }
    }
    
    @Override
    public void addToolStats(IToolContext context, ModifierEntry modifier, ModifierStatsBuilder builder) {
        ModDataNBT a = (ModDataNBT) context.getPersistentData();
        int b =a.getInt(gainsalonepoints);
        double c = Math.pow(0.4,b+1);
        if (b > 0){
            ToolStats.DURABILITY.multiply(builder, c);
            ToolStats.ATTACK_SPEED.multiply(builder, c);
            ToolStats.ATTACK_DAMAGE.multiply(builder, c);
            ToolStats.ACCURACY.multiply(builder, c);
            ToolStats.DRAW_SPEED.multiply(builder, c);
            ToolStats.VELOCITY.multiply(builder, c);
            ToolStats.MINING_SPEED.multiply(builder, c);
            ToolStats.ARMOR.multiply(builder, c);
            ToolStats.ARMOR_TOUGHNESS.multiply(builder, c);
            ToolStats.PROJECTILE_DAMAGE.multiply(builder, c);
            ToolStats.KNOCKBACK_RESISTANCE.multiply(builder, c);
            ToolStats.BLOCK_AMOUNT.multiply(builder, c);
            ToolStats.BLOCK_ANGLE.multiply(builder, c);
        }
    }
    private void onEntityDeath(LivingDeathEvent event) {
        Entity a = event.getEntity();
        Entity b = event.getSource().getEntity();
        if (b instanceof Player player&&a instanceof LivingEntity){
            int c = getMainhandModifierlevel(player,MomotinkerModifiers.gainsalone.getId());
            ModDataNBT d = ToolStack.from(player.getMainHandItem()).getPersistentData();
            if (c>0){
                d.putInt(gainsalonepoints,d.getInt(gainsalonepoints)+1);
                ToolStack.from(player.getMainHandItem()).rebuildStats();
            }
        }
    }

    private void onBreakBlockEvent(BlockEvent.BreakEvent event) {
        Player player = event.getPlayer();
        if (player!=null&&event.getState()!=null&&!event.getState().isAir()){
            int c = getMainhandModifierlevel(player,MomotinkerModifiers.gainsalone.getId());
            if (c>0){
                ModDataNBT d = ToolStack.from(player.getMainHandItem()).getPersistentData();
                d.putInt(gainsalonepoints,d.getInt(gainsalonepoints)+1);
                ToolStack.from(player.getMainHandItem()).rebuildStats();
            }
        }
    }
}