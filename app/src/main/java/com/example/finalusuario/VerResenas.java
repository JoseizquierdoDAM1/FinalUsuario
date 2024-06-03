package com.example.finalusuario;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.GenericTypeIndicator;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class VerResenas extends AppCompatActivity {
     Usuario usuario;
     Restaurante restaurante;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ver_resenas);
        Intent intent = getIntent();
        usuario = (Usuario) intent.getSerializableExtra("usuario");

        restaurante = (Restaurante) intent.getSerializableExtra("restaurante");
        cargar();
    }
    public void cargar() {
        DatabaseReference restaurantesRef = FirebaseDatabase.getInstance().getReference("Restaurantes").child(restaurante.getId());

        restaurantesRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                GenericTypeIndicator<ArrayList<Reseña>> genericTypeIndicator2 = new GenericTypeIndicator<ArrayList<Reseña>>() {};
                ArrayList<Reseña> reseñas = dataSnapshot.child("reseñas").getValue(genericTypeIndicator2);
                String nombre=dataSnapshot.child("nombre").getValue(String.class);
                Toast.makeText(VerResenas.this, nombre, Toast.LENGTH_SHORT).show();

                if(reseñas!=null) {
                    RecyclerView recyclerView = findViewById(R.id.recyclerVerReseñas);
                    ReseñaAdapter adapter = new ReseñaAdapter(getApplicationContext(), reseñas);
                    recyclerView.addItemDecoration(new SpaceItemDecoration(50));
                    recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
                    recyclerView.setAdapter(adapter);
                } else{
                    Toast.makeText(VerResenas.this, "no hay reseñas", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Manejar errores de Firebase
            }
        });
    }
}