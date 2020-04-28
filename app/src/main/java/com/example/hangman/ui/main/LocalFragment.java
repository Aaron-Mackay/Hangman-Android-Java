package com.example.hangman.ui.main;

import android.content.res.Resources;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.hangman.ListRVAdapter;
import com.example.hangman.R;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

public class LocalFragment extends Fragment {
    List<String> filenames = new ArrayList<>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup viewGroup, Bundle savedInstanceState) {
        View RootView = inflater.inflate(R.layout.fragment_local, viewGroup, false);
        filenames = listRaw(); //gets list of filenames from /res/raw directory

        // Lookup the recyclerview in activity layout
        RecyclerView recyclerViewLocal = RootView.findViewById(R.id.recyclerViewId);

        // Create adapter passing in the sample user data
        ListRVAdapter adapter = new ListRVAdapter(filenames, getActivity());
        // Attach the adapter to the recyclerview to populate items
        recyclerViewLocal.setAdapter(adapter);
        // Set layout manager to position the items
        recyclerViewLocal.setLayoutManager(new LinearLayoutManager(getActivity()));


        return RootView;
    }

    public List<String> listRaw() {
        List<String> temp = new ArrayList<>();
        Field[] fields = R.raw.class.getFields();
        for (int count = 0; count < fields.length; count++) {
            temp.add(fields[count].getName());
            Log.i("Raw Asset: ", fields[count].getName());
        }
        return temp;
    }
}