package com.momosensei.momotinker.tool;

import com.momosensei.momotinker.event.ClientEvent;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.Model;
import net.minecraft.client.model.geom.EntityModelSet;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.client.extensions.common.IClientItemExtensions;
import slimeknights.tconstruct.library.client.armor.ArmorModelManager;
import slimeknights.tconstruct.library.client.armor.MultilayerArmorModel;
import slimeknights.tconstruct.library.tools.definition.ModifiableArmorMaterial;
import slimeknights.tconstruct.library.tools.item.armor.ModifiableArmorItem;
import slimeknights.tconstruct.tools.item.ArmorSlotType;

import javax.annotation.Nonnull;
import java.util.function.Consumer;

public class aa extends ModifiableArmorItem {
    private final ResourceLocation name;
    public aa(ModifiableArmorMaterial material, ArmorSlotType slot, Properties builderIn) {
        super(material, slot, builderIn);
        this.name = material.getId();
    }

    @Override
    public void initializeClient(Consumer<IClientItemExtensions> consumer) {
        consumer.accept(new ArmorModelManager.ArmorModelDispatcher() {
            @Override
            protected ResourceLocation getName() {
                return name;
            }
            @Nonnull
            @Override
            public Model getGenericArmorModel(LivingEntity living, ItemStack stack, EquipmentSlot slot, HumanoidModel<?> original) {
                return MultilayerArmorModel.INSTANCE.setup(living, stack, slot, ArmorRender.MODEL, this.getModel(stack));
            }
        });
        //consumer.accept(ArmorRender.INSTANCE);
    }

    private static final class ArmorRender implements IClientItemExtensions {
        private static final ArmorRender INSTANCE = new ArmorRender();
        private static HumanoidModel<?> MODEL;

        @Override
        public HumanoidModel<?> getHumanoidArmorModel(LivingEntity entityLiving, ItemStack itemStack, EquipmentSlot armorSlot, HumanoidModel<?> _default) {
            if (MODEL == null) {
                EntityModelSet models = Minecraft.getInstance().getEntityModels();
                ModelPart root = models.bakeLayer(ClientEvent.HELM_LAYER);
                MODEL = new HelmModel<>(root);
            }
            return MODEL;
        }

    }

}
