package com.example.league_table;

public class ScorerItem {
    private int rank; // 순위
    private int goals; // 득점 수
    private String playerName; // 선수 이름
    private String teamName; // 소속팀 이름 (TLA)
    private String teamCrest; // 소속팀 엠블럼 URL

    // 생성자
    public ScorerItem(int rank, String playerName, String teamName, String teamCrest, int goals) {
        this.rank = rank;
        this.playerName = playerName;
        this.teamName = teamName;
        this.teamCrest = teamCrest;
        this.goals = goals;
    }

    // 각 데이터를 가져오기 위한 Getter 메서드들
    public int getRank() {
        return rank;
    }

    public int getGoals() {
        return goals;
    }

    public String getPlayerName() {
        return playerName;
    }

    public String getTeamName() {
        return teamName;
    }

    public String getTeamCrest() {
        return teamCrest;
    }
}