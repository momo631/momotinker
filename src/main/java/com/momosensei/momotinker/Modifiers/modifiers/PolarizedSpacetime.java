package com.momosensei.momotinker.Modifiers.modifiers;

import com.momosensei.momotinker.Modifiers.momomodifier;
import com.momosensei.momotinker.Momotinker;
import com.momosensei.momotinker.mobs.CapStorageData;
import com.momosensei.momotinker.mobs.SerialClass;
import com.momosensei.momotinker.register.MomotinkerModifiers;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import slimeknights.tconstruct.library.modifiers.Modifier;
import slimeknights.tconstruct.library.tools.nbt.IToolStackView;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.function.Supplier;

import static com.momosensei.momotinker.mobs.CapStorageData.cast;


public class PolarizedSpacetime extends momomodifier {
    public PolarizedSpacetime() {
    }
    public static final ResourceLocation polarized = Momotinker.getResource("polarized");
    @Override
    public Component onRemoved(IToolStackView iToolStackView, Modifier modifier) {
        iToolStackView.getPersistentData().remove(polarized);
        return null;
    }
    @Override
    public void OnLivingHurt(LivingHurtEvent event) {
        Data data = getOrCreateData(polarized, Data::new);
        String id = event.getSource().getMsgId();
        if (event.getEntity() instanceof Player player&&getAllModifierlevel(player, MomotinkerModifiers.polarizedspacetime.getId())>0) {
            int a = getAllModifierlevel(player, MomotinkerModifiers.polarizedspacetime.getId());
            if (data.memory.contains(id)) {
                data.memory.remove(id);
                data.memory.add(0, id);
                int val = data.level.compute(id, (k, old) -> old == null ? 1 : old + 1);
                double b = 0.9-0.05*a;
                if (b<0.4)b=0.4;
                double factor = Math.pow(b, val - 1);
                event.setAmount((float) (event.getAmount() * factor));
            } else {
                data.memory.add(0, id);
                data.level.put(id, 1);
                int b =getAllModifierAmount(player,MomotinkerModifiers.polarizedspacetime.getId());
                if (data.memory.size() > b) {
                    String old = data.memory.remove(data.memory.size() - 1);
                    data.level.remove(old);
                }
            }
        }
    }

    @SerialClass.SerialField
    private final HashMap<ResourceLocation, CapStorageData> data = new HashMap<>();
    public <T extends CapStorageData> T getOrCreateData(ResourceLocation id, Supplier<T> sup) {
        return cast(data.computeIfAbsent(id, e -> sup.get()));
    }
    @Nullable
    public <T extends CapStorageData> T getData(ResourceLocation id) {
        return cast(this.data.get(id));
    }
    @SerialClass
    public static class Data extends CapStorageData {

        @SerialClass.SerialField
        public final ArrayList<String> memory = new ArrayList<>();

        @SerialClass.SerialField
        public final HashMap<String, Integer> level = new HashMap<>();

    }
}