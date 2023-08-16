package com.yos.imusicplayer;
import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.SearchView;
import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
public class MainActivity extends AppCompatActivity {

    private ListView listView;
    private ArrayList<String> mp3FilesList = new ArrayList<String>();
    SearchView searchView;
    private ArrayAdapter<String> arrayAdapter;
    private static final int PERMISSION_REQUEST_CODE = 1;
    @RequiresApi(api = Build.VERSION_CODES.TIRAMISU)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        listView = findViewById(R.id.listView);
        //searchView = findViewById(R.id.searchView);
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED ) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                    PERMISSION_REQUEST_CODE);
            //Toast.makeText(this, "Please allow the permission..", Toast.LENGTH_SHORT).show();
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                    PERMISSION_REQUEST_CODE);
        }
        else
        {
            showstorage();

        }
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_MEDIA_AUDIO)
                != PackageManager.PERMISSION_GRANTED ) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.READ_MEDIA_AUDIO},
                    PERMISSION_REQUEST_CODE);
            //Toast.makeText(this, "Please allow the permission..", Toast.LENGTH_SHORT).show();
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.READ_MEDIA_AUDIO},
                    PERMISSION_REQUEST_CODE);
        }
        else
        {
            showstorage();
        }

    }
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == PERMISSION_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                //Toast.makeText(this, "Granted..", Toast.LENGTH_SHORT).show();
                showstorage();
            } else {
                // Permission denied, handle accordingly (e.g., show a message or disable audio functionality)
            }
        }
    }
    public ArrayList<File> fetchSongs(File file){
        ArrayList<File> arrayList = new ArrayList<File>();
        File [] songs = file.listFiles();
        if(songs !=null){
            for(File myFile: songs){
                if(!myFile.isHidden() && myFile.isDirectory()){
                    arrayList.addAll(fetchSongs(myFile));
                }
                else{
                    if(myFile.getName().endsWith(".mp3") && !myFile.getName().startsWith(".")){
                        arrayList.add(myFile);
                    }
                }
            }
        }
        Collections.sort(arrayList);
        return arrayList;
    }
    public void showstorage(){

        ArrayList<File> mySongs = fetchSongs(Environment.getExternalStorageDirectory());
        String []items = new String[mySongs.size()];
        for(int i=0;i<mySongs.size();i++){
            items[i] = mySongs.get(i).getName().replace(".mp3", "");
        }
        ArrayAdapter<String> adapter;
        adapter = new ArrayAdapter<String>(MainActivity.this, android.R.layout.simple_list_item_1,items);
        listView.setAdapter(adapter);
        //when the items are clicked code below we are writing it here because we dont want to write it two times in above code
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(MainActivity.this,PlaySong.class);
                String currentsong = listView.getItemAtPosition(position).toString();
                intent.putExtra("songList",mySongs);
                intent.putExtra("currentsong",currentsong);
                intent.putExtra("position",position);
                startActivity(intent);
            }
        });
    }
}
