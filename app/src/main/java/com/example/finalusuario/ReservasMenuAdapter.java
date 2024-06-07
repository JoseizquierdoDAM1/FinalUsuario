package com.example.finalusuario;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.GenericTypeIndicator;
import com.google.firebase.database.ValueEventListener;

import android.app.AlertDialog;
import android.content.DialogInterface;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;


public class ReservasMenuAdapter extends RecyclerView.Adapter<ReservasMenuAdapter.ReservasMenuAdapterViwewHolder> {

    Context activityContext;
    private List<Reserva> reservas;
    private String tiporecycler;

    // Constructor del adaptador
    public ReservasMenuAdapter(Context activityContext, List<Reserva> reservas,String tiporecycler) {
        this.activityContext = activityContext;
        this.reservas = reservas;
        this.tiporecycler=tiporecycler;
    }

    @NonNull
    @Override
    public ReservasMenuAdapterViwewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_menu_reservas, parent, false);
        return new ReservasMenuAdapterViwewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ReservasMenuAdapterViwewHolder holder, int position) {
        Reserva reserva = reservas.get(position);
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        String fechaFormateada = sdf.format(reserva.getDia());
        Toast.makeText(activityContext, fechaFormateada, Toast.LENGTH_SHORT).show();
        holder.labelComensalesReservaMenu.setText(String.valueOf(reserva.getComensales()));
        holder.labelFechaReservaMenu.setText(fechaFormateada);
        holder.labelHoraReservaMenu.setText(reserva.getHora());
        holder.labelNombreRestauranteMenuReservas.setText(reserva.getRestaurante());

        DatabaseReference restaurantesRef = FirebaseDatabase.getInstance().getReference("Restaurantes").child(reserva.getIdRestaurante());
        restaurantesRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String imageUrl = dataSnapshot.child("imagen").getValue(String.class);

                Glide.with(holder.itemView.getContext())
                        .load(imageUrl)
                        .into(holder.imageview5);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Manejar errores de Firebase
            }
        });

        if(tiporecycler.equals("reservas")) {
            holder.eliminar.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    AlertDialog.Builder dialogo1 = new AlertDialog.Builder(activityContext); // Cambiado a activityContext
                    dialogo1.setTitle("Alerta");
                    dialogo1.setMessage("¿ Quieres eliminar esta reserva ?");
                    dialogo1.setCancelable(false);
                    dialogo1.setPositiveButton("Confirmar", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialogo1, int id) {
                            guardarHistorial(reserva);
                            reservas.remove(reserva);
                            guardarReservas(reserva.getIdRestaurante(), reservas);
                            guardarMensaje(reserva);
                            notifyDataSetChanged(); // Actualiza el RecyclerView
                        }
                    });
                    dialogo1.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialogo1, int id) {
                            dialogo1.dismiss();
                        }
                    });
                    dialogo1.show();
                }
            });
        }else{
            holder.eliminar.setVisibility(View.INVISIBLE);
        }
    }

    @Override
    public int getItemCount() {
        return reservas.size();
    }

    public static class ReservasMenuAdapterViwewHolder extends RecyclerView.ViewHolder {
        TextView labelComensalesReservaMenu, labelFechaReservaMenu, labelHoraReservaMenu, labelNombreRestauranteMenuReservas;
        ImageView imageview5, eliminar;

        public ReservasMenuAdapterViwewHolder(@NonNull View itemView) {
            super(itemView);
            labelNombreRestauranteMenuReservas = itemView.findViewById(R.id.labelNombreRestauranteMenuReservas);
            labelComensalesReservaMenu = itemView.findViewById(R.id.labelComensalesReservaMenu);
            labelFechaReservaMenu = itemView.findViewById(R.id.labelFechaReservaMenu);
            labelHoraReservaMenu = itemView.findViewById(R.id.labelHoraReservaMenu);
            eliminar = itemView.findViewById(R.id.eliminar);
            imageview5 = itemView.findViewById(R.id.imageView5);
        }
    }

    public void guardarReservas(String idRestaurante, List<Reserva> reservas) {
        DatabaseReference restauranteRef = FirebaseDatabase.getInstance().getReference("Restaurantes").child(idRestaurante);
        restauranteRef.child("reservas").setValue(reservas);
    }
    public void guardarHistorial(Reserva reserva) {
        String idRestaurante = reserva.getIdRestaurante();
        if (idRestaurante == null || idRestaurante.isEmpty()) {
            Toast.makeText(activityContext, "ID de restaurante no válido", Toast.LENGTH_SHORT).show();
            return;
        }

        DatabaseReference restaurantesRef = FirebaseDatabase.getInstance().getReference("Restaurantes").child(idRestaurante);

        restaurantesRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                GenericTypeIndicator<ArrayList<Reserva>> genericTypeIndicator = new GenericTypeIndicator<ArrayList<Reserva>>() {};
                List<Reserva> reservas = snapshot.child("historialReservas").getValue(genericTypeIndicator);
                if (reservas == null) {
                    Toast.makeText(activityContext, "Reservas es null", Toast.LENGTH_SHORT).show();
                    reservas = new ArrayList<>();
                }
                reservas.add(reserva);
                Toast.makeText(activityContext, "Después: " + reservas.size(), Toast.LENGTH_SHORT).show();

                // Asegúrate de que la referencia del nodo es la correcta
                DatabaseReference restauranteRef = FirebaseDatabase.getInstance().getReference("Restaurantes").child(idRestaurante);
                restauranteRef.child("historialReservas").setValue(reservas).addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        Toast.makeText(activityContext, "Historial guardado correctamente", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(activityContext, "Error al guardar historial", Toast.LENGTH_SHORT).show();
                    }
                });
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(activityContext, "Error en la consulta: " + error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }


    public void guardarMensaje(Reserva r) {
        DatabaseReference usuariosRef = FirebaseDatabase.getInstance().getReference("Usuarios").child(r.getIdUsuario());

        usuariosRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                List<String> mensajes = new ArrayList<>();
                GenericTypeIndicator<ArrayList<String>> genericTypeIndicator = new GenericTypeIndicator<ArrayList<String>>() {};
                List<String> mensajesUser = dataSnapshot.child("mensajes").getValue(genericTypeIndicator);

                if (mensajesUser != null) {
                    mensajes.addAll(mensajesUser);
                }

                String mensaje = "Su reserva para el " + new SimpleDateFormat("dd/MM/yyyy").format(r.getDia()) + " a las " + r.getHora() + " ha sido cancelada";
                mensajes.add(mensaje);

                // Asegurarse de que el Toast se ejecute en el hilo principal
                new Handler(Looper.getMainLooper()).post(() -> Toast.makeText(activityContext, mensaje, Toast.LENGTH_SHORT).show());

                usuariosRef.child("mensajes").setValue(mensajes).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Log.d("Firebase", "Mensaje guardado correctamente");
                        } else {
                            Log.e("Firebase", "Error al guardar el mensaje", task.getException());
                        }
                    }
                });
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Manejar errores de Firebase
                new Handler(Looper.getMainLooper()).post(() -> Toast.makeText(activityContext, "Error al guardar el mensaje", Toast.LENGTH_SHORT).show());
            }
        });
    }
}
