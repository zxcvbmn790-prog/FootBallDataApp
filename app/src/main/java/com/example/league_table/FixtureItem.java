package com.example.league_table;

public class FixtureItem {
    private int matchId; // TODO: 서버 통신을 위한 매치 ID 추가
    private String homeTeamName, homeTeamCrest, awayTeamName, awayTeamCrest, matchDate;

    // 투표 관련 필드 (서버에서 받은 값으로 덮어써질 예정)
    private int homeVotes = 0;
    private int awayVotes = 0;
    private boolean votesVisible = false;

    public FixtureItem(int matchId, String homeTeamName, String homeTeamCrest, String awayTeamName, String awayTeamCrest, String matchDate) {
        this.matchId = matchId; // ID 초기화
        this.homeTeamName = homeTeamName;
        this.homeTeamCrest = homeTeamCrest;
        this.awayTeamName = awayTeamName;
        this.awayTeamCrest = awayTeamCrest;
        this.matchDate = matchDate;

        // 초기 투표 값 0:0
        this.homeVotes = 0;
        this.awayVotes = 0;
    }

    public FixtureItem(String tla, String crest, String tla1, String crest1, String utcDate) {
    }

    // Getters
    public int getMatchId() { return matchId; }
    public String getHomeTeamName() { return homeTeamName; }
    public String getHomeTeamCrest() { return homeTeamCrest; }
    public String getAwayTeamName() { return awayTeamName; }
    public String getAwayTeamCrest() { return awayTeamCrest; }
    public String getMatchDate() { return matchDate; }

    // 투표 관련 Getter/Setter
    public int getHomeVotes() { return homeVotes; }
    public int getAwayVotes() { return awayVotes; }
    public boolean areVotesVisible() { return votesVisible; }

    // 서버에서 투표 결과를 받아와서 업데이트하는 메서드 (추가)
    public void setVotes(int homeVotes, int awayVotes) {
        this.homeVotes = homeVotes;
        this.awayVotes = awayVotes;
    }

    public void setVotesVisible(boolean votesVisible) {
        this.votesVisible = votesVisible;
    }

    // 클라이언트 측 시뮬레이션 투표 (실제 서버 연동 시에는 사용하지 않음)
    public void incrementHomeVotes() { this.homeVotes += 1; }
    public void incrementAwayVotes() { this.awayVotes += 1; }
}