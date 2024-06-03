package com.example.finalusuario;

import static androidx.constraintlayout.helper.widget.MotionEffect.TAG;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.icu.util.Calendar;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
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

import java.util.ArrayList;
import java.util.Date;
import java.util.List;


public class ResenaActivity extends AppCompatActivity {
    private String idrestaurante;
    private Usuario usuario;

    private ImageView e1;
    private ImageView e2;
    private ImageView e3;
    private ImageView e4;
    private ImageView e5;
    int numerodeEstrellas=0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_resena);

        Intent intent = getIntent();
        idrestaurante = intent.getStringExtra("Restauranteid");


        usuario = (Usuario) intent.getSerializableExtra("usuario");


        mostrarInfo();

        e1=findViewById(R.id.Estrella1);
        e2=findViewById(R.id.Estrella2);
        e3=findViewById(R.id.Estrella3);
        e4=findViewById(R.id.Estrella4);
        e5=findViewById(R.id.Estrella5);
    }

    public void mostrarInfo(){
        TextView lNombre=findViewById(R.id.labelNombreRestaurante);
        ImageView limagen=findViewById(R.id.labelImagenRestaurante);

        DatabaseReference restauranteRef = FirebaseDatabase.getInstance().getReference("Restaurantes").child(idrestaurante);

        restauranteRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    String id = dataSnapshot.child("id").getValue(String.class);
                    String nombre = dataSnapshot.child("nombre").getValue(String.class);
                    String ciudad = dataSnapshot.child("ciudad").getValue(String.class);
                    String tipo = dataSnapshot.child("tipo").getValue(String.class);
                    String imageUrl = dataSnapshot.child("imagen").getValue(String.class);

                    lNombre.setText(nombre);
                    Glide.with(ResenaActivity.this)
                            .load(imageUrl)
                            .into(limagen);
                } else {
                    Toast.makeText(ResenaActivity.this, "Restaurante no encontrado.", Toast.LENGTH_SHORT).show();
                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Manejar errores de Firebase
                Toast.makeText(ResenaActivity.this, "Error al obtener los datos del restaurante.", Toast.LENGTH_SHORT).show();
            }
        });

    }

    public void crearReseña(View view) {
        EditText textoReseña = findViewById(R.id.labelReseña);

        DatabaseReference restauranteRef = FirebaseDatabase.getInstance().getReference("Restaurantes").child(idrestaurante).child("reseñas");

        restauranteRef.addListenerForSingleValueEvent(new ValueEventListener() {
            ArrayList<Reseña> reseñas= new ArrayList<>();
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                GenericTypeIndicator<ArrayList<Reseña>> genericTypeIndicator = new GenericTypeIndicator<ArrayList<Reseña>>() {};
                ArrayList<Reseña> reseñasRestaurante = dataSnapshot.getValue(genericTypeIndicator);
                int id=0;
                if(reseñasRestaurante==null){
                    id=1;
                    reseñasRestaurante=new ArrayList<>();
                }else{
                    id=reseñasRestaurante.size()+1;
                }
                Calendar calendar = Calendar.getInstance();
                Date currentDate = calendar.getTime();
                Reseña reseña = new Reseña(id,usuario.getId(),idrestaurante,usuario.getNombreUsuario(),textoReseña.getText().toString(),numerodeEstrellas,currentDate);

                reseñasRestaurante.add(reseña);

                DatabaseReference restauranteRef = FirebaseDatabase.getInstance().getReference("Restaurantes").child(idrestaurante);
                ArrayList<Reseña> finalReseñasRestaurante = reseñasRestaurante;
                restauranteRef.child("reseñas").setValue(reseñasRestaurante).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            valoracionrestaurante(finalReseñasRestaurante);
                            // La operación se completó exitosamente
                            Log.d(TAG, "Datos de reseñas actualizados correctamente.");
                        } else {
                            // La operación falló
                            Log.e(TAG, "Error al actualizar los datos de reseñas: " + task.getException().getMessage());
                        }
                    }
                });

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Manejar errores de Firebase
                Toast.makeText(ResenaActivity.this, "Error al acceder a la base de datos.", Toast.LENGTH_SHORT).show();
            }
        });




    }

    public void valoracionrestaurante(ArrayList<Reseña> reseñas){
        int valoracion=0;
        for (Reseña r:reseñas){
            valoracion=valoracion+r.getValoracion();
        }
        valoracion=valoracion/reseñas.size();

        DatabaseReference restauranteRef = FirebaseDatabase.getInstance().getReference("Restaurantes").child(idrestaurante);
        restauranteRef.child("valoracion").setValue(valoracion).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    Intent i= new Intent(ResenaActivity.this,VerRestaurantes.class);
                    i.putExtra("usuario",usuario);
                    startActivity(i);
                    // La operación se completó exitosamente
                    Log.d(TAG, "Datos de reseñas actualizados correctamente.");
                } else {
                    // La operación falló
                    Log.e(TAG, "Error al actualizar los datos de reseñas: " + task.getException().getMessage());
                }
            }
        });
    }




    public void unaestrella(View view){
        if(this.numerodeEstrellas==1){
            e1.setVisibility(View.INVISIBLE);
        }else{
            e1.setVisibility(View.VISIBLE);
        }

       e2.setVisibility(View.INVISIBLE);
       e3.setVisibility(View.INVISIBLE);
       e4.setVisibility(View.INVISIBLE);
       e5.setVisibility(View.INVISIBLE);
        this.numerodeEstrellas=1;
    }

    public void dosestrellas(View view){
        e1.setVisibility(View.VISIBLE);
        e2.setVisibility(View.VISIBLE);
        e3.setVisibility(View.INVISIBLE);
        e4.setVisibility(View.INVISIBLE);
        e5.setVisibility(View.INVISIBLE);
        this.numerodeEstrellas=2;

    }
    public void tresestrellas(View view){
        e1.setVisibility(View.VISIBLE);
        e2.setVisibility(View.VISIBLE);
        e3.setVisibility(View.VISIBLE);
        e4.setVisibility(View.INVISIBLE);
        e5.setVisibility(View.INVISIBLE);
        this.numerodeEstrellas=3;

    }
    public void cuatroestrellas(View view){
        e1.setVisibility(View.VISIBLE);
        e2.setVisibility(View.VISIBLE);
        e3.setVisibility(View.VISIBLE);
        e4.setVisibility(View.VISIBLE);
        e5.setVisibility(View.INVISIBLE);
        this.numerodeEstrellas=4;

    }
    public void cincoestrellas(View view){
        e1.setVisibility(View.VISIBLE);
        e2.setVisibility(View.VISIBLE);
        e3.setVisibility(View.VISIBLE);
        e4.setVisibility(View.VISIBLE);
        e5.setVisibility(View.VISIBLE);
        this.numerodeEstrellas=5;

    }


}