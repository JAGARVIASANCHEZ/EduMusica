package com.example.edumusicav2;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

/**
 * Activity principal de la aplicación, para elegir los minijuegos.
 */
public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity"; // Definir el tag para el log

    FirebaseAuth auth;
    Button btn_logout, btn_melodias, btn_intervalos;
    ImageView btnAjustes;
    TextView tvMaxPuntMelodias, tvMaxPuntIntervalos;
    FirebaseUser user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Log.d(TAG, "onCreate llamado");

        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        auth = FirebaseAuth.getInstance();
        btn_logout = findViewById(R.id.logout);
        btn_melodias = findViewById(R.id.btn_melodias);
        btn_intervalos = findViewById(R.id.btn_intervalo);
        btnAjustes = findViewById(R.id.settings_icon);
        tvMaxPuntMelodias = findViewById(R.id.tv_max_score_melodias);
        tvMaxPuntIntervalos = findViewById(R.id.tv_max_score_intervalos);

        user = auth.getCurrentUser();

        // Comprobación al iniciar la app de que hay un usuario logueado. En caso negativo, abrir Login
        if (user == null) {

            Log.d(TAG, "Usuario no logueado, iniciando LoginActivity");

            Intent intent = new Intent(getApplicationContext(), Login.class);
            startActivity(intent);
            finish();
        }

        // Mostrar las puntuaciones máximas al iniciar la activity
        mostrarPuntacionesMax();

        // Botón para Juego Melodías
        btn_melodias.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Log.d(TAG, "Botón de juego melodías presionado");

                Intent intent = new Intent(getApplicationContext(), JuegoMelodias.class);
                startActivity(intent);
            }
        });

        // Botón para Juego Intervalos
        btn_intervalos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Log.d(TAG, "Botón de juego intervalos presionado");

                Intent intent = new Intent(getApplicationContext(), JuegoIntervalos.class);
                startActivity(intent);
            }
        });

        // Cerrar sesión
        btn_logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Log.d(TAG, "Botón de logout presionado");

                FirebaseAuth.getInstance().signOut();
                Intent intent = new Intent(getApplicationContext(), Login.class);
                startActivity(intent);
                finish();
            }
        });

        // Botón de diálogo de ajustes
        btnAjustes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Log.d(TAG, "Botón de ajustes presionado");

                showSettingsDialog();
            }
        });
    }

    /**
     * Muestra el diálogo de ajustes.
     */
    private void showSettingsDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);  // Constructor de opciones de diálogo de ajustes
        builder.setTitle("Ajustes");   // Poner Título al diálogo de ajustes

        String[] options = {"Cambiar tamaño de la fuente", "Restablecer progreso"}; // Array de Strings con opciones de ajuste
        builder.setItems(options, new DialogInterface.OnClickListener() { // Colocamos los Strings en el constructor de diálogo de ajustes

            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which) {
                    case 0:
                        Log.d(TAG, "Opción de cambiar tamaño de la fuente seleccionada");
                        tamFuente(); // Cambiar el tamaño de la fuente
                        break;
                    case 1:
                        Log.d(TAG, "Opción de restablecer progreso seleccionada");
                        resetProgresoUsuario(); // Resetear el progreso del usuario
                        break;
                }
            }
        });

        builder.setNegativeButton("Cancelar", null); // Mostrar botón de cierre de diálogo
        builder.show();
    }

    /**
     * Cambia el tamaño de la fuente.
     */
    private void tamFuente() {
        String[] fontSizes = {"Pequeño", "Mediano", "Grande"};
        AlertDialog.Builder builder = new AlertDialog.Builder(this); // Nuevo diálogo con las opciones de tamaño de fuente
        builder.setTitle("Selecciona el tamaño de la fuente");
        builder.setItems(fontSizes, new DialogInterface.OnClickListener() { // Colocación de opciones de tamaño de fuentes
            @Override
            public void onClick(DialogInterface dialog, int tam) {
                switch (tam) {
                    case 0:
                        cambiarTamFuente(0.85f);
                        break;

                    case 1:
                        cambiarTamFuente(1.0f);
                        break;

                    case 2:
                        cambiarTamFuente(1.15f);
                        break;
                }
            }
        });
        builder.setNegativeButton("Cancelar", null);
        builder.show();
    }

    /**
     * Establece el tamaño de la fuente y recarga la actividad.
     *
     * @param scale Escala del tamaño de la fuente.
     */
    private void cambiarTamFuente(float scale) {

        Configuration configuration = getResources().getConfiguration();
        configuration.fontScale = scale;
        getResources().updateConfiguration(configuration, getResources().getDisplayMetrics());
        recreate(); // Recargar la activity para aplicar el cambio de tamaño de la fuente
    }

    /**
     * Restablece el progreso del usuario.
     */
    private void resetProgresoUsuario() {
        new AlertDialog.Builder(this)
                .setTitle("Restablecer progreso")
                .setMessage("¿Estás seguro de que deseas restablecer tu progreso?")
                .setPositiveButton("Sí", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Log.d(TAG, "Progreso restablecido confirmado por el usuario");

                        // Restablecer el progreso aquí
                        SharedPreferences prefs = getSharedPreferences("Progress", MODE_PRIVATE); // Llamada a Progress.xml en sharedpref
                        SharedPreferences.Editor editor = prefs.edit();
                        editor.clear(); // Limpiar el progreso guardado
                        editor.apply();
                        Toast.makeText(MainActivity.this, "Progreso restablecido", Toast.LENGTH_SHORT).show();

                        // Actualizar las vistas con las puntuaciones restablecidas
                        mostrarPuntacionesMax();
                    }
                })
                .setNegativeButton("No", null)
                .show();
    }

    /**
     * Muestra las puntuaciones máximas guardadas.
     */
    private void mostrarPuntacionesMax() {

        Log.d(TAG, "Mostrando puntuaciones máximas");

        SharedPreferences prefs = getSharedPreferences("Progress", MODE_PRIVATE); // Llamada a Progress.xml en sharedpref una vez más, para mostrar las puntuaciones máximas
        int maxScoreMelodias = prefs.getInt("maxScoreMelodias", 0);
        int maxScoreIntervalos = prefs.getInt("maxScoreIntervalos", 0);
        tvMaxPuntMelodias.setText("Puntuación máxima de melodías: " + maxScoreMelodias + "/" + JuegoMelodias.TOTAL_PREGUNTAS);
        tvMaxPuntIntervalos.setText("Puntuación máxima de intervalos: " + maxScoreIntervalos + "/" + JuegoIntervalos.TOTAL_PREGUNTAS);
    }
}
