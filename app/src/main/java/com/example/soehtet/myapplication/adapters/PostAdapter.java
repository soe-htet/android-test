package com.example.soehtet.myapplication.adapters;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.soehtet.myapplication.R;
import com.example.soehtet.myapplication.model.Post;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;

import java.util.ArrayList;

/**
 * Created by soehtet on 13/10/16.
 */

public class PostAdapter extends RecyclerView.Adapter<PostAdapter.MyViewHolder> {


    ArrayList<Post> posts;
    Context context;

    public PostAdapter(ArrayList<Post> posts, Context context){

        this.posts = posts;
        this.context = context;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.cell_post, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, int position) {
        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(context).build();
        ImageLoader.getInstance().init(config);
        DisplayImageOptions options = new DisplayImageOptions.Builder()
                .showImageOnLoading(android.R.color.transparent)
                .cacheInMemory(true).cacheOnDisc(true)
                .bitmapConfig(Bitmap.Config.RGB_565).build();
        DisplayImageOptions userimgoptions = new DisplayImageOptions.Builder()
                .displayer(new RoundedBitmapDisplayer(50))
                .showImageOnLoading(android.R.color.transparent)
                .cacheInMemory(true).cacheOnDisc(true)
                .bitmapConfig(Bitmap.Config.RGB_565).build();
        ImageLoader.getInstance().displayImage(posts.get(position).getPathImage().replace("localhost", "10.0.1.48"), holder.postImage, options ,new ImageLoadingListener() {
            @Override
            public void onLoadingStarted(String imageUri, View view) {

            }

            @Override
            public void onLoadingFailed(String imageUri, View view, FailReason failReason) {
                    holder.postImage.setVisibility(View.GONE);
            }

            @Override
            public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
                if (loadedImage == null){
                    holder.postImage.setVisibility(View.GONE);
                }else
                {
                    holder.postImage.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onLoadingCancelled(String imageUri, View view) {

            }
        });
        ImageLoader.getInstance().displayImage(posts.get(position).getAva().replace("localhost","10.0.1.48"), holder.profile , userimgoptions);
        holder.username.setText(posts.get(position).getUsername());
        holder.time.setText(posts.get(position).getDate());
        holder.post.setText(posts.get(position).getText());
    }

    @Override
    public int getItemCount() {
        return posts.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        ImageView profile, postImage;
        TextView username,time,post;

        public MyViewHolder(View view) {
            super(view);
            profile = (ImageView) view.findViewById(R.id.profile);
            postImage = (ImageView) view.findViewById(R.id.postImg);
            username = (TextView) view.findViewById(R.id.username);
            time = (TextView) view.findViewById(R.id.postTime);
            post = (TextView) view.findViewById(R.id.post);
        }
    }
}
