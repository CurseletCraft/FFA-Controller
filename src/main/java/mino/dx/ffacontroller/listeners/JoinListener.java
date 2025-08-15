package mino.dx.ffacontroller.listeners;

import io.papermc.paper.event.connection.PlayerConnectionValidateLoginEvent;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerLoginEvent;
import org.bukkit.event.player.PlayerQuitEvent;

@SuppressWarnings("all")
public class JoinListener implements Listener {

    @EventHandler
    public void onJoin(PlayerJoinEvent e) {}

    @EventHandler
    public void onLeave(PlayerQuitEvent e) {}

    @EventHandler
    public void onLogin(PlayerLoginEvent e) {}

    @EventHandler
    public void onLoginv2(PlayerConnectionValidateLoginEvent e) {}
}
