package me.antonio.combo;

import me.antonio.combo.clan.Clan;
import me.antonio.combo.clan.listener.ClanChatListener;
import me.antonio.combo.clan.listener.ClanPvPListener;
import me.antonio.combo.command.media.*;
import me.antonio.combo.file.TogglesFile;
import me.antonio.combo.listener.PlayerDeathListener;
import me.antonio.combo.listener.PlayerJoinListener;
import me.antonio.combo.player.Profile;
import me.antonio.combo.scoreboard.ScoreboardProvider;
import mx.fxmxgragfx.api.command.PCommand;
import mx.fxmxgragfx.api.handler.Handler;
import mx.fxmxgragfx.api.scoreboard.CScoreBoard;
import mx.fxmxgragfx.api.tablist.shared.TabHandler;
import mx.fxmxgragfx.providers.TablistProvider;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

/**
 * Developed by FxMxGRAGFX
 * Project: Combo
 **/

public class Combo extends JavaPlugin {

    public static Combo INSTANCE;

    @Override
    public void onEnable() {
        INSTANCE = Combo.getPlugin(Combo.class);
        /* License File
        if(!getDataFolder().exists()) {
            getDataFolder().mkdir();
            new LicenseFile();
        }
         */
        Profile.loadAll();
        loadTablist();
        loadScoreboard();
        loadListeners();
        if(TogglesFile.getConfig().getBoolean("CLANS")) {
            Clan.loadAll();
        }
        loadCommands();
    }

    private void loadCommands() {
        if(TogglesFile.getConfig().getBoolean("COMMANDS.MEDIA.DISCORD")) new PCommand(DiscordCommand.class);
        if(TogglesFile.getConfig().getBoolean("COMMANDS.MEDIA.TEAMSPEAK")) new PCommand(TeamSpeakCommand.class);
        if(TogglesFile.getConfig().getBoolean("COMMANDS.MEDIA.WEBSITE")) new PCommand(WebsiteCommand.class);
        if(TogglesFile.getConfig().getBoolean("COMMANDS.MEDIA.STORE")) new PCommand(StoreCommand.class);
        if(TogglesFile.getConfig().getBoolean("COMMANDS.MEDIA.TWITTER")) new PCommand(TwitterCommand.class);
    }

    private void loadListeners() {
        new Handler(PlayerJoinListener.class);
        if(TogglesFile.getConfig().getBoolean("CLANS")) {
            new Handler(ClanChatListener.class);
            new Handler(ClanPvPListener.class);
        }
        new Handler(PlayerDeathListener.class);
    }

    private void loadScoreboard() {
        if(!TogglesFile.getConfig().getBoolean("SCOREBOARD")) return;
        new CScoreBoard(this, new ScoreboardProvider());
    }

    private void loadTablist() {
        if(!TogglesFile.getConfig().getBoolean("TABLIST")) return;
        if(Bukkit.getVersion().contains("1.7")) {
            new TabHandler(new mx.fxmxgragfx.api.tablist.v1_7.TablistAdapter(), new TablistProvider(), this, 20L);
        } else if(Bukkit.getVersion().contains("1.8")) {
            new TabHandler(new mx.fxmxgragfx.api.tablist.v1_8.TablistAdapter(), new TablistProvider(), this, 20L);
        }
    }

    @Override
    public void onDisable() {
        Profile.saveAll();
    }
}
