package com.example.hangman.ui.main;

import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;

import com.example.hangman.APIFetch;
import com.example.hangman.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

/**
 * A placeholder fragment containing a simple view.
 */
public class ImportFragment extends Fragment implements View.OnClickListener {
    Handler handler;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup viewGroup, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_import, viewGroup, false);

        Button b = (Button) v.findViewById(R.id.downloadButton);
        b.setOnClickListener(this);

        return v;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            //handle multiple view click events
            case R.id.downloadButton:
                EditText mEdit = (EditText) getView().findViewById(R.id.listNameInput);
                final String listName = mEdit.getText().toString().trim();


                handler = new Handler();
                new Thread() {
                    public void run() {
                        try {
                            APIFetch.apiFetch(listName, getContext());
                            handler.post(new Runnable() {
                                public void run() {
                                    Toast.makeText(getContext(),
                                            String.format("%s downloaded", listName),
                                            Toast.LENGTH_LONG).show();
                                }
                            });
                        } catch (IOException e) {
                            e.printStackTrace();
                            handler.post(new Runnable() {
                                public void run() {
                                    Toast.makeText(getContext(),
                                            String.format("Failed to download %s", listName),
                                            Toast.LENGTH_LONG).show();
                                }
                            });
                        }
                        ;
                    }
                }.start();
        }
    }


}