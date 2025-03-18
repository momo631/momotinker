package com.momosensei.momotinker.register;


import com.momosensei.momotinker.Momotinker;
import slimeknights.tconstruct.library.tools.definition.ToolDefinition;
import slimeknights.tconstruct.library.tools.stat.FloatToolStat;
import slimeknights.tconstruct.library.tools.stat.ToolStatId;
import slimeknights.tconstruct.library.tools.stat.ToolStats;

public class MomotinkerToolDefinitions {
    public MomotinkerToolDefinitions() {
    }
    public static final FloatToolStat SLASH_COLOR = ToolStats.register(new FloatToolStat(name("slash_color"), -3135232, 0.0F, 0.0F, 11));
    public static final FloatToolStat SCALE = ToolStats.register(new FloatToolStat(name("scale"), -3135232, 1.0F, 0.0F, Integer.MAX_VALUE));

    private static ToolStatId name(String name) {
        return new ToolStatId(Momotinker.MOD_ID, name);
    }
    public static final ToolDefinition TRIGGER_BLADE = ToolDefinition.create(MomotinkerItem.trigger_blade);
    public static final ToolDefinition DIVINE_PUNISHMENT_SPEAR = ToolDefinition.create(MomotinkerItem.divine_punishment_spear);
    public static final ToolDefinition ENTROPY_BURNING_CUBE = ToolDefinition.create(MomotinkerItem.entropy_burning_cube);
    public static final ToolDefinition ENTROPY_BURNING_SWORD = ToolDefinition.create(MomotinkerItem.entropy_burning_sword);
    public static final ToolDefinition ENTROPY_BURNING_RIDING_SPEAR = ToolDefinition.create(MomotinkerItem.entropy_burning_riding_spear);
    public static final ToolDefinition ENTROPY_BURNING_CANNON = ToolDefinition.create(MomotinkerItem.entropy_burning_cannon);
    public static final ToolDefinition ECLIPSE_CONTAINER = ToolDefinition.create(MomotinkerItem.eclipse_container);
    public static final ToolDefinition CORONAL_KEY = ToolDefinition.create(MomotinkerItem.coronal_key);
    public static final ToolDefinition MOON_LOCK = ToolDefinition.create(MomotinkerItem.moon_lock);

}
