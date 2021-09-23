package com.rangerv.spotyhub.views.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.rangerv.spotifywebapi.model.SavedTrackObject;
import com.rangerv.spotyhub.R;
import com.rangerv.spotyhub.extensions.Extensions;

import java.util.ArrayList;
import java.util.List;

public class SavedTrackAdapter extends RecyclerView.Adapter<SavedTrackAdapter.SavedTrackViewHolder> {

    private final List<SavedTrackObject> tracks = new ArrayList<>();

    public void setData(List<SavedTrackObject> tracks){
        this.tracks.clear();
        this.tracks.addAll(tracks);
        notifyDataSetChanged();
    }

    @Override public SavedTrackAdapter.SavedTrackViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new SavedTrackViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_track_1, parent, false));
    }

    @Override public void onBindViewHolder(SavedTrackAdapter.SavedTrackViewHolder holder, int position) {
        holder.bind(tracks.get(position));
    }

    @Override public int getItemCount() {
        return tracks.size();
    }


    public static class SavedTrackViewHolder extends RecyclerView.ViewHolder {
        final TextView trackNameTxt, trackArtistNameTxt;
        final ImageView trackImg;

        public SavedTrackViewHolder(View itemView) {
            super(itemView);
            trackNameTxt = itemView.findViewById(R.id.text_list_item_track_1_track_name);
            trackArtistNameTxt = itemView.findViewById(R.id.text_list_item_track_1_track_artist);
            trackImg = itemView.findViewById(R.id.image_list_item_track_1_cover);
        }

        void bind(SavedTrackObject track){
            trackNameTxt.setText(track.track.name);
            trackArtistNameTxt.setText(track.track.artists.get(0).name);
            new Extensions.ImageLoadTask(track.track.album.images.get(1).url, trackImg).execute();
        }
    }

}
