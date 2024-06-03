package com.example.finalusuario;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;

public class ReseñaAdapter extends RecyclerView.Adapter<ReseñaAdapter.ReseñaViewHolder> {
    private ArrayList<Reseña> reseñas;
    private Usuario usuario;
    private Context applicationContext;

    public ReseñaAdapter(Context applicationContext, ArrayList<Reseña> reseñas) {
        this.applicationContext = applicationContext;
        this.reseñas = reseñas;
    }

    @NonNull
    @Override
    public ReseñaAdapter.ReseñaViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_resenas, parent, false);
        return new ReseñaViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ReseñaAdapter.ReseñaViewHolder holder, int position) {
        Reseña reseña=reseñas.get(position);
        if(reseña.getValoracion()==1){
            holder.ellr1.setVisibility(View.VISIBLE);
        }
        if(reseña.getValoracion()==2){
            holder.ellr1.setVisibility(View.VISIBLE);
            holder.ellr2.setVisibility(View.VISIBLE);
        }
        if(reseña.getValoracion()==3){
            holder.ellr1.setVisibility(View.VISIBLE);
            holder.ellr2.setVisibility(View.VISIBLE);
            holder.ellr3.setVisibility(View.VISIBLE);
        }
        if(reseña.getValoracion()==4){
            holder.ellr1.setVisibility(View.VISIBLE);
            holder.ellr2.setVisibility(View.VISIBLE);
            holder.ellr3.setVisibility(View.VISIBLE);
            holder.ellr4.setVisibility(View.VISIBLE);
        }
        if(reseña.getValoracion()==5){
            holder.ellr1.setVisibility(View.VISIBLE);
            holder.ellr2.setVisibility(View.VISIBLE);
            holder.ellr3.setVisibility(View.VISIBLE);
            holder.ellr4.setVisibility(View.VISIBLE);
            holder.ellr5.setVisibility(View.VISIBLE);
        }

        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        String formattedDate = dateFormat.format(reseña.getFecha());

        // Asignar datos a las vistas del ViewHolder
        holder.textViewName.setText(reseña.getNombreUsuario());
        holder.textViewDescription.setText(reseña.getTextoReseña());
        holder.fecha.setText(formattedDate);

        // Puedes cargar la imagen aquí usando Glide o Picasso si es necesario

        DatabaseReference usuariosRef = FirebaseDatabase.getInstance().getReference("Usuarios").child(reseña.getIdUsuario());

        usuariosRef.addListenerForSingleValueEvent(new ValueEventListener() {
           @Override
           public void onDataChange(@NonNull DataSnapshot snapshot) {
               String urlImagen = snapshot.child("urlImagen").getValue(String.class);
               if(urlImagen!=null) {
                   Glide.with(applicationContext).load(urlImagen).into(holder.imageView);
               }
               else {
                   Glide.with(applicationContext).load(R.drawable.perfil).into(holder.imageView);
               }
           }

           @Override
           public void onCancelled(@NonNull DatabaseError error) {

           }
       }
        );

    }

    @Override
    public int getItemCount() {
        return reseñas.size();
    }

    public class ReseñaViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView,ellr1,ellr2,ell23,ellr3,ellr4,ellr5;
        TextView textViewName, textViewDescription,fecha;

        public ReseñaViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.imageView);
            textViewName = itemView.findViewById(R.id.textViewName);
            textViewDescription = itemView.findViewById(R.id.textViewDescription);
            fecha=itemView.findViewById(R.id.fecha);
            ellr1=itemView.findViewById(R.id.ellr1);
            ellr2=itemView.findViewById(R.id.ellr2);
            ellr3=itemView.findViewById(R.id.ellr3);
            ellr4=itemView.findViewById(R.id.ellr4);
            ellr5=itemView.findViewById(R.id.ellr5);
        }
    }
}
