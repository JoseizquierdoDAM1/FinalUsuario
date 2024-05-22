package com.example.finalusuario;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.GenericTypeIndicator;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class VerRestaurantes extends AppCompatActivity {
    private Usuario usuario;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ver_restaurantes);
        Intent intent = getIntent();
        usuario = (Usuario) intent.getSerializableExtra("usuario");
        @SuppressLint({"MissingInflatedId", "LocalSuppress"}) TextView t = findViewById(R.id.labelVerNombre);
        t.setText("Bienvenido " + usuario.getNombreUsuario());
        cargar();
        verNotificaciones();

    }

    public void cargar() {

        DatabaseReference restaurantesRef = FirebaseDatabase.getInstance().getReference("Restaurantes");

        restaurantesRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                ArrayList<Restaurante> restaurantes = new ArrayList<>();
                for (DataSnapshot restauranteSnapshot : dataSnapshot.getChildren()) {
                    // Obtener los datos del restaurante del snapshot
                    String id = restauranteSnapshot.child("id").getValue(String.class);
                    String nombre = restauranteSnapshot.child("nombre").getValue(String.class);
                    String ciudad = restauranteSnapshot.child("ciudad").getValue(String.class);
                    String tipo = restauranteSnapshot.child("tipo").getValue(String.class);
                    String dniUsuario = restauranteSnapshot.child("dniUsuario").getValue(String.class);
                    String imageUrl = restauranteSnapshot.child("imagen").getValue(String.class);
                    GenericTypeIndicator<ArrayList<Reserva>> genericTypeIndicator = new GenericTypeIndicator<ArrayList<Reserva>>() {
                    };
                    ArrayList<Reserva> reservas = restauranteSnapshot.child("reservas").getValue(genericTypeIndicator);
                    int comensales = restauranteSnapshot.child("comensales").getValue(Integer.class);

                    Restaurante r = new Restaurante(id, nombre, tipo, ciudad, dniUsuario, imageUrl, comensales, reservas);
                    restaurantes.add(r);
                }
                RecyclerView recyclerView = findViewById(R.id.recycler);
                RestauranteAdapter adapter = new RestauranteAdapter(getApplicationContext(), restaurantes, usuario);
                adapter.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        // Obtener el restaurante seleccionado
                        int position = recyclerView.getChildAdapterPosition(view);
                        Restaurante restaurante = restaurantes.get(position);

                        // Abrir la actividad DetalleRestaurante y pasar el restaurante seleccionado como extra
                        Intent intent = new Intent(VerRestaurantes.this, DetalleRestaurante.class);
                        intent.putExtra("restaurante", restaurante);
                        intent.putExtra("usuario", usuario);
                        startActivity(intent);


                    }
                });
                recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
                recyclerView.setAdapter(adapter);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    public void notificaciones(View view) {
        MensajeDialogFragment dialogFragment = MensajeDialogFragment.newInstance(usuario);
        dialogFragment.show(getSupportFragmentManager(), "MensajeDialogFragment");

        ImageView img=findViewById(R.id.noHayMensajes);
        ImageView img2=findViewById(R.id.hayMensajes);
        img.setVisibility(View.VISIBLE);
        img2.setVisibility(View.INVISIBLE);
    }

    public void verNotificaciones() {
        DatabaseReference mensajesRef = FirebaseDatabase.getInstance().getReference("Usuarios").child(usuario.getNombreUsuario()).child("mensajes");

        mensajesRef.addListenerForSingleValueEvent(new ValueEventListener() {
           @Override
           public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
               GenericTypeIndicator<ArrayList<String>> t = new GenericTypeIndicator<ArrayList<String>>() {
               };
               List<String> mensajes = dataSnapshot.getValue(t);

               ImageView img=findViewById(R.id.noHayMensajes);
               ImageView img2=findViewById(R.id.hayMensajes);
               if (mensajes == null) {
                   img.setVisibility(View.VISIBLE);
                   img2.setVisibility(View.INVISIBLE);
               }else{
                   img.setVisibility(View.INVISIBLE);
                   img2.setVisibility(View.VISIBLE);
               }
           }

           @Override
           public void onCancelled(@NonNull DatabaseError error) {

           }
       }
        );


    }
}