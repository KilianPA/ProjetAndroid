package com.projet.kilian.mathieu;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Binder;
import android.os.Environment;
import android.os.IBinder;

import java.io.File;

import android.app.DownloadManager;
import android.net.Uri;
import android.util.Log;

import java.util.UUID;

public class ServiceDownload extends Service {

    public ServiceDownload() {
    }

    public class MonBinder extends Binder {
        // Le Binder possède une méthode pour renvoyer le Service
        public ServiceDownload getService() {
            return ServiceDownload.this;
        }
    }

    private MonBinder mBinder = new MonBinder();

    @Override
    public IBinder onBind(Intent intent) {
        return mBinder;
    }

    public String telecharge() {
        return "test";
    }

    public long download(String downloadUrlOfImage) {
        Log.i("[LOG KILIAN]", "DOWNLOAD RUN");
        String filename = UUID.randomUUID().toString();

        File direct =
                new File(Environment
                        .getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES)
                        .getAbsolutePath() + "/" + "projet" + "/");


        if (!direct.exists()) {
            direct.mkdir();
        }

        DownloadManager dm = (DownloadManager) getSystemService(DOWNLOAD_SERVICE);
        Uri downloadUri = Uri.parse(downloadUrlOfImage);
        DownloadManager.Request request = new DownloadManager.Request(downloadUri);
        request.setAllowedNetworkTypes(DownloadManager.Request.NETWORK_WIFI | DownloadManager.Request.NETWORK_MOBILE)
                .setAllowedOverRoaming(false)
                .setTitle(filename)
                .setMimeType("image/jpeg")
                .setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED)
                .setDestinationInExternalPublicDir(Environment.DIRECTORY_PICTURES,
                        File.separator + "projet" + File.separator + filename);

        return dm.enqueue(request);

    }
}

