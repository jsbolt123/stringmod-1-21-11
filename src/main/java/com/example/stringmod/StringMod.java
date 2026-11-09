package com.example.stringmod;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.minecraft.commands.Commands;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;

public class StringMod implements ModInitializer {

    @Override
    public void onInitialize() {
        CommandRegistrationCallback.EVENT.register((dispatcher, registryAccess, environment) ->
                // No .requires(...) here, so every player (even non-ops) can run it.
                dispatcher.register(Commands.literal("string").executes(context -> {
                    ServerPlayer player = context.getSource().getPlayerOrException();
                    Inventory inventory = player.getInventory();

                    int filled = 0;
                    // Slots 0-35 are the hotbar + main inventory. Armor and off-hand are left alone.
                    for (int i = 0; i < 36; i++) {
                        if (inventory.getItem(i).isEmpty()) {
                            inventory.setItem(i, new ItemStack(Items.STRING, 64));
                            filled++;
                        }
                    }

                    inventory.setChanged();
                    player.inventoryMenu.broadcastChanges();

                    final int count = filled;
                    if (count == 0) {
                        context.getSource().sendSuccess(() -> Component.literal("Your inventory has no empty slots."), false);
                    } else {
                        context.getSource().sendSuccess(
                                () -> Component.literal("Filled " + count + " empty slot" + (count == 1 ? "" : "s") + " with string."),
                                false);
                    }
                    return count;
                })));
    }
}
