package com.example.muzicchilling;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;

public class SongsAdapter extends ArrayAdapter<Song> {
    ArrayList<Song> songs;

    public SongsAdapter(@NonNull Context context, int textViewResourceId, @NonNull List<Song> objects) {
        super(context, 0, textViewResourceId, objects);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        convertView= LayoutInflater.from(parent.getContext()).inflate(R.layout.song_item,null);

        TextView tvTitle= convertView.findViewById(R.id.txtSongname);
        TextView tvArtist=convertView.findViewById(R.id.txtAuthor);
        Song song= getItem(position);
        tvTitle.setText(song.getTitle());
        tvArtist.setText(song.getArtist());
        return convertView;
    }
//    public  void filterSongs(ArrayList<Song> filteredList){
//        songs= filteredList;
//        notifyDataSetChanged();
//    }

}
