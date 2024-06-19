package com.example.edumusicav2;

import android.annotation.SuppressLint;
import android.content.res.Resources;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.List;

/**
 * Activity para el juego de identificación de melodías.
 */
public class JuegoMelodias extends AppCompatActivity {

    private static final String TAG = "JuegoMelodias"; // Definir el tag para el log

    private ImageView imgMelodia;
    private Button button1, button2, button3, button4, btnResolver, btnSiguiente;
    private TextView tvPuntuacion, tvContadorPreguntas;

    private int melodiaElegida = -1;
    private int melodyCorrecta = -1;
    private int puntuacion = 0;
    private int contadorPreguntas = 0;
    public static final int TOTAL_PREGUNTAS = 10;

    private List<Integer> idAudiosMelodias;
    private List<Integer> idImagenMelodias;

    /**
     * Método llamado cuando la actividad es creada.
     *
     * @param savedInstanceState Estado guardado de la instancia anterior.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "onCreate llamado");
        setContentView(R.layout.activity_juego_melodias);

        imgMelodia = findViewById(R.id.melodyImage);
        tvPuntuacion = findViewById(R.id.scoreText);
        tvContadorPreguntas = findViewById(R.id.questionCounterText);
        button1 = findViewById(R.id.button1);
        button2 = findViewById(R.id.button2);
        button3 = findViewById(R.id.button3);
        button4 = findViewById(R.id.button4);
        btnResolver = findViewById(R.id.btnResolver);
        btnSiguiente = findViewById(R.id.btnSiguiente);

        cargarRecursosMelodias();
        siguientePregunta();

        button1.setOnClickListener(v -> {
            repMelodia(0);
            resaltarBoton(button1);
        });
        button2.setOnClickListener(v -> {
            repMelodia(1);
            resaltarBoton(button2);
        });
        button3.setOnClickListener(v -> {
            repMelodia(2);
            resaltarBoton(button3);
        });
        button4.setOnClickListener(v -> {
            repMelodia(3);
            resaltarBoton(button4);
        });

        btnResolver.setOnClickListener(v -> checkRespuesta());
        btnSiguiente.setOnClickListener(v -> siguientePregunta());
    }

    /**
     * Carga los recursos de melodías y sus imágenes correspondientes.
     */
    private void cargarRecursosMelodias() {
        Log.d(TAG, "Cargando recursos de melodías");

        Resources res = getResources();
        String packageName = getPackageName();

        String[] audiosMelodias = res.getStringArray(R.array.audios_melodias);
        String[] imgMelodias = res.getStringArray(R.array.imagenes_melodias);

        idAudiosMelodias = new ArrayList<>();
        idImagenMelodias = new ArrayList<>();

        for (String audio : audiosMelodias) {
            @SuppressLint("DiscouragedApi") int audioId = res.getIdentifier(audio, "raw", packageName);
            if (audioId != 0) {
                idAudiosMelodias.add(audioId);
            }
        }

        for (String imageFile : imgMelodias) {
            @SuppressLint("DiscouragedApi") int imageId = res.getIdentifier(imageFile, "drawable", packageName);
            if (imageId != 0) {
                idImagenMelodias.add(imageId);
            }
        }

        Log.d(TAG, "Audio IDs: " + idAudiosMelodias.size());
        Log.d(TAG, "Image IDs: " + idImagenMelodias.size());
    }

    /**
     * Establece una nueva pregunta con una melodía y opciones aleatorias.
     */
    private void siguientePregunta() {
        Log.d(TAG, "Estableciendo nueva pregunta");
        if (contadorPreguntas >= TOTAL_PREGUNTAS) {
            GameUtils.finJuego(this, puntuacion, "Melodias");
            return;
        }

        if (idAudiosMelodias.isEmpty() || idImagenMelodias.isEmpty()) {
            Log.e(TAG, "Error: Las listas de melodías están vacías");
            return;
        }

        List<Integer> randomIndices = GameUtils.randomizarListaEnteros(idAudiosMelodias.size());
        melodyCorrecta = randomIndices.get(0);

        imgMelodia.setImageResource(idImagenMelodias.get(melodyCorrecta));

        List<Integer> buttonTags = GameUtils.randomizarListaEnteros(4);
        button1.setTag(randomIndices.get(buttonTags.get(0)));
        button2.setTag(randomIndices.get(buttonTags.get(1)));
        button3.setTag(randomIndices.get(buttonTags.get(2)));
        button4.setTag(randomIndices.get(buttonTags.get(3)));

        GameUtils.resetBoton(button1, button2, button3, button4);
        GameUtils.activarBoton(true, button1, button2, button3, button4, btnResolver);
        btnSiguiente.setEnabled(false);
        melodiaElegida = -1;
        contadorPreguntas++;
        tvContadorPreguntas.setText("Pregunta: " + contadorPreguntas + "/" + TOTAL_PREGUNTAS);
    }

    /**
     * Reproduce la melodía seleccionada.
     *
     * @param index Índice del botón seleccionado.
     */
    private void repMelodia(int index) {
        Log.d(TAG, "Reproduciendo melodía del botón " + index);

        int melodia;
        switch (index) {
            case 0:
                melodia = (int) button1.getTag();
                break;
            case 1:
                melodia = (int) button2.getTag();
                break;
            case 2:
                melodia = (int) button3.getTag();
                break;
            case 3:
                melodia = (int) button4.getTag();
                break;
            default:
                return;
        }

        MediaPlayer mediaPlayer = MediaPlayer.create(this, idAudiosMelodias.get(melodia));
        mediaPlayer.start();
        melodiaElegida = melodia;
    }

    /**
     * Verifica si la respuesta seleccionada es correcta y actualiza la puntuación.
     */
    private void checkRespuesta() {
        Log.d(TAG, "Verificando respuesta");

        if (melodiaElegida == -1) {
            Toast.makeText(this, "Selecciona una melodía primero", Toast.LENGTH_SHORT).show();
            return;
        }

        boolean correcta;

        if (melodiaElegida == melodyCorrecta) {
            correcta = true;
        } else {
            correcta = false;
        }

        Button btnElegido;
        if (melodiaElegida == (int) button1.getTag()) {
            btnElegido = button1;
        } else if (melodiaElegida == (int) button2.getTag()) {
            btnElegido = button2;
        } else if (melodiaElegida == (int) button3.getTag()) {
            btnElegido = button3;
        } else {
            btnElegido = button4;
        }

        btnElegido.setBackgroundColor(correcta ? Color.GREEN : Color.RED);
        Button btnCorrecto = GameUtils.identificarBoton(button1, button2, button3, button4, melodyCorrecta);
        if (btnCorrecto != null) {
            btnCorrecto.setBackgroundColor(Color.GREEN);
        }

        if (correcta) {
            puntuacion++;
        }
        tvPuntuacion.setText("Puntuación: " + puntuacion + "/" + TOTAL_PREGUNTAS);

        GameUtils.guardarMaxPuntuacion(this, puntuacion, "Melodias");

        GameUtils.activarBoton(false, button1, button2, button3, button4, btnResolver);
        btnSiguiente.setEnabled(true);
    }

    /**
     * Resalta el botón seleccionado.
     *
     * @param button Botón seleccionado.
     */
    private void resaltarBoton(Button button) {
        Log.d(TAG, "Resaltando botón seleccionado");
        GameUtils.resetBoton(button1, button2, button3, button4);
        button.setBackgroundColor(Color.BLUE);
    }
}
