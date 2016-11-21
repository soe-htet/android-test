package com.example.soehtet.myapplication.helpers;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * Created by soehtet on 13/10/16.
 */

public class Helper {



    public static String convertInputStramtoString(InputStream inputStream) throws IOException {

        BufferedReader bf = new BufferedReader(new InputStreamReader(inputStream));
        String line = "";
        String result = "";

        while ((line = bf.readLine()) != null){
            result += line;
        }

        if (inputStream != null){
            inputStream.close();
        }

        return result;
    }
}
