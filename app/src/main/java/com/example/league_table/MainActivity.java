package com.example.league_table;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class MainActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private LeagueAdapter adapter;
    private List<LeagueItem> teamList = new ArrayList<>();

    private RecyclerView fixturesRecyclerView;
    private FixtureAdapter fixtureAdapter;
    private List<FixtureItem> fixtureList = new ArrayList<>();

    private ProgressBar progressBar;
    private List<ImageView> leagueIcons = new ArrayList<>();

    private static final String BASE_URL = "https://api.football-data.org/v4/competitions/";
    private static final String API_KEY = "98344f614f34453ba57993405115956d";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // 순위 RecyclerView 설정
        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new LeagueAdapter(this, teamList);
        recyclerView.setAdapter(adapter);

        // 경기 일정 RecyclerView 설정
        fixturesRecyclerView = findViewById(R.id.fixturesRecyclerView);
        fixturesRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        fixtureAdapter = new FixtureAdapter(this, fixtureList);
        fixturesRecyclerView.setAdapter(fixtureAdapter);

        progressBar = findViewById(R.id.progressBar);

        ImageView imgPL = findViewById(R.id.imgPL);
        ImageView imgBL = findViewById(R.id.imgBL);
        ImageView imgPD = findViewById(R.id.imgPD);
        ImageView imgSA = findViewById(R.id.imgSA);
        ImageView imgFL1 = findViewById(R.id.imgFL1);

        leagueIcons.add(imgPL);
        leagueIcons.add(imgBL);
        leagueIcons.add(imgPD);
        leagueIcons.add(imgSA);
        leagueIcons.add(imgFL1);

        imgPL.setOnClickListener(v -> loadLeague("PL", v));
        imgBL.setOnClickListener(v -> loadLeague("BL1", v));
        imgPD.setOnClickListener(v -> loadLeague("PD", v));
        imgSA.setOnClickListener(v -> loadLeague("SA", v));
        imgFL1.setOnClickListener(v -> loadLeague("FL1", v));

        loadLeague("PL", imgPL);
    }

    private void updateSelectedIcon(View selectedView) {
        for (ImageView icon : leagueIcons) {
            icon.setSelected(icon == selectedView);
        }
    }

    private void loadLeague(String code, View selectedView) {
        updateSelectedIcon(selectedView);
        progressBar.setVisibility(View.VISIBLE);
        recyclerView.setVisibility(View.GONE);

        adapter.setCurrentLeagueCode(code); // 어댑터에 현재 리그 코드 전달
        teamList.clear();

        loadFixtures(code); // 경기 일정 로딩 호출

        String url = BASE_URL + code + "/standings";
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url(url)
                .addHeader("X-Auth-Token", API_KEY)
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                runOnUiThread(() -> {
                    progressBar.setVisibility(View.GONE);
                    Toast.makeText(MainActivity.this, "데이터 로드 실패", Toast.LENGTH_SHORT).show();
                });
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (!response.isSuccessful() || response.body() == null) {

                    runOnUiThread(() -> {
                        progressBar.setVisibility(View.GONE);
                        Toast.makeText(MainActivity.this, "API 오류", Toast.LENGTH_SHORT).show();
                    });
                    return;
                }
                String json = response.body().string();
                android.util.Log.d("API_RAW_DATA", "받은 JSON: " + json);
                parseLeague(json);
            }
        });
    }

    private void parseLeague(String json) {
        try {
            JSONObject root = new JSONObject(json);
            JSONArray standings = root.getJSONArray("standings");

            if (standings.length() == 0) {
                runOnUiThread(() -> {
                    progressBar.setVisibility(View.GONE);
                    Toast.makeText(MainActivity.this, "데이터가 없습니다.", Toast.LENGTH_SHORT).show();
                });
                return;
            }
            JSONObject first = standings.getJSONObject(0);
            JSONArray table = first.getJSONArray("table");

            for (int i = 0; i < table.length(); i++) {
                JSONObject t = table.getJSONObject(i);
                JSONObject team = t.getJSONObject("team");
                int currentTeamId = team.getInt("id");
                Log.d("ParseLeague", "파싱된 Team ID: " + currentTeamId + ", Team Name: " + team.getString("tla"));
                teamList.add(new LeagueItem(
                        t.getInt("position"),
                        team.getString("tla"),
                        team.getString("crest"),
                        t.getInt("playedGames"),
                        t.getInt("won"),
                        t.getInt("draw"),
                        t.getInt("lost"),
                        t.getInt("goalsFor"),
                        t.getInt("goalsAgainst"),
                        t.getInt("goalDifference"),
                        t.getInt("points"),
                        t.optString("form", ""),
                        currentTeamId // 추출한 ID 사용
                ));
            }

            runOnUiThread(() -> {
                adapter.notifyDataSetChanged();
                progressBar.setVisibility(View.GONE);
                recyclerView.setVisibility(View.VISIBLE);
            });

        } catch (Exception e) {
            e.printStackTrace();
            Log.e("ParseLeague", "JSON 파싱 오류: " + e.getMessage());
            runOnUiThread(() -> progressBar.setVisibility(View.GONE));
        }
    }

    private void loadFixtures(String code) {
        fixtureList.clear();
        String url = BASE_URL + code + "/matches?status=SCHEDULED&limit=5";
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url(url)
                .addHeader("X-Auth-Token", API_KEY)
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {}

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.isSuccessful() && response.body() != null) {
                    parseFixtures(response.body().string());
                }
            }
        });
    }

    private void parseFixtures(String json) {
        try {
            JSONObject root = new JSONObject(json);
            JSONArray matches = root.getJSONArray("matches");

            for (int i = 0; i < matches.length(); i++) {
                JSONObject match = matches.getJSONObject(i);
                JSONObject homeTeam = match.getJSONObject("homeTeam");
                JSONObject awayTeam = match.getJSONObject("awayTeam");

                fixtureList.add(new FixtureItem(
                        homeTeam.getString("tla"),
                        homeTeam.getString("crest"),
                        awayTeam.getString("tla"),
                        awayTeam.getString("crest"),
                        match.getString("utcDate")
                ));
            }
            runOnUiThread(() -> fixtureAdapter.notifyDataSetChanged());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}