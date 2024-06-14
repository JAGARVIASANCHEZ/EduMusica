package com.example.edumusicav2;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Bundle;
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

public class MainActivity extends AppCompatActivity {

    FirebaseAuth auth;
    Button btn_logout, btn_melodias, btn_intervalos;
    ImageView settingsIcon;
    TextView tvMaxScoreMelodias, tvMaxScoreIntervalos;
    FirebaseUser user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
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
        settingsIcon = findViewById(R.id.settings_icon);
        tvMaxScoreMelodias = findViewById(R.id.tv_max_score_melodias);
        tvMaxScoreIntervalos = findViewById(R.id.tv_max_score_intervalos);

        user = auth.getCurrentUser();


        //Comprobacion al iniciar la app de que hay un usuario logueado. En caso negativo, abrir Login
        if (user == null) {
            Intent intent = new Intent(getApplicationContext(), Login.class);
            startActivity(intent);
            finish();
        }

        // Mostrar las puntuaciones máximas al iniciar la activity
        displayMaxScores();

        //Boton para Juego Melodias
        btn_melodias.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), JuegoMelodias.class);
                startActivity(intent);
            }
        });

        //Boton para JuegoIntervalos
        btn_intervalos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), JuegoIntervalos.class);
                startActivity(intent);
            }
        });

        //Cerrar sesion
        btn_logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth.getInstance().signOut();
                Intent intent = new Intent(getApplicationContext(), Login.class);
                startActivity(intent);
                finish();
            }
        });

        //Boton de dialogo de ajustes
        settingsIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showSettingsDialog();
            }
        });
    }

    //Mostrar dialogo de ajustes
    private void showSettingsDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);  //Constructor de opciones de dialogo de ajustes
        builder.setTitle("Ajustes");   //Poner Titulo al dialogo de ajustes



        String[] options = {"Cambiar tamaño de la fuente", "Restablecer progreso"}; // Array de Strings con opciones de ajuste
        builder.setItems(options, new DialogInterface.OnClickListener() {           // Colocamos los Strings en el contructor de dialogo de ajustes

            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which) {

                    case 0:
                        changeFontSize(); // Cambiar el tamaño de la fuente
                        break;

                    case 1:
                        resetProgress(); // Resetear el progreso del usuario
                        break;


                }
            }
        });

        builder.setNegativeButton("Cancelar", null);   //Mostrar boton de cierre de dialogo
        builder.show();
    }

    //Cambiar el tamaño de la fuente
    private void changeFontSize() {
        String[] fontSizes = {"Pequeño", "Mediano", "Grande"};
        AlertDialog.Builder builder = new AlertDialog.Builder(this);  //Nuevo dialogo con las opciones de tamaño de fuente
        builder.setTitle("Selecciona el tamaño de la fuente");
        builder.setItems(fontSizes, new DialogInterface.OnClickListener() {  //colocacion de opciones de tamaño de fuentes
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which) {
                    case 0:
                        // Cambiar a tamaño pequeño
                        setFontSize(0.85f);
                        break;
                    case 1:
                        // Cambiar a tamaño mediano
                        setFontSize(1.0f);
                        break;
                    case 2:
                        // Cambiar a tamaño grande
                        setFontSize(1.15f);
                        break;
                }
            }
        });
        builder.setNegativeButton("Cancelar", null);
        builder.show();
    }


    private void setFontSize(float scale) {
        Configuration configuration = getResources().getConfiguration();
        configuration.fontScale = scale;
        getResources().updateConfiguration(configuration, getResources().getDisplayMetrics());
        recreate(); // Recargar la activity para aplicar el cambio de tamaño de la fuente
    }




    //Resetear progreso de usuario
    private void resetProgress() {
        new AlertDialog.Builder(this)
                .setTitle("Restablecer progreso")
                .setMessage("¿Estás seguro de que deseas restablecer tu progreso?")
                .setPositiveButton("Sí", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // Restablecer el progreso aquí
                        SharedPreferences prefs = getSharedPreferences("Progress", MODE_PRIVATE);  //Llamada a Progress.xml en sharedpref
                        SharedPreferences.Editor editor = prefs.edit();
                        editor.clear(); // Limpiar el progreso guardado
                        editor.apply();
                        Toast.makeText(MainActivity.this, "Progreso restablecido", Toast.LENGTH_SHORT).show();
                        // Actualizar las vistas con las puntuaciones restablecidas
                        displayMaxScores();
                    }
                })
                .setNegativeButton("No", null)
                .show();
    }

    private void displayMaxScores() {
        SharedPreferences prefs = getSharedPreferences("Progress", MODE_PRIVATE);               //Llamada a Progress.xml en sharedpref una vez más, para mostrar las puntuaciones máximas
        int maxScoreMelodias = prefs.getInt("maxScoreMelodias", 0);
        int maxScoreIntervalos = prefs.getInt("maxScoreIntervalos", 0);
        tvMaxScoreMelodias.setText("Puntuación máxima de melodías: " + maxScoreMelodias + "/" + JuegoMelodias.TOTAL_QUESTIONS);
        tvMaxScoreIntervalos.setText("Puntuación máxima de intervalos: " + maxScoreIntervalos + "/" + JuegoIntervalos.TOTAL_QUESTIONS);
    }
}
