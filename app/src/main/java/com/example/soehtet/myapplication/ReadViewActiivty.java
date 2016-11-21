package com.example.soehtet.myapplication;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by soehtet on 30/9/16.
 */

public class ReadViewActiivty extends Activity {

    TextView txtData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_readview);

        txtData = (TextView) findViewById(R.id.data);

        readData();
    }



    private void readData(){
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                HttpURLConnection connection = null;
                InputStream inputStream = null;

                try{
                    URL url = new URL("http://www.burmesesoungs.com/app_book/Tears_of_the_Giraffe.txt");
                    connection = (HttpURLConnection) url.openConnection();
                    connection.setRequestMethod("GET");

                    int statusCode = connection.getResponseCode();
                    if (statusCode == 200){
                        Log.i("Got text!", "text");
                        BufferedReader in = new BufferedReader(new InputStreamReader(url.openStream()));
                        StringBuffer sb = new StringBuffer(0);
                        String line;
                        String result="";

                        while ((line = in.readLine()) != null) {
                            // str is one line of text; readLine() strips the newline character(s)
                            sb.append(line);
                            sb.append("\n");
                        }

                        displayText(sb.toString());
                    }
                }catch (Exception e){
                    e.printStackTrace();
                }finally {
                    if (connection != null){
                        connection.disconnect();
                    }
                }
            }
        });

        thread.start();
    }

    void displayText(final String result){
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                txtData.setText(result);
            }
        });
    }
}
