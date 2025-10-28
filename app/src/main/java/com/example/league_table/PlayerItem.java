package com.example.league_table;

public class PlayerItem {
    private String name;
    private String position;
    private int jerseyNumber;

    public PlayerItem(String name, String position, int jerseyNumber) {
        this.name = name;
        this.position = position;
        this.jerseyNumber = jerseyNumber;
    }

    public String getName() { return name; }
    public String getPosition() { return position; }
    public int getJerseyNumber() { return jerseyNumber; }
}