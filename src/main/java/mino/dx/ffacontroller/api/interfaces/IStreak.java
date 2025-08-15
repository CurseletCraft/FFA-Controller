package mino.dx.ffacontroller.api.interfaces;

import java.util.UUID;

public interface IStreak {
    int getCurrentStreak(UUID uuid);
    int getBestStreak(UUID uuid);
    void addKill(UUID uuid);
    void resetStreak(UUID uuid);
    void updateStreak(UUID uuid, int current, int best);
}
