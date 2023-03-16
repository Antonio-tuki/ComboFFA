package me.antonio.combo.command.media;

import me.antonio.combo.file.MessagesFile;
import mx.fxmxgragfx.api.CC;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

import java.util.Locale;

/**
 * Developed by FxMxGRAGFX
 * Project: Combo
 **/

public class WebsiteCommand extends Command {

    protected WebsiteCommand() {
        super(WebsiteCommand.class.getSimpleName().replace("Command", "").toLowerCase(Locale.ROOT));
    }

    @Override
    public boolean execute(CommandSender sender, String label, String[] args) {
        CC.translate(MessagesFile.getConfig().getStringList("MEDIA." + getClass().getSimpleName().replace("Command", "").toUpperCase(Locale.ROOT))).forEach(sender::sendMessage);
        return true;
    }
}
