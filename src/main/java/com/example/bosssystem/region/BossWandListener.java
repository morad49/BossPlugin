package com.example.bosssystem.region;

import com.example.bosssystem.BossSystem;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

public class BossWandListener implements Listener {

    private final BossSystem plugin;

    public BossWandListener(BossSystem plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onWandInteract(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        ItemStack hand = player.getInventory().getItemInMainHand();

        if (hand.getType() == Material.BLAZE_ROD && hand.hasItemMeta() && hand.getItemMeta().getDisplayName().contains("Boss Selection Wand")) {
            if (event.getClickedBlock() == null) return;
            event.setCancelled(true);

            if (event.getAction() == Action.LEFT_CLICK_BLOCK) {
                plugin.getRegionManager().setPos1(player.getUniqueId(), event.getClickedBlock().getLocation());
                player.sendMessage(plugin.getMessageManager().colorize("&aPosition 1 set to: " + event.getClickedBlock().getX() + ", " + event.getClickedBlock().getY() + ", " + event.getClickedBlock().getZ()));
            } else if (event.getAction() == Action.RIGHT_CLICK_BLOCK) {
                plugin.getRegionManager().setPos2(player.getUniqueId(), event.getClickedBlock().getLocation());
                player.sendMessage(plugin.getMessageManager().colorize("&aPosition 2 set to: " + event.getClickedBlock().getX() + ", " + event.getClickedBlock().getY() + ", " + event.getClickedBlock().getZ()));
            }
        }
    }
}
