package desahogate.proyectofinal.desahogate.Tabs.DIR_Perfil.Tabs_Perfil.Friends;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import desahogate.proyectofinal.desahogate.R;

public class FriendsViewHolder extends RecyclerView.ViewHolder {

    View mView;

    public FriendsViewHolder(View itemView) {
        super(itemView);

        mView = itemView;

    }

    public void setDate(String date){

        TextView userStatusView = (TextView) mView.findViewById(R.id.textBuscarEstado);
        userStatusView.setText(date);

    }

    public void setName(String name){

        TextView userNameView = (TextView) mView.findViewById(R.id.textBuscarUsuario);
        userNameView.setText(name);

    }

    public void setUserImage(String thumb_image, Context ctx){

        ImageView userImageView =  mView.findViewById(R.id.imageBuscarPerfil);
        Glide.with(ctx).load(thumb_image).placeholder(R.drawable.default_avatar).into(userImageView);

    }
}

