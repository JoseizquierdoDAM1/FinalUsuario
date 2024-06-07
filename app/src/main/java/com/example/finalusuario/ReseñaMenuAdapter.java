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

public class ReseñaMenuAdapter extends RecyclerView.Adapter<ReseñaMenuAdapter.ReseñaViewHolder> {
    private ArrayList<Reseña> reseñas;
    private Usuario usuario;
    private Context applicationContext;

    public ReseñaMenuAdapter(Context applicationContext, ArrayList<Reseña> reseñas) {
        this.applicationContext = applicationContext;
        this.reseñas = reseñas;
    }

    @NonNull
    @Override
    public ReseñaMenuAdapter.ReseñaViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_menu_resenas, parent, false);
        return new ReseñaViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ReseñaMenuAdapter.ReseñaViewHolder holder, int position) {
        Reseña reseña=reseñas.get(position);
        if(reseña.getValoracion()==1){
            holder.ellmr1.setVisibility(View.VISIBLE);
        }
        if(reseña.getValoracion()==2){
            holder.ellmr1.setVisibility(View.VISIBLE);
            holder.ellmr2.setVisibility(View.VISIBLE);
        }
        if(reseña.getValoracion()==3){
            holder.ellmr1.setVisibility(View.VISIBLE);
            holder.ellmr2.setVisibility(View.VISIBLE);
            holder.ellmr3.setVisibility(View.VISIBLE);
        }
        if(reseña.getValoracion()==4){
            holder.ellmr1.setVisibility(View.VISIBLE);
            holder.ellmr2.setVisibility(View.VISIBLE);
            holder.ellmr3.setVisibility(View.VISIBLE);
            holder.ellmr4.setVisibility(View.VISIBLE);
        }
        if(reseña.getValoracion()==5){
            holder.ellmr1.setVisibility(View.VISIBLE);
            holder.ellmr2.setVisibility(View.VISIBLE);
            holder.ellmr3.setVisibility(View.VISIBLE);
            holder.ellmr4.setVisibility(View.VISIBLE);
            holder.ellmr5.setVisibility(View.VISIBLE);
        }

        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        String formattedDate = dateFormat.format(reseña.getFecha());

        // Asignar datos a las vistas del ViewHolder

        holder.descripcionmenureseñas.setText(reseña.getTextoReseña());
        holder.fechaMenuReseñas.setText(formattedDate);

        // Puedes cargar la imagen aquí usando Glide o Picasso si es necesario

        DatabaseReference usuariosRef = FirebaseDatabase.getInstance().getReference("Restaurantes").child(reseña.getIdRestaurante());

        usuariosRef.addListenerForSingleValueEvent(new ValueEventListener() {
           @Override
           public void onDataChange(@NonNull DataSnapshot snapshot) {
               String imagen = snapshot.child("imagen").getValue(String.class);
               String nombre= snapshot.child("nombre").getValue(String.class);

               Glide.with(applicationContext).load(imagen).into(holder.imageViewmenuReseñas);

               holder.textViewNameMenuReseñas.setText(nombre);
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
        ImageView imageViewmenuReseñas,ellmr1,ellmr2,ellmr3,ellmr4,ellmr5;
        TextView textViewNameMenuReseñas, descripcionmenureseñas,fechaMenuReseñas;

        public ReseñaViewHolder(@NonNull View itemView) {
            super(itemView);
            imageViewmenuReseñas = itemView.findViewById(R.id.imageViewmenuReseñas);
            textViewNameMenuReseñas = itemView.findViewById(R.id.textViewNameMenuReseñas);
            descripcionmenureseñas = itemView.findViewById(R.id.descripcionmenureseñas);
            fechaMenuReseñas=itemView.findViewById(R.id.fechaMenuReseñas);
            ellmr1=itemView.findViewById(R.id.ellmr1);
            ellmr2=itemView.findViewById(R.id.ellmr2);
            ellmr3=itemView.findViewById(R.id.ellmr3);
            ellmr4=itemView.findViewById(R.id.ellmr4);
            ellmr5=itemView.findViewById(R.id.ellmr5);
        }
    }
}
