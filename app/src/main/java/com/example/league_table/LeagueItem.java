package com.example.league_table;

public class LeagueItem {
    private int position;
    private String name;
    private String crestUrl;
    private int played, won, draw, lost, goalsFor, goalsAgainst, goalDiff, points;
    private String form; // 'form' 데이터를 저장할 필드 추가

    public LeagueItem(int position, String name, String crestUrl,
                      int played, int won, int draw, int lost,
                      int goalsFor, int goalsAgainst, int goalDiff,
                      int points, String form) {
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
        this.form = form; // 생성자에서 form 데이터 저장
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
    public String getForm() { return form; } // form 데이터를 가져올 getter 추가
}