package com.projet.kilian.mathieu;

import android.app.DownloadManager;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;
import android.support.v4.app.ActivityCompat;
import android.Manifest;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        registerReceiver(onDownloadComplete,new IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE));

        ActivityCompat.requestPermissions(this, new String[]{
                Manifest.permission.WRITE_EXTERNAL_STORAGE
        }, 1);

        Button btn = (Button) findViewById(R.id.addBtn);
        ListView list = (ListView) findViewById(R.id.list_view);
        arrayList = new ArrayList<String>();

        // Adapter: You need three parameters 'the context, id of the layout (it will be where the data is shown),
        // and the array that contains the data
        adapter = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_spinner_item, arrayList);
        list.setAdapter(adapter);

    }
    private ArrayList arrayList;
    private ArrayAdapter adapter;
    private ServiceDownload serviceDownload;
    private long downloadID;

    private BroadcastReceiver onDownloadComplete = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            //Fetching the download id received with the broadcast
            long id = intent.getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID, -1);
            //Checking if the received broadcast is for our enqueued download by matching download id
            if (downloadID == id) {
                Toast.makeText(MainActivity.this, "Download Completed", Toast.LENGTH_SHORT).show();
            }
        }
    };

    private ServiceConnection serviceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            serviceDownload = ((ServiceDownload.MonBinder) service).getService();
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            serviceDownload = null;
        }
    };

    @Override
    public void onStart() {
        super.onStart();
        Intent monIntent = new Intent(this, ServiceDownload.class);
        bindService(monIntent, serviceConnection, Context.BIND_AUTO_CREATE);
    }

    public void onClickDownload(View view) {
        if (serviceDownload != null) {

//            EditText editText = (EditText)findViewById(R.id.url_input);
//            String url = editText.getText().toString();
//            String downloadUrlOfImage = "https://static1.millenium.org/articles/4/33/04/44/@/975216-pokemon-detective-pikachu-cartes-article_m-1.jpg";
//            downloadID = serviceDownload.download(url);
//            Toast.makeText(this, "test", Toast.LENGTH_LONG).show();
        }
    }

    public void addItems(View view) {
        arrayList.add("TESET");
        // next thing you have to do is check if your adapter has changed
        adapter.notifyDataSetChanged();
    }

//    public void onClickLiason(View view) {
//        Intent monIntent = new Intent(this, ServiceDownload.class);
//        bindService(monIntent, serviceConnection, Context.BIND_AUTO_CREATE);
//    }
}
