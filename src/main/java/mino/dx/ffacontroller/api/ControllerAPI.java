package mino.dx.ffacontroller.api;

import mino.dx.ffacontroller.FFAController;
import mino.dx.ffacontroller.api.interfaces.IStreak;
import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.ApiStatus.Experimental;

@SuppressWarnings("unused")
@Experimental
public class ControllerAPI {
    public static IStreak getStreakManager() {
        Plugin plugin = Bukkit.getPluginManager().getPlugin("FFA-Controller");
        if(plugin instanceof FFAController ffaController) {
            return ffaController.getStreakManager();
        }
        throw new IllegalStateException("FFA-Controller is not installed!");
    }
}
