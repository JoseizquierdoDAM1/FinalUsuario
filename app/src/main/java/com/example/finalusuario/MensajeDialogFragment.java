package com.example.finalusuario;

import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.GenericTypeIndicator;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class MensajeDialogFragment extends DialogFragment {

    private Usuario user;
    private RecyclerView messageRecyclerView;
    private MessageAdapter messageAdapter;
    private List<String> messages = new ArrayList<>();

    // Constructor estático para crear una instancia del DialogFragment con un usuario
    public static MensajeDialogFragment newInstance(Usuario user) {
        MensajeDialogFragment fragment = new MensajeDialogFragment();
        Bundle args = new Bundle();
        args.putSerializable("usuario", user);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            user = (Usuario) getArguments().getSerializable("usuario");
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_dialog, container, false);

        messageRecyclerView = view.findViewById(R.id.recyclerfragment);
        Button closeButton = view.findViewById(R.id.closeButton);

        messageRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        messageAdapter = new MessageAdapter(messages);
        messageRecyclerView.setAdapter(messageAdapter);

        closeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                eliminarMensajes();
                dismiss();
            }
        });

        loadMessages();

        return view;
    }

    private void loadMessages() {
        if (user != null) {
            DatabaseReference mensajesRef = FirebaseDatabase.getInstance().getReference("Usuarios").child(user.getNombreUsuario()).child("mensajes");

            mensajesRef.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    GenericTypeIndicator<ArrayList<String>> t = new GenericTypeIndicator<ArrayList<String>>() {};
                    List<String> mensajes = dataSnapshot.getValue(t);
                    if (mensajes != null) {
                        messages.addAll(mensajes);
                        messageAdapter.notifyDataSetChanged();
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    // Manejar errores
                }
            });
        }
    }

    public void eliminarMensajes(){
        DatabaseReference mensajesRef = FirebaseDatabase.getInstance().getReference("Usuarios").child(user.getNombreUsuario()).child("mensajes");

        mensajesRef.removeValue();

    }

}


