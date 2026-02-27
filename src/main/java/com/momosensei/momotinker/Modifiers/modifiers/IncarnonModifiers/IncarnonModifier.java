package com.momosensei.momotinker.Modifiers.modifiers.IncarnonModifiers;

import com.momosensei.momotinker.Modifiers.momomodifier;
import com.momosensei.momotinker.Momotinker;
import com.momosensei.momotinker.network.Channel;
import com.momosensei.momotinker.network.packet.HudCharge.IncarnonTimeCharge;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.UseAnim;
import net.minecraft.world.level.Level;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import org.jetbrains.annotations.NotNull;
import slimeknights.tconstruct.library.modifiers.Modifier;
import slimeknights.tconstruct.library.modifiers.ModifierEntry;
import slimeknights.tconstruct.library.modifiers.hook.interaction.GeneralInteractionModifierHook;
import slimeknights.tconstruct.library.modifiers.hook.interaction.InteractionSource;
import slimeknights.tconstruct.library.tools.nbt.IToolStackView;
import slimeknights.tconstruct.library.tools.nbt.ModDataNBT;
import slimeknights.tconstruct.library.tools.nbt.ToolStack;

import javax.annotation.Nullable;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class IncarnonModifier extends momomodifier {
    public IncarnonModifier() {
        super();
    }
    public static final ResourceLocation incarnon_phase = Momotinker.getResource("incarnon_phase");
    public static final ResourceLocation incarnon_a = Momotinker.getResource("incarnon_a");
    public static final ResourceLocation incarnon_b = Momotinker.getResource("incarnon_b");
    public static final ResourceLocation incarnon_c = Momotinker.getResource("incarnon_c");
    public static final ResourceLocation incarnon_d = Momotinker.getResource("incarnon_d");

    public static final ResourceLocation incarnon_task_phase = Momotinker.getResource("incarnon_task_phase");
    public static final ResourceLocation incarnon_task_goal = Momotinker.getResource("incarnon_task_goal");

    public static final ResourceLocation incarnon_energy = Momotinker.getResource("incarnon_energy");
    public static final ResourceLocation is_incarnon = Momotinker.getResource("is_incarnon");
    public static final ResourceLocation can_incarnon = Momotinker.getResource("can_incarnon");

    public int incarnon_task_a = 0;
    public int incarnon_task_b = 0;
    public int incarnon_task_c = 0;
    public int incarnon_task_d = 0;
    public int incarnon_energy_max = 0;

    public int use_time = 0;

    private final Map<UUID, Integer> incarnon_charge = new ConcurrentHashMap<>();

    @Override
    public boolean isNoLevels() {
        return true;
    }

    @Override
    public @Nullable Component onRemoved(IToolStackView iToolStackView, Modifier modifier) {
        iToolStackView.getPersistentData().remove(incarnon_phase);
        iToolStackView.getPersistentData().remove(incarnon_a);
        iToolStackView.getPersistentData().remove(incarnon_b);
        iToolStackView.getPersistentData().remove(incarnon_c);
        iToolStackView.getPersistentData().remove(incarnon_d);

        iToolStackView.getPersistentData().remove(incarnon_task_phase);
        iToolStackView.getPersistentData().remove(incarnon_task_goal);

        iToolStackView.getPersistentData().remove(incarnon_energy);
        iToolStackView.getPersistentData().remove(is_incarnon);
        return null;
    }
    @Override
    public void OnEntityDeath(LivingDeathEvent event) {
        super.OnEntityDeath(event);
    }
    @Override
    public void OnLivingHurt(LivingHurtEvent event) {
        super.OnLivingHurt(event);
    }

    @Override
    public void onInventoryTick(IToolStackView tool, ModifierEntry modifier, Level world, LivingEntity entity, int index, boolean isSelected, boolean isCorrectSlot, ItemStack stack) {
        ModDataNBT data = tool.getPersistentData();
        if (!world.isClientSide) {
            if (data.getInt(incarnon_phase) < 4) {
                if (data.getInt(incarnon_task_goal)!=getTaskGoal(data.getInt(incarnon_phase))){
                    data.putInt(incarnon_task_goal, getTaskGoal(data.getInt(incarnon_phase)));
                }
                if (data.getInt(incarnon_task_phase) >= data.getInt(incarnon_task_goal)) {
                    data.putInt(incarnon_phase, data.getInt(incarnon_phase) + 1);
                    data.putInt(incarnon_task_phase, 0);
                    data.putInt(incarnon_task_goal, 0);
                }
                removeIncarnon(tool);
            }
            if (data.getFloat(incarnon_energy)>this.incarnon_energy_max){
                data.putFloat(incarnon_energy,this.incarnon_energy_max);
            }
            if (data.getFloat(incarnon_energy)<0){
                data.putFloat(incarnon_energy,0);
            }
            if (data.getFloat(incarnon_energy) > 0 && data.getBoolean(is_incarnon) && entity.tickCount % 20 == 0){
                data.putFloat(incarnon_energy,data.getFloat(incarnon_energy)-1);
            }
            if (data.getBoolean(can_incarnon)&&data.getFloat(incarnon_energy)==0){
                data.putBoolean(can_incarnon,false);
            }
            isincarnon(tool,stack,data.getBoolean(can_incarnon));
        }
        float perc = Mth.clamp(data.getFloat(incarnon_energy) / 120, 0, 1);
        int currentStage = (int)Math.floor(perc * 10);
        if (entity instanceof ServerPlayer player1&&stack == player1.getMainHandItem()) {
            UUID playerId = player1.getUUID();
            if (incarnon_charge.getOrDefault(playerId, -1) != currentStage) {
                Channel.sendToPlayer(new IncarnonTimeCharge(perc), player1);
                incarnon_charge.put(playerId, currentStage);
            }
        }
    }
    private int getTaskGoal(int phase) {
        return switch (phase) {
            case 1 ->incarnon_task_b;
            case 2 ->incarnon_task_c;
            case 3 ->incarnon_task_d;
            default -> incarnon_task_a;
        };
    }
    private void removeIncarnon(IToolStackView tool){
        ModDataNBT a = tool.getPersistentData();
        if (a.getInt(incarnon_phase)<1&&a.getInt(incarnon_a)!=0){
            a.remove(incarnon_a);
        }
        if (a.getInt(incarnon_phase)<2&&a.getInt(incarnon_b)!=0){
            a.remove(incarnon_b);
        }
        if (a.getInt(incarnon_phase)<3&&a.getInt(incarnon_c)!=0){
            a.remove(incarnon_c);
        }
        if (a.getInt(incarnon_phase)<4&&a.getInt(incarnon_d)!=0){
            a.remove(incarnon_d);
        }
    }

    private void isincarnon(IToolStackView tool,ItemStack stack,boolean a){
        if (tool.getPersistentData().getBoolean(is_incarnon)!=a) {
            tool.getPersistentData().putBoolean(is_incarnon, a);
            ToolStack.from(stack).rebuildStats();
        }
    }
    @Override
    public @NotNull InteractionResult onToolUse(IToolStackView tool, ModifierEntry modifier, Player player, InteractionHand interactionHand, InteractionSource interactionSource) {
        if (interactionSource==InteractionSource.RIGHT_CLICK){
            GeneralInteractionModifierHook.startUsing(tool, modifier.getId(), player, interactionHand);
            return InteractionResult.CONSUME;
        }
        else return InteractionResult.PASS;
    }
    @Override
    public @NotNull UseAnim getUseAction(IToolStackView tool, ModifierEntry modifier) {
        return UseAnim.BOW;
    }
    @Override
    public int getUseDuration(IToolStackView tool, ModifierEntry modifier) {
        if (tool.getPersistentData().getBoolean(is_incarnon)){
            return (int) (this.use_time*1.5f);
        }
        return this.use_time;
    }
    @Override
    public void onFinishUsing(IToolStackView tool, ModifierEntry modifier, LivingEntity entity) {
        if (entity instanceof Player){
            if (!tool.getPersistentData().getBoolean(is_incarnon)&&tool.getPersistentData().getFloat(incarnon_energy)>0){
                tool.getPersistentData().putBoolean(can_incarnon,true);
            }
            if (tool.getPersistentData().getBoolean(is_incarnon)){
                tool.getPersistentData().putBoolean(can_incarnon,false);
                tool.getPersistentData().putFloat(incarnon_energy,0);
            }
        }
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

        texts.put("phase_task_1", Component.empty());
        texts.put("phase_task_2", Component.empty());
        texts.put("phase_task_3", Component.empty());
        texts.put("phase_task_4", Component.empty());

        texts.put("incarnon_on", Component.empty());

        return texts;
    }
}
