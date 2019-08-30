package desahogate.proyectofinal.desahogate.Tabs.DIR_Buscar;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import de.hdodenhof.circleimageview.CircleImageView;
import desahogate.proyectofinal.desahogate.R;



public class Adaptador_Buscar extends RecyclerView.ViewHolder {

    View mView;

    public Adaptador_Buscar(View itemView) {
        super(itemView);

        mView = itemView;

    }

    public void setDisplayName(String name) {

        TextView userNameView = (TextView) mView.findViewById(R.id.textBuscarUsuario);
        userNameView.setText(name);

    }

    public void setUserStatus(String status) {

        TextView userStatusView = (TextView) mView.findViewById(R.id.textBuscarEstado);
        userStatusView.setText(status);


    }

    public void setUserImage(String thumb_image, Context ctx) {

        CircleImageView userImageView =  mView.findViewById(R.id.imageBuscarPerfil);
        if (thumb_image.equals("default")) {
            Glide.with(ctx).load(R.drawable.default_avatar).placeholder(R.drawable.default_avatar).into(userImageView);
        }else{
            Glide.with(ctx).load(thumb_image).into(userImageView);
        }

    }

}
