package com.example.league_table;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.List;

public class LeagueAdapter extends RecyclerView.Adapter<LeagueAdapter.ViewHolder> {
    private Context context;
    private List<LeagueItem> list;

    public LeagueAdapter(Context context, List<LeagueItem> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // XML 파일 이름은 실제 사용하는 이름으로 맞춰주세요. (예: item_league.xml)
        View view = LayoutInflater.from(context).inflate(R.layout.item_league_row, parent, false);
        return new ViewHolder(view);
    }

    @RequiresApi(api = Build.VERSION_CODES.Q)
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        LeagueItem item = list.get(position);

        // --- 데이터 설정 ---
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

        // --- ✨ 자연스럽게 사라지는 그라데이션 적용 로직 ✨ ---
        int teamPosition = item.getPosition();

        Drawable background = ContextCompat.getDrawable(context, R.drawable.item_background_template).mutate();
        GradientDrawable gradientDrawable = (GradientDrawable) background;

        int solidColor = ContextCompat.getColor(context, R.color.white);
        int startColor = solidColor;
        int endColor = solidColor;
        boolean applyGradient = true;
        if (MainActivity.League_rank.equals("FL1")) {
            if (teamPosition >= 1 && teamPosition <= 3) {
                startColor = ContextCompat.getColor(context, R.color.blue_start); // 연한 파랑
                endColor = ContextCompat.getColor(context, R.color.blue_end);     // 진한 파랑
            } else if (teamPosition == 4) {
                startColor = ContextCompat.getColor(context, R.color.orange_start);
                endColor = ContextCompat.getColor(context, R.color.orange_end);
            } else if (teamPosition == 5) {
                startColor = ContextCompat.getColor(context, R.color.green_start);
                endColor = ContextCompat.getColor(context, R.color.green_end);
            } else if (teamPosition >= 18 && teamPosition <= 20) {
                startColor = ContextCompat.getColor(context, R.color.red_start);
                endColor = ContextCompat.getColor(context, R.color.red_end);
            } else {
                applyGradient = false;
            }
        } else {

            if (teamPosition >= 1 && teamPosition <= 4) {
                startColor = ContextCompat.getColor(context, R.color.blue_start); // 연한 파랑
                endColor = ContextCompat.getColor(context, R.color.blue_end);     // 진한 파랑
            } else if (teamPosition == 5) {
                startColor = ContextCompat.getColor(context, R.color.orange_start);
                endColor = ContextCompat.getColor(context, R.color.orange_end);
            } else if (teamPosition == 6) {
                startColor = ContextCompat.getColor(context, R.color.green_start);
                endColor = ContextCompat.getColor(context, R.color.green_end);
            } else if (teamPosition >= 18 && teamPosition <= 20) {
                startColor = ContextCompat.getColor(context, R.color.red_start);
                endColor = ContextCompat.getColor(context, R.color.red_end);
            } else {
                applyGradient = false;
            }
        }

        if (applyGradient) {
            // ▼▼▼ 수정된 부분 ▼▼▼
            // 색상: [진한색, 연한색, 흰색]
            // 위치: [ 0%  ,  30%  , 70% ] -> 70% 지점에서 완전히 흰색이 됨
            int[] colors = {endColor, startColor, solidColor};
            float[] positions = {0.0f, 0.15f, 0.3f};
            // ▲▲▲ 수정 끝 ▲▲▲

            gradientDrawable.setColors(colors, positions);
            gradientDrawable.setOrientation(GradientDrawable.Orientation.LEFT_RIGHT);

        } else {
            gradientDrawable.setColor(solidColor);
        }

        holder.itemView.setBackground(gradientDrawable);
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