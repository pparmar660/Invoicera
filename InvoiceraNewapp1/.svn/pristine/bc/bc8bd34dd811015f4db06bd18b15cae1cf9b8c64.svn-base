package com.invoicera.Webservices;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Environment;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by Parvesh on 22/7/15.
 */
public class DownloadPdfFile {

    Context context;
    public  DownloadPdfFile(Context context){
        this.context=context;
    }

    // downloadAndOpenPDF
  public   void downloadAndOpenPDF(final String pdfUrl) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                Uri path = Uri.fromFile(downloadFile(pdfUrl));
                try {
                    Intent intent = new Intent(Intent.ACTION_VIEW);
                    intent.setDataAndType(path, "application/pdf");
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    context.startActivity(intent);
                    // finish();
                } catch (ActivityNotFoundException e) {

                }
            }
        }).start();

    }

    File downloadFile(String dwnload_file_path) {
        File file = null;
        try {

            URL url = new URL(dwnload_file_path);
            HttpURLConnection urlConnection = (HttpURLConnection) url
                    .openConnection();

            urlConnection.setRequestMethod("GET");
            urlConnection.setDoOutput(true);

            // connect
            urlConnection.connect();

            // set the path where we want to <span id="IL_AD11"
            // class="IL_AD">save the</span> file
            File SDCardRoot = Environment.getExternalStorageDirectory();
            // create a new file, to save the downloaded file
            file = new File(SDCardRoot, "Invoicera.pdf");

            FileOutputStream fileOutput = new FileOutputStream(file);

            // Stream used for reading the data from the internet
            InputStream inputStream = urlConnection.getInputStream();

            // create a buffer...
            byte[] buffer = new byte[1024 * 1024];
            int bufferLength = 0;

            while ((bufferLength = inputStream.read(buffer)) > 0) {
                fileOutput.write(buffer, 0, bufferLength);

            }
            // close the output stream when complete //
            fileOutput.close();
            // setText("<span id="IL_AD8" class="IL_AD">Download</span> Complete. Open PDF Application installed in the device.");

        } catch (final Exception e) {
            e.printStackTrace();
        }
        return file;
    }
}
