package com.example.edumusicav2;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

/**
 * Actividad para el juego de identificacion de intervalos.
 */
public class JuegoIntervalos extends AppCompatActivity {

    public static final int TOTAL_PREGUNTAS = 8;

    private static final String TAG = "JuegoIntervalos";

    private TextView tvPregunta, tvPuntuacion;
    private Button button1, button2, button3, button4, btnSiguiente, btnEscala, btnIntervalo;

    private List<PreguntasIntervalo> preguntasIntervalos;
    private int intPreguntaActual = 0;
    private int puntuacion = 0;
    private MediaPlayer mediaPlayer;

    /**
     * Método llamado cuando se crea la actividad.
     *
     * @param savedInstanceState Estado guardado de la instancia anterior.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_juego_intervalos);

        tvPregunta = findViewById(R.id.textPregunta);
        tvPuntuacion = findViewById(R.id.scoreText);
        button1 = findViewById(R.id.button1);
        button2 = findViewById(R.id.button2);
        button3 = findViewById(R.id.button3);
        button4 = findViewById(R.id.button4);
        btnSiguiente = findViewById(R.id.btnSiguiente);
        btnEscala = findViewById(R.id.btnEscala);
        btnIntervalo = findViewById(R.id.btnIntervalo);

        // Inicializar las preguntas
        builderPregunta();

        // Mostrar la primera pregunta
        showPregunta();

        // Configurar oyentes de botones
        button1.setOnClickListener(view -> checkAnswer(button1));
        button2.setOnClickListener(view -> checkAnswer(button2));
        button3.setOnClickListener(view -> checkAnswer(button3));
        button4.setOnClickListener(view -> checkAnswer(button4));
        btnSiguiente.setOnClickListener(view -> siguientePregunta());
        btnEscala.setOnClickListener(view -> reprodEscala());
        btnIntervalo.setOnClickListener(view -> reprodIntervalo());
    }

    /**
     * Crea las preguntas del juego.
     */
    private void builderPregunta() {
        preguntasIntervalos = new ArrayList<>();
        String[] respuestasArray = getResources().getStringArray(R.array.respuestas_intervalos);
        String[] audiosArray = getResources().getStringArray(R.array.audios_intervalos);

        Random random = new Random();

        for (int i = 0; i < TOTAL_PREGUNTAS; i++) {
            int indexRandom = random.nextInt(audiosArray.length);
            String respuestaCorrecta = respuestasArray[indexRandom];
            String audioNombre = audiosArray[indexRandom];

            List<String> opciones = new ArrayList<>();
            opciones.add(respuestaCorrecta);

            while (opciones.size() < 4) {
                String respuestaAleatoria = respuestasArray[random.nextInt(respuestasArray.length)];
                if (!opciones.contains(respuestaAleatoria)) {
                    opciones.add(respuestaAleatoria);
                }
            }

            Collections.shuffle(opciones);
            preguntasIntervalos.add(new PreguntasIntervalo(audioNombre, opciones.toArray(new String[0]), respuestaCorrecta));
        }

        Collections.shuffle(preguntasIntervalos);

        Log.d(TAG, "Preguntas creadas.");
    }

    /**
     * Muestra la pregunta actual.
     */
    private void showPregunta() {
        resetBotones();
        if (intPreguntaActual < preguntasIntervalos.size()) {
            PreguntasIntervalo currentPreguntasIntervalo = preguntasIntervalos.get(intPreguntaActual);

            tvPregunta.setText("Escucha el intervalo");

            String[] choices = currentPreguntasIntervalo.getOpciones();
            button1.setText(choices[0]);
            button2.setText(choices[1]);
            button3.setText(choices[2]);
            button4.setText(choices[3]);

            reprodIntervalo();
            Log.d(TAG, "Pregunta mostrada: " + intPreguntaActual);
        } else {
            GameUtils.finJuego(this, puntuacion, "Intervalos");
            Log.d(TAG, "Juego terminado.");
        }
    }

    /**
     * Verifica la respuesta seleccionada por el usuario.
     *
     * @param selectedButton El botón seleccionado por el usuario.
     */
    private void checkAnswer(Button selectedButton) {
        PreguntasIntervalo currentPreguntasIntervalo = preguntasIntervalos.get(intPreguntaActual);
        String selectedAnswer = selectedButton.getText().toString();
        String correctAnswer = currentPreguntasIntervalo.getOpcionCorrecta();

        boolean correcta;
        if (selectedAnswer.equals(correctAnswer)) {
            correcta = true;
        } else {
            correcta = false;
        }

        if (correcta) {
            puntuacion++;
            selectedButton.setBackgroundColor(Color.GREEN);
            Log.d(TAG, "Respuesta correcta.");
        } else {
            selectedButton.setBackgroundColor(Color.RED);
            GameUtils.resaltarRespuestaCorrecta(button1, button2, button3, button4, correctAnswer);
            Log.d(TAG, "Respuesta incorrecta.");
        }

        tvPuntuacion.setText("Puntuación: " + puntuacion + "/" + TOTAL_PREGUNTAS);
        GameUtils.guardarMaxPuntuacion(this, puntuacion, "Intervalos");
        GameUtils.activarBoton(false, button1, button2, button3, button4);
    }

    /**
     * Reinicia los botones de respuesta a su estado inicial.
     */
    private void resetBotones() {
        button1.setBackgroundColor(getResources().getColor(android.R.color.holo_blue_light));
        button2.setBackgroundColor(getResources().getColor(android.R.color.holo_blue_light));
        button3.setBackgroundColor(getResources().getColor(android.R.color.holo_blue_light));
        button4.setBackgroundColor(getResources().getColor(android.R.color.holo_blue_light));
        GameUtils.activarBoton(true, button1, button2, button3, button4);
        Log.d(TAG, "Botones reiniciados.");
    }

    /**
     * Muestra la siguiente pregunta.
     */
    private void siguientePregunta() {
        intPreguntaActual++;
        showPregunta();
    }

    /**
     * Reproduce la escala mayor.
     */
    private void reprodEscala() {
        if (mediaPlayer != null) {
            mediaPlayer.release();
        }
        mediaPlayer = MediaPlayer.create(this, R.raw.escala_mayor);
        mediaPlayer.start();

        Log.d(TAG, "Escala mayor reproducida.");
    }

    /**
     * Reproduce el intervalo del ejercicio actual.
     */
    private void reprodIntervalo() {
        if (mediaPlayer != null) {
            mediaPlayer.release();
        }
        String audioNombre = preguntasIntervalos.get(intPreguntaActual).getNombreAudio();
        @SuppressLint("DiscouragedApi") int resID = getResources().getIdentifier(audioNombre, "raw", getPackageName());
        mediaPlayer = MediaPlayer.create(this, resID);
        mediaPlayer.start();

        Log.d(TAG, "Intervalo reproducido: " + audioNombre);
    }
}
