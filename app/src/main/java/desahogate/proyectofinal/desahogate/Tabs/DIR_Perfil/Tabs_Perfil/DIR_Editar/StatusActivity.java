package desahogate.proyectofinal.desahogate.Tabs.DIR_Perfil.Tabs_Perfil.DIR_Editar;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.HashMap;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;
import desahogate.proyectofinal.desahogate.R;

public class StatusActivity extends AppCompatActivity {

    private Toolbar mToolbar;

    private CircleImageView mDisplayImage;
    private EditText mStatus;
    private EditText mBio;
    private Button btnGuardarEdit;
    private ProgressDialog mProgressDialog;


    private static final int GALLERY_PICK = 1;

    //Firebase
    private DatabaseReference mUserDatabase;
    private FirebaseUser mCurrentUser;
    private StorageReference mImageStorage;

    private Uri resultUri = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_status);

        inicializarControles();

        mDisplayImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent galleryIntent = new Intent();
                galleryIntent.setType("image/*");
                galleryIntent.setAction(Intent.ACTION_GET_CONTENT);

                startActivityForResult(Intent.createChooser(galleryIntent, "SELECT IMAGE"), GALLERY_PICK);

            }
        });

        String current_uid = mCurrentUser.getUid();

        mUserDatabase = FirebaseDatabase.getInstance().getReference().child("Users").child(current_uid);
        mUserDatabase.keepSynced(true);

        mUserDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                final String image = dataSnapshot.child("image").getValue().toString();
                mStatus.setText(dataSnapshot.child("status").getValue().toString());
                mBio.setText(dataSnapshot.child("biography").getValue().toString());

                if (image.equals("default")) {
                    // Default o que cargue la url
                    Glide.with(StatusActivity.this).load(R.drawable.default_avatar).placeholder(R.drawable.default_avatar).into(mDisplayImage);

                } else {
                    Glide.with(StatusActivity.this).load(image).into(mDisplayImage);
                }


            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        btnGuardarEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String status = mStatus.getText().toString();
                String bio = mBio.getText().toString();

                //Progress
                mProgressDialog = new ProgressDialog(StatusActivity.this);
                mProgressDialog.setTitle("Guardando cambios");
                mProgressDialog.setMessage("Por favor espere mientras guardamos los cambios.");
                mProgressDialog.show();

                mUserDatabase.child("status").setValue(status).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {

                        if(task.isSuccessful()){

                            mProgressDialog.dismiss();

                        } else {

                            Toast.makeText(StatusActivity.this, "Hubo un error al guardar los cambios del estado.", Toast.LENGTH_LONG).show();

                        }

                    }
                });

                mUserDatabase.child("biography").setValue(bio).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {

                        if(task.isSuccessful()){

                            mProgressDialog.dismiss();

                        } else {

                            Toast.makeText(StatusActivity.this, "Hubo un error al guardar los cambios en la biografía.", Toast.LENGTH_LONG).show();

                        }

                    }
                });


                String current_user_id = mCurrentUser.getUid();

                StorageReference filepath = mImageStorage.child("profile_images").child(current_user_id + ".jpg");

                filepath.putFile(resultUri).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {

                        if (task.isSuccessful()) {

                            mProgressDialog.dismiss();


                            final String download_url = task.getResult().getDownloadUrl().toString();
                            //Glide.with(getActivity()).load(download_url).into(mDisplayImage);

                            Map update_hashMap = new HashMap();
                            update_hashMap.put("image", download_url);

                            mUserDatabase.updateChildren(update_hashMap, new DatabaseReference.CompletionListener() {
                                @Override
                                public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {

                                    if (databaseError != null) {

                                    }

                                }
                            });


                        } else {

                            Toast.makeText(StatusActivity.this, "Error in uploading.", Toast.LENGTH_LONG).show();

                        }

                    }
                });


            }
        });



    }

    private void inicializarControles() {
        mDisplayImage = (CircleImageView) findViewById(R.id.imagePerfil);
        mBio = (EditText) findViewById(R.id.userBiografia);
        mStatus = (EditText) findViewById(R.id.userName);
        btnGuardarEdit = (Button) findViewById(R.id.status_save_btn);

        mImageStorage = FirebaseStorage.getInstance().getReference();
        mCurrentUser = FirebaseAuth.getInstance().getCurrentUser();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == GALLERY_PICK && resultCode == RESULT_OK) {

            resultUri = data.getData();

        }

    }

}
