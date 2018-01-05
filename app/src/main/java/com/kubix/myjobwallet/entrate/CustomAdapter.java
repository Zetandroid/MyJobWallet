package com.kubix.myjobwallet.entrate;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.kubix.myjobwallet.R;
import com.kubix.myjobwallet.utility.VariabiliGlobali;

import java.util.List;

class CustomAdapter extends RecyclerView.Adapter<CustomAdapter.MyViewHolder> {

    private List<Entrate> entrateList;
    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView titolo, entrata, promemoria, dataEntrata, giornoCorrente, numeroCorrente, meseCorrente, annoCorrente;
        public ImageView categoriaImg, notificaImg;
        MyViewHolder(View view) {
            super(view);
            titolo = (TextView) view.findViewById(R.id.textEntrata_titolo);
            entrata = (TextView) view.findViewById(R.id.textEntrata_cifra);
            categoriaImg = (ImageView) view.findViewById(R.id.iconsEntrata_tag);
            //notificaImg = (ImageView) view.findViewById(R.id.imgNotificaEntrata);
            //promemoria = (TextView) view.findViewById(R.id.txtPromemoria);
            //dataEntrata = (TextView) view.findViewById(R.id.txtDataEntrata);
            //giornoCorrente = (TextView) view.findViewById(R.id.testoNumeroGiornoEntrata);
            //numeroCorrente = (TextView) view.findViewById(R.id.testoNumeroGiornoEntrata);
            //meseCorrente = (TextView)view.findViewById(R.id.testoMeseEntrata);
            //annoCorrente = (TextView)view.findViewById(R.id.testoAnnoEntrata);
        }
    }


    CustomAdapter(List<Entrate> moviesList) {
        this.entrateList = moviesList;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.custom_lista_entrate, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        Entrate entrate = entrateList.get(position);
        holder.titolo.setText(entrate.getTitolo());
        holder.entrata.setText(entrate.getEntrata() + " " + VariabiliGlobali.simboloValuta);
        String categoria = entrate.getCategoria();

        if(categoria.equals("Bonifico")){
            holder.categoriaImg.setImageResource(R.drawable.ic_btnsheet_entrate_bonifico);
        }else if(categoria.equals("Assegno")){
            holder.categoriaImg.setImageResource(R.drawable.ic_btnsheet_entrate_assegno);
        }else if(categoria.equals("Vincita")){
            holder.categoriaImg.setImageResource(R.drawable.ic_btnsheet_entrate_vincita);
        }else if(categoria.equals("Regalo")){
            holder.categoriaImg.setImageResource(R.drawable.ic_btnsheet_entrate_regalo);
        }else if(categoria.equals("Beni")){
            holder.categoriaImg.setImageResource(R.drawable.ic_btnsheet_entrate_beni);
        }else if(categoria.equals("Rimborsi")){
            holder.categoriaImg.setImageResource(R.drawable.ic_btnsheet_entrate_rimborsi);
        }else if(categoria.equals("Vendite")){
            holder.categoriaImg.setImageResource(R.drawable.ic_btnsheet_entrate_vendite);
        }else if(categoria.equals("Altro")){
            holder.categoriaImg.setImageResource(R.drawable.ic_btnsheet_spese_altro);
        }

        //holder.promemoria.setText(entrate.getPromemoria());
        //holder.dataEntrata.setText(entrate.getDataEntrata());

        //String oraEntrata = entrate.getPromemoria();
        //String dataEntrata = entrate.getDataEntrata();

        //if(oraEntrata.equals("") && dataEntrata.equals("")){
            //holder.notificaImg.setVisibility(View.INVISIBLE);
        //}else{
            //holder.notificaImg.setVisibility(View.VISIBLE);
        }

        //holder.giornoCorrente .setText(entrate.getNumeroCorrente());
        //holder.numeroCorrente.setText(entrate.getNumeroCorrente());
        //holder.meseCorrente.setText(entrate.getMeseCorrente());
        //holder.annoCorrente.setText(entrate.getAnnoCorrente());



    //}

    @Override
    public int getItemCount() {
        return entrateList.size();
    }


}



