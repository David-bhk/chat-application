package com.example.chatf.adapter;

import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.chatf.R;
import com.example.chatf.model.dataCommunication;
import com.example.chatf.session.preferences;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import de.hdodenhof.circleimageview.CircleImageView;

public class CommunicationRecyclerAdapter extends RecyclerView.Adapter<CommunicationRecyclerAdapter.communicationViewHolder> {
    Context context;
    List<dataCommunication> listCommunication;
    List<String> listData;
    DatabaseReference database = FirebaseDatabase.getInstance().getReference();


    public CommunicationRecyclerAdapter() {
    }

    public CommunicationRecyclerAdapter(Context context, List<dataCommunication> listCommunication, List<String> listData) {
        this.context = context;
        this.listCommunication = listCommunication;
        this.listData = listData;
    }



    @NonNull
    @Override
    public communicationViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_communication, parent, false);

        return new communicationViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull communicationViewHolder holder, int position) {
        //int plus = position + 1;
        //dataCommunication itemPlus = listCommunication.get(position);
        String itemData = listData.get(position);

        dataCommunication itemNormal = listCommunication.get(position);
        Date date = new Date();
        Locale locale = new Locale("in", "ID");
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MMM/yyyy", locale);
        long yesterday = date.getTime() - (1000 * 60 * 60 * 24);

        if (itemNormal.getDate().equals(itemData)){
            holder.linearDate.setVisibility(View.GONE);

        }
        if (itemNormal.getDate().equals(simpleDateFormat.format(yesterday))){
            holder.textDate.setText("Yesterday");
        }else if (itemNormal.getDate().equals(simpleDateFormat.format(date.getTime()))){
            holder.textDate.setText("Today");
        }else{
            holder.textDate.setText(itemNormal.getDate());
        }
            holder.binView(listCommunication.get(position));
    }

    @Override
    public int getItemCount() {
        return listCommunication.size();
    }

    public class communicationViewHolder extends RecyclerView.ViewHolder {
        TextView de, message, time, textDate;
        CircleImageView imageView;
        LinearLayout linear, linear2, linearDate;
        CardView cardView, cardDate;

        public communicationViewHolder(@NonNull View itemView) {
            super(itemView);
            de= itemView.findViewById(R.id.de);
            message = itemView.findViewById(R.id.message);
            time= itemView.findViewById(R.id.time);
            imageView = itemView.findViewById(R.id.imageView);
            linear = itemView.findViewById(R.id.linear);
            linearDate = itemView.findViewById(R.id.linearDate);
            cardDate = itemView.findViewById(R.id.cardDate);
            cardView = itemView.findViewById(R.id.cardView);
            textDate = itemView.findViewById(R.id.textDate);
            linear2 = itemView.findViewById(R.id.linear2);
        }

        public void  binView(dataCommunication dataCommunication) {

            Locale locale = new Locale("in", "ID");
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("hh:mm aa", locale);

            de.setText(dataCommunication.getDe());
            message.setText(dataCommunication.getMessage());
            time.setText(simpleDateFormat.format(dataCommunication.getTime()));


            database.child("login").child(dataCommunication.getKey()).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    String srcImage = dataSnapshot.child("image").getValue(String.class);
                    Glide.with(context).load(srcImage).placeholder(R.drawable.profile).into(imageView);

                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });

            if (dataCommunication.getKey().equals(preferences.getKeyData(context))){
                de.setVisibility(View.GONE);
                imageView.setVisibility(View.GONE);
                linear.setGravity(Gravity.CENTER|Gravity.END);
                linear2.setGravity(Gravity.CENTER|Gravity.END);
                message.setTextColor(context.getResources().getColor(android.R.color.white));
                cardView.setCardBackgroundColor(context.getResources().getColor(R.color.red));
            }

        }
    }
}
