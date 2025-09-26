package mino.dx.ffacontroller.api.events;

import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.HandlerList;
import org.bukkit.event.player.PlayerEvent;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@SuppressWarnings("unused")
public class KillstreakChangeEvent extends PlayerEvent implements Cancellable {

    private static final HandlerList HANDLER_LIST = new HandlerList();

    @Nullable
    private final Player killer; // Ai đã chấm dứt streak, có thể null

    private final int oldStreak;
    private final int newStreak;

    private boolean cancelled;

    /**
     * @param player Người có streak thay đổi
     * @param killer Người gây ra (hoặc null nếu tự chết, môi trường...)
     * @param oldStreak Streak trước đó
     * @param newStreak Streak mới
     */
    public KillstreakChangeEvent(
            @NotNull Player player,
            @Nullable Player killer,
            int oldStreak,
            int newStreak) {
        super(player);
        this.killer = killer;
        this.oldStreak = oldStreak;
        this.newStreak = newStreak;
    }

    @Nullable
    public Player getKiller() {
        return killer;
    }

    public int getNewStreak() {
        return newStreak;
    }

    public int getOldStreak() {
        return oldStreak;
    }

    @Override
    @NotNull
    public HandlerList getHandlers() {
        return HANDLER_LIST;
    }

    public static HandlerList getHandlerList() {
        return HANDLER_LIST;
    }

    @Override
    public boolean isCancelled() {
        return cancelled;
    }

    @Override
    public void setCancelled(boolean cancel) {
        this.cancelled = cancel;
    }
}
