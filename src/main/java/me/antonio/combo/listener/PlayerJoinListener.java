package me.antonio.combo.listener;

import me.antonio.combo.player.Profile;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

/**
 * Developed by FxMxGRAGFX
 * Project: Combo
 **/

public class PlayerJoinListener implements Listener {

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onPlayerJoin(PlayerJoinEvent event) {
        new Thread(() -> {
            Profile profile = Profile.get(event.getPlayer());
        }).run();
    }
}
