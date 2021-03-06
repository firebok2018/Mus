package musicp.firebok.com.music;

import android.Manifest;
import android.content.ContentResolver;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.provider.MediaStore;
import android.service.autofill.FieldClassification;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private static final int MY_PERMISSION_REQUEST=1;
    MediaPlayer mp= new MediaPlayer();
    ArrayList<String> arraylist;
    ListView listview;
    ArrayAdapter<String> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED){
            if (ActivityCompat.shouldShowRequestPermissionRationale(MainActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE)){
                ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, MY_PERMISSION_REQUEST);
            }else {
                ActivityCompat.requestPermissions(MainActivity.this, new  String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, MY_PERMISSION_REQUEST);
            }

        }else{
            doStuff();
            idArraylist();
        }


    }
    public  void idArraylist(){
        arraylist= new ArrayList<String>();
        for (int i=0;i<arraylist.size();i++){
            Toast.makeText(this, "cantidad de musicas  "+i, Toast.LENGTH_SHORT).show();
        }

    }
    public  void doStuff(){
        ListView listView= (ListView) findViewById(R.id.listView);
        arraylist = new ArrayList<String>();
        getMusic();
        adapter= new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, arraylist);
        listView.setAdapter(adapter);


        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Toast.makeText(MainActivity.this, "Seleccionado"+id + position, Toast.LENGTH_SHORT).show();
                switch (position){
                    default:
                        if (!mp.isPlaying()) { //si no suena al play
                            mp.start();
                        } else if (mp.isPlaying()) { //si suena la paro
                            mp.pause();
                        }
                        break;
                }


            }

        });
    }
    public void getMusic(){
        ContentResolver contentResolver = getContentResolver();
        Uri songUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
        Cursor songCursor = contentResolver.query(songUri,null,null,null,null);

        if (songCursor != null && songCursor.moveToFirst()){
            int songTitle= songCursor.getColumnIndex(MediaStore.Audio.Media.TITLE);
            int songArtista= songCursor.getColumnIndex(MediaStore.Audio.Media.ARTIST);

            do{
                String currentTitle=songCursor.getString(songTitle);
                String currentArtista=songCursor.getString(songArtista);



                arraylist.add(currentTitle+ "\n"+ currentArtista +"\t");
            }while (songCursor.moveToNext());
        }
    }

    @Override
    public   void onRequestPermissionsResult (int requestCode, String[] permissions, int[] grantResults) {

        switch (requestCode) {
            case MY_PERMISSION_REQUEST:
                if (grantResults.length >0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                    if (ContextCompat.checkSelfPermission(MainActivity.this,Manifest.permission.READ_EXTERNAL_STORAGE ) == PackageManager.PERMISSION_GRANTED) {
                        Toast.makeText(this, "Permission granted",Toast.LENGTH_SHORT).show();
                        doStuff();
                    }else {
                        Toast.makeText(this, "not Permission granted",Toast.LENGTH_SHORT).show();
                        finish();
                    }
                }
                return;
        }
    }
}
