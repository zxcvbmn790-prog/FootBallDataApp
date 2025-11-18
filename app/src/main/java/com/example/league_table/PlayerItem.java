package com.example.league_table;

public class PlayerItem {
    private String name;
    private String position;
    private String nationality; // 'jerseyNumber' 대신 'nationality'

    public PlayerItem(String name, String position, String nationality) {
        this.name = name;
        this.position = position;
        this.nationality = nationality;
    }

    // Getters
    public String getName() { return name; }
    public String getPosition() { return position; }
    public String getNationality() { return nationality; } // Getter 수정
}