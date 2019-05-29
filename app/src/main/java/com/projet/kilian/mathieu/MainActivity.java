package com.projet.kilian.mathieu;

import android.app.AlertDialog;
import android.app.DownloadManager;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.graphics.Color;
import android.os.Environment;
import android.os.IBinder;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.graphics.Palette;
import android.support.v7.widget.Toolbar;
import android.text.InputType;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.TableRow;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.view.Gravity;
import android.widget.TextView;
import android.widget.TableLayout;
import android.widget.Toast;
import android.support.v4.app.ActivityCompat;
import android.Manifest;

import java.io.File;

import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        registerReceiver(onDownloadComplete, new IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE));

        ActivityCompat.requestPermissions(this, new String[]{
                Manifest.permission.WRITE_EXTERNAL_STORAGE
        }, 1);

        Images image = new Images("https://cdn-media.rtl.fr/cache/yTmuq70Y1RXtxeCaAGfbAg/880v587-0/online/image/2019/0227/7797067326_pikachu-ryan-reynolds-pret-pour-son-enquete.JPG","pikapika");
        Images image2 = new Images("https://www.presse-citron.net/wordpress_prod/wp-content/uploads/2018/11/heres-the-first-trailer-detective-pikachu-starring-ryan-reynolds-social-e1542153165941.jpg","letest2");

        //On ouvre la base de donnÈes pour Ècrire dedans
        imageBdd.open();

        //On insËre le livre que l'on vient de crÈer
        imageBdd.insertImage(image);
        imageBdd.insertImage(image2);

        imageBdd.cleardelatable();
    }
    ImagesBDD imageBdd = new ImagesBDD(this);
    private ArrayList arrayList;
    private ArrayAdapter adapter;
    private ServiceDownload serviceDownload;
    private long downloadID;

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.add_item:
                AlertDialog.Builder monBuilder = new AlertDialog.Builder(MainActivity.this);
                monBuilder.setTitle("Ajouter une photo");
                final EditText input = new EditText(this);
// Specify the type of input expected; this, for example, sets the input as a password, and will mask the text
                input.setInputType(InputType.TYPE_CLASS_TEXT);
                monBuilder.setView(input);

                monBuilder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        serviceDownload.download(input.getText().toString());
                    }
                });
                monBuilder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
                monBuilder.show();
        }
        return super.onOptionsItemSelected(item);
    }

    private BroadcastReceiver onDownloadComplete = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            //Fetching the download id received with the broadcast
            long id = intent.getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID, -1);
            //Checking if the received broadcast is for our enqueued download by matching download id
            if (downloadID == id) {
                Toast.makeText(MainActivity.this, "Download Completed", Toast.LENGTH_SHORT).show();
                loadContent();
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
        this.loadContent();
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

    public void loadContent() {
        RelativeLayout container = findViewById(R.id.container);
        container.removeAllViews();
        String path;
//        final String[] images = {"filename.jpg", "9115df8d-f4c6-4ae4-92e2-c1a918da0b6c", "http://fr.web.img6.acsta.net/videothumbnails/19/02/26/18/07/4429308.jpg" , "cfbbfd6a-9ad5-41e6-b5fb-798574537e75", "5109cac9-29c8-4ce4-84bb-69c17ac52191"};
        final ArrayList<Images> images = imageBdd.getAllImages();

        //Si un livre est retournÈ (donc si le livre ‡ bien ÈtÈ ajoutÈ ‡ la BDD)
        final TableLayout table = new TableLayout(getApplicationContext());
        table.setColumnStretchable(3, true);

        for (int i = 0; i < images.size(); i++) {
            Boolean isUrl = false;


            ImageView image = new ImageView(this);

            if (images.get(i).getPath().contains("http")) {
                isUrl = true;
                path = images.get(i).getPath();
                Picasso
                        .get()
                        .load(path)
                        .resize(500, 500)
                        .centerInside().into(image);
            } else {
                path = Environment
                        .getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES)
                        .getAbsolutePath() + "/" + "projet/" + images.get(i).getPath();
            File f = new File(path);
            Picasso
                    .get()
                    .load(f)
                    .resize(500, 500)
                    .centerInside().into(image);
            }

            final int finalI = i;
            final String finalPath = path;
            final Boolean finalIsUrl = isUrl;
            image.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent monIntent = new Intent(getApplicationContext(), PhotoEnGrandActivite.class);
                    Bundle monBundle = new Bundle();
                    if (finalIsUrl) {
                        Log.i("LOG KILIAN", "ici url");
                        monBundle.putString("url", images.get(finalI).getPath());
                    } else {
                        Log.i("LOG KILIAN", "ici images");
                        monBundle.putString("path", finalPath);
                    }
                    monIntent.putExtras(monBundle);
                    startActivity(monIntent);
                }
            });

            TableRow[] tableRow = new TableRow[images.size()];
            tableRow[i] = new TableRow(getApplicationContext());
            tableRow[i].setGravity(Gravity.CENTER);

            TextView pos = new TextView(getApplicationContext());
            pos.setGravity(Gravity.LEFT);
            pos.setPadding(80, 80, 80, 80);
            pos.setText(images.get(i).getPath());

            Button btn = new Button(getApplicationContext());
            btn.setBackgroundColor(Color.TRANSPARENT);
            btn.setCompoundDrawablesRelativeWithIntrinsicBounds(R.drawable.ic_file_download_black_24dp, 0, 0, 0);
            btn.setGravity(Gravity.RIGHT);
            btn.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    serviceDownload.download(images.get(finalI).getPath());
                }
            });

            if (image.getParent() != null) {
                ((ViewGroup) image.getParent()).removeView(image); // <- fix
            }

            tableRow[i].addView(image, new TableRow.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, android.view.ViewGroup.LayoutParams.WRAP_CONTENT, 2));
            tableRow[i].addView(pos, new TableRow.LayoutParams(android.view.ViewGroup.LayoutParams.WRAP_CONTENT, android.view.ViewGroup.LayoutParams.WRAP_CONTENT, 10));
            if (isUrl) {
                tableRow[i].addView(btn, new TableRow.LayoutParams(android.view.ViewGroup.LayoutParams.WRAP_CONTENT, android.view.ViewGroup.LayoutParams.WRAP_CONTENT));
            }
            table.addView(tableRow[i]);

        }

        container.addView(table);

    }

//    public void onClickLiason(View view) {
//        Intent monIntent = new Intent(this, ServiceDownload.class);
//        bindService(monIntent, serviceConnection, Context.BIND_AUTO_CREATE);
//    }
}
