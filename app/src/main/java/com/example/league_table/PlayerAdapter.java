package com.example.league_table;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

public class PlayerAdapter extends RecyclerView.Adapter<PlayerAdapter.PlayerViewHolder> {

    private Context context;
    private List<PlayerItem> playerList;

    public PlayerAdapter(Context context, List<PlayerItem> playerList) {
        this.context = context;
        this.playerList = playerList;
    }

    @NonNull
    @Override
    public PlayerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_player, parent, false);
        return new PlayerViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PlayerViewHolder holder, int position) {
        PlayerItem item = playerList.get(position);

        holder.tvPlayerName.setText(item.getName());
        if (item.getJerseyNumber() > 0) {
            holder.tvPlayerJerseyNumber.setText(String.valueOf(item.getJerseyNumber()));
        } else {
            holder.tvPlayerJerseyNumber.setText("-");
        }
        holder.tvPlayerPosition.setText(translatePosition(item.getPosition()));
    }

    @Override
    public int getItemCount() {
        return playerList.size();
    }

    private String translatePosition(String position) {
        if (position == null) return "N/A";
        switch (position) {
            case "Goalkeeper": return "골키퍼";
            case "Defence": case "Defender": return "수비수";
            case "Midfield": case "Midfielder": return "미드필더";
            case "Offence": case "Attacker": case "Forward": return "공격수";
            default: return position;
        }
    }

    public static class PlayerViewHolder extends RecyclerView.ViewHolder {
        TextView tvPlayerJerseyNumber, tvPlayerPosition, tvPlayerName;
        public PlayerViewHolder(@NonNull View itemView) {
            super(itemView);
            tvPlayerJerseyNumber = itemView.findViewById(R.id.tvPlayerJerseyNumber);
            tvPlayerPosition = itemView.findViewById(R.id.tvPlayerPosition);
            tvPlayerName = itemView.findViewById(R.id.tvPlayerName);
        }
    }
}