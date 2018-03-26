package com.khadir.android.try3_v1;

import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.drawable.Drawable;
import android.media.MediaPlayer;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;

public class MainActivity extends AppCompatActivity implements MediaPlayer.OnCompletionListener {

    int song_playing_number = 0;
    String songs[] = new String[10];
    MediaPlayer mediaPlayer;
    CardView cardView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        TextView t, t1, nos;
        t = findViewById(R.id.id);
        t1 = findViewById(R.id.image_path);
        nos = findViewById(R.id.nos);
        cardView = findViewById(R.id.card);

        mediaPlayer = new MediaPlayer();
        mediaPlayer.setOnCompletionListener(this);

        LinearLayout root = findViewById(R.id.root);

        Uri uri_for_album_art = MediaStore.Audio.Albums.EXTERNAL_CONTENT_URI;
        Uri uri_for_songs = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;

        String p[] = {MediaStore.Audio.Albums.ALBUM_ART, MediaStore.Audio.Albums.ALBUM, MediaStore.Audio.Albums.NUMBER_OF_SONGS};
        String s[] = {MediaStore.Audio.Media.ALBUM, MediaStore.Audio.Media.DISPLAY_NAME, MediaStore.Audio.Media.DATA};
        String selection = MediaStore.Audio.Media.ALBUM + "=?";

        Cursor cursor = getContentResolver().query(uri_for_album_art, p, null, null, MediaStore.Audio.Albums.ALBUM_KEY);
        if (cursor != null) {
            Log.v("MainActivity", "cursor is not empty");
            cursor.moveToFirst();//1
//            cursor.moveToNext();//2
//            cursor.moveToNext();//3
//            cursor.moveToNext();//4 merci
//            cursor.moveToNext();//5 aandrudu
//            cursor.moveToNext();//6 baahubali
//            cursor.moveToNext();//6 baahubali
            Log.v("MainActivity", "album art is " + cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Audio.Albums.ALBUM_ART)));
            String path = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Audio.Albums.ALBUM_ART));
            t1.setText(path);

            String album = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Audio.Albums.ALBUM));
            t.setTextColor(Color.WHITE);
            t.setText(album);

            String nos1 = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Audio.Albums.NUMBER_OF_SONGS));
            nos.setText(getString(R.string.No_of_songs_are) + " " + nos1);

            Bitmap bm = BitmapFactory.decodeFile(path);
            ImageView image = findViewById(R.id.image);
//            image.setImageBitmap(bm);
            Drawable drawable = Drawable.createFromPath(path);
            cardView.setBackground(drawable);

            Cursor songsCursor = getContentResolver().query(uri_for_songs, s, selection, new String[]{album}, null);
            if (songsCursor != null) {
                int i = 0;
                songsCursor.moveToFirst();
                do {
                    TextView textView = new TextView(this);
                    String song_name = songsCursor.getString(songsCursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DISPLAY_NAME));
                    String data = songsCursor.getString(songsCursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DATA));
                    songs[i++] = data;
                    Log.v("MainActivity", "song name is " + song_name);
                    textView.setText(song_name);
                    root.addView(textView);
                } while (songsCursor.moveToNext());
            }
            songsCursor.close();
        } else {
            Log.v("MainActivity", "cursor is!!!!!!!! empty");
        }
        cursor.close();

        try {
            mediaPlayer.setDataSource(songs[song_playing_number]);
            mediaPlayer.prepare();
        } catch (IOException e) {
            e.printStackTrace();
        }

        mediaPlayer.start();
        mediaPlayer.seekTo(200000);


//        if (mediaPlayer.getCurrentPosition() == mediaPlayer.getDuration()) {
//            Toast.makeText(this, "completed", Toast.LENGTH_SHORT).show();
//        }
    }

    @Override
    public void onCompletion(MediaPlayer mp) {
        Toast.makeText(getApplicationContext(), "completed playing a song", Toast.LENGTH_LONG).show();

        song_playing_number++;
        mp.reset();
        try {
            mp.setDataSource(songs[song_playing_number]);
            mp.prepare();
        } catch (IOException e) {
            e.printStackTrace();
        }
        mp.start();
        mp.seekTo(200000);
    }
}
