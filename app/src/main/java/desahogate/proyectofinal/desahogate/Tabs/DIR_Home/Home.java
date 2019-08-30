package desahogate.proyectofinal.desahogate.Tabs.DIR_Home;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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


public class Home extends Fragment {

    private FirebaseAuth mAuth;
    private String mCurrentUserId;


    private RecyclerView mRecyclerView;
    private SwipeRefreshLayout mRefreshLayout;

    private View view;
    private DatabaseReference mPubliDatabase;

    List<Datos> list = new ArrayList<>();
    private Adaptador_Home adapter;

    public Home() {
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_home, container, false);

        mRecyclerView = (RecyclerView) view.findViewById(R.id.rv);

        mRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.refresh);

        mAuth = FirebaseAuth.getInstance();
        mCurrentUserId = mAuth.getCurrentUser().getUid();

        mPubliDatabase = FirebaseDatabase.getInstance().getReference().child("publi");
        mPubliDatabase.keepSynced(true);

        LinearLayoutManager llm = new LinearLayoutManager(getContext());
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


        return view;
    }


    @Override
    public void onStart() {
        super.onStart();

        Query conversationQuery = mPubliDatabase.orderByChild("time");

        conversationQuery.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {

                list.clear();

                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {

                    Datos studentDetails = dataSnapshot.getValue(Datos.class);

                    list.add(studentDetails);
                }

                adapter = new Adaptador_Home(getContext(), list);

                mRecyclerView.setAdapter(adapter);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) { }
        });
    }



    private void loadMorePublication() {

        mRefreshLayout.setRefreshing(false);

        Query conversationQuery = mPubliDatabase.orderByChild("time");


        conversationQuery.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {

                list.clear();
                Datos studentDetails;
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {

                    studentDetails = dataSnapshot.getValue(Datos.class);

                    list.add(studentDetails);
                }

                adapter = new Adaptador_Home(getContext(), list);

                mRecyclerView.setAdapter(adapter);

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
    }



}
