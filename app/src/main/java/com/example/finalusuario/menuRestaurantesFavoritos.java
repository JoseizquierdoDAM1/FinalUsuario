package com.example.finalusuario;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.PopupMenu;


import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.GenericTypeIndicator;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;


public class menuRestaurantesFavoritos extends AppCompatActivity {
    private Usuario usuario;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu_restaurantes_favoritos);
        Intent intent = getIntent();
        usuario = (Usuario) intent.getSerializableExtra("usuario");
        menu();
        cargar();
    }
    public void menu() {
        ImageView i = findViewById(R.id.imageView2);
        i.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PopupMenu popup = new PopupMenu(v.getContext(), v);
                popup.inflate(R.menu.menu_popup2);
                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        if(item.getItemId()==R.id.action_perfil){
                            Intent i= new Intent(menuRestaurantesFavoritos.this, menuPerfil.class);
                            i.putExtra("usuario",usuario);
                            startActivity(i);
                        }
                        if(item.getItemId()==R.id.action_reservas){
                            Intent i= new Intent(menuRestaurantesFavoritos.this, menuReservas.class);
                            i.putExtra("usuario",usuario);
                            startActivity(i);
                        }
                        if(item.getItemId()==R.id.action_reseñas){
                            Intent i= new Intent(menuRestaurantesFavoritos.this, menuResenas.class);
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

    public void cargar() {
        DatabaseReference restaurantesRef = FirebaseDatabase.getInstance().getReference("Usuarios").child(usuario.getId());

        restaurantesRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                GenericTypeIndicator<ArrayList<Restaurante>> genericTypeIndicator2 = new GenericTypeIndicator<ArrayList<Restaurante>>() {};
                ArrayList<Restaurante> restaurantes = dataSnapshot.child("restaurantesFavoritos").getValue(genericTypeIndicator2);

                if(restaurantes!=null){
                RecyclerView recyclerView = findViewById(R.id.recyclermenuRestaurantesFavoritos);
                RestauranteAdapter adapter = new RestauranteAdapter(getApplicationContext(), restaurantes,usuario);
                recyclerView.addItemDecoration(new SpaceItemDecoration(70));
                recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
                recyclerView.setAdapter(adapter);}
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Manejar errores de Firebase
            }
        });
    }

    public void principal(View view){
        Intent i= new Intent(menuRestaurantesFavoritos.this, VerRestaurantes.class);
        i.putExtra("usuario",usuario);
        startActivity(i);

    }
}