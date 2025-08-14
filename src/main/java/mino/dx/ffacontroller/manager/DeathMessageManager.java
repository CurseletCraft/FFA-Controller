package mino.dx.ffacontroller.manager;

import mino.dx.ffacontroller.FFAController;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.util.UUID;
import java.util.logging.Level;

public class DeathMessageManager {
    private final FFAController plugin;
    private final File file;
    private YamlConfiguration data;

    public DeathMessageManager(FFAController plugin) {
        this.plugin = plugin;
        this.file = new File(plugin.getDataFolder(), "data.yml");
        try {
            if (!file.exists()) {
                if (!file.createNewFile()) {
                    plugin.getLogger().log(Level.INFO, "File already exists and was not recreated.");
                }
            }
        } catch (IOException e) {
            plugin.getLogger().log(Level.SEVERE, "Could not create data.yml", e);
        }
        this.data = YamlConfiguration.loadConfiguration(file);
    }

    public boolean isDeathMessageEnabled(UUID uuid) {
        return data.getBoolean(uuid.toString(), true); // mặc định là true nếu không có
    }

    public void setDeathMessage(UUID uuid, boolean enabled) {
        data.set(uuid.toString(), enabled);
        save();
    }

    public void save() {
        try {
            data.save(file);
        } catch (IOException e) {
            plugin.getLogger().log(Level.SEVERE, "Could not save death messages!", e);
        }
    }

    public void reload() {
        this.data = YamlConfiguration.loadConfiguration(file);
    }
}
