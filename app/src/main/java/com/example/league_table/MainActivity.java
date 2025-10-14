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
    public static String League_rank;
    private RecyclerView recyclerView;
    private LeagueAdapter adapter;
    private List<LeagueItem> teamList = new ArrayList<>();
    private ProgressBar progressBar;
    private List<ImageView> leagueIcons = new ArrayList<>(); // 리그 아이콘들을 관리할 리스트

    private static final String BASE_URL = "https://api.football-data.org/v4/competitions/";
    private static final String API_KEY = "98344f614f34453ba57993405115956d"; // 본인의 API 키 사용 권장

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        recyclerView = findViewById(R.id.recyclerView);
        progressBar = findViewById(R.id.progressBar);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new LeagueAdapter(this, teamList);
        recyclerView.setAdapter(adapter);

        ImageView imgPL = findViewById(R.id.imgPL);
        ImageView imgBL = findViewById(R.id.imgBL);
        ImageView imgPD = findViewById(R.id.imgPD);
        ImageView imgSA = findViewById(R.id.imgSA);
        ImageView imgFL1 = findViewById(R.id.imgFL1);

        // 아이콘들을 리스트에 추가
        leagueIcons.add(imgPL);
        leagueIcons.add(imgBL);
        leagueIcons.add(imgPD);
        leagueIcons.add(imgSA);
        leagueIcons.add(imgFL1);

        // 클릭 리스너 수정: 클릭된 View를 loadLeague 메서드로 전달
        imgPL.setOnClickListener(v -> loadLeague("PL", v));
        imgBL.setOnClickListener(v -> loadLeague("BL1", v));
        imgPD.setOnClickListener(v -> loadLeague("PD", v));
        imgSA.setOnClickListener(v -> loadLeague("SA", v));
        imgFL1.setOnClickListener(v -> loadLeague("FL1", v));

        // 기본 PL 불러오기 (초기 선택 아이콘 지정)
        loadLeague("PL", imgPL);
    }

    // 선택된 아이콘을 업데이트하는 메서드 추가
    private void updateSelectedIcon(View selectedView) {
        for (ImageView icon : leagueIcons) {
            icon.setSelected(icon == selectedView);
        }
    }

    // loadLeague 메서드 수정: 선택된 View를 파라미터로 받음
    private void loadLeague(String code, View selectedView) {
        updateSelectedIcon(selectedView); // 아이콘 선택 상태 업데이트
        progressBar.setVisibility(View.VISIBLE);
        recyclerView.setVisibility(View.GONE); // 로딩 중에는 리스트 숨김
        teamList.clear();
        adapter.notifyDataSetChanged();
        League_rank = code;
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
                Log.d("API_RESPONSE", "JSON Data: " + json);
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
                teamList.add(new LeagueItem(
                        t.getInt("position"),
                        team.getString("tla"), // 3글자 약칭 사용
                        team.getString("crest"),
                        t.getInt("playedGames"),
                        t.getInt("won"),
                        t.getInt("draw"),
                        t.getInt("lost"),
                        t.getInt("goalsFor"),
                        t.getInt("goalsAgainst"),
                        t.getInt("goalDifference"),
                        t.getInt("points"),
                        t.optString("form", "")
                ));
            }

            runOnUiThread(() -> {
                adapter.notifyDataSetChanged();
                progressBar.setVisibility(View.GONE);
                recyclerView.setVisibility(View.VISIBLE); // 데이터 로드 완료 후 리스트 표시
            });

        } catch (Exception e) {
            e.printStackTrace();
            runOnUiThread(() -> progressBar.setVisibility(View.GONE));
        }
    }
}