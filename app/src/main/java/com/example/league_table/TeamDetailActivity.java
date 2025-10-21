package com.example.league_table;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

public class TeamDetailActivity extends AppCompatActivity {

    private ImageView imgDetailCrest;
    private TextView tvDetailName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_team_detail);

        imgDetailCrest = findViewById(R.id.imgDetailCrest);
        tvDetailName = findViewById(R.id.tvDetailName);

        // 1. 이전 Activity에서 보낸 Intent를 받음
        Intent intent = getIntent();

        // 2. Intent에서 "TEAM_NAME", "CREST_URL" 키로 데이터를 꺼냄
        String teamName = intent.getStringExtra("TEAM_NAME");
        String crestUrl = intent.getStringExtra("CREST_URL");

        // 3. 받아온 데이터로 UI 업데이트
        tvDetailName.setText(teamName);
        Glide.with(this).load(crestUrl).into(imgDetailCrest);
    }
}