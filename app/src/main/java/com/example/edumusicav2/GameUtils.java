package com.example.edumusicav2;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.util.Log;
import android.widget.Button;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Clase que contiene utilidades para nuestra aplicación.
 */
public class GameUtils {

    private static final String TAG = "GameUtils"; // Tag para el log

    /**
     * Mezcla una lista de enteros del 0 a size-1.
     *
     * @param size Tamaño de la lista.
     * @return Lista mezclada de enteros.
     */
    public static List<Integer> randomizarListaEnteros(int size) {

        Log.d(TAG, "Mezclando lista de tamaño:" + size);

        List<Integer> list = new ArrayList<>();
        for (int i = 0; i < size; i++) {
            list.add(i);
        }
        Collections.shuffle(list);
        return list;
    }

    /**
     * Guarda la puntuación máxima si es mayor que la actual.
     *
     * @param context   Contexto de la aplicación.
     * @param nuevaPuntuacion  Nueva puntuación.
     * @param nombreJuego  Nombre del juego.
     */
    public static void guardarMaxPuntuacion(Context context, int nuevaPuntuacion, String nombreJuego) {

        Log.d(TAG, "Guardando puntuacion maxima para" + nombreJuego + "con la puntuacion:" + nuevaPuntuacion);

        SharedPreferences prefs = context.getSharedPreferences("Progress", Context.MODE_PRIVATE);
        int maxPuntuacion = prefs.getInt("maxPuntuacion" + nombreJuego, 0);
        if (nuevaPuntuacion > maxPuntuacion) {

            Log.d(TAG, "alcanzado comparador puntuaciones");

            SharedPreferences.Editor editor = prefs.edit();
            editor.putInt("maxPuntuacion" + nombreJuego, nuevaPuntuacion);
            editor.apply();
            Toast.makeText(context, "Nueva puntuación máxima de " + nombreJuego + ": " + nuevaPuntuacion, Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * Devuelve el botón que coincide con el tag especificado.
     *
     * @param button1 Primer botón.
     * @param button2 Segundo botón.
     * @param button3 Tercer botón.
     * @param button4 Cuarto botón.
     * @param tag     Tag a buscar.
     * @return Botón que coincide con el tag.
     */
    public static Button identificarBoton(Button button1, Button button2, Button button3, Button button4, int tag) {

        Log.d(TAG, "Identificando boton por tag:" + tag);

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

    /**
     * Reinicia los estilos de los botones.
     *
     * @param buttons Botones a reiniciar.
     */
    public static void resetBoton(Button... buttons) {

        Log.d(TAG, "Reset de botones");

        for (Button button : buttons) {
            button.setBackgroundColor(Color.DKGRAY);
        }
    }

    /**
     * Habilita o deshabilita los botones.
     *
     * @param enable  True para habilitar, false para deshabilitar.
     * @param buttons Botones a modificar.
     */
    public static void activarBoton(boolean enable, Button... buttons) {

        Log.d(TAG, "en activarBoton");

        for (Button button : buttons) {
            button.setEnabled(enable);
        }
    }

    /**
     * Termina el juego y guarda la puntuación máxima.
     *
     * @param context  Contexto de la aplicación.
     * @param puntuacion    Puntuación final.
     * @param nombreJuego Tipo de juego.
     */
    public static void finJuego(Context context, int puntuacion, String nombreJuego) {

        Log.d(TAG, "Terminando el juego" + nombreJuego + " with final puntuacion: " + puntuacion);

        Toast.makeText(context, "Juego terminado! Tu puntuación final es: " + puntuacion, Toast.LENGTH_LONG).show();
        guardarMaxPuntuacion(context, puntuacion, nombreJuego);
        Intent intent = new Intent(context, MainActivity.class);
        context.startActivity(intent);
    }

    /**
     * Resalta la respuesta correcta en los botones.
     *
     * @param button1       Primer botón.
     * @param button2       Segundo botón.
     * @param button3       Tercer botón.
     * @param button4       Cuarto botón.
     * @param respuestaCorrecta Respuesta correcta.
     */
    public static void resaltarRespuestaCorrecta(Button button1, Button button2, Button button3, Button button4, String respuestaCorrecta) {

        Log.d(TAG, "Resaltar respuesta" + respuestaCorrecta);

        if (button1.getText().toString().equals(respuestaCorrecta)) {
            button1.setBackgroundColor(Color.GREEN);
        } else if (button2.getText().toString().equals(respuestaCorrecta)) {
            button2.setBackgroundColor(Color.GREEN);
        } else if (button3.getText().toString().equals(respuestaCorrecta)) {
            button3.setBackgroundColor(Color.GREEN);
        } else if (button4.getText().toString().equals(respuestaCorrecta)) {
            button4.setBackgroundColor(Color.GREEN);
        }
    }
}
