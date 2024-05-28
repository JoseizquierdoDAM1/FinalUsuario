package com.example.finalusuario;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.provider.CalendarContract;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.CalendarView;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

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

import android.content.Intent;
import android.provider.CalendarContract;
import android.provider.CalendarContract.Events;
import java.util.Calendar;
import java.util.Date;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import android.Manifest;


public class DetalleRestaurante extends AppCompatActivity {
    private Restaurante restaurante;
    long hoy = System.currentTimeMillis();
    Date fechaSeleccionada;
    Spinner spinnerTurno;
    Spinner spinnerHora;
    Usuario usuario;
    EditText comensales=null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detalle_restaurante);

        comensales=findViewById(R.id.editTextComensales);

        Intent intent = getIntent();
        restaurante = (Restaurante) intent.getSerializableExtra("restaurante");


        Intent intentusuario = getIntent();
        usuario = (Usuario) intentusuario.getSerializableExtra("usuario");

        CalendarView myCalendarView = (CalendarView) findViewById(R.id.calendarView);
        myCalendarView.setMinDate(hoy);

        Date fecha = new Date(hoy);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String fechaFormateada = sdf.format(fecha);
        try {
            fechaSeleccionada = sdf.parse(fechaFormateada);
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }

        myCalendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(CalendarView view, int year, int month, int dayOfMonth) {
                // Mostrar la fecha completa
                String fechaCompleta = String.format("%d-%02d-%02d", year, month + 1, dayOfMonth);
                Toast.makeText(getApplicationContext(), fechaCompleta, Toast.LENGTH_SHORT).show();
                try {
                    fechaSeleccionada = sdf.parse(fechaCompleta);
                } catch (ParseException e) {
                    throw new RuntimeException(e);
                }
            }
        });



        rellenarSpinners();



    }
    public void rellenarSpinners(){

        DatabaseReference restauranteRef = FirebaseDatabase.getInstance().getReference("Restaurantes").child(restaurante.getId());

        restauranteRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot restauranteSnapshot) {
                Restaurante r = new Restaurante();
                // Obtener los datos del restaurante del snapshot
                GenericTypeIndicator<ArrayList<String>> genericTypeIndicator = new GenericTypeIndicator<ArrayList<String>>() {};
                ArrayList<String> turnos= restauranteSnapshot.child("turnos").getValue(genericTypeIndicator);

                spinnerTurno = (Spinner) findViewById(R.id.spinnerTurno);
                ArrayAdapter<String> adapter = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_spinner_item, turnos);
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spinnerTurno.setAdapter(adapter);



            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    public void buscar(View view) {
        DatabaseReference restauranteRef = FirebaseDatabase.getInstance().getReference("Restaurantes").child(restaurante.getId());

        restauranteRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot restauranteSnapshot) {
                Restaurante r = new Restaurante();
                // Obtener los datos del restaurante del snapshot
                GenericTypeIndicator<ArrayList<String>> genericTypeIndicator = new GenericTypeIndicator<ArrayList<String>>() {};
                ArrayList<String> horastdesayuno = restauranteSnapshot.child("horastdesayuno").getValue(genericTypeIndicator);
                ArrayList<String> horastcomida = restauranteSnapshot.child("horastcomida").getValue(genericTypeIndicator);
                ArrayList<String> horastcena= restauranteSnapshot.child("horastcena").getValue(genericTypeIndicator);


                spinnerHora = (Spinner) findViewById(R.id.spinnerHora);
                ArrayAdapter<String> adapter=null;
                if(spinnerTurno.getSelectedItem().toString().equals("Desayuno")){
                   adapter= new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_spinner_item, horastdesayuno);
                }
                if(spinnerTurno.getSelectedItem().toString().equals("Comida")){
                    adapter= new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_spinner_item, horastcomida);
                }
                if(spinnerTurno.getSelectedItem().toString().equals("Cena")){
                    adapter= new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_spinner_item, horastcena);
                }


                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spinnerHora.setAdapter(adapter);



            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }

    public void Reservar(View view) {
        DatabaseReference restauranteRef = FirebaseDatabase.getInstance().getReference("Restaurantes").child(restaurante.getId());

        restauranteRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot restauranteSnapshot) {
                // Obtener los datos del restaurante del snapshot
                GenericTypeIndicator<ArrayList<Reserva>> genericTypeIndicator = new GenericTypeIndicator<ArrayList<Reserva>>() {};
                ArrayList<Reserva> reservas = restauranteSnapshot.child("reservas").getValue(genericTypeIndicator);

                if(reservas != null){
                    String horaseleccionada = (String) spinnerHora.getSelectedItem();
                    int comensalesTotales=0;
                    for(Reserva reserva:reservas){
                        if(reserva.getDia().equals(fechaSeleccionada)&&reserva.getHora().equals(horaseleccionada)){
                            comensalesTotales=comensalesTotales+Integer.valueOf(reserva.getComensales());
                        }
                    }
                    int comensalesReserva= Integer.parseInt(comensales.getText().toString());
                    if((comensalesReserva+comensalesTotales)<=restaurante.getComensales()) {
                        hacerReserva();
                    }else{
                        Toast.makeText(DetalleRestaurante.this, "Lo sentimos pero no hay mesas disponibles en el turno seleccionado", Toast.LENGTH_SHORT).show();
                    }
                }else{
                    if(Integer.valueOf(comensales.getText().toString())>restaurante.getComensales()){
                        Toast.makeText(DetalleRestaurante.this, "Lo sentimos pero no hay mesas disponibles en el turno seleccionado", Toast.LENGTH_SHORT).show();
                    }else{
                        hacerReserva();
                    }
                }


            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });



    }
    public void hacerReserva() {
        String turnoseleccionado = (String) spinnerTurno.getSelectedItem();
        String horaseleccionada = (String) spinnerHora.getSelectedItem();

        Toast.makeText(getApplicationContext(), fechaSeleccionada + "  " + horaseleccionada, Toast.LENGTH_SHORT).show();
        int id=0;


        if (restaurante.getReservas() == null) {
            restaurante.setReservas(new ArrayList<>());
            id=1;
        }else {
            id=restaurante.getReservas().size()+1;
        }
        Reserva r = new Reserva(id,usuario.getId(),usuario.getNombreUsuario(), restaurante.getId(),restaurante.getNombre(), fechaSeleccionada, turnoseleccionado, horaseleccionada, Integer.valueOf(comensales.getText().toString()));
        // Agregar la nueva reserva a la lista
        restaurante.getReservas().add(r);

        // Guardar en Firebase
        DatabaseReference restauranteRef = FirebaseDatabase.getInstance().getReference("Restaurantes").child(restaurante.getId());
        restauranteRef.child("reservas").setValue(restaurante.getReservas())
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        // Operación de escritura exitosa, redirigir a la actividad de ver restaurantes
                        guardarMensaje(r);

                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        // Error al escribir en Firebase, mostrar mensaje de error o realizar alguna acción de manejo de errores
                        Toast.makeText(DetalleRestaurante.this, "Error al realizar la reserva", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    public void guardarMensaje(Reserva r) {
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

                String mensaje = "Su reserva para el " + new SimpleDateFormat("dd/MM/yyyy").format(r.getDia()) + " a las " + r.getHora() + " ha sido realizada con exito";
                mensajes.add(mensaje);

                // Asegurarse de que el Toast se ejecute en el hilo principal
                new Handler(Looper.getMainLooper()).post(() -> Toast.makeText(DetalleRestaurante.this, mensaje, Toast.LENGTH_SHORT).show());

                usuariosRef.child("mensajes").setValue(mensajes).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Log.d("Firebase", "Mensaje guardado correctamente");
                            Intent i = new Intent(DetalleRestaurante.this, VerRestaurantes.class);
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
                new Handler(Looper.getMainLooper()).post(() -> Toast.makeText(DetalleRestaurante.this, "Error al guardar el mensaje", Toast.LENGTH_SHORT).show());
            }
        });


    }








}