package com.example.league_table;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast; // 투표 완료 메시지를 위해 추가
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import com.google.android.material.button.MaterialButton;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;

public class FixtureAdapter extends RecyclerView.Adapter<FixtureAdapter.FixtureViewHolder> {
    private Context context;
    private List<FixtureItem> fixtureList;

    public FixtureAdapter(Context context, List<FixtureItem> fixtureList) {
        this.context = context;
        this.fixtureList = fixtureList;
    }

    @NonNull
    @Override
    public FixtureViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_fixture, parent, false);
        return new FixtureViewHolder(view);
    }

    // 서버로 투표를 보내고 결과를 갱신하는 시뮬레이션 메서드 (실제 구현 시 서버 통신 코드로 대체해야 함)
    private void sendVoteAndUpdate(FixtureItem item, boolean isHomeVote, int position) {
        if (item.areVotesVisible()) return;

        // 1. 서버로 투표 전송 시뮬레이션
        if (isHomeVote) {
            item.incrementHomeVotes();
        } else {
            item.incrementAwayVotes();
        }
        item.setVotesVisible(true);

        // 2. 투표 결과 서버에서 받아와서 업데이트 시뮬레이션 (여기서는 클라이언트 측 값 사용)
        // 실제 구현 시: OkHttp POST 요청 -> 성공 응답 -> OkHttp GET 요청으로 최신 투표 결과 로드

        // UI 업데이트
        notifyItemChanged(position);
    }

    // 투표 결과를 표시하는 헬퍼 메서드
    private void updateVoteResults(FixtureViewHolder holder, FixtureItem item) {
        if (item.areVotesVisible()) {
            // 투표가 완료되면 버튼 숨기기
            holder.layoutVoteButtons.setVisibility(View.GONE);
            // 승률 결과 표시
            holder.layoutVoteResult.setVisibility(View.VISIBLE);

            int totalVotes = item.getHomeVotes() + item.getAwayVotes();

            // 총 투표수가 0인 경우 처리
            int homePercent = (totalVotes > 0) ? (item.getHomeVotes() * 100 / totalVotes) : 0;
            int awayPercent = (totalVotes > 0) ? (item.getAwayVotes() * 100 / totalVotes) : 0;

            // 텍스트 포맷 (예: 600표 (60%))
            String homeText = String.format(Locale.getDefault(), "%d표 (%d%%)", item.getHomeVotes(), homePercent);
            String awayText = String.format(Locale.getDefault(), "%d표 (%d%%)", item.getAwayVotes(), awayPercent);

            holder.tvHomeVoteResult.setText(homeText);
            holder.tvAwayVoteResult.setText(awayText);

            // 승률이 높은 쪽에 색상 강조
            if (homePercent > awayPercent) {
                holder.tvHomeVoteResult.setTextColor(Color.parseColor("#388E3C")); // 진한 초록색
                holder.tvAwayVoteResult.setTextColor(Color.parseColor("#757575")); // 회색 (일반)
            } else if (awayPercent > homePercent) {
                holder.tvHomeVoteResult.setTextColor(Color.parseColor("#757575")); // 회색 (일반)
                holder.tvAwayVoteResult.setTextColor(Color.parseColor("#388E3C")); // 진한 초록색
            } else {
                // 무승부이거나 투표가 0일 경우
                holder.tvHomeVoteResult.setTextColor(Color.parseColor("#212121")); // 검은색
                holder.tvAwayVoteResult.setTextColor(Color.parseColor("#212121")); // 검은색
            }

        } else {
            // 투표 전에는 버튼 표시, 결과 숨기기
            holder.layoutVoteButtons.setVisibility(View.VISIBLE);
            holder.layoutVoteResult.setVisibility(View.GONE);

            // 투표 전 초기 텍스트 설정 (승률 강조 색상 제거)
            holder.tvHomeVoteResult.setText("0표 (0%)");
            holder.tvAwayVoteResult.setText("0표 (0%)");
            holder.tvHomeVoteResult.setTextColor(Color.parseColor("#212121"));
            holder.tvAwayVoteResult.setTextColor(Color.parseColor("#212121"));
        }
    }

    @Override
    public void onBindViewHolder(@NonNull FixtureViewHolder holder, int position) {
        Log.d("ADAPTER_BIND", "Binding position: " + position);
        FixtureItem item = fixtureList.get(position);

        holder.tvHomeTeam.setText(item.getHomeTeamName());
        holder.tvAwayTeam.setText(item.getAwayTeamName());
        holder.tvMatchDate.setText(formatDate(item.getMatchDate()));

        Glide.with(context).load(item.getHomeTeamCrest()).into(holder.imgHomeCrest);
        Glide.with(context).load(item.getAwayTeamCrest()).into(holder.imgAwayCrest);

        // 투표 버튼 텍스트 설정
        holder.btnVoteHome.setText(item.getHomeTeamName() + " 승");
        holder.btnVoteAway.setText(item.getAwayTeamName() + " 승");

        // 투표 결과 업데이트 (표시/숨김 및 색상 로직 포함)
        updateVoteResults(holder, item);

        // 버튼 클릭 리스너 (투표 및 결과 표시)
        holder.btnVoteHome.setOnClickListener(v -> sendVoteAndUpdate(item, true, position));
        holder.btnVoteAway.setOnClickListener(v -> sendVoteAndUpdate(item, false, position));

        // 팀 상세 화면 이동 리스너 (기존 로직 유지)
        holder.imgHomeCrest.setOnClickListener(v -> {
            Intent intent = new Intent(context, TeamDetailActivity.class);
            intent.putExtra("TEAM_NAME", item.getHomeTeamName());
            intent.putExtra("CREST_URL", item.getHomeTeamCrest());
            context.startActivity(intent);
        });

        holder.imgAwayCrest.setOnClickListener(v -> {
            Intent intent = new Intent(context, TeamDetailActivity.class);
            intent.putExtra("TEAM_NAME", item.getAwayTeamName());
            intent.putExtra("CREST_URL", item.getAwayTeamCrest());
            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return fixtureList.size();
    }

    // UTC 날짜 형식을 한국 시간(KST)으로 변환하는 메서드 (기존 로직 유지)
    private String formatDate(String utcDateStr) {
        try {
            SimpleDateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'", Locale.getDefault());
            inputFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
            Date date = inputFormat.parse(utcDateStr);

            SimpleDateFormat outputFormat = new SimpleDateFormat("MM.dd (E) HH:mm", Locale.KOREAN);
            outputFormat.setTimeZone(TimeZone.getTimeZone("Asia/Seoul"));
            return outputFormat.format(date);
        } catch (ParseException e) {
            e.printStackTrace();
            return utcDateStr; // 변환 실패 시 원본 날짜 반환
        }
    }

    public static class FixtureViewHolder extends RecyclerView.ViewHolder {
        TextView tvMatchDate, tvHomeTeam, tvAwayTeam;
        ImageView imgHomeCrest, imgAwayCrest;

        // 투표 관련 View 변수
        MaterialButton btnVoteHome, btnVoteAway;
        LinearLayout layoutVoteResult, layoutVoteButtons;
        TextView tvHomeVoteResult, tvAwayVoteResult;

        public FixtureViewHolder(@NonNull View itemView) {
            super(itemView);
            tvMatchDate = itemView.findViewById(R.id.tvMatchDate);
            tvHomeTeam = itemView.findViewById(R.id.tvHomeTeam);
            tvAwayTeam = itemView.findViewById(R.id.tvAwayTeam);
            imgHomeCrest = itemView.findViewById(R.id.imgHomeCrest);
            imgAwayCrest = itemView.findViewById(R.id.imgAwayCrest);

            // 투표 관련 View ID 연결
            btnVoteHome = itemView.findViewById(R.id.btnVoteHome);
            btnVoteAway = itemView.findViewById(R.id.btnVoteAway);
            layoutVoteButtons = itemView.findViewById(R.id.layoutVoteButtons);
            layoutVoteResult = itemView.findViewById(R.id.layoutVoteResult);
            tvHomeVoteResult = itemView.findViewById(R.id.tvHomeVoteResult);
            tvAwayVoteResult = itemView.findViewById(R.id.tvAwayVoteResult);
        }
    }
}