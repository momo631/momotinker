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

    private static ToolStatId name(String name) {
        return new ToolStatId(Momotinker.MOD_ID, name);
    }
    public static final ToolDefinition TRIGGER_BLADE = ToolDefinition.create(MomotinkerTools.trigger_blade);

}
