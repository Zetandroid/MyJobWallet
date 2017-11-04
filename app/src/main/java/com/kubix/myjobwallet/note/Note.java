package com.kubix.myjobwallet.note;

public class Note {
    private String titolo, note;

    public Note(String s) {
    }

    public Note(String titolo, String note) {
        this.titolo = titolo;
        this.note = note;
    }

    public String getTitolo() {
        return titolo;
    }

    public void setTitle(String name) {
        this.titolo = name;
    }


    public String getNote() {
        return note;
    }

    public void setNote(String genre) {
        this.note = genre;
    }


}

