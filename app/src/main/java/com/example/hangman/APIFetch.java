package com.example.hangman;

import android.content.Context;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.List;

public class APIFetch {
    public static void apiFetch(String listName, Context context) throws IOException {

        //todo build function for testing on home wifi
        String APIURL = "http://185.108.171.164:2500/api/%s.json";
        URL url = new URL(String.format(APIURL, listName.toLowerCase()));

        InputStream inputStream = url.openStream();
        Path check = Paths.get(String.valueOf(context.getFilesDir()) + String.format("/%s.json", listName));
        Log.d("inthread", String.valueOf(check));
        Files.copy(inputStream, check, StandardCopyOption.REPLACE_EXISTING);
        //todo add toast message for success/fail


    }

    public static List<String> apiListsFetch() throws JSONException, IOException {

        //todo build function for testing on home wifi
        String APIURL = "http://185.108.171.164:2500/api";
        URL url = new URL(APIURL);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.connect();

        InputStreamReader streamreader = new InputStreamReader(connection.getInputStream());
        BufferedReader reader = new BufferedReader(
                streamreader);

        StringBuffer json = new StringBuffer(1024);
        String tmp = "";
        while ((tmp = reader.readLine()) != null)
            json.append(tmp).append("\n");
        reader.close();

        JSONArray data = new JSONArray(json.toString());

        List<String> temp = new ArrayList<>();
        for (int i = 0; i < data.length(); i++) {
            temp.add((String) data.get(i));
        }

        return temp;
        //todo add toast message for success/fail

    }
}
