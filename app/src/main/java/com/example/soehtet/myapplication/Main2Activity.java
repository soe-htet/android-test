package com.example.soehtet.myapplication;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import com.example.soehtet.myapplication.adapters.PostAdapter;
import com.example.soehtet.myapplication.helpers.Helper;
import com.example.soehtet.myapplication.model.Post;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

public class Main2Activity extends AppCompatActivity {


    RecyclerView lstPosts;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);

        lstPosts = (RecyclerView) findViewById(R.id.lstPosts);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        lstPosts.setLayoutManager(mLayoutManager);


        getPosts();

    }




    private void getPosts(){
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                HttpURLConnection connection = null;
                InputStream inputStream = null;

                try{
                    URL url = new URL("http://10.0.1.48/twitter/posts.php?uuid=&text=&id=29");
                    connection = (HttpURLConnection) url.openConnection();
                    connection.setRequestMethod("POST");

                    int statusCode = connection.getResponseCode();

                    Log.i("Got soung!", "Ggot");
                    inputStream = new BufferedInputStream(connection.getInputStream());
                    String response = Helper.convertInputStramtoString(inputStream);
                    generatePosts(response);
                    Log.i("Got soung!", response);
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
    ArrayList<Post> posts = new ArrayList<>();

    private void generatePosts(final String response) {

        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                try {
                    JSONObject postJson = new JSONObject(response);
                    JSONArray array = postJson.getJSONArray("posts");
                    for (int i=0 ;i < array.length(); i++){
                        JSONObject obj = array.getJSONObject(i);

                        Post post = new Post(obj.getInt("id"), obj.getString("username"), obj.getString("text"), obj.getString("path"), obj.getString("date"), obj.getString("ava"));

                        posts.add(post);
                    }

                    lstPosts.setAdapter(new PostAdapter(posts,Main2Activity.this));

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }
}
