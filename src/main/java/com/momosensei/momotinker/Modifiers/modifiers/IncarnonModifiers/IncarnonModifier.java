package com.momosensei.momotinker.Modifiers.modifiers.IncarnonModifiers;

import com.momosensei.momotinker.Modifiers.momomodifier;
import com.momosensei.momotinker.Momotinker;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import slimeknights.tconstruct.library.modifiers.Modifier;
import slimeknights.tconstruct.library.tools.nbt.IToolStackView;

import javax.annotation.Nullable;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class IncarnonModifier extends momomodifier {

    public static final ResourceLocation incarnon_phase = Momotinker.getResource("incarnon_phase");
    public static final ResourceLocation incarnon_a = Momotinker.getResource("incarnon_a");
    public static final ResourceLocation incarnon_b = Momotinker.getResource("incarnon_b");
    public static final ResourceLocation incarnon_c = Momotinker.getResource("incarnon_c");
    public static final ResourceLocation incarnon_d = Momotinker.getResource("incarnon_d");

    @Override
    public boolean isNoLevels() {
        return true;
    }
    @Override
    public @Nullable Component onRemoved(IToolStackView iToolStackView, Modifier modifier) {
        iToolStackView.getPersistentData().remove(incarnon_a);
        iToolStackView.getPersistentData().remove(incarnon_b);
        iToolStackView.getPersistentData().remove(incarnon_c);
        iToolStackView.getPersistentData().remove(incarnon_d);
        return null;
    }

    private static final Map<Class<? extends IncarnonModifier>, Map<String, Component>> ALL_MODIFIER_TEXTS = new HashMap<>();

    private static final Map<String, Component> DEFAULT_TEXTS = createDefaultTexts();

    static {
        registerModifierTexts(IncarnonModifier.class, DEFAULT_TEXTS);
    }

    /**
     * @param modifierClass 修改器的Class对象
     * @param texts 文本映射（Key: 文本标识符, Value: 显示文本）
     */
    protected static void registerModifierTexts(Class<? extends IncarnonModifier> modifierClass, Map<String, Component> texts) {
        ALL_MODIFIER_TEXTS.put(modifierClass, texts);
    }

    public static Map<String, Component> getTextsForClass(Class<? extends IncarnonModifier> clazz) {
        Map<String, Component> texts = ALL_MODIFIER_TEXTS.get(clazz);
        return new HashMap<>(Objects.requireNonNullElse(texts, DEFAULT_TEXTS));
    }

    private static Map<String, Component> createDefaultTexts() {
        Map<String, Component> texts = new HashMap<>();
        texts.put("phase_1_ability_1", Component.empty());
        texts.put("phase_1_ability_2", Component.empty());

        texts.put("phase_2_ability_1", Component.empty());
        texts.put("phase_2_ability_2", Component.empty());

        texts.put("phase_3_ability_1", Component.empty());
        texts.put("phase_3_ability_2", Component.empty());

        texts.put("phase_4_ability_1", Component.empty());
        texts.put("phase_4_ability_2", Component.empty());
        texts.put("phase_4_ability_3", Component.empty());
        return texts;
    }
}
