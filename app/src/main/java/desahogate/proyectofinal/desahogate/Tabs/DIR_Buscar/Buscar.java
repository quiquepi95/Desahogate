package desahogate.proyectofinal.desahogate.Tabs.DIR_Buscar;

import android.content.Intent;
import android.database.SQLException;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;


import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import desahogate.proyectofinal.desahogate.PerfilActivity;
import desahogate.proyectofinal.desahogate.R;


public class Buscar extends Fragment {
    private static final String TAG = "TAG";
    private View viewBuscar;
    private RecyclerView mUsersList;
    private DatabaseReference mUsersDatabase;
    private EditText searchView;
    private Button btnBuscar;
    private FirebaseAuth mAuth;
    private String mCurrentUserId;
    private SwipeRefreshLayout mRefreshLayout;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        viewBuscar = inflater.inflate(R.layout.fragment_buscar, container, false);

        inicializar();

        // RECYCLER VIEW
        mUsersList.setHasFixedSize(true);

        LinearLayoutManager llm = new LinearLayoutManager(this.getContext());
        mUsersList.setLayoutManager(llm);


        mUsersDatabase.keepSynced(true);


        //Me carga de nuevo el contenido
        mRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                loadMorePublication();
            }
        });


        // LLAMADA DEL BOTÓN BUSCAR
        btnBuscar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String nombre = searchView.getText().toString();
                final Query busqueda = mUsersDatabase.orderByChild("name").equalTo(nombre.trim());


                //Mediante esto compruebo si existe el usuario
                busqueda.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        if (dataSnapshot.exists()) {

                            FirebaseRecyclerAdapter<Datos_Buscar, Adaptador_Buscar> firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<Datos_Buscar, Adaptador_Buscar>(

                                    Datos_Buscar.class,
                                    R.layout.activity_plantilla_buscar,
                                    Adaptador_Buscar.class,
                                    busqueda

                            ) {

                                @Override
                                protected void populateViewHolder(Adaptador_Buscar usersViewHolder, final Datos_Buscar users, int position) {

                                    Toast.makeText(getActivity(), "Usuario encontrado: " + users.getName(), Toast.LENGTH_LONG).show();
                                    usersViewHolder.setDisplayName(users.getName());
                                    usersViewHolder.setUserStatus(users.getStatus());
                                    usersViewHolder.setUserImage(users.getImage(), getContext());

                                    final String user_id = getRef(position).getKey();

                                    usersViewHolder.mView.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View view) {

                                            Intent profileIntent = new Intent(getActivity(), PerfilActivity.class);
                                            profileIntent.putExtra("user_id", user_id);
                                            startActivity(profileIntent);

                                        }
                                    });

                                }

                                @Override
                                protected void onCancelled(DatabaseError error) {
                                    super.onCancelled(error);
                                    Toast.makeText(getActivity(), "Ha ocurrido un error.", Toast.LENGTH_LONG).show();
                                }
                            };

                            mUsersList.setAdapter(firebaseRecyclerAdapter);

                        }else {
                            Toast.makeText(getActivity(), "Usuario no encontrado", Toast.LENGTH_LONG).show();
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });

            }

        });

        return viewBuscar;
    }


    private void inicializar() {
        mUsersList = (RecyclerView) viewBuscar.findViewById(R.id.rvBuscar);
        searchView = (EditText) viewBuscar.findViewById(R.id.multiAutoCompleteTextView);
        btnBuscar = (Button) viewBuscar.findViewById(R.id.btnBuscar);
        mRefreshLayout = (SwipeRefreshLayout) viewBuscar.findViewById(R.id.refresh);


        // FIREBASE
        mAuth = FirebaseAuth.getInstance();
        mCurrentUserId = mAuth.getCurrentUser().getUid();
        mUsersDatabase = FirebaseDatabase.getInstance().getReference().child("Users");
    }


    @Override
    public void onStart() {
        super.onStart();

        FirebaseRecyclerAdapter<Datos_Buscar, Adaptador_Buscar> firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<Datos_Buscar, Adaptador_Buscar>(

                Datos_Buscar.class,
                R.layout.activity_plantilla_buscar,
                Adaptador_Buscar.class,
                mUsersDatabase

        ) {

            @Override
            protected void populateViewHolder(Adaptador_Buscar usersViewHolder, Datos_Buscar users, int position) {

                usersViewHolder.setDisplayName(users.getName());
                usersViewHolder.setUserStatus(users.getStatus());
                usersViewHolder.setUserImage(users.getImage(), getContext());

                final String user_id = getRef(position).getKey();

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

        mUsersList.setAdapter(firebaseRecyclerAdapter);
    }



    private void loadMorePublication() {

        mRefreshLayout.setRefreshing(false);

        FirebaseRecyclerAdapter<Datos_Buscar, Adaptador_Buscar> firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<Datos_Buscar, Adaptador_Buscar>(

                Datos_Buscar.class,
                R.layout.activity_plantilla_buscar,
                Adaptador_Buscar.class,
                mUsersDatabase

        ) {

            @Override
            protected void populateViewHolder(Adaptador_Buscar usersViewHolder, Datos_Buscar users, int position) {

                usersViewHolder.setDisplayName(users.getName());
                usersViewHolder.setUserStatus(users.getStatus());
                usersViewHolder.setUserImage(users.getImage(), getContext());

                final String user_id = getRef(position).getKey();

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

        mUsersList.setAdapter(firebaseRecyclerAdapter);    }
}
