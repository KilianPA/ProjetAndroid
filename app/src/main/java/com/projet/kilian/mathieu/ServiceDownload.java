package com.projet.kilian.mathieu;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.Environment;
import android.os.IBinder;

import java.io.File;

import android.app.DownloadManager;
import android.net.Uri;
import android.util.Log;

import java.util.ArrayList;
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

    public long download(ArrayList<Images> listesImages, ImagesBDD imageBdd) {
        Log.i("[LOG KILIAN]", "DOWNLOAD RUN");
        // On boucle sur listesImages et on telecharge chaque photo
        for (int i = 0; i < listesImages.size(); i++) {
            Log.i("[LOG KILIAN]", listesImages.get(i).getPath());

            //On génère un nom à notre image
            String filename = UUID.randomUUID().toString();

            File direct =
                    new File(Environment
                            .getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES)
                            .getAbsolutePath() + "/" + "projet" + "/");

            // Si notre dossier n'existe pas on le créé
            if (!direct.exists()) {
                direct.mkdir();
            }
            // Pour telecharger nos images on utilise le DowloadManager
            //https://developer.android.com/reference/android/app/DownloadManager
            DownloadManager dm = (DownloadManager) getSystemService(DOWNLOAD_SERVICE);
            Uri downloadUri = Uri.parse(listesImages.get(i).getPath());
            DownloadManager.Request request = new DownloadManager.Request(downloadUri);
            request.setAllowedNetworkTypes(DownloadManager.Request.NETWORK_WIFI | DownloadManager.Request.NETWORK_MOBILE)
                    .setAllowedOverRoaming(false)
                    .setTitle(filename)
                    .setMimeType("image/jpeg")
                    .setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED)
                    .setDestinationInExternalPublicDir(Environment.DIRECTORY_PICTURES,
                            File.separator + "projet" + File.separator + filename);

            imageBdd.open();
            imageBdd.updatePath(listesImages.get(i).getId(), filename);
            imageBdd.close();
            // On ajoute le telechargement à la queue
            dm.enqueue(request);
        }
        // On return une valeur pour signifier à notre BroadCast Receiver que les téléchargements sont terminés
        return 1;

    }
}

