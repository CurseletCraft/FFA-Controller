package mino.dx.ffacontroller.api.interfaces;

import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.function.Consumer;

@SuppressWarnings("unused")
public interface IStreak {

    int getCurrentStreak(UUID uuid);
    CompletableFuture<Integer> getCurrentStreakAsync(UUID uuid, Consumer<Throwable> errorHandler);

    int getBestStreak(UUID uuid);
    CompletableFuture<Integer> getBestStreakAsync(UUID uuid, Consumer<Throwable> errorHandler);

    void addKill(UUID uuid);
    CompletableFuture<Void> addKillAsync(UUID uuid, Consumer<Throwable> errorHandler);

    void resetStreak(UUID uuid);
    CompletableFuture<Void> resetStreakAsync(UUID uuid, Consumer<Throwable> errorHandler);

    void updateStreak(UUID uuid, int current, int best);
    CompletableFuture<Void> updateStreakAsync(UUID uuid, int current, int best, Consumer<Throwable> errorHandler);

    default CompletableFuture<Integer> getCurrentStreakAsync(UUID uuid) {
        return getCurrentStreakAsync(uuid, null);
    }

    default CompletableFuture<Void> addKillAsync(UUID uuid) {
        return addKillAsync(uuid, null);
    }

    default CompletableFuture<Void> resetStreakAsync(UUID uuid) {
        return resetStreakAsync(uuid, null);
    }

}
