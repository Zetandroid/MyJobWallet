package com.kubix.myjobwallet.spese;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.kubix.myjobwallet.R;

import java.util.List;

class CustomAdapter extends RecyclerView.Adapter<CustomAdapter.MyViewHolder> {

    private List<Uscite> usciteList;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView titolo, uscita, promemoria, dataUscita;

        MyViewHolder(View view) {
            super(view);
            titolo = (TextView) view.findViewById(R.id.txtTitoloSpesa);
            uscita = (TextView) view.findViewById(R.id.txtSpesa);
            promemoria = (TextView) view.findViewById(R.id.txtPromemoriaUsc);
            dataUscita = (TextView) view.findViewById(R.id.txtDataSpesa);
        }
    }


    CustomAdapter(List<Uscite> moviesList) {
        this.usciteList = moviesList;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.custom_lista_spese, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        Uscite uscite = usciteList.get(position);
        holder.titolo.setText(uscite.getTitolo());
        holder.uscita.setText(uscite.getUscita());
        holder.promemoria.setText(uscite.getPromemoria());
        holder.dataUscita.setText(uscite.getDataUscita());
    }

    @Override
    public int getItemCount() {
        return usciteList.size();
    }


}



