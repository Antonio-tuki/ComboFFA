package me.antonio.combo.clan;

import lombok.Getter;
import lombok.Setter;
import me.antonio.combo.file.ClansFile;
import me.antonio.combo.file.MessagesFile;
import mx.fxmxgragfx.api.CC;
import org.bukkit.Bukkit;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Developed by FxMxGRAGFX
 * Project: Combo
 **/

@Getter
@Setter
public class Clan {

    private static Map<String, Clan> clans = new HashMap<>();
    
    private String name;
    private Player leader;
    private List<Player> captains;
    private List<Player> members;

    private Clan(String name) {
        if(!clans.containsKey(name)) {
            clans.put(name, this);
            this.name = name;
        }
    }

    public static void create(String name, Player player) {
        if(!clans.containsKey(name)) {
            Clan clan = new Clan(name);
            clan.setLeader(player);
            clans.put(name, clan);
            player.sendMessage(CC.translate(MessagesFile.getConfig().getString("CLANS.CREATED")));
        }
    }

    public static void disband(Player player) {

    }

    public static Clan get(String name) {
        if(clans.containsKey(name)) {
            return clans.get(name);
        } else {
            return null;
        }
    }

    public static void saveAll() {
        if(!clans.isEmpty()) {
            clans.keySet().forEach(clan -> {
                ClansFile.getConfig().set("CLANS." + clan, clans.get(clan));
            });
        }
    }

    public static void loadAll() {
        AtomicInteger loaded = new AtomicInteger(0);
        ConfigurationSection section = ClansFile.getConfig().getConfigurationSection("CLANS");
        if(section != null) {
            section.getKeys(false).forEach(s -> {
                clans.put(s, (Clan) ClansFile.getConfig().get("CLANS." + s));
                loaded.getAndIncrement();
            });
        }
        Bukkit.getConsoleSender().sendMessage(CC.translate("&aSuccessfully loaded " + loaded + " clans!"));
    }
}
