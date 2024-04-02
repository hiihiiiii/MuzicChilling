package com.example.muzicchilling;

import android.Manifest;
import android.content.ContentResolver;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import androidx.core.app.ActivityCompat;

import java.util.ArrayList;


public class ListMusicActivity extends AppCompatActivity {
    private static final int REQUEST_PERMISSION=99;
    ArrayList<Song> songArrayList;
    ListView listView;
    SongsAdapter songsAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_music);
        listView= findViewById(R.id.lstSong);

        songArrayList= new ArrayList<>();
        songsAdapter= new SongsAdapter(this,R.layout.song_item,songArrayList);
        listView.setAdapter(songsAdapter);

       if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && ActivityCompat.checkSelfPermission(this,Manifest.permission.READ_MEDIA_AUDIO)!= PackageManager.PERMISSION_GRANTED){
           ActivityCompat.requestPermissions(this, new String[]{
                    Manifest.permission.READ_MEDIA_AUDIO
           }, REQUEST_PERMISSION);
       }else {
           getSongs();
       }

       //mo nhac
     listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
         @Override
         public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
             Song song= songArrayList.get(position);
             Intent openMusicPlayer= new Intent(ListMusicActivity.this, MusicPlayerActivity.class);
             openMusicPlayer.putExtra("song",song);
             startActivity(openMusicPlayer);
         }
     });


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.search_btn,menu);
        MenuItem menuItem= menu.findItem(R.id.search_button);
        SearchView searchView= (SearchView) menuItem.getActionView();

        SearchSong(searchView);

        return super.onCreateOptionsMenu(menu);
    }

    private void SearchSong(SearchView searchView) {
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                filterSongs(newText.toLowerCase());
                return true;
            }
        });
    }
    private void filterSongs(String query){
        ArrayList<Song> filteredList= new ArrayList<>();

        if(songArrayList.size()>0){
            for (Song song : songArrayList){
                if(song.getTitle().toLowerCase().contains(query)){
                    filteredList.add(song);
                }
            }
            if(songsAdapter!= null){
                songsAdapter.getFilter();
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(requestCode== REQUEST_PERMISSION){
            if(grantResults[0] ==PackageManager.PERMISSION_GRANTED) {
                getSongs();
            }else {
                Toast.makeText(this, "Quyen truy cap bi tu choi",Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void getSongs() {
        //read song from device
        ContentResolver contentResolver= getContentResolver();
        Uri songUri= MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
        Cursor cursor= contentResolver.query(songUri,null,null,null,null);
        if(cursor!= null && cursor.moveToFirst()){
            int indexTitle= cursor.getColumnIndex(MediaStore.Audio.Media.TITLE);
            int indexArtist= cursor.getColumnIndex(MediaStore.Audio.Media.ARTIST);
            int indexData= cursor.getColumnIndex(MediaStore.Audio.Media.DATA);
            do{
                String title= cursor.getString(indexTitle);
                String artist= cursor.getString(indexArtist);
                String path= cursor.getString(indexData);
                songArrayList.add(new Song(title,artist,path));
            }while (cursor.moveToNext());
        }
        cursor.close();
        songsAdapter.notifyDataSetChanged();
    }
}