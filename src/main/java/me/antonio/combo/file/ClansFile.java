package me.antonio.combo.file;

import me.antonio.combo.Combo;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.util.Locale;

/**
 * Developed by FxMxGRAGFX
 * Project: Combo
 **/

public class ClansFile extends YamlConfiguration {

    private String fileName = getClass().getSimpleName().replace("File", "").toLowerCase(Locale.ROOT) + ".yml";
    private File file = new File(Combo.INSTANCE.getDataFolder(), fileName);
    private static ClansFile config;

    public ClansFile() {
        if(!file.exists()) {
            Combo.INSTANCE.saveResource(fileName, false);
            config = this;
        }
    }

    public static ClansFile getConfig() {
        if(config == null) {
            config = new ClansFile();
        }
        return config;
    }

    public void save() {
        try {
            super.save(this.file);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void reload() {
        try {
            super.load(this.file);
        } catch (IOException | InvalidConfigurationException e) {
            e.printStackTrace();
        }
    }
}
