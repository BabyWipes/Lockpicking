package com.huskehhh.lockpick;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;
import java.util.Random;

public class Lockpicking extends JavaPlugin implements Listener {

    YamlConfiguration config = YamlConfiguration.loadConfiguration(new File("plugins/Lockpick/config.yml"));

    public void onEnable() {
        createConfig();
        getServer().getPluginManager().registerEvents(this, this);
    }

    public void onDisable() {

    }

    @EventHandler
    public void onInteract(PlayerInteractEvent e) {
        Player p = e.getPlayer();

        if (p.hasPermission("lockpick.use")) {

            if (e.getAction() == Action.RIGHT_CLICK_BLOCK) {

                Block b = e.getClickedBlock();

                if (b.getType() == Material.CHEST && p.getItemInHand().getTypeId() == config.getInt("Lockpick-ID")) {

                    Random rand = new Random(config.getInt("Lockpick-Chance"));

                    int r = rand.nextInt();

                    if (r <= 4) {
                        p.sendMessage(ChatColor.GREEN + "The lock fell off!");
                        e.setCancelled(false);
                    } else {

                        p.sendMessage(ChatColor.RED + "Your hands jammed in the lock!");

                        if (p.getItemInHand().getAmount() != 0) {
                            p.getItemInHand().setAmount(p.getItemInHand().getAmount() - 1);
                        } else {
                            p.getInventory().remove(p.getItemInHand());
                        }

                        double h = p.getHealth();
                        p.setHealth(h - 4);

                        e.setCancelled(true);
                    }

                }

            }

        }

    }

    private void createConfig() {
        boolean exists = (new File("plugins/Lockpick/config.yml")).exists();
        if (!exists) {
            (new File("plugins/Lockpick")).mkdir();

            config.options().header("Lockpick, made by Husky!");
            config.set("Lockpick-ID", Integer.valueOf(274));
            config.set("Lockpick-Chance", 10);

            try {
                config.save("plugins/Lockpick/config.yml");
            } catch (IOException var3) {
                var3.printStackTrace();
            }
        }

    }

}
