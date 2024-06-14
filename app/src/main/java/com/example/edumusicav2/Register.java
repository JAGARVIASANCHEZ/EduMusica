package com.example.edumusicav2;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

/**
 * Activity para el registro de nuevos usuarios en la aplicación.
 */
public class Register extends AppCompatActivity {

    TextInputEditText editTextEmail, editTextPassword, editTextRePassword;
    TextView textView;
    Button buttonReg;
    FirebaseAuth mAuth;
    ProgressBar progressBar;

    private static final String TAG = "RegisterActivity"; // Definir el tag para el log

    /**
     * Al inicio de la app, comprobar que no hay un usuario logueado,
     * en caso de que lo haya iniciar MainActivity.
     */
    @Override
    public void onStart() {
        super.onStart();

        FirebaseUser currentUser = mAuth.getCurrentUser();
        if(currentUser != null){
            Log.d(TAG, "Usuario ya logueado, iniciando MainActivity.");
            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
            startActivity(intent);
            finish();
        }
    }

    /**
     * Método llamado cuando la actividad es creada.
     *
     * @param savedInstanceState Estado guardado de la instancia anterior.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_register);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        mAuth = FirebaseAuth.getInstance();   // Instancia de usuario en FireBase
        editTextEmail = findViewById(R.id.email);
        editTextPassword = findViewById(R.id.password);
        editTextRePassword= findViewById(R.id.repassword);
        buttonReg = findViewById(R.id.btn_register);
        progressBar = findViewById(R.id.progressbar);   // Circunferencia de progreso para dar fluidez
        textView = findViewById(R.id.loginNow);

        // Volver a Login
        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "Volver a login seleccionado.");
                Intent intent = new Intent(getApplicationContext(), Login.class);
                startActivity(intent);
                finish();
            }
        });

        buttonReg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "Botón de registro presionado.");
                progressBar.setVisibility(View.VISIBLE);
                String email, password, repassword;
                email = String.valueOf(editTextEmail.getText());
                password = String.valueOf(editTextPassword.getText());
                repassword = String.valueOf(editTextRePassword.getText());

                // Comprobaciones al registro de nuevo usuario
                if (TextUtils.isEmpty(email)){
                    Log.d(TAG, "Email vacío.");
                    Toast.makeText(Register.this,"Introduce e-mail",Toast.LENGTH_SHORT).show();
                    progressBar.setVisibility(View.GONE);
                    return;
                }

                if (TextUtils.isEmpty(password)){
                    Log.d(TAG, "Contraseña vacía.");
                    Toast.makeText(Register.this,"Introduce contraseña",Toast.LENGTH_SHORT).show();
                    progressBar.setVisibility(View.GONE);
                    return;
                }

                if (TextUtils.isEmpty(repassword)){
                    Log.d(TAG, "Repetición de contraseña vacía.");
                    Toast.makeText(Register.this,"Confirma contraseña",Toast.LENGTH_SHORT).show();
                    progressBar.setVisibility(View.GONE);
                    return;
                }

                if(!repassword.equals(password)){
                    Log.d(TAG, "Las contraseñas no coinciden.");
                    Toast.makeText(Register.this,"Contraseñas no coincidentes",Toast.LENGTH_SHORT).show();
                    progressBar.setVisibility(View.GONE);
                }
                else{
                    Log.d(TAG, "Registrando nuevo usuario.");
                    // Llamada a creación nuevo usuario
                    mAuth.createUserWithEmailAndPassword(email, password)
                            .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    progressBar.setVisibility(View.GONE);

                                    if (task.isSuccessful()) {
                                        Log.d(TAG, "Registro exitoso.");
                                        Toast.makeText(Register.this, "Cuenta creada",
                                                Toast.LENGTH_SHORT).show();
                                        Intent intent = new Intent(getApplicationContext(), Login.class);
                                        startActivity(intent);
                                        finish();
                                    } else {
                                        Log.w(TAG, "Registro fallido.", task.getException());
                                        Toast.makeText(Register.this, "Registro fallido",
                                                Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                }
            }
        });
    }
}
