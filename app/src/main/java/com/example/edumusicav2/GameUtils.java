
package com.example.edumusicav2;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.widget.Button;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class GameUtils {

    public static List<Integer> shuffleList(int size) {
        List<Integer> list = new ArrayList<>();
        for (int i = 0; i < size; i++) {
            list.add(i);
        }
        Collections.shuffle(list);
        return list;
    }

    public static void saveMaxScore(Context context, int newScore, String gameType) {
        SharedPreferences prefs = context.getSharedPreferences("Progress", Context.MODE_PRIVATE);
        int maxScore = prefs.getInt("maxScore" + gameType, 0);
        if (newScore > maxScore) {
            SharedPreferences.Editor editor = prefs.edit();
            editor.putInt("maxScore" + gameType, newScore);
            editor.apply();
            Toast.makeText(context, "Nueva puntuación máxima de " + gameType + ": " + newScore, Toast.LENGTH_SHORT).show();
        }
    }

    public static Button getButtonByTag(Button button1, Button button2, Button button3, Button button4, int tag) {
        if ((int) button1.getTag() == tag) {
            return button1;
        } else if ((int) button2.getTag() == tag) {
            return button2;
        } else if ((int) button3.getTag() == tag) {
            return button3;
        } else if ((int) button4.getTag() == tag) {
            return button4;
        }
        return null;
    }

    public static void resetButtonStyles(Button... buttons) {
        for (Button button : buttons) {
            button.setBackgroundColor(Color.DKGRAY);
        }
    }

    public static void enableButtons(boolean enable, Button... buttons) {
        for (Button button : buttons) {
            button.setEnabled(enable);
        }
    }

    public static void endGame(Context context, int score, String gameType) {
        Toast.makeText(context, "Juego terminado! Tu puntuación final es: " + score, Toast.LENGTH_LONG).show();
        saveMaxScore(context, score, gameType);
        Intent intent = new Intent(context, MainActivity.class);
        context.startActivity(intent);
    }

    public static void highlightCorrectAnswer(Button button1, Button button2, Button button3, Button button4, String correctAnswer) {
        if (button1.getText().toString().equals(correctAnswer)) {
            button1.setBackgroundColor(Color.GREEN);
        } else if (button2.getText().toString().equals(correctAnswer)) {
            button2.setBackgroundColor(Color.GREEN);
        } else if (button3.getText().toString().equals(correctAnswer)) {
            button3.setBackgroundColor(Color.GREEN);
        } else if (button4.getText().toString().equals(correctAnswer)) {
            button4.setBackgroundColor(Color.GREEN);
        }
    }
}
