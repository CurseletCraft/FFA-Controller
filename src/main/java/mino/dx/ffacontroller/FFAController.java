package mino.dx.ffacontroller;

import io.papermc.paper.plugin.lifecycle.event.types.LifecycleEvents;
import mino.dx.ffacontroller.commands.DiscordCmd;
import mino.dx.ffacontroller.commands.ReloadCmd;
import mino.dx.ffacontroller.commands.ToggleDeathMsgCmd;
import mino.dx.ffacontroller.hook.PlaceholderApiHook;
import mino.dx.ffacontroller.listeners.ChatListener;
import mino.dx.ffacontroller.listeners.DeathListener;
import mino.dx.ffacontroller.manager.DeathMessageManager;
import mino.dx.ffacontroller.utils.ExceptionUtil;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

public final class FFAController extends JavaPlugin {

    private DeathMessageManager deathMessageManager;

    @Override
    public void onEnable() {
        saveDefaultConfig();
        this.deathMessageManager = new DeathMessageManager(this);

        Bukkit.getScheduler().runTask(this, () -> new PlaceholderApiHook(this).register());
        registerListeners();
        registerCommands();
        getLogger().info("FFAController has been enabled!");

        ExceptionUtil.throwException("test", new Exception());
        ExceptionUtil.warningException("test", new Exception());
    }

    private void registerListeners() {
        Bukkit.getPluginManager().registerEvents(new DeathListener(this), this);
        Bukkit.getPluginManager().registerEvents(new ChatListener(this), this);

//        if(getConfig().getBoolean("killstreak.enable")) {
//            Bukkit.getPluginManager().registerEvents(new StreakListener(this), this);
//        }
    }

    private void registerCommands() {
        this.getLifecycleManager().registerEventHandler(
                LifecycleEvents.COMMANDS,
                event -> {
                    event.registrar().register(ReloadCmd.build(this).build(), "Reload plugin");
                    event.registrar().register(ToggleDeathMsgCmd.build(this).build(), "Tắt / bật death message");
                    event.registrar().register(DiscordCmd.build(this).build(), "Lấy link discord của server");
                });
    }

    // Getter
    public DeathMessageManager getDeathMessageManager() {
        return deathMessageManager;
    }
}
