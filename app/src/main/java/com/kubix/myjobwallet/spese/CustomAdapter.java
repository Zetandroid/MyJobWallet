package com.kubix.myjobwallet.spese;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import com.kubix.myjobwallet.R;
import java.util.List;

class CustomAdapter extends RecyclerView.Adapter<CustomAdapter.MyViewHolder> {

    private List<Uscite> usciteList;
    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView titolo, uscita, promemoria, dataUscita, giornoCorrente, numeroCorrente, meseCorrente, annoCorrente;
        public ImageView categoriaImg, notificaImg;
        MyViewHolder(View view) {
            super(view);
            titolo = (TextView) view.findViewById(R.id.txtTitoloSpesa);
            uscita = (TextView) view.findViewById(R.id.txtSpesa);
            categoriaImg = (ImageView) view.findViewById(R.id.iconaTagUscite);
            notificaImg = (ImageView) view.findViewById(R.id.imgNotificaSpese);
            promemoria = (TextView) view.findViewById(R.id.txtPromemoriaUsc);
            dataUscita = (TextView) view.findViewById(R.id.txtDataSpesa);
            numeroCorrente = (TextView) view.findViewById(R.id.testoNumeroGiornoSpese);
            meseCorrente = (TextView)view.findViewById(R.id.testoMeseSpese);
            annoCorrente = (TextView)view.findViewById(R.id.testoAnnoSpese);
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
        holder.uscita.setText(uscite.getUscita() + " €");
        String categoria = uscite.getCategoriaUscita();
        if(categoria.equals("Casa")){
            holder.categoriaImg.setImageResource(R.drawable.ic_btnsheet_spese_casa);
        }else if(categoria.equals("Trasporti")){
            holder.categoriaImg.setImageResource(R.drawable.ic_btnsheet_spese_trasporti);
        }else if(categoria.equals("Auto")){
            holder.categoriaImg.setImageResource(R.drawable.ic_btnsheet_spese_auto);
        }else if(categoria.equals("Carburante")){
            holder.categoriaImg.setImageResource(R.drawable.ic_btnsheet_spese_carburante);
        }else if(categoria.equals("Bollette")){
            holder.categoriaImg.setImageResource(R.drawable.ic_btnsheet_spese_bollette);
        }else if(categoria.equals("Shopping")){
            holder.categoriaImg.setImageResource(R.drawable.ic_btnsheet_spese_shopping);
        }else if(categoria.equals("Cibo & Bevande")){
            holder.categoriaImg.setImageResource(R.drawable.ic_btnsheet_spese_cibo);
        }else if(categoria.equals("Svago")){
            holder.categoriaImg.setImageResource(R.drawable.ic_btnsheet_spese_svago);
        }else if(categoria.equals("Viaggi")){
            holder.categoriaImg.setImageResource(R.drawable.ic_btnsheet_spese_viaggi);
        }else if (categoria.equals("Altro")){
            holder.categoriaImg.setImageResource(R.drawable.ic_btnsheet_spese_altro);
        }

        holder.promemoria.setText(uscite.getPromemoria());
        holder.dataUscita.setText(uscite.getDataUscita());

        String oraEntrata = uscite.getPromemoria();
        String dataUscita = uscite.getDataUscita();

        if(oraEntrata.equals("") && dataUscita.equals("")){
            holder.notificaImg.setVisibility(View.INVISIBLE);
        }else{
            holder.notificaImg.setVisibility(View.VISIBLE);
        }

        holder.giornoCorrente .setText(uscite.getGiornoCorrente());
        holder.numeroCorrente.setText(uscite.getNumeroCorrente());
        holder.meseCorrente.setText(uscite.getMeseCorrente());
        holder.annoCorrente.setText(uscite.getAnnoCorrente());
    }

    @Override
    public int getItemCount() {
        return usciteList.size();
    }

}



