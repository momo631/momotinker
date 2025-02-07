package com.momosensei.momotinker.tool;


import com.momosensei.momotinker.Momotinker;
import com.momosensei.momotinker.register.MomotinkerItem;
import slimeknights.tconstruct.library.tools.definition.ToolDefinition;
import slimeknights.tconstruct.library.tools.stat.FloatToolStat;
import slimeknights.tconstruct.library.tools.stat.ToolStatId;
import slimeknights.tconstruct.library.tools.stat.ToolStats;

public class MomoToolDefinitions {
    public MomoToolDefinitions() {
    }
    public static final FloatToolStat SLASH_COLOR = ToolStats.register(new FloatToolStat(name("slash_color"), -3135232, 0.0F, 0.0F, 11));

    private static ToolStatId name(String name) {
        return new ToolStatId(Momotinker.MOD_ID, name);
    }
    public static final ToolDefinition TROGGER_BLADE = ToolDefinition.create(MomotinkerItem.trigger_blade);
}
