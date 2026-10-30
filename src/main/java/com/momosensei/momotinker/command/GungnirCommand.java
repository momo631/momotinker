package com.momosensei.momotinker.command;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import com.momosensei.momotinker.Items.tool.gungnir.gungnir_disabled_item;
import com.momosensei.momotinker.data.gungnir.GungnirMarkedData;
import com.momosensei.momotinker.data.gungnir.GungnirState;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.arguments.EntityArgument;
import net.minecraft.network.chat.Component;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.item.ItemStack;

import java.util.UUID;

public final class GungnirCommand {

    private GungnirCommand() {}

    public static void register(CommandDispatcher<CommandSourceStack> dispatcher) {
        LiteralArgumentBuilder<CommandSourceStack> root = Commands.literal("gungnir")
                .requires(src -> src.hasPermission(2));

        // /gungnir recover —— 强制清理当前玩家所有 gungnir 状态并还原占位物
        root.then(Commands.literal("recover")
                .executes(GungnirCommand::recoverSelf));

        // /gungnir recover <player> —— 对指定玩家执行（需 OP）
        root.then(Commands.literal("recover")
                .then(Commands.argument("target", EntityArgument.player())
                        .executes(GungnirCommand::recoverOther)));

        // /gungnir clear —— 只清状态，不还原物品（调试用）
        root.then(Commands.literal("clear")
                .executes(GungnirCommand::clearSelf));

        dispatcher.register(root);
    }

    private static int recoverSelf(CommandContext<CommandSourceStack> ctx) {
        CommandSourceStack src = ctx.getSource();
        ServerPlayer sp;
        try {
            sp = src.getPlayerOrException();
        } catch (Exception e) {
            src.sendFailure(Component.literal("该命令必须由玩家执行"));
            return 0;
        }
        int n = forceRecover(sp);
        src.sendSuccess(
                () -> Component.literal("已恢复 " + n + " 个禁用物品，并清理 gungnir 状态"),
                false);
        return 1;
    }

    private static int recoverOther(CommandContext<CommandSourceStack> ctx) {
        CommandSourceStack src = ctx.getSource();
        ServerPlayer target;
        try {
            target = EntityArgument.getPlayer(ctx, "target");
        } catch (Exception e) {
            src.sendFailure(Component.literal("未找到目标玩家"));
            return 0;
        }
        int n = forceRecover(target);
        String name = target.getGameProfile().getName();
        src.sendSuccess(
                () -> Component.literal("已为 " + name + " 恢复 " + n + " 个禁用物品"),
                true);
        return 1;
    }

    private static int clearSelf(CommandContext<CommandSourceStack> ctx) {
        CommandSourceStack src = ctx.getSource();
        ServerPlayer sp;
        try {
            sp = src.getPlayerOrException();
        } catch (Exception e) {
            src.sendFailure(Component.literal("该命令必须由玩家执行"));
            return 0;
        }
        forceClearState(sp);
        src.sendSuccess(() -> Component.literal("已清理 gungnir 状态"), false);
        return 1;
    }

    public static void forceClearState(ServerPlayer sp) {
        MinecraftServer server = sp.getServer();
        if (server == null) return;
        UUID id = sp.getUUID();
        GungnirMarkedData data = GungnirMarkedData.get(server);
        data.clearSlot(id);
        data.clearMark(id);
        GungnirState.clearItemNbt(sp);
    }

    public static int forceRecover(ServerPlayer sp) {
        MinecraftServer server = sp.getServer();
        if (server == null) return 0;

        int restored = 0;
        var inv = sp.getInventory();
        for (int i = 0; i < inv.getContainerSize(); i++) {
            ItemStack stack = inv.getItem(i);
            if (!(stack.getItem() instanceof gungnir_disabled_item)) continue;

            ItemStack original = GungnirState.extractOriginal(stack);
            if (original != null && !original.isEmpty()) {
                inv.setItem(i, original);
                restored++;
            } else {
                inv.setItem(i, ItemStack.EMPTY);
            }
        }
        for (InteractionHand hand : InteractionHand.values()) {
            ItemStack inHand = sp.getItemInHand(hand);
            if (!(inHand.getItem() instanceof gungnir_disabled_item)) continue;
            ItemStack original = GungnirState.extractOriginal(inHand);
            if (original != null && !original.isEmpty()) {
                sp.setItemInHand(hand, original);
                restored++;
            } else {
                sp.setItemInHand(hand, ItemStack.EMPTY);
            }
        }

        forceClearState(sp);
        syncInventory(sp);
        return restored;
    }

    private static void syncInventory(ServerPlayer sp) {
        sp.getInventory().setChanged();
        sp.containerMenu.broadcastChanges();
    }
}
