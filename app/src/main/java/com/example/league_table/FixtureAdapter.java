package com.example.league_table;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;

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

    @Override
    public void onBindViewHolder(@NonNull FixtureViewHolder holder, int position) {
        FixtureItem item = fixtureList.get(position);

        holder.tvHomeTeam.setText(item.getHomeTeamName());
        holder.tvAwayTeam.setText(item.getAwayTeamName());
        holder.tvMatchDate.setText(formatDate(item.getMatchDate()));

        Glide.with(context).load(item.getHomeTeamCrest()).into(holder.imgHomeCrest);
        Glide.with(context).load(item.getAwayTeamCrest()).into(holder.imgAwayCrest);
    }

    @Override
    public int getItemCount() {
        return fixtureList.size();
    }

    // UTC 날짜 형식을 한국 시간(KST)으로 변환하는 메서드
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

        public FixtureViewHolder(@NonNull View itemView) {
            super(itemView);
            tvMatchDate = itemView.findViewById(R.id.tvMatchDate);
            tvHomeTeam = itemView.findViewById(R.id.tvHomeTeam);
            tvAwayTeam = itemView.findViewById(R.id.tvAwayTeam);
            imgHomeCrest = itemView.findViewById(R.id.imgHomeCrest);
            imgAwayCrest = itemView.findViewById(R.id.imgAwayCrest);
        }
    }
}