package com.example.finalusuario;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.GenericTypeIndicator;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Locale;

public class VerRestaurantes extends AppCompatActivity {
    private Usuario usuario;
    private int filtrar=0;
    LinearLayout layoutEntero;
    LinearLayout layoutItems;
    LinearLayout layoutBotones;
    Button bfiltrar;
    Button beliminarFiltros;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ver_restaurantes);
        Intent intent = getIntent();
        usuario = (Usuario) intent.getSerializableExtra("usuario");
        @SuppressLint({"MissingInflatedId", "LocalSuppress"}) TextView t = findViewById(R.id.labelVerNombre);
        t.setText("Bienvenido " + usuario.getNombreUsuario());
         layoutEntero= findViewById(R.id.layoutentero);
         layoutItems= findViewById(R.id.layoutItems);
         layoutBotones= findViewById(R.id.layoutBotones);
         bfiltrar=findViewById(R.id.button3);
         beliminarFiltros=findViewById(R.id.button5);
        menu();
        reseña();
        cargar(70);
        verNotificaciones();

    }

    public void rellenarSpinnerComunidades(){
        final ArrayList<String>[] nombresComunidades = new ArrayList[]{new ArrayList<>()};

// Obtener la referencia al nodo "comunidades" en la base de datos
        DatabaseReference restaurantesRef = FirebaseDatabase.getInstance().getReference("Restaurantes");

        restaurantesRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // Iterar sobre los datos dentro del nodo "comunidades"
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    // Obtener el nombre de cada comunidad
                    String comunidadaAutonoma = snapshot.child("comunidadaAutonoma").getValue(String.class);

                        nombresComunidades[0].add(comunidadaAutonoma);


                }

                nombresComunidades[0] = new ArrayList<>(new LinkedHashSet<>(nombresComunidades[0]));
                Spinner spinnerComunidades = findViewById(R.id.spinercomunidadesmain);

                ArrayAdapter<String> adapter = new ArrayAdapter<>(VerRestaurantes.this, android.R.layout.simple_spinner_item, nombresComunidades[0]);


                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);


                spinnerComunidades.setAdapter(adapter);


                spinnerComunidades.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                        // Obtener el nombre de la comunidad seleccionada
                        String comunidadSeleccionada = nombresComunidades[0].get(position);

                        // Llamar al método provincias y pasar el nombre de la comunidad como argumento

                        rellenarSpinnerProvincias(comunidadSeleccionada);

                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {
                        // Manejar el caso en el que no se seleccione ninguna comunidad
                    }
                });
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Manejar errores
            }
        });

    }



    public void rellenarSpinnerProvincias(String comunidad){
        final ArrayList<String>[] nombresProvincias = new ArrayList[]{new ArrayList<>()};

// Obtener la referencia al nodo "comunidades" en la base de datos
        DatabaseReference restaurantesRef = FirebaseDatabase.getInstance().getReference("Restaurantes");

// Consulta para obtener restaurantes donde la comunidad sea "Madrid"
        Query query = restaurantesRef.orderByChild("comunidadaAutonoma").equalTo(comunidad);

        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // Iterar sobre los datos dentro del nodo "comunidades"
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    // Obtener el nombre de cada comunidad
                    String provincia = snapshot.child("provincia").getValue(String.class);

                    nombresProvincias[0].add(provincia);
                }

                nombresProvincias[0] =new ArrayList<>(new LinkedHashSet<>(nombresProvincias[0]));


                Spinner  spinnerprovincias = findViewById(R.id.spinerprovinciasmain);

                ArrayAdapter<String> adapter = new ArrayAdapter<>(VerRestaurantes.this, android.R.layout.simple_spinner_item, nombresProvincias[0]);


                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);


                spinnerprovincias.setAdapter(adapter);


                spinnerprovincias.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                        // Obtener el nombre de la comunidad seleccionada
                        String provinciaSeleccionada = nombresProvincias[0].get(position);

                        // Llamar al método provincias y pasar el nombre de la comunidad como argumento
                        rellenarSpinnerCiudad(provinciaSeleccionada);
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {
                        // Manejar el caso en el que no se seleccione ninguna comunidad
                    }
                });
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Manejar errores
            }
        });
    }

    public void rellenarSpinnerCiudad(String provincia){
        final ArrayList<String>[] nombresCiudades = new ArrayList[]{new ArrayList<>()};

// Obtener la referencia al nodo "comunidades" en la base de datos
        DatabaseReference restaurantesRef = FirebaseDatabase.getInstance().getReference("Restaurantes");

// Consulta para obtener restaurantes donde la comunidad sea "Madrid"
        Query query = restaurantesRef.orderByChild("provincia").equalTo(provincia);

        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // Iterar sobre los datos dentro del nodo "comunidades"
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    // Obtener el nombre de cada comunidad
                    String ciudad = snapshot.child("ciudad").getValue(String.class);

                    nombresCiudades[0].add(ciudad);
                }

                nombresCiudades[0] =new ArrayList<>(new LinkedHashSet<>(nombresCiudades[0]));


                Spinner  spinerciudades = findViewById(R.id.spinerciudadesmain);

                ArrayAdapter<String> adapter = new ArrayAdapter<>(VerRestaurantes.this, android.R.layout.simple_spinner_item, nombresCiudades[0]);


                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);


                spinerciudades.setAdapter(adapter);


            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Manejar errores
            }
        });
    }


    public void Filtrar(View view) {
        Spinner ciudades = findViewById(R.id.spinerciudadesmain);
        String ciudadS = (String) ciudades.getSelectedItem();
        if (filtrar==1) {
            Toast.makeText(VerRestaurantes.this, ciudadS, Toast.LENGTH_SHORT).show();
        DatabaseReference restaurantesRef = FirebaseDatabase.getInstance().getReference("Restaurantes");
        Query query = restaurantesRef.orderByChild("ciudad").equalTo(ciudadS);

        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                ArrayList<Restaurante> restaurantes = new ArrayList<>();
                for (DataSnapshot restauranteSnapshot : dataSnapshot.getChildren()) {
                    String id = restauranteSnapshot.child("id").getValue(String.class);
                    String nombre = restauranteSnapshot.child("nombre").getValue(String.class);
                    String tipo = restauranteSnapshot.child("tipo").getValue(String.class);
                    String dniUsuario = restauranteSnapshot.child("dniUsuario").getValue(String.class);
                    String imageUrl = restauranteSnapshot.child("imagen").getValue(String.class);
                    int valoracion = restauranteSnapshot.child("valoracion").getValue(Integer.class);
                    String comunidadaAutonoma = restauranteSnapshot.child("comunidadaAutonoma").getValue(String.class);
                    String provincia = restauranteSnapshot.child("provincia").getValue(String.class);
                    String ciudad = restauranteSnapshot.child("ciudad").getValue(String.class);
                    GenericTypeIndicator<ArrayList<Reserva>> genericTypeIndicator = new GenericTypeIndicator<ArrayList<Reserva>>() {
                    };
                    ArrayList<Reserva> reservas = restauranteSnapshot.child("reservas").getValue(genericTypeIndicator);

                    GenericTypeIndicator<ArrayList<Reseña>> genericTypeIndicator2 = new GenericTypeIndicator<ArrayList<Reseña>>() {
                    };
                    ArrayList<Reseña> reseñas = restauranteSnapshot.child("reseñas").getValue(genericTypeIndicator2);

                    int comensales = restauranteSnapshot.child("comensales").getValue(Integer.class);

                    Restaurante r = new Restaurante(id, nombre, tipo, comunidadaAutonoma, provincia, ciudad, dniUsuario, imageUrl, comensales, reservas, reseñas, valoracion);
                    restaurantes.add(r);

                }

                if (restaurantes != null) {
                    RecyclerView recyclerView = findViewById(R.id.recyclermenuRestaurantesFavoritos);
                    RestauranteAdapter adapter = new RestauranteAdapter(getApplicationContext(), restaurantes, usuario);
                    adapter.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            int position = recyclerView.getChildAdapterPosition(view);
                            Restaurante restaurante = restaurantes.get(position);

                            Intent intent = new Intent(VerRestaurantes.this, DetalleRestaurante.class);
                            intent.putExtra("restaurante", restaurante);
                            intent.putExtra("usuario", usuario);
                            startActivity(intent);
                        }
                    });

                    recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
                    recyclerView.setAdapter(adapter);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Manejar errores de Firebase
            }
        });
    }else{
            filtrar=1;
            rellenarSpinnerComunidades();
            layoutEntero= findViewById(R.id.layoutentero);
            layoutItems= findViewById(R.id.layoutItems);
            layoutBotones= findViewById(R.id.layoutBotones);
            bfiltrar=findViewById(R.id.button3);
            beliminarFiltros=findViewById(R.id.button5);

            RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) bfiltrar.getLayoutParams();
            params.leftMargin = 10; // Establece el margen izquierdo en dp (reemplaza con el valor deseado)
            bfiltrar.setLayoutParams(params);

            beliminarFiltros.setVisibility(View.VISIBLE);

            // Cambiar el ancho del layoutEntero a 70dp
            LinearLayout.LayoutParams layoutEnteroParams = (LinearLayout.LayoutParams) layoutEntero.getLayoutParams();
            layoutEnteroParams.height = (int) (70 * getResources().getDisplayMetrics().density);
            layoutEntero.setLayoutParams(layoutEnteroParams);

            // Cambiar el margen superior de layoutBotones a 35dp

            RelativeLayout.LayoutParams layoutBotonesParams = new RelativeLayout.LayoutParams(
                    layoutBotones.getLayoutParams().width, layoutBotones.getLayoutParams().height);
            layoutBotonesParams.topMargin = (int) (35 * getResources().getDisplayMetrics().density);
            layoutBotones.setLayoutParams(layoutBotonesParams);

            // Hacer visible el layoutItems
            layoutItems.setVisibility(View.VISIBLE);
        }

    }

    public void eliminarFiltros(View view){
        filtrar=0;

        rellenarSpinnerComunidades();
        layoutEntero= findViewById(R.id.layoutentero);
        layoutItems= findViewById(R.id.layoutItems);
        layoutBotones= findViewById(R.id.layoutBotones);
        bfiltrar=findViewById(R.id.button3);
        beliminarFiltros=findViewById(R.id.button5);

        RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) bfiltrar.getLayoutParams();
        params.leftMargin = 330; // Establece el margen izquierdo en dp (reemplaza con el valor deseado)
        bfiltrar.setLayoutParams(params);

        beliminarFiltros.setVisibility(View.INVISIBLE);

        // Cambiar el ancho del layoutEntero a 70dp
        LinearLayout.LayoutParams layoutEnteroParams = (LinearLayout.LayoutParams) layoutEntero.getLayoutParams();
        layoutEnteroParams.height = (int) (45 * getResources().getDisplayMetrics().density);
        layoutEntero.setLayoutParams(layoutEnteroParams);

        // Cambiar el margen superior de layoutBotones a 35dp

        RelativeLayout.LayoutParams layoutBotonesParams = new RelativeLayout.LayoutParams(
                layoutBotones.getLayoutParams().width, layoutBotones.getLayoutParams().height);
        layoutBotonesParams.topMargin = (int) (10 * getResources().getDisplayMetrics().density);
        layoutBotones.setLayoutParams(layoutBotonesParams);

        // Hacer visible el layoutItems
        layoutItems.setVisibility(View.INVISIBLE);

        cargar(0);
    }

    public void menu() {
        ImageView i = findViewById(R.id.imageView2);
        i.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PopupMenu popup = new PopupMenu(v.getContext(), v);
                popup.inflate(R.menu.menu_popup);
                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        if(item.getItemId()==R.id.action_perfil){
                            Intent i= new Intent(VerRestaurantes.this, menuPerfil.class);
                            i.putExtra("usuario",usuario);
                            startActivity(i);
                        }
                        if(item.getItemId()==R.id.action_reservas){
                            Intent i= new Intent(VerRestaurantes.this, menuReservas.class);
                            i.putExtra("usuario",usuario);
                            startActivity(i);
                        }
                        if(item.getItemId()==R.id.action_reseñas){
                            Intent i= new Intent(VerRestaurantes.this, menuResenas.class);
                            i.putExtra("usuario",usuario);
                            startActivity(i);
                        }
                        if(item.getItemId()==R.id.action_restFav){
                            Intent i= new Intent(VerRestaurantes.this, menuRestaurantesFavoritos.class);
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
    public void cargar(int espacio) {
        DatabaseReference restaurantesRef = FirebaseDatabase.getInstance().getReference("Restaurantes");

        restaurantesRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                ArrayList<Restaurante> restaurantes = new ArrayList<>();
                for (DataSnapshot restauranteSnapshot : dataSnapshot.getChildren()) {
                    String id = restauranteSnapshot.child("id").getValue(String.class);
                    String nombre = restauranteSnapshot.child("nombre").getValue(String.class);
                    String ciudad = restauranteSnapshot.child("ciudad").getValue(String.class);
                    String tipo = restauranteSnapshot.child("tipo").getValue(String.class);
                    String dniUsuario = restauranteSnapshot.child("dniUsuario").getValue(String.class);
                    String imageUrl = restauranteSnapshot.child("imagen").getValue(String.class);
                    int valoracion= restauranteSnapshot.child("valoracion").getValue(Integer.class);
                    GenericTypeIndicator<ArrayList<Reserva>> genericTypeIndicator = new GenericTypeIndicator<ArrayList<Reserva>>() {};
                    ArrayList<Reserva> reservas = restauranteSnapshot.child("reservas").getValue(genericTypeIndicator);

                    GenericTypeIndicator<ArrayList<Reseña>> genericTypeIndicator2 = new GenericTypeIndicator<ArrayList<Reseña>>() {};
                    ArrayList<Reseña> reseñas = restauranteSnapshot.child("reseñas").getValue(genericTypeIndicator2);

                    int comensales = restauranteSnapshot.child("comensales").getValue(Integer.class);

                    Restaurante r = new Restaurante(id, nombre, tipo, ciudad, dniUsuario, imageUrl, comensales, reservas,reseñas,valoracion);
                    restaurantes.add(r);

                }
                RecyclerView recyclerView = findViewById(R.id.recyclermenuRestaurantesFavoritos);
                RestauranteAdapter adapter = new RestauranteAdapter(getApplicationContext(), restaurantes,usuario);
                recyclerView.addItemDecoration(new SpaceItemDecoration(espacio));
                adapter.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        int position = recyclerView.getChildAdapterPosition(view);
                        Restaurante restaurante = restaurantes.get(position);

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
                // Manejar errores de Firebase
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
        DatabaseReference mensajesRef = FirebaseDatabase.getInstance().getReference("Usuarios").child(usuario.getId()).child("mensajes");

        mensajesRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                GenericTypeIndicator<ArrayList<String>> genericTypeIndicator = new GenericTypeIndicator<ArrayList<String>>() {};
                ArrayList<String> mensajes = dataSnapshot.getValue(genericTypeIndicator);

                ImageView img = findViewById(R.id.noHayMensajes);
                ImageView img2 = findViewById(R.id.hayMensajes);

                if (mensajes == null || mensajes.isEmpty()) {
                    Toast.makeText(VerRestaurantes.this, "Lista de mensajes vacía", Toast.LENGTH_SHORT).show();
                    img.setVisibility(View.VISIBLE);
                    img2.setVisibility(View.INVISIBLE);
                } else {
                    img.setVisibility(View.INVISIBLE);
                    img2.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(VerRestaurantes.this, "Error al acceder a la base de datos", Toast.LENGTH_SHORT).show();
            }
        });
    }



    public void reseña() {
        DatabaseReference restaurantesRef = FirebaseDatabase.getInstance().getReference("Restaurantes");

        restaurantesRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                ArrayList<Reserva> reservas = new ArrayList<>();
                Date today = new Date();


                for (DataSnapshot restauranteSnapshot : dataSnapshot.getChildren()) {
                    // Obtener las reservas del restaurante del snapshot
                    GenericTypeIndicator<ArrayList<Reserva>> genericTypeIndicator = new GenericTypeIndicator<ArrayList<Reserva>>() {};
                    ArrayList<Reserva> reservasRestaurante = restauranteSnapshot.child("reservas").getValue(genericTypeIndicator);

                    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
                    SimpleDateFormat dateTimeFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault());

                    if (reservasRestaurante != null) {
                        for (Reserva r : reservasRestaurante) {
                            if (r.getNombreUsuario().equals(usuario.getNombreUsuario())) {
                                // Formatear la fecha de la reserva
                                String fecha = dateFormat.format(r.getDia());
                                // Concatenar la fecha y la hora
                                String fechaHoraString = fecha + " " + r.getHora();

                                // Convertir la cadena a un objeto Date
                                try {
                                    Date reservaDate = dateTimeFormat.parse(fechaHoraString);
                                    if (reservaDate != null && reservaDate.before(today)) {
                                        Toast.makeText(VerRestaurantes.this, "Fecha actual: " + today + " | Fecha de reserva: " + reservaDate, Toast.LENGTH_SHORT).show();
                                        reservas.add(r);
                                    }
                                } catch (ParseException e) {
                                    e.printStackTrace();
                                }
                            }
                        }
                    }
                }

                // Mostrar las reservas una por una
                mostrarReservas(reservas);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Manejar errores de Firebase
                Toast.makeText(VerRestaurantes.this, "Error al acceder a la base de datos.", Toast.LENGTH_SHORT).show();
            }
        });
    }


    private void mostrarReservas(List<Reserva> reservas) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        if (reservas.isEmpty()) {
            Toast.makeText(VerRestaurantes.this, "No hay reservas anteriores.", Toast.LENGTH_SHORT).show();
            return;
        }

        Reserva reservaActual = reservas.get(0);
        AlertDialog.Builder dialogo1 = new AlertDialog.Builder(VerRestaurantes.this);
        dialogo1.setTitle("Reservas Anteriores");


        dialogo1.setMessage("¿Quieres hacer una reseña sobre la reserva en el restaurante " + reservaActual.getRestaurante() + " en la fecha " + sdf.format(reservaActual.getDia()) +" a las "+reservaActual.getHora()+ "?");
        dialogo1.setCancelable(false);
        dialogo1.setPositiveButton("Sí", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialogo1, int id) {
                // Ir a la pantalla de reseña
                eliminarReserva(reservaActual);
                guardarHistorial(reservaActual);
                 Intent intent = new Intent(VerRestaurantes.this, ResenaActivity.class);
                intent.putExtra("usuario", usuario);
                intent.putExtra("Restauranteid", reservaActual.getIdRestaurante());
                startActivity(intent);

                // Eliminar la reserva actual de la lista
                reservas.remove(0);

                // Mostrar la siguiente reserva después de regresar de la actividad de reseña
                mostrarReservas(reservas);
            }
        });
        dialogo1.setNegativeButton("No", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialogo1, int id) {
                // Eliminar la reserva actual de la lista
                reservas.remove(0);
                eliminarReserva(reservaActual);
                guardarHistorial(reservaActual);

                // Mostrar la siguiente reserva
                mostrarReservas(reservas);
            }
        });
        dialogo1.show();
    }

    public void eliminarReserva(Reserva reserva) {
        DatabaseReference restauranteRef = FirebaseDatabase.getInstance().getReference("Restaurantes").child(reserva.getIdRestaurante()).child("reservas");

        restauranteRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                GenericTypeIndicator<ArrayList<Reserva>> genericTypeIndicator = new GenericTypeIndicator<ArrayList<Reserva>>() {};
                ArrayList<Reserva> reservasRestaurante = dataSnapshot.getValue(genericTypeIndicator);


                if (reservasRestaurante != null) {
                    ArrayList<Reserva> reservasActualizadas = new ArrayList<>();
                    for (Reserva r : reservasRestaurante) {
                        if (r.getId() != reserva.getId()) { // Solo agrega reservas diferentes a la que se elimina
                            reservasActualizadas.add(r);
                        }
                    }
                    DatabaseReference restauranteRef = FirebaseDatabase.getInstance().getReference("Restaurantes").child(reserva.getIdRestaurante());
                    restauranteRef.child("reservas").setValue(reservasActualizadas);
                    Toast.makeText(VerRestaurantes.this, "Reserva Eliminada", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(VerRestaurantes.this, "Lista de reservas vacia", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Manejar errores de Firebase
                Toast.makeText(VerRestaurantes.this, "Error al acceder a la base de datos.", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void guardarHistorial(Reserva reserva) {
        String idRestaurante = reserva.getIdRestaurante();
        if (idRestaurante == null || idRestaurante.isEmpty()) {
            Toast.makeText(VerRestaurantes.this, "ID de restaurante no válido", Toast.LENGTH_SHORT).show();
            return;
        }

        DatabaseReference restaurantesRef = FirebaseDatabase.getInstance().getReference("Restaurantes").child(idRestaurante);

        restaurantesRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                GenericTypeIndicator<ArrayList<Reserva>> genericTypeIndicator = new GenericTypeIndicator<ArrayList<Reserva>>() {};
                List<Reserva> reservas = snapshot.child("historialReservas").getValue(genericTypeIndicator);
                if (reservas == null) {
                    Toast.makeText(VerRestaurantes.this, "Reservas es null", Toast.LENGTH_SHORT).show();
                    reservas = new ArrayList<>();
                }
                reservas.add(reserva);
                Toast.makeText(VerRestaurantes.this, "Después: " + reservas.size(), Toast.LENGTH_SHORT).show();

                // Asegúrate de que la referencia del nodo es la correcta
                DatabaseReference restauranteRef = FirebaseDatabase.getInstance().getReference("Restaurantes").child(idRestaurante);
                restauranteRef.child("historialReservas").setValue(reservas).addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        Toast.makeText(VerRestaurantes.this, "Historial guardado correctamente", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(VerRestaurantes.this, "Error al guardar historial", Toast.LENGTH_SHORT).show();
                    }
                });
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(VerRestaurantes.this, "Error en la consulta: " + error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }





}