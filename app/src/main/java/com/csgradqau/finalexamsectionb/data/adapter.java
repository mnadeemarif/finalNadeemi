package com.csgradqau.finalexamsectionb.data;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


import com.csgradqau.finalexamsectionb.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.List;

public class adapter extends RecyclerView.Adapter<adapter.customerViewHolder>{
    private Context context;
    private List<product> userList;

    public class customerViewHolder extends RecyclerView.ViewHolder {
        public TextView type;
        public TextView quantity;
        public TextView price;
        public ImageView profile;

        public customerViewHolder(View view) {
            super(view);
            type = view.findViewById(R.id.type);
            quantity = view.findViewById(R.id.quantity);
            profile = view.findViewById(R.id.selfie);
            price = view.findViewById(R.id.price);

        }

    }


    public adapter(Context context, List<product> userList) {
        this.context = context;
        this.userList = userList;
    }

    @Override
    public customerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.detail_list_row, parent, false);

        return new customerViewHolder(itemView);
    }



    @Override
    public void onBindViewHolder(customerViewHolder holder, int position) {
//        user usr = userList.get(position);
        product usr = userList.get(position);
        final customerViewHolder h = holder;
        FirebaseStorage fs = FirebaseStorage.getInstance();
        String img = "profile_pics/"+usr.getId();
        StorageReference stRef = fs.getReference(img);
        final long ONE_MEGABYTE = 1024 * 1024;
        stRef.getBytes(ONE_MEGABYTE).addOnSuccessListener(new OnSuccessListener<byte[]>() {
            @Override
            public void onSuccess(byte[] bytes) {
                // Data for "images/island.jpg" is returns, use this as needed
                Bitmap bmp= BitmapFactory.decodeByteArray(bytes,0,bytes.length);
                h.profile.setImageBitmap(bmp);

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                // Handle any errors

            }
        });
       // Bitmap bitmap = BitmapFactory.decodeByteArray(usr.getProfile(), 0, usr.getProfile().length);
        //holder.profile.setImageBitmap((bitmap));
        holder.type.setText(usr.getType());
        holder.quantity.setText(usr.getQuantity());
        holder.price.setText(usr.getPrice());
    }

    @Override
    public int getItemCount() {
        return userList.size();
    }


}
