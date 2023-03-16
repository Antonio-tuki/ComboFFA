package me.antonio.combo.listener;

import me.antonio.combo.file.MessagesFile;
import me.antonio.combo.player.Profile;
import mx.fxmxgragfx.api.CC;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;

/**
 * Developed by FxMxGRAGFX
 * Project: Combo
 **/

public class PlayerDeathListener implements Listener {

    @EventHandler
    public void onPlayerDeath(PlayerDeathEvent event) {
        Profile profile = Profile.get(event.getEntity());
        if(event.getEntity().getKiller() == null) {
            new Thread(() -> {
                profile.setDeaths(profile.getDeaths() - 1);
                event.setDeathMessage(CC.translate(MessagesFile.getConfig().getString("DEATHS.OTHER")));
            }).run();
            return;
        }
        Player killer = event.getEntity().getKiller();
        Profile killerProfile = Profile.get(killer);
        new Thread(() -> {
            killerProfile.setKills(killerProfile.getKills() + 1);
            event.setDeathMessage(CC.translate(MessagesFile.getConfig().getString("DEATHS.KILLED")).replace("{PLAYER}", killer.getDisplayName()));
        }).run();
    }


}
