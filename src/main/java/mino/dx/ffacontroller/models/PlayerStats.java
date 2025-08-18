package mino.dx.ffacontroller.models;

public class PlayerStats {
    String kills;
    String deaths;
    String streak;
    String timePlayed;
    String eloContext;

    // Constructor
    public PlayerStats(String kills, String deaths, String streak, String timePlayed, String eloContext) {
        this.kills = kills;
        this.deaths = deaths;
        this.streak = streak;
        this.timePlayed = timePlayed;
        this.eloContext = eloContext;
    }

    public String getKills() {
        return kills;
    }

    public String getDeaths() {
        return deaths;
    }

    public String getStreak() {
        return streak;
    }

    public String getTimePlayed() {
        return timePlayed;
    }

    public String getEloContext() {
        return eloContext;
    }
}
