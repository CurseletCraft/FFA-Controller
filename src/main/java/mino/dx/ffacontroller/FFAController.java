package mino.dx.ffacontroller;

import io.papermc.paper.plugin.lifecycle.event.types.LifecycleEvents;
import mino.dx.ffacontroller.api.interfaces.IStreak;
import mino.dx.ffacontroller.commands.*;
import mino.dx.ffacontroller.controller.KillStreakManager;
import mino.dx.ffacontroller.hook.PlaceholderApiHook;
import mino.dx.ffacontroller.listeners.*;
import mino.dx.ffacontroller.manager.DeathMessageManager;
import net.j4c0b3y.api.command.bukkit.BukkitCommandHandler;
import net.j4c0b3y.api.menu.MenuHandler;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

public final class FFAController extends JavaPlugin {

    private DeathMessageManager deathMessageManager;
    private IStreak killStreakManager;

    private BukkitCommandHandler commandHandler;
    private MenuHandler menuHandler;

    @Override
    public void onEnable() {

        saveDefaultConfig();
        this.deathMessageManager = new DeathMessageManager(this);
        this.killStreakManager = new KillStreakManager(this);

        Bukkit.getScheduler().runTask(this, () -> new PlaceholderApiHook(this).register());
        registerListeners();

        menuHandler = new MenuHandler(this);

        commandHandler = new BukkitCommandHandler(this);
        commandHandler.setUsageHandler(new CommandUsageHandler());
        registerCommands();

        getLogger().info("FFA-Controller has been enabled!");
    }

    @Override
    public void onDisable() {
        if(killStreakManager != null && killStreakManager instanceof KillStreakManager) {
            ((KillStreakManager) killStreakManager).close();
        }
        getLogger().info("FFA-Controller has been disabled!");
    }

    private void registerListeners() {
        Bukkit.getPluginManager().registerEvents(new DeathListener(this), this);
        Bukkit.getPluginManager().registerEvents(new ChatListener(this), this);
        Bukkit.getPluginManager().registerEvents(new JoinListener(this), this);

        if(getConfig().getBoolean("killstreak.enable")) {
            Bukkit.getPluginManager().registerEvents(new StreakListener(this), this);
            Bukkit.getPluginManager().registerEvents(new StreakNotifierListener(this), this);
        }
    }

    private void registerCommands() {
        this.getLifecycleManager().registerEventHandler(
                LifecycleEvents.COMMANDS,
                event -> {
                    event.registrar().register(ReloadCmd.build(this).build(), "Reload plugin");
                    event.registrar().register(ToggleDeathMsgCmd.build(this).build(), "Tắt / bật death message");
                    event.registrar().register(DiscordCmd.build(this).build(), "Lấy link discord của server");
                    event.registrar().register(PlayerStatsBrigadierCmd.build(this).build(), "PlayerStats brigadier");
                });

        commandHandler.register(new PlayerStatsCmd(this));
    }

    // Getter
    public DeathMessageManager getDeathMessageManager() {
        return deathMessageManager;
    }

    public IStreak getStreakManager() {
        return killStreakManager;
    }

    @SuppressWarnings("unused")
    public MenuHandler getMenuHandler() {
        return menuHandler;
    }
}
