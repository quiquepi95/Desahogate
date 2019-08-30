package desahogate.proyectofinal.desahogate.Tabs.DIR_Perfil.Tabs_Perfil.DIR_Publi;

import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import desahogate.proyectofinal.desahogate.R;
import desahogate.proyectofinal.desahogate.Tabs.DIR_Home.Adaptador_Home;
import desahogate.proyectofinal.desahogate.Tabs.DIR_Home.Datos;

public class MisPublicaciones extends AppCompatActivity {

    private DatabaseReference mConvDatabase, mUsersDatabase, mPubliDatabase;
    private FirebaseAuth mAuth;
    private String mCurrentUserId;

    private RecyclerView mRecyclerView;
    private SwipeRefreshLayout mRefreshLayout;

    List<Datos> list = new ArrayList<>();
    private Adaptador_Home adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mispublicaciones);

        mRecyclerView = (RecyclerView) findViewById(R.id.rvMisPubli);

        mRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.refreshMisPubli);


        mAuth = FirebaseAuth.getInstance();
        mCurrentUserId = mAuth.getCurrentUser().getUid();

        mPubliDatabase = FirebaseDatabase.getInstance().getReference().child("publi");
        mPubliDatabase.keepSynced(true);

        LinearLayoutManager llm = new LinearLayoutManager(this);
        llm.setReverseLayout(true);
        llm.setStackFromEnd(true);

        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(llm);

        mRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                loadMorePublication();
            }
        });

    }

    @Override
    public void onStart() {
        super.onStart();

        Query conversationQuery = mPubliDatabase.orderByChild("UID").equalTo(mCurrentUserId);

        conversationQuery.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {

                list.clear();

                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {

                    Datos studentDetails = dataSnapshot.getValue(Datos.class);

                    list.add(studentDetails);
                }

                adapter = new Adaptador_Home(MisPublicaciones.this, list );
                mRecyclerView.setAdapter(adapter);

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {


            }
        });
    }



    private void loadMorePublication() {

        mRefreshLayout.setRefreshing(false);

        Query conversationQuery = mPubliDatabase.orderByChild("UID").equalTo(mCurrentUserId);

        conversationQuery.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {

                list.clear();

                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {

                    Datos studentDetails = dataSnapshot.getValue(Datos.class);

                    list.add(studentDetails);
                }

                adapter = new Adaptador_Home(MisPublicaciones.this, list);

                mRecyclerView.setAdapter(adapter);

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {


            }
        });
    }
}
