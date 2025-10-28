package com.example.league_table;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

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

public class TeamDetailActivity extends AppCompatActivity {

    private static final String API_KEY = "98344f614f34453ba57993405115956d"; // 실제 키 사용 권장
    private static final String BASE_URL = "https://api.football-data.org/v4/teams/";

    private ImageView imgDetailCrest;
    private TextView tvDetailName, tvCoach, tvVenue, tvFounded, tvColors;
    private RecyclerView squadRecyclerView;

    private PlayerAdapter playerAdapter;
    private List<PlayerItem> playerList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_team_detail);

        imgDetailCrest = findViewById(R.id.imgDetailCrest);
        tvDetailName = findViewById(R.id.tvDetailName);
        tvCoach = findViewById(R.id.tvCoach);
        tvVenue = findViewById(R.id.tvVenue);
        tvFounded = findViewById(R.id.tvFounded);
        tvColors = findViewById(R.id.tvColors);
        squadRecyclerView = findViewById(R.id.squadRecyclerView);

        squadRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        playerAdapter = new PlayerAdapter(this, playerList);
        squadRecyclerView.setAdapter(playerAdapter);

        Intent intent = getIntent();
        int teamId = intent.getIntExtra("TEAM_ID", -1);
        Log.d("TeamDetailActivity", "받은 TEAM_ID: " + teamId);

        if (teamId != -1) {
            loadTeamDetails(teamId);
        } else {
            Log.e("TeamDetailActivity", "유효하지 않은 TEAM_ID");
        }
    }

    private void loadTeamDetails(int teamId) {
        String url = BASE_URL + teamId;
        Log.d("TeamDetailActivity", "API 호출 URL: " + url);
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url(url)
                .addHeader("X-Auth-Token", API_KEY)
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.e("TeamDetailActivity", "API 호출 실패", e);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.isSuccessful() && response.body() != null) {
                    String json = response.body().string();
                    Log.d("TeamDetailActivity", "API 응답 성공 (JSON): " + json);
                    parseTeamDetails(json);
                } else {
                    Log.e("TeamDetailActivity", "API 응답 실패: " + response.code());
                }
            }
        });
    }

    private void parseTeamDetails(String json) {
        try {
            Log.d("TeamDetailActivity", "parseTeamDetails 시작");
            JSONObject root = new JSONObject(json);

            String name = root.optString("name", "N/A");
            String crestUrl = root.optString("crest", null);
            String venue = root.optString("venue", "N/A");
            int founded = root.optInt("founded", 0);
            String colors = root.optString("clubColors", "N/A");
            String coachName = "N/A";
            if (root.has("coach") && !root.isNull("coach")) {
                JSONObject coachObj = root.optJSONObject("coach");
                if (coachObj != null) coachName = coachObj.optString("name", "N/A");
            }
            Log.d("TeamDetailActivity", "기본 정보 파싱 완료");

            playerList.clear();
            JSONArray squad = root.optJSONArray("squad");
            if (squad != null) {
                Log.d("TeamDetailActivity", "선수단 JSONArray 가져옴, 크기: " + squad.length());
                for (int i = 0; i < squad.length(); i++) {
                    JSONObject player = squad.optJSONObject(i);
                    if (player == null) continue;

                    String position = player.optString("position", "N/A");
                    if ("Coach".equals(position)) continue;

                    playerList.add(new PlayerItem(
                            player.optString("name", "N/A"),
                            position,
                            player.optInt("shirtNumber", 0)
                    ));
                }
            } else {
                Log.w("TeamDetailActivity", "선수단(squad) 정보 없음");
            }
            Log.d("TeamDetailActivity", "선수단 리스트 생성 완료, 크기: " + playerList.size());

            String finalCoachName = coachName;
            runOnUiThread(() -> {
                Log.d("TeamDetailActivity", "UI 업데이트 시작");
                tvDetailName.setText(name);
                tvVenue.setText(venue);
                tvFounded.setText(founded > 0 ? String.valueOf(founded) : "-");
                tvColors.setText(colors);
                tvCoach.setText(finalCoachName);
                if (crestUrl != null) Glide.with(this).load(crestUrl).into(imgDetailCrest);
                playerAdapter.notifyDataSetChanged();
                Log.d("TeamDetailActivity", "UI 업데이트 완료");
            });

        } catch (Exception e) {
            Log.e("TeamDetailActivity", "JSON 파싱 오류", e);
        }
    }
}