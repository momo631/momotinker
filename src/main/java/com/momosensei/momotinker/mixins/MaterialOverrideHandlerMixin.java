package com.momosensei.momotinker.mixins;

import com.momosensei.momotinker.register.MomotinkerTools;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.renderer.block.model.ItemOverrides;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import slimeknights.tconstruct.library.client.model.tools.ToolModel;
import slimeknights.tconstruct.library.tools.nbt.ModDataNBT;
import slimeknights.tconstruct.library.tools.nbt.ToolStack;

import static com.momosensei.momotinker.Items.tool.nyarlathotep.nyarlathotep_disguise;
import static com.momosensei.momotinker.Items.tool.nyarlathotep.nyarlathotep_disguise_tool;
import static com.momosensei.momotinker.Modifiers.momomodifier.getToolStack;
import static com.momosensei.momotinker.Modifiers.momomodifier.isToolStack;

@Mixin(ToolModel.MaterialOverrideHandler.class)
public class MaterialOverrideHandlerMixin {

    @Inject(method = "resolve", at = @At("RETURN"), cancellable = true)
    private void nyarlathotep$resolveDisguise(BakedModel originalModel, ItemStack stack,
                                              ClientLevel world, LivingEntity entity, int seed,
                                              CallbackInfoReturnable<BakedModel> cir) {
       if (stack.isEmpty()||!isToolStack(stack)) {
            return;
        }
        ToolStack originalStack = getToolStack(stack);
        if (originalStack == null||!stack.is(MomotinkerTools.nyarlathotep.get())) {
            return;
        }
        ModDataNBT data = originalStack.getPersistentData();
        if (data.getFloat(nyarlathotep_disguise) == 0) {
            return;
        }

        CompoundTag toolTag = data.getCompound(nyarlathotep_disguise_tool);
        if (toolTag.isEmpty()) {
            return;
        }

        ItemStack disguised = ItemStack.of(toolTag);
        if (disguised.isEmpty()) {
            return;
        }

        BakedModel disguisedBase = Minecraft.getInstance()
                .getItemRenderer()
                .getModel(disguised, world, entity, seed);

        if (disguisedBase == Minecraft.getInstance().getModelManager().getMissingModel()) {
            return;
        }

        ItemOverrides overrides = disguisedBase.getOverrides();
        BakedModel finalModel;
        if (overrides != ItemOverrides.EMPTY) {
            finalModel = overrides.resolve(disguisedBase, disguised, world, entity, seed);
        } else {
            finalModel = disguisedBase;
        }

        if (finalModel != null) {
            cir.setReturnValue(finalModel);
        }
    }
}
