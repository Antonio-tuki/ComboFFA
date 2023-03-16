package me.antonio.combo.player;

import lombok.Getter;
import lombok.Setter;
import me.antonio.combo.clan.ChatType;
import me.antonio.combo.clan.Clan;
import me.antonio.combo.file.ProfilesFile;
import mx.fxmxgragfx.api.CC;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.configuration.ConfigurationSection;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Developed by FxMxGRAGFX
 * Project: Combo
 **/


@Getter
@Setter
public class Profile {

    private static Map<UUID, Profile> profiles = new HashMap<>();

    private UUID uuid;
    private PlayerState playerState = PlayerState.SPAWN;
    private int kills = 0;
    private int deaths = 0;
    private double kdr = kills / deaths;
    private Clan clan;
    private ChatType chatType = ChatType.PUBLIC;

    private Profile(UUID uuid) {
        if(!profiles.containsKey(uuid)) {
            profiles.put(uuid, this);
            this.uuid = uuid;
        }
    }

    public static Profile get(OfflinePlayer player) {
        if(profiles.containsKey(player.getUniqueId())) {
            return profiles.get(player.getUniqueId());
        } else {
            return new Profile(player.getUniqueId());
        }
    }

    public static void saveAll() {
        AtomicInteger saved = new AtomicInteger(0);
        if(!profiles.isEmpty()) {
            profiles.keySet().forEach(uuid -> {
                ProfilesFile.getConfig().set("PROFILES." + uuid.toString(), profiles.get(uuid));
                saved.getAndIncrement();
            });
        }
        Bukkit.getConsoleSender().sendMessage(CC.translate("&aSuccessfully saved " + saved + " profiles!"));
    }

    public static void loadAll() {
        AtomicInteger loaded = new AtomicInteger(0);
        ConfigurationSection section = ProfilesFile.getConfig().getConfigurationSection("PROFILES");
        if(section != null) {
            section.getKeys(false).forEach(s -> {
                profiles.put(UUID.fromString(s), (Profile) ProfilesFile.getConfig().get("PROFILES." + s));
                loaded.getAndIncrement();
            });
        }
        Bukkit.getConsoleSender().sendMessage(CC.translate("&aSuccessfully loaded " + loaded + " profiles!"));
    }

}
