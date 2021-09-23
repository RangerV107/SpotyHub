package com.rangerv.spotyhub.views.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.rangerv.spotifywebapi.model.TrackObject;
import com.rangerv.spotyhub.R;
import com.rangerv.spotyhub.extensions.Extensions;

import java.util.ArrayList;
import java.util.List;

public class TrackAdapter extends RecyclerView.Adapter<TrackAdapter.TrackViewHolder> {

    private final List<TrackObject> tracks = new ArrayList<>();

    public void setData(List<TrackObject> tracks){
        this.tracks.clear();
        this.tracks.addAll(tracks);
        notifyDataSetChanged();
    }

    @Override public TrackAdapter.TrackViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new TrackViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_track_1, parent, false));
    }

    @Override public void onBindViewHolder(TrackAdapter.TrackViewHolder holder, int position) {
        holder.bind(tracks.get(position));
    }

    @Override public int getItemCount() {
        return tracks.size();
    }

    public static class TrackViewHolder extends RecyclerView.ViewHolder {
        final TextView trackNameTxt, trackArtistNameTxt;
        final ImageView trackImg;

        public TrackViewHolder(View itemView) {
            super(itemView);
            trackNameTxt = itemView.findViewById(R.id.text_list_item_track_1_track_name);
            trackArtistNameTxt = itemView.findViewById(R.id.text_list_item_track_1_track_artist);
            trackImg = itemView.findViewById(R.id.image_list_item_track_1_cover);
        }

        void bind(TrackObject track){
            trackNameTxt.setText(track.name);
            trackArtistNameTxt.setText(track.artists.get(0).name);
            new Extensions.ImageLoadTask(track.album.images.get(1).url, trackImg).execute();
        }
    }


}
