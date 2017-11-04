package com.kubix.myjobwallet.note;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.kubix.myjobwallet.R;

import java.util.List;

class CustomAdapter extends RecyclerView.Adapter<CustomAdapter.MyViewHolder> {

    private List<Note> noteList;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView titolo, note;

        MyViewHolder(View view) {
            super(view);
            titolo = (TextView) view.findViewById(R.id.titolo);
            note = (TextView) view.findViewById(R.id.note);
        }
    }


    CustomAdapter(List<Note> moviesList) {
        this.noteList = moviesList;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.custom_lista_note, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        Note note = noteList.get(position);
        holder.titolo.setText(note.getTitolo());
        holder.note.setText(note.getNote());
    }

    @Override
    public int getItemCount() {
        return noteList.size();
    }


}



