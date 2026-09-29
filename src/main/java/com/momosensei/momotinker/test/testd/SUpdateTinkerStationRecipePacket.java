package com.momosensei.momotinker.test.testd;
/*
import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.Level;
import net.minecraftforge.network.NetworkEvent.Context;
import slimeknights.mantle.network.packet.IThreadsafePacket;
import slimeknights.mantle.recipe.helper.RecipeHelper;
import slimeknights.mantle.util.BlockEntityHelper;
import slimeknights.tconstruct.library.recipe.tinkerstation.ITinkerStationRecipe;

import java.util.Optional;

public class SUpdateTinkerStationRecipePacket implements IThreadsafePacket {
  private final BlockPos pos;
  private final ResourceLocation recipe;
  public SUpdateTinkerStationRecipePacket(BlockPos pos, ITinkerStationRecipe recipe) {
    this.pos = pos;
    this.recipe = recipe.getId();
  }

  public SUpdateTinkerStationRecipePacket(FriendlyByteBuf buffer) {
    this.pos = buffer.readBlockPos();
    this.recipe = buffer.readResourceLocation();
  }

  @Override
  public void encode(FriendlyByteBuf buffer) {
    buffer.writeBlockPos(pos);
    buffer.writeResourceLocation(recipe);
  }

  @Override
  public void handleThreadsafe(Context context) {
    HandleClient.handle(this);
  }

  private static class HandleClient {
    private static void handle(SUpdateTinkerStationRecipePacket packet) {
      Level world = Minecraft.getInstance().level;
      if (world != null) {
        Optional<ITinkerStationRecipe> recipe = RecipeHelper.getRecipe(world.getRecipeManager(), packet.recipe, ITinkerStationRecipe.class);

        // if the screen is open, use that to get the TE and update the screen
        boolean handled = false;
        if (Minecraft.getInstance().screen instanceof STinkerStationScreen stationScreen) {
          STinkerStationBlockEntity te = stationScreen.getTileEntity();
            if (te != null && te.getBlockPos().equals(packet.pos)) {
                recipe.ifPresent(te::updateRecipe);
                stationScreen.updateDisplay();
                handled = true;
            }
        }
        // if the wrong screen is open or no screen, use the tile directly
        if (!handled) {
          recipe.ifPresent(r -> BlockEntityHelper.get(STinkerStationBlockEntity.class, world, packet.pos).ifPresent(te -> te.updateRecipe(r)));
        }
      }
    }
  }
}

 */
