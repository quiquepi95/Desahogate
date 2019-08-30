package desahogate.proyectofinal.desahogate.Tabs;

import android.Manifest;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.location.LocationProvider;
import android.net.Uri;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ServerValue;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import desahogate.proyectofinal.desahogate.Chats.ChatActivity;
import desahogate.proyectofinal.desahogate.LoginActivity;
import desahogate.proyectofinal.desahogate.R;
import desahogate.proyectofinal.desahogate.Tabs.DIR_Buscar.Buscar;
import desahogate.proyectofinal.desahogate.Tabs.DIR_Home.Home;
import desahogate.proyectofinal.desahogate.Tabs.DIR_Mensajes.Mensajes;
import desahogate.proyectofinal.desahogate.Tabs.DIR_Notificaciones.Notificaciones;
import desahogate.proyectofinal.desahogate.Tabs.DIR_Perfil.Perfil;
import desahogate.proyectofinal.desahogate.Tabs.DIR_Perfil.Tabs_Perfil.Friends.FriendsActivity;
import id.zelory.compressor.Compressor;


public class TabDesahogateActivity extends AppCompatActivity {

    private static final int GALLERY_PICK = 1;
    static Uri imageUri;
    private boolean imagen = false;

    private FirebaseAuth mAuth;
    private DatabaseReference mUserRef;

    private SectionsPagerAdapter mSectionsPagerAdapter;

    private ViewPager mViewPager;
    Toolbar toolbar;
    TabLayout tabLayout;

