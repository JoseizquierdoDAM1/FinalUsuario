package com.example.finalusuario;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

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


        // Asignar datos a las vistas del ViewHolder
        holder.textViewName.setText(reseña.getNombreUsuario());
        holder.textViewDescription.setText(reseña.getTextoReseña());

        // Puedes cargar la imagen aquí usando Glide o Picasso si es necesario
        // Glide.with(applicationContext).load(restaurante.getImagenUrl()).into(holder.imageView);
    }

    @Override
    public int getItemCount() {
        return reseñas.size();
    }

    public class ReseñaViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;
        TextView textViewName, textViewDescription;

        public ReseñaViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.imageView);
            textViewName = itemView.findViewById(R.id.textViewName);
            textViewDescription = itemView.findViewById(R.id.textViewDescription);
        }
    }
}
