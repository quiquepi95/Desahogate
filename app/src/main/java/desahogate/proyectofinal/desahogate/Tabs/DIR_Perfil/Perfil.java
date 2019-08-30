package desahogate.proyectofinal.desahogate.Tabs.DIR_Perfil;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import de.hdodenhof.circleimageview.CircleImageView;
import desahogate.proyectofinal.desahogate.Tabs.DIR_Perfil.Tabs_Perfil.DIR_Publi.MisPublicaciones;
import desahogate.proyectofinal.desahogate.Tabs.DIR_Perfil.Tabs_Perfil.Friends.FriendsActivity;
import desahogate.proyectofinal.desahogate.R;
import desahogate.proyectofinal.desahogate.Tabs.DIR_Perfil.Tabs_Perfil.DIR_Editar.StatusActivity;

public class Perfil extends Fragment {

    private DatabaseReference mUserDatabase;
    private FirebaseUser mCurrentUser;

    private Fragment fragment = null;
    private CircleImageView mDisplayImage;
    private TextView mName;
    private TextView mStatus;
    private TextView mBiography;

    private Button btnPubli;
    private Button btnAmigos;
    private Button btnEditar;

    private StorageReference mImageStorage;

    private View view;

    private ViewPager mViewPager;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_perfil, container, false);

        inicializarControles();

        mImageStorage = FirebaseStorage.getInstance().getReference();

        mCurrentUser = FirebaseAuth.getInstance().getCurrentUser();

        String current_uid = mCurrentUser.getUid();

        mUserDatabase = FirebaseDatabase.getInstance().getReference().child("Users").child(current_uid);
        mUserDatabase.keepSynced(true);

        mUserDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                String name = dataSnapshot.child("name").getValue().toString();
                final String image = dataSnapshot.child("image").getValue().toString();
                String status = dataSnapshot.child("status").getValue().toString();
                String biography = dataSnapshot.child("biography").getValue().toString();

                mName.setText(name);
                mStatus.setText(status);
                mBiography.setText(biography);

                if (image.equals("default")) {
                        // Default o que cargue la url
                    Glide.with(getActivity()).load(R.drawable.default_avatar).placeholder(R.drawable.default_avatar).into(mDisplayImage);

                } else {
                    Glide.with(getActivity()).load(image).into(mDisplayImage);
                }


            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


        btnPubli.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent publi_intent = new Intent(getActivity(), MisPublicaciones.class);
                startActivity(publi_intent);

            }
        });

        btnAmigos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent friends_intent = new Intent(getActivity(), FriendsActivity.class);
                startActivity(friends_intent);

            }
        });


        btnEditar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent status_intent = new Intent(getActivity(), StatusActivity.class);
                startActivity(status_intent);

            }
        });




        return view;
    }

    private void inicializarControles() {
        mDisplayImage = (CircleImageView) view.findViewById(R.id.imagePerfil);
        mName = (TextView) view.findViewById(R.id.textViewName);
        mStatus = (TextView) view.findViewById(R.id.textViewUser);
        mBiography = (TextView) view.findViewById(R.id.textViewBio);

        btnPubli = (Button) view.findViewById(R.id.btnPubli);
        btnAmigos = (Button) view.findViewById(R.id.btnAmigos);
        btnEditar = (Button) view.findViewById(R.id.btnEditar);
    }


}