    private String ubicacionUser = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tab_desahogate);

        inicializar();

        setSupportActionBar(toolbar);
        mViewPager.setAdapter(mSectionsPagerAdapter);

        mViewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        tabLayout.addOnTabSelectedListener(new TabLayout.ViewPagerOnTabSelectedListener(mViewPager));


        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                displayInputDialog();
            }
        });

        if (mAuth.getCurrentUser() != null) {
            mUserRef = FirebaseDatabase.getInstance().getReference().child("Users").child(mAuth.getCurrentUser().getUid());
        }

    }

    private void inicializar() {
        mAuth = FirebaseAuth.getInstance();
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());
        mViewPager = (ViewPager) findViewById(R.id.container);
        tabLayout = (TabLayout) findViewById(R.id.tabs);


    }


    @Override
    public void onStart() {
        super.onStart();
        FirebaseUser currentUser = mAuth.getCurrentUser();

        if (currentUser == null) {

            sendToStart();

        }
    }


    private void sendToStart() {
        Intent startIntent = new Intent(TabDesahogateActivity.this, LoginActivity.class);
        startActivity(startIntent);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_tab_desahogate, menu);
        return true;
    }

    /**
     * @param item
     * @return
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        super.onOptionsItemSelected(item);

        if (item.getItemId() == R.id.main_logout_btn) {

            FirebaseAuth.getInstance().signOut();

            sendToStart();

        }

        int id = item.getItemId();

        if (id == R.id.action_settings) {
            return true;
        }

        return true;
    }

    /**
     * @param view
     */
    public void abrirCHats(View view) {
        Intent intent = new Intent(this, ChatActivity.class);
        startActivity(intent);
    }

    public void openFriends(View view) {
        Intent intent = new Intent(this, FriendsActivity.class);
        startActivity(intent);
    }


    /**
     * A placeholder fragment containing a simple view.
     */
    public static class PlaceholderFragment extends Fragment {
        /**
         * The fragment argument representing the section number for this
         * fragment.
         */
        private static final String ARG_SECTION_NUMBER = "section_number";

        public PlaceholderFragment() {
        }

        /**
         * Returns a new instance of this fragment for the given section
         * number.
         */
        public static PlaceholderFragment newInstance(int sectionNumber) {
            PlaceholderFragment fragment = new PlaceholderFragment();
            Bundle args = new Bundle();
            args.putInt(ARG_SECTION_NUMBER, sectionNumber);
            fragment.setArguments(args);
            return fragment;
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_tab_desahogate, container, false);
            TextView textView = (TextView) rootView.findViewById(R.id.section_label);
            textView.setText(getString(R.string.section_format, getArguments().getInt(ARG_SECTION_NUMBER)));
            return rootView;
        }
    }


    /**
     * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
     * one of the sections/tabs/pages.
     */
    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {

            switch (position) {

                case 0:
                    Home home = new Home();
                    return home;
                case 1:
                    Buscar buscar = new Buscar();
                    return buscar;
                case 2:
                    Perfil perfil = new Perfil();
                    return perfil;
                case 3:
                    Notificaciones notificaciones = new Notificaciones();
                    return notificaciones;
                case 4:
                    Mensajes mensaje = new Mensajes();
                    return mensaje;

            }
            return PlaceholderFragment.newInstance(position + 1);
        }

        /**
         * @return
         */
        @Override
        public int getCount() {
            // Show 5 total pages.
            return 5;
        }


    }


    private DatabaseReference mRootRef;

    private String mCurrentUserId;
    private EditText mensaje;

    private FirebaseUser mCurrentUser;
    private DatabaseReference mUserDatabase;
    private String namePubli;
    private StorageReference mImageStorage;
    Dialog d = null;

    private void displayInputDialog() {
        d = new Dialog(this);
        d.setContentView(R.layout.activity_publicacion);
        d.create();
        d.show();


        mRootRef = FirebaseDatabase.getInstance().getReference();
        mCurrentUserId = mAuth.getCurrentUser().getUid();

        mensaje = d.findViewById(R.id.editTextMensaje);
        mCurrentUser = FirebaseAuth.getInstance().getCurrentUser();
        mImageStorage = FirebaseStorage.getInstance().getReference();

        Button publicarBoton = d.findViewById(R.id.buttonPublicar);
        Button imagenPubli = d.findViewById(R.id.buttonImagenPublicar);
        Button cancelarPubli = d.findViewById(R.id.buttonCancelar);

        final Switch simpleSwitch = d.findViewById(R.id.switchUbicacion);

        final String[] localizacion = {""};

        simpleSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    locationStart();
                    localizacion[0] = ubicacionUser;
                } else {
                    localizacion[0] = "";
                }
            }
        });

        cancelarPubli.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                d.dismiss();
                mensaje.setText("");
                imagen = false;
                imageUri = null;
            }
        });

        imagenPubli.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent galleryIntent = new Intent();
                galleryIntent.setType("image/*");
                galleryIntent.setAction(Intent.ACTION_GET_CONTENT);

                startActivityForResult(Intent.createChooser(galleryIntent, "SELECT IMAGE"), GALLERY_PICK);
            }
        });


        publicarBoton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                d.dismiss();
                final String message = mensaje.getText().toString();

                if (!TextUtils.isEmpty(message) && imagen) {
                    imagen = false;

                    final String current_user_ref = "publi";

                    DatabaseReference user_message_push = mRootRef.child("publi").push();

                    final String push_id = user_message_push.getKey();

                    String current_uid = mCurrentUser.getUid();

                    mUserDatabase = FirebaseDatabase.getInstance().getReference().child("Users").child(current_uid);
                    mUserDatabase.keepSynced(true);

                    mUserDatabase.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {

                            namePubli = dataSnapshot.child("name").getValue().toString();

                            StorageReference filepath = mImageStorage.child("publi_images").child(push_id + ".jpg");

                            filepath.putFile(imageUri).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                                @Override
                                public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {

                                    if (task.isSuccessful()) {

                                        String download_url = task.getResult().getDownloadUrl().toString();

                                        Map messageMap = new HashMap();
                                        messageMap.put("name", namePubli + localizacion[0]);
                                        messageMap.put("image", download_url);
                                        messageMap.put("message", message);
                                        messageMap.put("time", ServerValue.TIMESTAMP);
                                        messageMap.put("KeyID", push_id);
                                        messageMap.put("UID", mCurrentUserId);

                                        Map messageUserMap = new HashMap();
                                        messageUserMap.put(current_user_ref + "/" + push_id, messageMap);

                                        mensaje.setText("");

                                        mRootRef.updateChildren(messageUserMap, new DatabaseReference.CompletionListener() {
                                            @Override
                                            public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
                                            }
                                        });
                                    }

                                }
                            });

                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });


                } else if (!TextUtils.isEmpty(message)) {


                    final String current_user_ref = "publi";

                    DatabaseReference user_message_push = mRootRef.child("publi").push();

                    final String push_id = user_message_push.getKey();


                    String current_uid = mCurrentUser.getUid();


                    mUserDatabase = FirebaseDatabase.getInstance().getReference().child("Users").child(current_uid);
                    mUserDatabase.keepSynced(true);

                    mUserDatabase.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {

                            namePubli = dataSnapshot.child("name").getValue().toString();

                            imagen = false;

                            Map messageMap = new HashMap();
                            messageMap.put("name", namePubli + localizacion[0]);
                            messageMap.put("image", "default");
                            messageMap.put("message", message);
                            messageMap.put("time", ServerValue.TIMESTAMP);
                            messageMap.put("KeyID", push_id);
                            messageMap.put("UID", mCurrentUserId);

                            Map messageUserMap = new HashMap();
                            messageUserMap.put(current_user_ref + "/" + push_id, messageMap);

                            mensaje.setText("");

                            mRootRef.updateChildren(messageUserMap, new DatabaseReference.CompletionListener() {
                                @Override
                                public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {


                                }
                            });

                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });

                } else if (imagen) {

                    final String current_user_ref = "publi";

                    DatabaseReference user_message_push = mRootRef.child("publi").push();

                    final String push_id = user_message_push.getKey();


                    String current_uid = mCurrentUser.getUid();


                    mUserDatabase = FirebaseDatabase.getInstance().getReference().child("Users").child(current_uid);
                    mUserDatabase.keepSynced(true);

                    mUserDatabase.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {

                            namePubli = dataSnapshot.child("name").getValue().toString();


                            StorageReference filepath = mImageStorage.child("publi_images").child(push_id + ".jpg");

                            filepath.putFile(imageUri).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                                @Override
                                public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {

                                    if (task.isSuccessful()) {

                                        imagen = false;

                                        String download_url = task.getResult().getDownloadUrl().toString();

                                        Map messageMap = new HashMap();
                                        messageMap.put("name", namePubli + localizacion[0]);
                                        messageMap.put("image", download_url);
                                        messageMap.put("message", "");
                                        messageMap.put("time", ServerValue.TIMESTAMP);
                                        messageMap.put("KeyID", push_id);
                                        messageMap.put("UID", mCurrentUserId);

                                        Map messageUserMap = new HashMap();
                                        messageUserMap.put(current_user_ref + "/" + push_id, messageMap);

                                        mensaje.setText("");

                                        mRootRef.updateChildren(messageUserMap, new DatabaseReference.CompletionListener() {
                                            @Override
                                            public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {

                                            }
                                        });
                                    }

                                }
                            });

                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });


                }

            }
        });
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == GALLERY_PICK && resultCode == RESULT_OK) {

            imageUri = data.getData();
            imagen = true;

        }

    }


    private void locationStart() {
        LocationManager mlocManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        Localizacion Local = new Localizacion();
        Local.setMainActivity(this);
        final boolean gpsEnabled = mlocManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
        if (!gpsEnabled) {
            Intent settingsIntent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
            startActivity(settingsIntent);
        }
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION,}, 1000);
            return;
        }
        mlocManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, (LocationListener) Local);
        mlocManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, (LocationListener) Local);

    }

    public void setLocation(Location loc) {

        if (loc.getLatitude() != 0.0 && loc.getLongitude() != 0.0) {
            try {
                Geocoder geocoder = new Geocoder(this, Locale.getDefault());
                List<Address> list = geocoder.getFromLocation(
                        loc.getLatitude(), loc.getLongitude(), 1);
                if (!list.isEmpty()) {
                    Address DirCalle = list.get(0);
                    ubicacionUser = " - " + DirCalle.getSubAdminArea() + ", " + DirCalle.getCountryName().toString();
                }

            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }

    public class Localizacion implements LocationListener {
        TabDesahogateActivity mainActivity;

        public TabDesahogateActivity getMainActivity() {
            return mainActivity;
        }

        public void setMainActivity(TabDesahogateActivity mainActivity) {
            this.mainActivity = mainActivity;
        }

        @Override
        public void onLocationChanged(Location loc) {

            this.mainActivity.setLocation(loc);
        }

        @Override
        public void onProviderDisabled(String provider) {

        }

        @Override
        public void onProviderEnabled(String provider) {

        }

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {

            switch (status) {
                case LocationProvider.AVAILABLE:
                    Log.d("debug", "LocationProvider.AVAILABLE");
                    break;
                case LocationProvider.OUT_OF_SERVICE:
                    Log.d("debug", "LocationProvider.OUT_OF_SERVICE");
                    break;
                case LocationProvider.TEMPORARILY_UNAVAILABLE:
                    Log.d("debug", "LocationProvider.TEMPORARILY_UNAVAILABLE");
                    break;
            }

        }
    }
}



