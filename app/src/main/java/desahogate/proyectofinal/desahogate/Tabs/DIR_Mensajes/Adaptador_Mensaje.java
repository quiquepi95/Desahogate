package desahogate.proyectofinal.desahogate.Tabs.DIR_Mensajes;

import android.content.Context;
import android.graphics.Typeface;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import de.hdodenhof.circleimageview.CircleImageView;
import desahogate.proyectofinal.desahogate.R;


public class Adaptador_Mensaje extends RecyclerView.ViewHolder {

    View mView;

    public Adaptador_Mensaje(View itemView) {
        super(itemView);

        mView = itemView;
    }

    public void setMessage(String message, boolean isSeen){

        TextView userStatusView = (TextView) mView.findViewById(R.id.user_single_status);
        userStatusView.setText(message);

        if(!isSeen){
            userStatusView.setTypeface(userStatusView.getTypeface(), Typeface.BOLD);
        } else {
            userStatusView.setTypeface(userStatusView.getTypeface(), Typeface.NORMAL);
        }

    }

    public void setName(String name){

        TextView userNameView = (TextView) mView.findViewById(R.id.user_single_name);
        userNameView.setText(name);

    }

    public void setUserImage(String imageUserProfile, Context ctx){

        CircleImageView userImageView = (CircleImageView) mView.findViewById(R.id.user_single_image);
        if (!imageUserProfile.equals("default")) {
            Glide.with(ctx).load(imageUserProfile)
                    .into(userImageView);
        }else{
            Glide.with(ctx).load(R.drawable.default_avatar)
                    .into(userImageView);
        }

    }
}


