package com.example.finalusuario;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.GenericTypeIndicator;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import org.w3c.dom.Text;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

public class menuPerfil extends AppCompatActivity {
    private Usuario usuario;
    private StorageReference mStorage;
    private static final int GALLERY_INTENT = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu_perfil);
        Intent intent = getIntent();
        usuario = (Usuario) intent.getSerializableExtra("usuario");
        mStorage = FirebaseStorage.getInstance().getReference();
        acceder();
        menu();
    }

    public void menu() {
        ImageView i = findViewById(R.id.imageView2);
        i.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PopupMenu popup = new PopupMenu(v.getContext(), v);
                popup.inflate(R.menu.menu_popup1);
                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        if(item.getItemId()==R.id.action_reservas){
                            Intent i= new Intent(menuPerfil.this, menuReservas.class);
                            i.putExtra("usuario",usuario);
                            startActivity(i);
                        }
                        if(item.getItemId()==R.id.action_reseñas){
                            Intent i= new Intent(menuPerfil.this, menuResenas.class);
                            i.putExtra("usuario",usuario);
                            startActivity(i);
                        }
                        if(item.getItemId()==R.id.action_restFav){
                            Intent i= new Intent(menuPerfil.this, menuRestaurantesFavoritos.class);
                            i.putExtra("usuario",usuario);
                            startActivity(i);
                        }

                        return true;
                    }
                });
                popup.show();
            }
        });
    }

    public void acceder() {
        ImageView imagenPerfil = findViewById(R.id.imagen);
        EditText labelGmail = findViewById(R.id.labelEmailPerfil);
         TextView labelnombre = findViewById(R.id.labelnombre);

        // Suponiendo que ya tengas la referencia a tu base de datos de Firebase
        DatabaseReference usuariosRef = FirebaseDatabase.getInstance().getReference("Usuarios").child(usuario.getId());

        usuariosRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot usuarioSnapshot) {
                // Obtener los datos del usuario del snapshot
                String nombre = usuarioSnapshot.child("nombreUsuario").getValue(String.class);
                String correo = usuarioSnapshot.child("correo").getValue(String.class);
                String contraseñaUsuario = usuarioSnapshot.child("contraseña").getValue(String.class);
                String tipoUser = usuarioSnapshot.child("tipUser").getValue(String.class);
                String dni = usuarioSnapshot.child("dni").getValue(String.class);
                String urlImagen = usuarioSnapshot.child("urlImagen").getValue(String.class);

                labelGmail.setText(correo);
                labelnombre.setText("Datos de: "+nombre);

                if (urlImagen != null) {
                    Glide.with(menuPerfil.this)
                            .load(urlImagen)
                            .into(imagenPerfil);
                    usuario.setUrlImagen(urlImagen);
                } else {
                    imagenPerfil.setImageResource(R.drawable.perfil);
                }


            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    public void guardar(View view) {
        EditText labelGmail = findViewById(R.id.labelEmailPerfil);
        EditText contraseña = findViewById(R.id.labelCambiarContraseñaPerfil);
        EditText Confirmar = findViewById(R.id.labelConfirmarContraseñaPerfil);

        // Suponiendo que ya tengas la referencia a tu base de datos de Firebase
        DatabaseReference usuariosRef = FirebaseDatabase.getInstance().getReference("Usuarios").child(usuario.getId());

        usuariosRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot usuarioSnapshot) {
                String contraseñaUsuario = usuarioSnapshot.child("contraseña").getValue(String.class);

                    String password = contraseña.getText().toString();
                    String confirmPassword = Confirmar.getText().toString();

                    if (!password.isEmpty() && !confirmPassword.isEmpty()) {
                        if(password.equals(confirmPassword)){
                        if (!password.equals(contraseñaUsuario)) {
                            // Guardar en base de datos
                            usuariosRef.child("contraseña").setValue(password);
                            usuariosRef.child("correo").setValue(labelGmail.getText().toString());
                            guardarmensaje();
                            Toast.makeText(menuPerfil.this, "Contraseña guardada con éxito", Toast.LENGTH_SHORT).show();

                        }else{
                            Toast.makeText(menuPerfil.this, "La contraseña no puede ser igual a la anterior", Toast.LENGTH_SHORT).show();
                        }
                        } else {
                            // Las contraseñas no coinciden
                            Toast.makeText(menuPerfil.this, "Las contraseñas no coinciden", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        // Uno o ambos campos están vacíos
                        usuariosRef.child("correo").setValue(labelGmail.getText().toString());
                        guardarmensaje();
                    }





            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }

    public void cambiarImagen(View view) {
        AlertDialog.Builder dialogo1 = new AlertDialog.Builder(menuPerfil.this);
        dialogo1.setTitle("Imagen");
        dialogo1.setMessage("¿ Quieres cambiar tu foto de perfil ?");
        dialogo1.setCancelable(false);
        dialogo1.setPositiveButton("Confirmar", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialogo1, int id) {
                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setType("image/*");
                startActivityForResult(intent, GALLERY_INTENT);
            }
        });
        dialogo1.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialogo1, int id) {
                dialogo1.dismiss();
            }
        });
        dialogo1.show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == GALLERY_INTENT && resultCode == RESULT_OK && data != null) {
            Uri uri = data.getData();
            if (uri != null) {
                subirImagenAFirebase(uri);
            }
        }
    }

    private void subirImagenAFirebase(Uri uri) {
        StorageReference filePath = mStorage.child("fotos").child(uri.getLastPathSegment());

        filePath.putFile(uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                filePath.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri downloadUri) {
                        String urlImagen = downloadUri.toString();
                        actualizarImagenEnFirebase(urlImagen);
                    }
                });
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(menuPerfil.this, "Error al subir la imagen", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void actualizarImagenEnFirebase(String urlImagen) {
        DatabaseReference usuariosRef = FirebaseDatabase.getInstance().getReference("Usuarios").child(usuario.getId());
        usuariosRef.child("urlImagen").setValue(urlImagen).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    Toast.makeText(menuPerfil.this, "Imagen actualizada correctamente", Toast.LENGTH_SHORT).show();
                    // Actualizar la interfaz de usuario
                    actualizarImagenPerfil(urlImagen);
                } else {
                    Toast.makeText(menuPerfil.this, "Error al actualizar la imagen", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void actualizarImagenPerfil(String urlImagen) {
        ImageView imagenPerfil = findViewById(R.id.imagen);
        Glide.with(this).load(urlImagen).into(imagenPerfil);
    }


    public void guardarmensaje() {
        DatabaseReference usuariosRef = FirebaseDatabase.getInstance().getReference("Usuarios").child(usuario.getId());

        usuariosRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                List<String> mensajes = new ArrayList<>();
                GenericTypeIndicator<ArrayList<String>> genericTypeIndicator = new GenericTypeIndicator<ArrayList<String>>() {};
                List<String> mensajesUser = dataSnapshot.child("mensajes").getValue(genericTypeIndicator);

                if (mensajesUser != null) {
                    mensajes.addAll(mensajesUser);
                }

                String mensaje = "Su perfil ha sido actualizado correctamente";
                mensajes.add(mensaje);
                usuariosRef.child("mensajes").setValue(mensajes).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Log.d("Firebase", "Mensaje guardado correctamente");
                            Intent i= new Intent(menuPerfil.this, VerRestaurantes.class);
                            i.putExtra("usuario",usuario);
                            startActivity(i);
                        } else {
                            Log.e("Firebase", "Error al guardar el mensaje", task.getException());
                        }
                    }
                });
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Manejar errores de Firebase
                new Handler(Looper.getMainLooper()).post(() -> Toast.makeText(menuPerfil.this, "Error al guardar el mensaje", Toast.LENGTH_SHORT).show());
            }
        });


    }

    public void principal(View view){
        Intent i= new Intent(menuPerfil.this, VerRestaurantes.class);
        i.putExtra("usuario",usuario);
        startActivity(i);

    }
}



