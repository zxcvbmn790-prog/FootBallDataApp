package com.example.league_table;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.List;

public class LeagueAdapter extends RecyclerView.Adapter<LeagueAdapter.ViewHolder> {
    private Context context;
    private List<LeagueItem> list;
    private String currentLeagueCode = "";

    public LeagueAdapter(Context context, List<LeagueItem> list) {
        this.context = context;
        this.list = list;
    }

    public void setCurrentLeagueCode(String leagueCode) {
        this.currentLeagueCode = (leagueCode != null) ? leagueCode : "";
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_league_row, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        LeagueItem item = list.get(position);

        holder.tvPosition.setText(String.valueOf(item.getPosition()));
        holder.tvName.setText(item.getName());
        holder.tvPlayed.setText(String.valueOf(item.getPlayed()));
        holder.tvWon.setText(String.valueOf(item.getWon()));
        holder.tvDraw.setText(String.valueOf(item.getDraw()));
        holder.tvLost.setText(String.valueOf(item.getLost()));
        holder.tvGF.setText(String.valueOf(item.getGoalsFor()));
        holder.tvGA.setText(String.valueOf(item.getGoalsAgainst()));
        holder.tvGD.setText(String.valueOf(item.getGoalDiff()));
        holder.tvPoints.setText(String.valueOf(item.getPoints()));

        Glide.with(context).load(item.getCrestUrl()).into(holder.imgCrest);

        // --- 그라데이션 적용 로직 ---
        applyGradientBackground(holder, item);

        // --- 최근 5경기(Form) 표시 로직 ---
        displayRecentForm(holder, item);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 1. 새로운 Activity로 이동하기 위한 Intent 생성
                Intent intent = new Intent(context, TeamDetailActivity.class);

                // 2. 클릭된 팀의 정보를 Intent에 담아서 전달
                intent.putExtra("TEAM_NAME", item.getName());
                intent.putExtra("CREST_URL", item.getCrestUrl());

                // 3. 새로운 Activity 시작
                context.startActivity(intent);
            }
        });
    }

    private void applyGradientBackground(ViewHolder holder, LeagueItem item) {
        int teamPosition = item.getPosition();
        Drawable background = ContextCompat.getDrawable(context, R.drawable.item_background_template).mutate();
        GradientDrawable gradientDrawable = (GradientDrawable) background;
        int solidColor = ContextCompat.getColor(context, R.color.white);
        int startColor = solidColor;
        int endColor = solidColor;
        boolean applyGradient = true;

        int blueRankMax, orangeRank, greenRank, relegationRankMin;

        if ("FL1".equals(currentLeagueCode)) {
            blueRankMax = 3; orangeRank = 4; greenRank = 5; relegationRankMin = 16;
        }if ("BL1".equals(currentLeagueCode)){
            blueRankMax = 4; orangeRank = 5; greenRank = 6; relegationRankMin = 16;
        }
        else {
            blueRankMax = 4; orangeRank = 5; greenRank = 6; relegationRankMin = 18;
        }

        if (teamPosition <= blueRankMax) {
            startColor = ContextCompat.getColor(context, R.color.blue_start);
            endColor = ContextCompat.getColor(context, R.color.blue_end);
        } else if (teamPosition == orangeRank) {
            startColor = ContextCompat.getColor(context, R.color.orange_start);
            endColor = ContextCompat.getColor(context, R.color.orange_end);
        } else if (teamPosition == greenRank) {
            startColor = ContextCompat.getColor(context, R.color.green_start);
            endColor = ContextCompat.getColor(context, R.color.green_end);
        } else if (teamPosition >= relegationRankMin) {
            startColor = ContextCompat.getColor(context, R.color.red_start);
            endColor = ContextCompat.getColor(context, R.color.red_end);
        } else {
            applyGradient = false;
        }

        if (applyGradient) {
            int[] colors = { endColor, startColor, solidColor };
            float[] positions = { 0.0f, 0.3f, 0.7f };
            gradientDrawable.setColors(colors, positions);
            gradientDrawable.setOrientation(GradientDrawable.Orientation.LEFT_RIGHT);
        } else {
            gradientDrawable.setColor(solidColor);
        }
        holder.itemView.setBackground(gradientDrawable);
    }

    private void displayRecentForm(ViewHolder holder, LeagueItem item) {
        holder.formContainer.removeAllViews();
        String form = item.getForm();
        if (form != null && !form.isEmpty()) {
            String[] results = form.split(",");
            for (String result : results) {
                TextView formView = new TextView(context);
                formView.setText(result.trim());
                formView.setTextColor(ContextCompat.getColor(context, R.color.white));
                formView.setTextSize(10f);
                formView.setGravity(Gravity.CENTER);

                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                        dpToPx(18), dpToPx(18));
                params.setMarginEnd(dpToPx(4));
                formView.setLayoutParams(params);

                int backgroundResId;
                switch (result.trim()) {
                    case "W": backgroundResId = R.drawable.form_win_background; break;
//                    case "D": backgroundResId = R.drawable.form_draw_background; break;
//                    case "L": backgroundResId = R.drawable.form_lose_background; break;
                    default: backgroundResId = 0; break;
                }
                if (backgroundResId != 0) {
                    formView.setBackgroundResource(backgroundResId);
                }
                holder.formContainer.addView(formView);
            }
        }
    }

    private int dpToPx(int dp) {
        return (int) (dp * context.getResources().getDisplayMetrics().density);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvPosition, tvName, tvPlayed, tvWon, tvDraw, tvLost, tvGF, tvGA, tvGD, tvPoints;
        ImageView imgCrest;
        LinearLayout formContainer;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvPosition = itemView.findViewById(R.id.tvPosition);
            imgCrest = itemView.findViewById(R.id.imgCrest);
            tvName = itemView.findViewById(R.id.tvName);
            tvPlayed = itemView.findViewById(R.id.tvPlayed);
            tvWon = itemView.findViewById(R.id.tvWon);
            tvDraw = itemView.findViewById(R.id.tvDraw);
            tvLost = itemView.findViewById(R.id.tvLost);
            tvGF = itemView.findViewById(R.id.tvGF);
            tvGA = itemView.findViewById(R.id.tvGA);
            tvGD = itemView.findViewById(R.id.tvGD);
            tvPoints = itemView.findViewById(R.id.tvPoints);
            formContainer = itemView.findViewById(R.id.formContainer);
        }
    }
}