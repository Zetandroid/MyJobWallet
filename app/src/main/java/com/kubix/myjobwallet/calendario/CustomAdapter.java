package com.kubix.myjobwallet.calendario;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.kubix.myjobwallet.R;

import java.util.List;

class CustomAdapter extends RecyclerView.Adapter<CustomAdapter.MyViewHolder> {

    private List<Turni> turniList;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView  numerogiorno, mese, anno, oraentrata, orauscita, ordinarie, straordinarie;
        MyViewHolder(View view) {
            super(view);
            numerogiorno = (TextView) view.findViewById(R.id.testoNumeroGiorno);
            mese = (TextView) view.findViewById(R.id.txtMese);
            anno = (TextView) view.findViewById(R.id.txtAnno);
            oraentrata = (TextView) view.findViewById(R.id.txtOraEntrata);
            orauscita = (TextView) view.findViewById(R.id.txtOraUscita);
            ordinarie = (TextView) view.findViewById(R.id.txtOreOrdinarie);
            straordinarie = (TextView) view.findViewById(R.id.txtOreStraordinarie);
        }
    }


    CustomAdapter(List<Turni> moviesList) {
        this.turniList = moviesList;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.custom_lista_turni, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        Turni turni = turniList.get(position);
        holder.numerogiorno.setText(turni.getNumeroGiorno());
        holder.mese.setText(turni.getMese());
        holder.anno.setText(turni.getAnno());
        holder.oraentrata.setText(turni.getOraEntrata());
        holder.orauscita.setText(turni.getOraUscita());
        holder.ordinarie.setText(turni.getOrdinarie());
        holder.straordinarie.setText(turni.getStraordinarie());
    }

    @Override
    public int getItemCount() {
        return turniList.size();
    }


}



