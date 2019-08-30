package desahogate.proyectofinal.desahogate.Tabs.DIR_Notificaciones;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;

import desahogate.proyectofinal.desahogate.PerfilActivity;
import desahogate.proyectofinal.desahogate.R;

/**
 * Created by AlumnoT on 08/02/2018.
 */

public class Notificaciones extends Fragment{

    private View view;
    private RecyclerView rvNoti;

    // Firebase
    private FirebaseAuth mAuth;
    private String mCurrentUserId;

    private DatabaseReference mNotificationDatabase;
    List<Datos_Notificaciones> list = new ArrayList<>();
    private Adaptador_Notificaciones adapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_notificaciones, container, false);
//http://www.sgoliver.net/blog/notificaciones-push-android-firebase-cloud-messaging-1/
//        https://firebase.google.com/docs/cloud-messaging/android/first-message
        // https://firebase.google.com/docs/cloud-messaging/http-server-ref?hl=es-419
        // https://firebase.google.com/docs/cloud-messaging/android/topic-messaging?hl=es-419

        inicializarControles();


        mAuth = FirebaseAuth.getInstance();
        mCurrentUserId = mAuth.getCurrentUser().getUid();

        mNotificationDatabase = FirebaseDatabase.getInstance().getReference().child("notifications").child(mCurrentUserId);
        mNotificationDatabase.keepSynced(true);

        LinearLayoutManager llm = new LinearLayoutManager(getContext());
        llm.setReverseLayout(true);
        llm.setStackFromEnd(true);

        rvNoti.setHasFixedSize(true);
        rvNoti.setLayoutManager(llm);


        return view;
    }

    private void inicializarControles() {
        rvNoti = view.findViewById(R.id.rvNotifications);
    }

    @Override
    public void onStart() {
        super.onStart();

        FirebaseRecyclerAdapter<Datos_Notificaciones, Adaptador_Notificaciones> firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<Datos_Notificaciones, Adaptador_Notificaciones>(

                Datos_Notificaciones.class,
                R.layout.activity_plantilla_notifications,
                Adaptador_Notificaciones.class,
                mNotificationDatabase

        ) {

            @Override
            protected void populateViewHolder(Adaptador_Notificaciones usersViewHolder, Datos_Notificaciones users, int position) {

//                Toast.makeText(getActivity(), "Usuario encontrado: " +users.getName(), Toast.LENGTH_LONG).show();
                usersViewHolder.setDisplayName(users.getName());
                usersViewHolder.setUserImage(users.getImage(), getContext());

                final String user_id = users.getFrom();

                usersViewHolder.mView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        Intent profileIntent = new Intent(getActivity(), PerfilActivity.class);
                        profileIntent.putExtra("user_id", user_id);
                        startActivity(profileIntent);

                    }
                });

            }


        };

        rvNoti.setAdapter(firebaseRecyclerAdapter);

    }

}
