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
import java.util.List;

public class ScorerAdapter extends RecyclerView.Adapter<ScorerAdapter.ScorerViewHolder> {

    private Context context;
    private List<ScorerItem> scorerList;

    public ScorerAdapter(Context context, List<ScorerItem> scorerList) {
        this.context = context;
        this.scorerList = scorerList;
    }

    @NonNull
    @Override
    public ScorerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // item_scorer.xml 레이아웃 사용
        View view = LayoutInflater.from(context).inflate(R.layout.item_scorer, parent, false);
        return new ScorerViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ScorerViewHolder holder, int position) {
        ScorerItem item = scorerList.get(position);

        // 데이터 설정
        holder.tvRank.setText(String.valueOf(item.getRank()));
        holder.tvPlayerName.setText(item.getPlayerName());
        holder.tvTeamName.setText(item.getTeamName()); // 팀 이름 (TLA)
        holder.tvGoals.setText(item.getGoals() + "골"); // 골 수

        // Glide로 팀 엠블럼 이미지 로드
        Glide.with(context)
                .load(item.getTeamCrest()) // 팀 엠블럼 URL
                .placeholder(R.drawable.pl) // 로딩 중 기본 이미지 (예시)
                .error(R.drawable.pl)       // 에러 시 기본 이미지 (예시)
                .into(holder.imgTeamCrest); // 표시할 ImageView
    }

    @Override
    public int getItemCount() {
        return scorerList.size();
    }

    // ViewHolder 클래스: item_scorer.xml의 뷰들을 참조
    public static class ScorerViewHolder extends RecyclerView.ViewHolder {
        TextView tvRank, tvPlayerName, tvTeamName, tvGoals;
        ImageView imgTeamCrest;

        public ScorerViewHolder(@NonNull View itemView) {
            super(itemView);
            tvRank = itemView.findViewById(R.id.tvRank);
            tvPlayerName = itemView.findViewById(R.id.tvPlayerName);
            tvTeamName = itemView.findViewById(R.id.tvTeamName);
            tvGoals = itemView.findViewById(R.id.tvGoals);
            imgTeamCrest = itemView.findViewById(R.id.imgTeamCrest);
        }
    }
}