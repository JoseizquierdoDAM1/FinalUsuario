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
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.regex.Matcher;
import java.util.regex.Pattern;


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
        error.setVisibility(View.INVISIBLE);
        final String nombreUsuario = nombreuser.getText().toString();
        if (!nombreUsuario.equals("") && !dni.getText().toString().equals("") && !email.getText().toString().equals("") && !contraseña.getText().toString().equals("") && !confirmarcontraseña.getText().toString().equals("")) {
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


                if(!usuarioExistente){
                    if (isValidEmailFormat(email.getText().toString())) {
                        if(isValidDNIFormat(dni.getText().toString())) {
                            if (contraseña.getText().toString().length() >= 8) {
                                if (contraseña.getText().toString().equals(confirmarcontraseña.getText().toString())) {
                                    // Asignar el identificador único al restaurante
                                    String id = usuariosRef.push().getKey();
                                    // Asignar el identificador único al restaurante
                                    Usuario u = new Usuario(id, nombreUsuario, email.getText().toString(), contraseña.getText().toString(), dni.getText().toString(), "no dueño");
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
                                error.setText("La contrasenya debe tener minimo 8 caracteres");
                                error.setVisibility(View.VISIBLE);
                            }
                        }else{
                            error.setText("El formato de el DNI no es correcto");
                            error.setVisibility(View.VISIBLE);
                        }
                    }else{
                        error.setText("El formato de el email no es correcto");
                        error.setVisibility(View.VISIBLE);
                    }

                }else {
                    error.setText("El nombre de usuario ya existe");
                    error.setVisibility(View.VISIBLE);
                }


            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Manejo de errores
            }
        });
        }else{
            error.setText("Tienes que llenar todos los campos");
            error.setVisibility(View.VISIBLE);
        }
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

    public boolean isValidEmailFormat(String email) {

        // Expresión regular para validar el formato de una dirección de correo electrónico
        String regex = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$";

        // Compilar la expresión regular en un objeto Pattern
        Pattern pattern = Pattern.compile(regex);

        // Crear un objeto Matcher para comparar el correo electrónico con la expresión regular
        Matcher matcher = pattern.matcher(email);
        // Devolver true si el correo electrónico coincide con el formato válido, de lo contrario, devolver false
        return matcher.matches();
    }

    public static boolean isValidDNIFormat(String dni) {
        // Expresión regular para validar el formato de un número de DNI
        String regex = "\\d{8}[A-HJ-NP-TV-Z]";

        // Compilar la expresión regular en un objeto Pattern
        Pattern pattern = Pattern.compile(regex);

        // Crear un objeto Matcher para comparar el DNI con la expresión regular
        Matcher matcher = pattern.matcher(dni);

        // Devolver true si el DNI coincide con el formato válido, de lo contrario, devolver false
        return matcher.matches();
    }





}