package com.example.finalusuario;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.GenericTypeIndicator;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class menuResenas extends AppCompatActivity {
    private Usuario usuario;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu_resenas);

        Intent intent = getIntent();
        usuario = (Usuario) intent.getSerializableExtra("usuario");
        cargar();
        menu();
    }

    public void menu() {
        ImageView i = findViewById(R.id.imageView2);
        i.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PopupMenu popup = new PopupMenu(v.getContext(), v);
                popup.inflate(R.menu.menu_popup3);
                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        if(item.getItemId()==R.id.action_perfil){
                            Intent i= new Intent(menuResenas.this, menuPerfil.class);
                            i.putExtra("usuario",usuario);
                            startActivity(i);
                        }
                        if(item.getItemId()==R.id.action_reservas){
                            Intent i= new Intent(menuResenas.this, menuReservas.class);
                            i.putExtra("usuario",usuario);
                            startActivity(i);
                        }

                        if(item.getItemId()==R.id.action_restFav){
                            Intent i= new Intent(menuResenas.this, menuRestaurantesFavoritos.class);
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
        DatabaseReference restaurantesRef = FirebaseDatabase.getInstance().getReference("Restaurantes");

        restaurantesRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                ArrayList<Reseña> reseñasUsuario = new ArrayList<>();
                ArrayList<Reseña> todasreseñas = new ArrayList<>();
                for (DataSnapshot restauranteSnapshot : dataSnapshot.getChildren()) {
                    GenericTypeIndicator<ArrayList<Reseña>> genericTypeIndicator2 = new GenericTypeIndicator<ArrayList<Reseña>>() {};
                    ArrayList<Reseña> reseñas = restauranteSnapshot.child("reseñas").getValue(genericTypeIndicator2);
                    if(reseñas!=null){
                    for(Reseña r:reseñas){
                       todasreseñas.add(r);
                    }}
                }

                for(Reseña r:todasreseñas){
                    if(r.getIdUsuario().equals(usuario.getId())){
                        reseñasUsuario.add(r);
                    }
                }
                if(reseñasUsuario!=null) {
                    RecyclerView recyclerView = findViewById(R.id.recyclerReseñasMenu);
                    ReseñaMenuAdapter adapter = new ReseñaMenuAdapter(getApplicationContext(), reseñasUsuario);
                    recyclerView.addItemDecoration(new SpaceItemDecoration(70));
                    recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
                    recyclerView.setAdapter(adapter);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Manejar errores de Firebase
            }
        });
    }

    public void principal(View view){
        Intent i= new Intent(menuResenas.this, VerRestaurantes.class);
        i.putExtra("usuario",usuario);
        startActivity(i);

    }
}