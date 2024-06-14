package com.example.edumusicav2;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class JuegoIntervalos extends AppCompatActivity {

    public static final int TOTAL_QUESTIONS = 8;

    private TextView questionText, scoreText;
    private Button button1, button2, button3, button4, nextButton, playScaleButton, playIntervalButton;

    private List<PreguntaIntervalo> preguntaIntervalos;
    private int currentQuestionIndex = 0;
    private int score = 0;
    private MediaPlayer mediaPlayer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_juego_intervalos);

        questionText = findViewById(R.id.questionText);
        scoreText = findViewById(R.id.scoreText);
        button1 = findViewById(R.id.button1);
        button2 = findViewById(R.id.button2);
        button3 = findViewById(R.id.button3);
        button4 = findViewById(R.id.button4);
        nextButton = findViewById(R.id.nextButton);
        playScaleButton = findViewById(R.id.playScaleButton);
        playIntervalButton = findViewById(R.id.playIntervalButton);

        // Inicializar las preguntas
        initializeQuestions();

        // Mostrar la primera pregunta
        showQuestion();

        // Configurar oyentes de botones
        button1.setOnClickListener(view -> checkAnswer(button1));
        button2.setOnClickListener(view -> checkAnswer(button2));
        button3.setOnClickListener(view -> checkAnswer(button3));
        button4.setOnClickListener(view -> checkAnswer(button4));
        nextButton.setOnClickListener(view -> nextQuestion());
        playScaleButton.setOnClickListener(view -> playScale());
        playIntervalButton.setOnClickListener(view -> playInterval());
    }

    private void initializeQuestions() {
        preguntaIntervalos = new ArrayList<>();
        String[] respuestasArray = getResources().getStringArray(R.array.respuestas_intervalos);
        String[] audiosArray = getResources().getStringArray(R.array.audios_intervalos);

        Random random = new Random();

        for (int i = 0; i < TOTAL_QUESTIONS; i++) {
            int randomIndex = random.nextInt(audiosArray.length);
            String respuestaCorrecta = respuestasArray[randomIndex];
            String audioNombre = audiosArray[randomIndex];

            List<String> opciones = new ArrayList<>();
            opciones.add(respuestaCorrecta);

            while (opciones.size() < 4) {
                String respuestaAleatoria = respuestasArray[random.nextInt(respuestasArray.length)];
                if (!opciones.contains(respuestaAleatoria)) {
                    opciones.add(respuestaAleatoria);
                }
            }

            Collections.shuffle(opciones);
            preguntaIntervalos.add(new PreguntaIntervalo(audioNombre, opciones.toArray(new String[0]), respuestaCorrecta));
        }

        Collections.shuffle(preguntaIntervalos);
    }

    private void showQuestion() {
        resetButtons();
        if (currentQuestionIndex < preguntaIntervalos.size()) {
            PreguntaIntervalo currentPreguntaIntervalo = preguntaIntervalos.get(currentQuestionIndex);

            questionText.setText("Escucha el intervalo");

            String[] choices = currentPreguntaIntervalo.getChoices();
            button1.setText(choices[0]);
            button2.setText(choices[1]);
            button3.setText(choices[2]);
            button4.setText(choices[3]);

            playInterval();
        } else {
            GameUtils.endGame(this, score, "Intervalos");
        }
    }

    private void checkAnswer(Button selectedButton) {
        PreguntaIntervalo currentPreguntaIntervalo = preguntaIntervalos.get(currentQuestionIndex);
        String selectedAnswer = selectedButton.getText().toString();
        String correctAnswer = currentPreguntaIntervalo.getCorrectAnswer();

        if (selectedAnswer.equals(correctAnswer)) {
            score++;
            selectedButton.setBackgroundColor(Color.GREEN);

        } else {
            selectedButton.setBackgroundColor(Color.RED);
            GameUtils.highlightCorrectAnswer(button1, button2, button3, button4, correctAnswer);
        }

        scoreText.setText("Puntuación: " + score + "/" + TOTAL_QUESTIONS);
        GameUtils.saveMaxScore(this, score, "Intervalos");
        GameUtils.enableButtons(false, button1, button2, button3, button4);
    }

    private void resetButtons() {
        button1.setBackgroundColor(getResources().getColor(android.R.color.holo_blue_light));
        button2.setBackgroundColor(getResources().getColor(android.R.color.holo_blue_light));
        button3.setBackgroundColor(getResources().getColor(android.R.color.holo_blue_light));
        button4.setBackgroundColor(getResources().getColor(android.R.color.holo_blue_light));
        GameUtils.enableButtons(true, button1, button2, button3, button4);
    }

    private void nextQuestion() {
        currentQuestionIndex++;
        showQuestion();
    }

    private void playScale() {
        if (mediaPlayer != null) {
            mediaPlayer.release();
        }
        mediaPlayer = MediaPlayer.create(this, R.raw.escala_mayor);
        mediaPlayer.start();
    }

    private void playInterval() {
        if (mediaPlayer != null) {
            mediaPlayer.release();
        }
        String audioNombre = preguntaIntervalos.get(currentQuestionIndex).getAudioName();
        @SuppressLint("DiscouragedApi") int resID = getResources().getIdentifier(audioNombre, "raw", getPackageName());
        mediaPlayer = MediaPlayer.create(this, resID);
        mediaPlayer.start();
    }
}
