package com.example.muzicchilling;

import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.SeekBar;
import android.widget.TextView;


import androidx.appcompat.app.AppCompatActivity;

import java.io.IOException;


public class MusicPlayerActivity extends AppCompatActivity implements View.OnClickListener {
    TextView tvBegin, tvEnd,tvTitle, tvArtist;
    SeekBar seekBarTime, seekBarVolumn;
    ImageButton btnPlay, btnBack;
    MediaPlayer mediaPlayer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_music_player);

        Song song=(Song) getIntent().getSerializableExtra("song");

        tvBegin= findViewById(R.id.txtBegin);
        tvEnd= findViewById(R.id.txtEnd);
        seekBarTime= findViewById(R.id.seekbarTime);
        seekBarVolumn= findViewById(R.id.seekbarVolumn);
        btnPlay= findViewById(R.id.btnPlay);
        tvTitle= findViewById(R.id.txtSong);
        tvArtist= findViewById(R.id.txtSinger);

        tvTitle.setText(song.getTitle());
        tvArtist.setText(song.getArtist());

      btnBack= findViewById(R.id.btnback);
      btnBack.setOnClickListener(new View.OnClickListener() {
          @Override
          public void onClick(View v) {
              if(mediaPlayer.isPlaying()){
                  mediaPlayer.stop();
              }
              finish();
          }
      });

        mediaPlayer= new MediaPlayer();
        try {
            mediaPlayer.setDataSource(song.getPath());
            mediaPlayer.prepare();
        } catch (IOException e) {
            e.printStackTrace();
        }
        mediaPlayer.setLooping(true);
        mediaPlayer.seekTo(0);
        mediaPlayer.setVolume(0.5f, 0.5f);
        String duration= millisecondsToString(mediaPlayer.getDuration());
        tvEnd.setText(duration);
        mediaPlayer.start();


        btnPlay.setOnClickListener(this);
        //set volumn
        seekBarVolumn.setProgress(50);
        seekBarVolumn.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                float volume=progress/100f;
                mediaPlayer.setVolume(volume,volume);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        seekBarTime.setMax(mediaPlayer.getDuration());
        seekBarTime.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if(fromUser){
                    mediaPlayer.seekTo(progress);
                    seekBar.setProgress(progress);
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        new Thread(new Runnable() {
            @Override
            public void run() {
                while (mediaPlayer!=null){
                    if(mediaPlayer.isPlaying()){
                        try {
                            final double current=mediaPlayer.getCurrentPosition();
                            final  String elapsedTime=millisecondsToString((int) current);
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    tvBegin.setText(elapsedTime);
                                    seekBarTime.setProgress((int) current);
                                }
                            });
                            Thread.sleep(1000);

                        }catch (InterruptedException e){

                        }
                    }
                }
            }
        }).start();
    }
    public  String millisecondsToString(int time){
        String elapsedTime="";
        int minutes=time/1000/60;
        int seconds= time/1000%60;
        elapsedTime=minutes+":";
        if(seconds<10){
            elapsedTime+="0";

        }
        elapsedTime+=seconds;
        return  elapsedTime;
    }
    @Override
    public void onClick(View v) {
        if(v.getId()==R.id.btnPlay){
            if(mediaPlayer.isPlaying()){
                //playing
                mediaPlayer.pause();;
                btnPlay.setBackgroundResource(R.drawable.ic_play);
            }else{
                //pause
                mediaPlayer.start();
                btnPlay.setBackgroundResource(R.drawable.ic_pause);
            }
        }
    }
}