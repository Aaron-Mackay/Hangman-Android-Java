package com.example.hangman;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.NumberPicker;
import android.widget.TextView;

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
    String[] pickerLetters = new String[]{"a", "b", "c", "d", "e", "f", "g", "h", "i", "j", "k", "l", "m", "n", "o", "p", "q", "r", "s", "t", "u", "v", "w", "x", "y", "z"};
    String[] guessedLetters = new String[26];
    TextView guessedLettersDisplay;
    TextView goalStringDisplay;
    GoalString goalString;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mainImage = findViewById(R.id.mainImage);
        guessedLettersDisplay = findViewById(R.id.guessedLetters);
        updateGuessedDisplay(guessedLetters);
        updatePicker(guessedLetters);
        goalStringDisplay = findViewById(R.id.goalStringDisplay);


        for (int i = 1; i <= 11; i++) {
            String imageName = String.format("stage%d", i);
            int imageId = getResources().getIdentifier(imageName, "drawable", getApplicationInfo().packageName);
            imageMap.put(imageName, imageId);
        }


        //TODO HARDCODED, EDIT
        goalString = new GoalString("check if the space hasn't");
        goalStringDisplay.setText(goalString.getClosedString());

    }

    public void onClickStep(View view) {
        String selectedLetter = pickerLetters[letterPicker.getValue()];
        boolean guessMatch = checkGuess(selectedLetter, goalString);
        if (!guessMatch) {
            stage++;
            if (stage <= guessesAllowed) {
                //todo if no guesses left, game over
                //todo if correct, point
                //todo add guessed letter to guessedLetters

                for (int i = 0; i < guessedLetters.length; i++) {
                    if (guessedLetters[i] == null) {
                        guessedLetters[i] = selectedLetter;
                        break;
                    }
                }

                mainImage.setImageResource(imageMap.get("stage" + stage));
                updateGuessedDisplay(guessedLetters);
            }

            updatePicker(guessedLetters);
        }
    }

    public void updatePicker(String[] guessedLetters) {
        List<String> temp = new LinkedList<String>(Arrays.asList(pickerLetters));
        int removedLetterCount = 0;
        for (int i = 0; i < pickerLetters.length; i++) {
            for (int j = 0; j < guessedLetters.length; j++) {
                if (guessedLetters[j] == null) {
                    break;
                } else if (guessedLetters[j].equalsIgnoreCase(pickerLetters[i])) {
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

    public void updateGuessedDisplay(String[] guessedLetters) {
        String display = "";
        for (String letter : guessedLetters) {
            if (letter != null) {
                display = display + letter + ", ";
            }
        }
        if (display.length() != 0) {
            display = display.substring(0, display.length() - 2);
        }
        guessedLettersDisplay.setText(display);
    }

    public boolean checkGuess(String guess, GoalString goalString) {
        char guessChar = guess.charAt(0);
        boolean match = false;
        String goalStringOpen = goalString.getOpenString();
        String goalStringClosed = goalString.getClosedString();
        String goalStringCurrent = "";

        for (int i = 0; i < goalStringOpen.length(); i++) {
            if (goalStringOpen.charAt(i) == guessChar) {
                match = true;
                goalStringCurrent = goalStringCurrent + guessChar;
            } else {
                goalStringCurrent = goalStringCurrent + goalStringClosed.charAt(i);
            }
        }
        goalString.setClosedString(goalStringCurrent); //updates with revealed string
        goalStringDisplay.setText(goalStringCurrent);
        return match;
    }
}
