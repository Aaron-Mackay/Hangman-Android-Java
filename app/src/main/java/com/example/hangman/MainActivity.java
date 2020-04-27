package com.example.hangman;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.NumberPicker;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class MainActivity extends AppCompatActivity {
    public Map<String, Integer> imageMap = new HashMap<>();
    ImageView mainImage;
    int stage = 1;
    final int guessesAllowed = 11;
    NumberPicker letterPicker;
    String[] pickerLetters = new String[] {"a","b","c","d","e","f","g","h","i","j","k","l","m","n","o","p","q","r","s","t","u","v","w","x","y","z"};
    String[] guessedLetters = new String[] {"a", "e"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mainImage = findViewById(R.id.mainImage);

        initilisePicker(guessedLetters);

        for (int i = 1; i <= 11; i++) {
            String imageName = String.format("stage%d", i);
            int imageId = getResources().getIdentifier(imageName, "drawable", getApplicationInfo().packageName);
            imageMap.put(imageName, imageId);
        }
    }

    public void step(View view) {
        stage++;
        if (stage <= guessesAllowed) {
            mainImage.setImageResource(imageMap.get("stage" + stage));
        }
    }

    public void initilisePicker(String[] guessedLetters) {
        List<String> temp = new LinkedList<String>(Arrays.asList(pickerLetters));
        int removedLetterCount = 0;
        for (int i = 0; i < pickerLetters.length; i++) {
            for (int j = 0; j < guessedLetters.length; j++) {
                if (guessedLetters[j].equalsIgnoreCase(pickerLetters[i])) {
                    temp.remove(i - removedLetterCount); //removes a letter from the pickerletters as it has been guessed, shifting index backwards to account for removed letters
                    removedLetterCount++;
                    break;
                }
            }
        }
        pickerLetters = new String[temp.size()];
        for (int i = 0; i < temp.size(); i++) {
            pickerLetters[i] = temp.get(i);
        }

        letterPicker = findViewById(R.id.letterWheel);
        letterPicker.setMinValue(0);
        letterPicker.setMaxValue(pickerLetters.length - 1);
        letterPicker.setDisplayedValues(pickerLetters);
    }
}
