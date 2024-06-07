package com.example.finalusuario;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;



public class RegistroUsuario extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registro_usuario);
    }
    public void subir(View view){
        final EditText nombreuser = findViewById(R.id.labelNombre);
        final EditText email = findViewById(R.id.labelEmail);
        final EditText dni = findViewById(R.id.dni);
        final EditText contraseña = findViewById(R.id.labelContraseña);
        final EditText confirmarcontraseña = findViewById(R.id.labelConfContraseña);
        final TextView error = findViewById(R.id.labelerrorregistroUser);

        final String nombreUsuario = nombreuser.getText().toString();

        DatabaseReference usuariosRef = FirebaseDatabase.getInstance().getReference("Usuarios");

        usuariosRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                boolean usuarioExistente = false;
                for (DataSnapshot usuarioSnapshot : dataSnapshot.getChildren()) {
                    String nombre = usuarioSnapshot.child("nombreUsuario").getValue(String.class);
                    if (nombreUsuario.equals(nombre)) {
                        usuarioExistente = true;
                        break;
                    }
                }

                if (!usuarioExistente) {
                    if (contraseña.getText().toString().equals(confirmarcontraseña.getText().toString())) {
                        // Asignar el identificador único al restaurante
                        String id = usuariosRef.push().getKey();
                        // Asignar el identificador único al restaurante
                        Usuario u = new Usuario(id,nombreUsuario, email.getText().toString(), contraseña.getText().toString(), dni.getText().toString(), "no dueño");
                        usuariosRef.child(id).setValue(u);
                        error.setText("¡Usuario registrado exitosamente!");
                        error.setVisibility(View.VISIBLE);
                        gmail(u.getCorreo());
                        Intent i = new Intent(RegistroUsuario.this, MainActivity.class);
                        startActivity(i);
                    } else {
                        error.setText("Las contraseñas no coinciden");
                        error.setVisibility(View.VISIBLE);
                    }
                } else {
                    error.setText("El nombre de usuario ya existe");
                    error.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Manejo de errores
            }
        });
    }

    public void gmail(String emailUsuario){
        new SendEmailTask().execute(emailUsuario, "Registro en reserBAR", "Gracias por crear su cuenta en reserBAR");

    }

    private class SendEmailTask extends AsyncTask<String, Void, Void> {

        @Override
        protected Void doInBackground(String... params) {
            String toEmail = params[0];
            String subject = params[1];
            String body = params[2];

            EmailSender.sendEmail(toEmail, subject, body);

            return null;
        }
    }





}