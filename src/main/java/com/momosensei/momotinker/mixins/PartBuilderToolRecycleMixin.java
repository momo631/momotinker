package com.momosensei.momotinker.mixins;

import com.momosensei.momotinker.network.Channel;
import com.momosensei.momotinker.network.packet.ItemStackPKT;
import com.momosensei.momotinker.register.MomotinkerModifiers;
import it.unimi.dsi.fastutil.ints.IntArrayList;
import it.unimi.dsi.fastutil.ints.IntList;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import slimeknights.tconstruct.library.recipe.partbuilder.IPartBuilderContainer;
import slimeknights.tconstruct.library.recipe.partbuilder.Pattern;
import slimeknights.tconstruct.library.tools.definition.module.material.ToolPartsHook;
import slimeknights.tconstruct.library.tools.nbt.ToolStack;
import slimeknights.tconstruct.library.tools.part.IToolPart;
import slimeknights.tconstruct.tables.recipe.PartBuilderToolRecycle;

import java.util.ArrayList;
import java.util.List;

@Mixin(PartBuilderToolRecycle.class)
public abstract class PartBuilderToolRecycleMixin {

    @Inject(method = "getLeftover", at = @At("HEAD"),cancellable = true, remap = false)
    public void modifyLeftover(IPartBuilderContainer inv, Pattern pattern, CallbackInfoReturnable<ItemStack> cir) {
        ToolStack tool = ToolStack.from(inv.getStack());
        if (tool.getModifierLevel(MomotinkerModifiers.frombrilliance.getId()) > 0) {
            cir.setReturnValue(ItemStack.EMPTY);
            cir.cancel();
            List<IToolPart> parts = new ArrayList<>();
            IntList indices = new IntArrayList();
            boolean found = false;
            List<IToolPart> requirements = ToolPartsHook.parts(tool.getDefinition());
            for (int i = 0; i < requirements.size(); i++) {
                IToolPart part = requirements.get(i);
                if (found || !pattern.equals(BuiltInRegistries.ITEM.getKey(part.asItem()))) {
                    parts.add(part);
                    indices.add(i);
                } else {
                    found = true;
                }
            }
            for (int i=0;i<indices.size();i++){
                ItemStack stack=parts.get(i).withMaterial(tool.getMaterial(indices.getInt(i)).getVariant());
                Channel.sendToServer(new ItemStackPKT(stack));

            }
        }
    }

}
