package com.example.hangman;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.NumberPicker;
import android.widget.TextView;

import com.opencsv.CSVReader;

import java.io.FileReader;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Random;

public class GameActivity extends AppCompatActivity {
    public Map<String, Integer> imageMap = new HashMap<>();
    ImageView mainImage;
    int stage = 1;
    final int guessesAllowed = 11;
    NumberPicker letterPicker;
    String[] pickerLetters;
    String[] guessedLetters;
    TextView guessedLettersDisplay;
    TextView goalStringDisplay;
    Button guessButton;
    Button restartButton;
    GoalString goalString;
    int points = 0;
    List<String[]> goalStringList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mainImage = findViewById(R.id.mainImage);
        guessedLettersDisplay = findViewById(R.id.guessedLetters);
        goalStringDisplay = findViewById(R.id.goalStringDisplay);
        guessButton = findViewById(R.id.guessButton);
        restartButton = findViewById(R.id.restartButton);
        for (int i = 1; i <= 11; i++) {
            String imageName = String.format("stage%d", i);
            int imageId = getResources().getIdentifier(imageName, "drawable", getApplicationInfo().packageName);
            imageMap.put(imageName, imageId);
        }

        //Hardcoded list selection for now todo list selector
        String chosenList = getIntent().getStringExtra("listName");
        goalStringList = loadList(chosenList);
        initialiseGame(goalStringList);
    }

    public void initialiseGame(List goalStringList) {
        mainImage.setImageResource(imageMap.get("stage1"));
        stage = 1;
        guessButton.setVisibility(View.VISIBLE);
        restartButton.setVisibility(View.GONE);
        goalString = selectFromList(goalStringList);
        pickerLetters = new String[]{"a", "b", "c", "d", "e", "f", "g", "h", "i", "j", "k", "l", "m", "n", "o", "p", "q", "r", "s", "t", "u", "v", "w", "x", "y", "z"};
        guessedLetters = new String[26];
        updateGuessedDisplay(guessedLetters);
        updatePicker(guessedLetters);
    }

    private GoalString selectFromList(List<String[]> goalStringList) {
        //Randomly selects goalString from within the loaded list
        if (goalStringList.isEmpty()) {
            //todo toast message, prompt for another list?
        }
        int random = new Random().nextInt(goalStringList.size());
        goalString = new GoalString(goalStringList.get(random)[0]);
        goalStringDisplay.setText(goalString.getClosedString());
        goalStringList.remove(random); //remove from session list so the word is not replayed
        return goalString;
    }

    private List<String[]> loadList(String chosenList) {
        int listId = getResources().getIdentifier("raw/" + chosenList, null, getApplicationInfo().packageName);
        InputStream inputStream = getResources().openRawResource(listId);
        CSVFile csvFile = new CSVFile(inputStream);
        return csvFile.read();
    }


    public void onClickStep(View view) {
        String selectedLetter = pickerLetters[letterPicker.getValue()];
        boolean guessMatch = checkGuess(selectedLetter, goalString);
        if (guessMatch && goalString.getClosedString().equalsIgnoreCase(goalString.getOpenString())) {
            winRound();
        } else if (!guessMatch) {
            stage++;
            if (stage <= guessesAllowed) {
                mainImage.setImageResource(imageMap.get("stage" + stage));

            } else {
                gameOver();
                return;
            }
        }
        for (int i = 0; i < guessedLetters.length; i++) {
            if (guessedLetters[i] == null) {
                guessedLetters[i] = selectedLetter;
                break;
            }
        }
        updateGuessedDisplay(guessedLetters);
        updatePicker(guessedLetters);
    }

    private void winRound() {
        points++;
        mainImage.setImageResource(R.drawable.win);
        guessButton.setVisibility(View.INVISIBLE);
        restartButton.setText("Continue");
        restartButton.setVisibility(View.VISIBLE);
    }

    private void gameOver() {
        goalStringDisplay.setText(goalString.getOpenString());
        mainImage.setImageResource(R.drawable.gameover);
        guessButton.setVisibility(View.INVISIBLE);
        restartButton.setText("Restart");
        restartButton.setVisibility(View.VISIBLE);
        //todo change step button to restart
        //todo hide scroller
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
        letterPicker.setDisplayedValues(null);
        letterPicker.setMinValue(0);
        letterPicker.setMaxValue(pickerLetters.length - 1);
        letterPicker.setDisplayedValues(pickerLetters);
    }

    public void updateGuessedDisplay(String[] guessedLetters) {
        String display = "";
        for (String letter : guessedLetters) {
            if (letter != null) {
                display = display + letter + "\n";
            }
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
            if (Character.toLowerCase(goalStringOpen.charAt(i)) == guessChar) {
                match = true;
                goalStringCurrent = goalStringCurrent + goalStringOpen.charAt(i);
            } else {
                goalStringCurrent = goalStringCurrent + goalStringClosed.charAt(i);
            }
        }
        goalString.setClosedString(goalStringCurrent); //updates with revealed string
        goalStringDisplay.setText(goalStringCurrent);
        return match;
    }

    public void restartGame(View view) {
        initialiseGame(goalStringList);
    }
}
