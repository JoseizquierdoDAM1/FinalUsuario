package com.example.finalusuario;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.GenericTypeIndicator;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class menuReservas extends AppCompatActivity {
Usuario usuario;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu_reservas);
        Intent intent = getIntent();
        usuario = (Usuario) intent.getSerializableExtra("usuario");
        cargar();
    }

    public void cargar() {
        DatabaseReference restaurantesRef = FirebaseDatabase.getInstance().getReference("Restaurantes");

        restaurantesRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                ArrayList<Reserva> reservasUsuario = new ArrayList<>();

                for (DataSnapshot restauranteSnapshot : dataSnapshot.getChildren()) {
                    ArrayList<Reserva> reservasRestaurante = restauranteSnapshot.child("reservas").getValue(new GenericTypeIndicator<ArrayList<Reserva>>() {});

                    if (reservasRestaurante != null) {
                        for (Reserva r : reservasRestaurante) {
                            if (r.getNombreUsuario().equals(usuario.getNombreUsuario())) {
                                reservasUsuario.add(r);
                            }
                        }
                    }
                }

                if (!reservasUsuario.isEmpty()) {
                    Toast.makeText(menuReservas.this, "Hay reservas", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(menuReservas.this, "No hay reservas", Toast.LENGTH_SHORT).show();
                }

                RecyclerView recyclerView = findViewById(R.id.recyclerMenuReservas);
                ReservasMenuAdapter adapter = new ReservasMenuAdapter(menuReservas.this, reservasUsuario); // Cambiado a menuReservas.this
                recyclerView.setLayoutManager(new LinearLayoutManager(menuReservas.this));
                recyclerView.setAdapter(adapter);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Manejar errores de Firebase
            }
        });
    }

}