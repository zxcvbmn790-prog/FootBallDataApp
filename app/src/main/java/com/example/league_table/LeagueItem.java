package com.example.league_table;

public class LeagueItem {
    private int position;
    private String name;
    private String crestUrl;
    private int played, won, draw, lost, goalsFor, goalsAgainst, goalDiff, points, teamId; // teamId 변수 확인
    private String form;

    public LeagueItem(int position, String name, String crestUrl,
                      int played, int won, int draw, int lost,
                      int goalsFor, int goalsAgainst, int goalDiff,
                      int points, String form, int id) { // 파라미터 이름 'id' 확인
        this.position = position;
        this.name = name;
        this.crestUrl = crestUrl;
        this.played = played;
        this.won = won;
        this.draw = draw;
        this.lost = lost;
        this.goalsFor = goalsFor;
        this.goalsAgainst = goalsAgainst;
        this.goalDiff = goalDiff;
        this.points = points;
        this.form = form;
        // ▼▼▼ 이 부분을 수정하세요 ▼▼▼
        this.teamId = id; // 파라미터 'id' 값을 인스턴스 변수 'teamId'에 저장
        // ▲▲▲ 수정 끝 ▲▲▲
    }

    // Getters
    public int getPosition() { return position; }
    public String getName() { return name; }
    public String getCrestUrl() { return crestUrl; }
    public int getPlayed() { return played; }
    public int getWon() { return won; }
    public int getDraw() { return draw; }
    public int getLost() { return lost; }
    public int getGoalsFor() { return goalsFor; }
    public int getGoalsAgainst() { return goalsAgainst; }
    public int getGoalDiff() { return goalDiff; }
    public int getPoints() { return points; }
    public String getForm() { return form; }
    public int getTeamId() { return teamId; }
}