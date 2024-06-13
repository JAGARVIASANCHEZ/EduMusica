package com.example.edumusicav2;

import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class JuegoMelodias extends AppCompatActivity {

    private ImageView melodyImage;
    private Button button1, button2, button3, button4, resolveButton, nextButton;
    private TextView scoreText, questionCounterText;

    private int selectedMelody = -1;
    private int correctMelody = -1;
    private int score = 0;
    private int questionCounter = 0;
    private static final int TOTAL_QUESTIONS = 3;

    private List<Integer> melodyAudioIds;
    private List<Integer> melodyImageIds;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_juego_melodias);

        melodyImage = findViewById(R.id.melodyImage);
        scoreText = findViewById(R.id.scoreText);
        questionCounterText = findViewById(R.id.questionCounterText);
        button1 = findViewById(R.id.button1);
        button2 = findViewById(R.id.button2);
        button3 = findViewById(R.id.button3);
        button4 = findViewById(R.id.button4);
        resolveButton = findViewById(R.id.resolveButton);
        nextButton = findViewById(R.id.nextButton);

        loadMelodyResources();

        setNewQuestion();

        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                playMelody(0);
                highlightButton(button1);
            }
        });
        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                playMelody(1);
                highlightButton(button2);
            }
        });
        button3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                playMelody(2);
                highlightButton(button3);
            }
        });
        button4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                playMelody(3);
                highlightButton(button4);
            }
        });

        resolveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkAnswer();
            }
        });
        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setNewQuestion();
            }
        });
    }

    private void loadMelodyResources() {
        Resources res = getResources();
        String packageName = getPackageName();

        String[] melodyAudioFiles = res.getStringArray(R.array.melody_audio_files);
        String[] melodyImageFiles = res.getStringArray(R.array.melody_image_files);

        melodyAudioIds = new ArrayList<>();
        melodyImageIds = new ArrayList<>();

        for (String audioFile : melodyAudioFiles) {
            int audioId = res.getIdentifier(audioFile, "raw", packageName);
            if (audioId != 0) {
                melodyAudioIds.add(audioId);
            }
        }

        for (String imageFile : melodyImageFiles) {
            int imageId = res.getIdentifier(imageFile, "drawable", packageName);
            if (imageId != 0) {
                melodyImageIds.add(imageId);
            }
        }
    }

    private void setNewQuestion() {

        if (questionCounter >= TOTAL_QUESTIONS) {
            endGame();
            return;
        }

        List<Integer> randomIndices = shuffleList(melodyAudioIds.size());
        correctMelody = randomIndices.get(0);

        melodyImage.setImageResource(melodyImageIds.get(correctMelody));

        List<Integer> buttonTags = shuffleList(4);
        button1.setTag(randomIndices.get(buttonTags.get(0)));
        button2.setTag(randomIndices.get(buttonTags.get(1)));
        button3.setTag(randomIndices.get(buttonTags.get(2)));
        button4.setTag(randomIndices.get(buttonTags.get(3)));

        resetButtonStyles();
        enableButtons(true);
        nextButton.setEnabled(false);
        selectedMelody = -1;
        questionCounter++;
        questionCounterText.setText("Pregunta: " + questionCounter + "/" + TOTAL_QUESTIONS);

    }

    private void playMelody(int index) {
        int melody;
        switch (index) {
            case 0:
                melody = (int) button1.getTag();
                break;
            case 1:
                melody = (int) button2.getTag();
                break;
            case 2:
                melody = (int) button3.getTag();
                break;
            case 3:
                melody = (int) button4.getTag();
                break;
            default:
                return;
        }
        MediaPlayer mediaPlayer = MediaPlayer.create(this, melodyAudioIds.get(melody));
        mediaPlayer.start();
        selectedMelody = melody;
    }

    private void checkAnswer() {
        if (selectedMelody == -1) {
            Toast.makeText(this, "Selecciona una melodía primero", Toast.LENGTH_SHORT).show();
            return;
        }

        boolean isCorrect = selectedMelody == correctMelody;

        Button selectedButton;
        if (selectedMelody == (int) button1.getTag()) {
            selectedButton = button1;
        } else if (selectedMelody == (int) button2.getTag()) {
            selectedButton = button2;
        } else if (selectedMelody == (int) button3.getTag()) {
            selectedButton = button3;
        } else {
            selectedButton = button4;
        }

        selectedButton.setBackgroundColor(isCorrect ? Color.GREEN : Color.RED);
        Button correctButton = getButtonByTag(correctMelody);
        if (correctButton != null) {
            correctButton.setBackgroundColor(Color.GREEN);
        }

        if (isCorrect) {
            score++;
        }
        scoreText.setText("Puntuación: " + score);

        enableButtons(false);
        nextButton.setEnabled(true);
    }

    private Button getButtonByTag(int tag) {
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

    private void enableButtons(boolean enable) {
        button1.setEnabled(enable);
        button2.setEnabled(enable);
        button3.setEnabled(enable);
        button4.setEnabled(enable);
        resolveButton.setEnabled(enable);
    }

    private void highlightButton(Button button) {
        resetButtonStyles();
        button.setBackgroundColor(Color.BLUE);
    }

    private void resetButtonStyles() {
        button1.setBackgroundColor(Color.LTGRAY);
        button2.setBackgroundColor(Color.LTGRAY);
        button3.setBackgroundColor(Color.LTGRAY);
        button4.setBackgroundColor(Color.LTGRAY);
    }

    private List<Integer> shuffleList(int size) {
        List<Integer> list = new ArrayList<>();
        for (int i = 0; i < size; i++) {
            list.add(i);
        }
        Collections.shuffle(list);
        return list;
    }

    private void endGame() {
        enableButtons(false);
        nextButton.setEnabled(false);
        resolveButton.setEnabled(false);
        Toast.makeText(this, "Juego terminado! Tu puntuación final es: " + score, Toast.LENGTH_LONG).show();
        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
        startActivity(intent);
    }
}
