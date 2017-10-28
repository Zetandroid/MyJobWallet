package com.kubix.myjobwallet.note;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.kubix.myjobwallet.R;

import java.util.ArrayList;

/**
 * Created by mowmo on 28/10/17.
 */

public class CustomAdapter extends RecyclerView.Adapter<CustomAdapter.MyViewHolder> {

    ArrayList<String> titoloNote;
    Context context;

    public CustomAdapter(Context context, ArrayList<String> personNames) {
        this.context = context;
        this.titoloNote = personNames;

    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // RICHIAMA IL LAYOUT DELLA GRIGLIA
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.custom_lista_note, parent, false);
        // IMPOSTA IL LAYOUT DELLA GRIGLIA.DIMENSIONI,MARGINI ECC
        MyViewHolder vh = new MyViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {
        // IMPOSTARE I DATI DEGLI ELEMENTI
        holder.notaTitolo.setText(titoloNote.get(position));
        // IMPLEMENTA L'EVENTO setOnClickListener PER LA VISUALIZZAZIONE DEGLI ELEMENTI
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // VISUALIZZA IL TESTO IN UN TOAST
                Toast.makeText(context, titoloNote.get(position), Toast.LENGTH_SHORT).show();
            }
        });

    }


    @Override
    public int getItemCount() {
        return titoloNote.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView notaTitolo;

        public MyViewHolder(View itemView) {
            super(itemView);

            // OTTIENE IL RIFERIMENTO PER LA VISUALIZZAZIONE DEGLI ELEMENTI
            notaTitolo = (TextView) itemView.findViewById(R.id.notaTitolo);

        }
    }
}



