package com.example.league_table;

public class FixtureItem {
    private String homeTeamName, homeTeamCrest, awayTeamName, awayTeamCrest, matchDate;

    public FixtureItem(String homeTeamName, String homeTeamCrest, String awayTeamName, String awayTeamCrest, String matchDate) {
        this.homeTeamName = homeTeamName;
        this.homeTeamCrest = homeTeamCrest;
        this.awayTeamName = awayTeamName;
        this.awayTeamCrest = awayTeamCrest;
        this.matchDate = matchDate;
    }

    // Getters
    public String getHomeTeamName() { return homeTeamName; }
    public String getHomeTeamCrest() { return homeTeamCrest; }
    public String getAwayTeamName() { return awayTeamName; }
    public String getAwayTeamCrest() { return awayTeamCrest; }
    public String getMatchDate() { return matchDate; }
}