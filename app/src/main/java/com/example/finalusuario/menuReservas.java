package com.example.finalusuario;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.PopupMenu;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.GenericTypeIndicator;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class menuReservas extends AppCompatActivity {
    private Usuario usuario;
    private Button reservas;
    private Button Historialreservas;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu_reservas);
        Intent intent = getIntent();
        usuario = (Usuario) intent.getSerializableExtra("usuario");
        reservas=findViewById(R.id.reservas);
        Historialreservas=findViewById(R.id.verHistorialReservas);
        cargar("reservas",50);
        menu();
    }

    public void menu() {
        ImageView i = findViewById(R.id.imageView2);
        i.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PopupMenu popup = new PopupMenu(v.getContext(), v);
                popup.inflate(R.menu.menu_popup4);
                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        if(item.getItemId()==R.id.action_perfil){
                            Intent i= new Intent(menuReservas.this, menuPerfil.class);
                            i.putExtra("usuario",usuario);
                            startActivity(i);
                        }

                        if(item.getItemId()==R.id.action_reseñas){
                            Intent i= new Intent(menuReservas.this, menuResenas.class);
                            i.putExtra("usuario",usuario);
                            startActivity(i);
                        }
                        if(item.getItemId()==R.id.action_restFav){
                            Intent i= new Intent(menuReservas.this, menuRestaurantesFavoritos.class);
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

    public void cargar(String tipo,int valor) {
        DatabaseReference restaurantesRef = FirebaseDatabase.getInstance().getReference("Restaurantes");

        restaurantesRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                ArrayList<Reserva> reservasUsuario = new ArrayList<>();

                for (DataSnapshot restauranteSnapshot : dataSnapshot.getChildren()) {
                    ArrayList<Reserva> reservasRestaurante = restauranteSnapshot.child(tipo).getValue(new GenericTypeIndicator<ArrayList<Reserva>>() {});

                    if (reservasRestaurante != null) {
                        for (Reserva r : reservasRestaurante) {
                            if (r.getNombreUsuario().equals(usuario.getNombreUsuario())) {
                                reservasUsuario.add(r);
                            }
                        }
                    }
                }


                RecyclerView recyclerView = findViewById(R.id.recyclerMenuReservas);
                ReservasMenuAdapter adapter = new ReservasMenuAdapter(menuReservas.this, reservasUsuario,tipo); // Cambiado a menuReservas.this
                recyclerView.addItemDecoration(new SpaceItemDecoration(valor));
                recyclerView.setLayoutManager(new LinearLayoutManager(menuReservas.this));
                recyclerView.setAdapter(adapter);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Manejar errores de Firebase
            }
        });
    }

    public void reservas(View view) {
        reservas.setVisibility(View.INVISIBLE);
        Historialreservas.setVisibility(View.VISIBLE);
        cargar("reservas",0);
    }

    public void HistorialReserva(View view) {
        reservas.setVisibility(View.VISIBLE);
        Historialreservas.setVisibility(View.INVISIBLE);
        cargar("historialReservas",0);
    }

    public void principal(View view){
        Intent i= new Intent(menuReservas.this, VerRestaurantes.class);
        i.putExtra("usuario",usuario);
        startActivity(i);

    }

}