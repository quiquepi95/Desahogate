package desahogate.proyectofinal.desahogate.Tabs.DIR_Home;


import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.content.DialogInterface;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;


import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import desahogate.proyectofinal.desahogate.R;


public class Adaptador_Home extends RecyclerView.Adapter<Adaptador_Home.MyViewHolder> {
    private final Context context;
    private List<Datos> data;

    //Id del usuario conectado
    String userUID = FirebaseAuth.getInstance().getCurrentUser().getUid();

    public Adaptador_Home(Context context, @NonNull List<Datos> data) {
        this.context = context;
        this.data = data;
    }


    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View row = LayoutInflater.from(parent.getContext()).inflate(R.layout.activity_plantilla_mensaje_home, parent, false);
        return new MyViewHolder(row);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, int position) {
        final Datos person = data.get(position);

        holder.getUserNameView().setText(person.getName());

        if (person.getMessage().isEmpty()) {
            holder.getUserMSJ().setText("");
        } else {
            holder.getUserMSJ().setText(person.getMessage());
        }

        Timestamp stamp = new Timestamp(person.getTime());
        final Date date = new Date(stamp.getTime());
        final DateFormat inFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm");

        holder.getUserFecha().setText(inFormat.format(date));

        Glide.clear(holder.userProfileView);


        //Cambio la foto de perfil de las publicaciones
        final DatabaseReference mUserDatabase = FirebaseDatabase.getInstance().getReference().child("Users").child(person.getUID());
        mUserDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (!dataSnapshot.child("image").getValue().toString().equals("default")) {
                    Glide.with(context).load(dataSnapshot.child("image").getValue().toString()).diskCacheStrategy(DiskCacheStrategy.ALL).into(holder.userProfileView);
                }else {
                    Glide.with(context).load(R.drawable.default_avatar).diskCacheStrategy(DiskCacheStrategy.ALL).placeholder(R.drawable.default_avatar).into(holder.userProfileView);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) { }
        });





        Glide.clear(holder.userImageView);

        if (!person.getImage().equals("default")) {
            Glide.with(context).load(person.getImage()).fitCenter().diskCacheStrategy(DiskCacheStrategy.ALL).into(holder.userImageView);
        }

        if (person.getUID().equals(userUID)) {

            holder.borrar.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    createSimpleDialog(person).show();
                }
            });
        } else {
            holder.borrar.setVisibility(View.INVISIBLE);
        }


    }

    @Override
    public int getItemCount() {

        return data.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder implements View
            .OnClickListener {

        desahogate.proyectofinal.desahogate.Tools.ResponsiveImage userImageView;

        TextView userMSJ;
        TextView userFecha;
        TextView userNameView;
        CircleImageView userProfileView;
        View vista;
        TextView borrar;

        public MyViewHolder(View itemView) {
            super(itemView);
            vista = itemView;
            userImageView = itemView.findViewById(R.id.imageViewPublicada);
            userMSJ = (TextView) itemView.findViewById(R.id.textHomeMensaje);
            userFecha = (TextView) itemView.findViewById(R.id.textViewFecha);
            userNameView = (TextView) itemView.findViewById(R.id.textViewNombre);
            userProfileView = itemView.findViewById(R.id.imageViewPerfil);
            borrar = itemView.findViewById(R.id.textID_Borrar);

            itemView.setOnClickListener(this);
        }


        public TextView getUserMSJ() {
            return userMSJ;
        }

        public TextView getUserFecha() {
            return userFecha;
        }

        public TextView getUserNameView() {
            return userNameView;
        }


        @Override
        public void onClick(View v) {
        }
    }


    /**
     * Me crea un dialogo preguntandome si deseo eliminar o no la publicación
     * @param person
     * @return
     */
    public AlertDialog createSimpleDialog(final Datos person) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);

        builder.setTitle("Eliminar Publicación")
                .setMessage("¿Está seguro que desea eliminar?")
                .setPositiveButton("Aceptar",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                                DatabaseReference bbddDatos = FirebaseDatabase.getInstance().getReference().child("publi");
                                Query busqueda = bbddDatos.orderByChild("time").equalTo(person.getTime());

                                busqueda.addValueEventListener(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(DataSnapshot snapshot) {

                                        Datos studentDetails = null;
                                        for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                                            studentDetails = dataSnapshot.getValue(Datos.class);
                                        }

                                        try {

                                            DatabaseReference bbddDatosEliminar = FirebaseDatabase.getInstance().getReference().child("publi").child(studentDetails.getKeyID());
                                            bbddDatosEliminar.removeValue();
                                            Toast.makeText(context, "Publicación Eliminada ", Toast.LENGTH_SHORT).show();

                                        } catch (Exception e) {
                                            System.out.println("Error controlado");
                                        }

                                    }

                                    @Override
                                    public void onCancelled(DatabaseError databaseError) {
                                    }
                                });

                                dialog.dismiss();
                            }
                        })
                .setNegativeButton("Cancelar",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.cancel();
                            }
                        });

        return builder.create();
    }

}

