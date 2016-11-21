package com.example.soehtet.myapplication;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.soehtet.myapplication.wshelpers.WSHelper;
import com.example.soehtet.myapplication.wshelpers.WSListener;

import org.w3c.dom.Text;

import java.io.FileNotFoundException;

/**
 * Created by soehtet on 18/11/16.
 */

public class Register extends Activity {

    TextView username,password,email,pic,phoneno,phonestatus;
    Listener listener;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.layout_register);

        listener = new Listener(this);

        username = (TextView) findViewById(R.id.username);
        password = (TextView) findViewById(R.id.password);
        email = (TextView) findViewById(R.id.email);
        phoneno = (TextView) findViewById(R.id.phonenumber);
        phonestatus = (TextView) findViewById(R.id.email);

        ((Button) findViewById(R.id.register)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                WSHelper helper = new WSHelper("get");

                try {
                    helper.resgister(listener,username.getText().toString(),password.getText().toString(),email.getText().toString(),"","999",true);
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
            }
        });

    }


    class Listener extends WSListener{
        public Listener(Context ctx) {
            super(ctx);
        }

        @Override
        public void onRegisterSuccess(String str) {
            super.onRegisterSuccess(str);
            Log.e("got data", str);
        }
    }


}
