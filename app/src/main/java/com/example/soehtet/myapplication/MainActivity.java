package com.example.soehtet.myapplication;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.example.soehtet.myapplication.database.DatabaseHelper;
import com.example.soehtet.myapplication.model.Result;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    Button detail;

    AutoCompleteTextView txtWord;
    TextView txtResult;
    Button btnSearch;

    DatabaseHelper mDBHelper;

    ArrayList<Result> results = new ArrayList<>();
    ArrayList<Result> suggestions = new ArrayList<>();
    ArrayAdapter<String> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mDBHelper = new DatabaseHelper(this);

        detail = (Button) findViewById(R.id.detail);
        detail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this,ReadViewActiivty.class));
            }
        });

        File database = getApplicationContext().getDatabasePath(DatabaseHelper.DBNAME);
        if(false == database.exists()) {
            mDBHelper.getReadableDatabase();
            //Copy db
            if(copyDatabase(this)) {
                Toast.makeText(this, "Copy database succes", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "Copy data error", Toast.LENGTH_SHORT).show();
                return;
            }
        }


        txtWord = (AutoCompleteTextView) findViewById(R.id.txtword);
        txtWord.addTextChangedListener(new CustomTextWatcher());


        txtResult = (TextView) findViewById(R.id.txtResult);
        btnSearch = (Button) findViewById(R.id.btnSearch);

        btnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String tmp = "";
                results = new DatabaseHelper(MainActivity.this).getListProduct(txtWord.getText().toString());
                if (results != null && results.size() >0){

                    tmp += results.get(0).getDefinition();

                    txtResult.setText(tmp);

                }
            }
        });




    }

    private boolean copyDatabase(Context context) {
        try {

            InputStream inputStream = context.getAssets().open(DatabaseHelper.DBNAME);
            String outFileName = DatabaseHelper.DBLOCATION + DatabaseHelper.DBNAME;
            OutputStream outputStream = new FileOutputStream(outFileName);
            byte[]buff = new byte[1024];
            int length = 0;
            while ((length = inputStream.read(buff)) > 0) {
                outputStream.write(buff, 0, length);
            }
            outputStream.flush();
            outputStream.close();
            Log.w("MainActivity","DB copied");
            return true;
        }catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }


    private class CustomTextWatcher implements TextWatcher{

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

        }

        @Override
        public void afterTextChanged(Editable s) {
            String tmp = "";
            ArrayList<String> sug = new ArrayList<>();
            suggestions = new DatabaseHelper(MainActivity.this).getSuggestion(txtWord.getText().toString());
            if (suggestions != null && suggestions.size() >0){

               if (suggestions.size() > 10){
                   for (int i = 0 ; i < 10 ; i++){
                       if (suggestions.get(i).getWordname() != null){
                           sug.add(suggestions.get(i).getWordname().toString());
                       }
                   }
               }else{
                   for (int i = 0 ; i < suggestions.size() ; i++){
                       if (suggestions.get(i).getWordname() != null){
                           sug.add(suggestions.get(i).getWordname().toString());
                       }
                   }
               }
            }
            Log.e("wordlist", sug.toString());
            adapter = new ArrayAdapter<String>(MainActivity.this, android.R.layout.simple_expandable_list_item_2,android.R.id.text1, sug);
            txtWord.setAdapter(adapter);
            txtWord.setThreshold(5);
            txtWord.showDropDown();
        }
    }
}
