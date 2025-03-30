package com.momosensei.momotinker.tool;

import com.momosensei.momotinker.event.ModEventListener;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.geom.EntityModelSet;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.client.extensions.common.IClientItemExtensions;
import slimeknights.tconstruct.library.tools.definition.ModifiableArmorMaterial;
import slimeknights.tconstruct.library.tools.definition.ToolDefinition;
import slimeknights.tconstruct.library.tools.item.armor.ModifiableArmorItem;

import javax.annotation.Nullable;
import java.util.function.Consumer;

public class aa extends ModifiableArmorItem {
    private final ResourceLocation name;
    public aa(ModifiableArmorMaterial materialIn, EquipmentSlot slot, Properties builderIn, ToolDefinition toolDefinition) {
        super(materialIn, slot, builderIn, toolDefinition);
        this.name = materialIn.getId();
    }

    @Nullable
    @Override
    public String getArmorTexture(ItemStack stack, Entity entity, EquipmentSlot slot, String type) {
         return getDummyArmorTexture(slot);
    }
    public static final String HEAD_A = new ResourceLocation("momotinker", "textures/item/tool/aa/helmet/part_1_"+".png").toString();
    public static final String HEAD_B = new ResourceLocation("momotinker", "textures/item/tool/aa/helmet/part_2_"+".png").toString();

    public static String getDummyArmorTexture(EquipmentSlot slot) {
        return slot == EquipmentSlot.HEAD ? HEAD_A : HEAD_B;
    }

    @Override
    public void initializeClient(Consumer<IClientItemExtensions> consumer) {
        consumer.accept(ArmorRender.INSTANCE);
    }
    private static final class ArmorRender implements IClientItemExtensions {
        private static final ArmorRender INSTANCE = new ArmorRender();
        private static HumanoidModel<?> MODEL;

        @Override
        public HumanoidModel<?> getHumanoidArmorModel(LivingEntity entityLiving, ItemStack itemStack, EquipmentSlot armorSlot, HumanoidModel<?> _default) {
            if (MODEL == null) {
                EntityModelSet models = Minecraft.getInstance().getEntityModels();
                ModelPart root = models.bakeLayer(ModEventListener.HELM_LAYER);
                MODEL = new HelmModel<>(root);
            }
            return MODEL;
        }
    }

}
