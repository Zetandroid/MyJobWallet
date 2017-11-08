package com.kubix.myjobwallet.spese;

public class Uscite {
    private String titolo, uscita, promemoria, dataUscita;

    public Uscite(String s) {
    }

    public Uscite(String titolo, String uscita, String promemoria, String dataEntrata) {
        this.titolo = titolo;
        this.uscita = uscita;
        this.promemoria = promemoria;
        this.dataUscita = dataEntrata;
    }

    public String getTitolo() {
        return titolo;
    }
    public void setTitle(String name) {
        this.titolo = name;
    }

    public String getUscita() {
        return uscita;
    }
    public void setEntrata(String genre) {
        this.uscita = genre;
    }

    public String getPromemoria() {
        return promemoria;
    }
    public void setPromemoria(String entr) {
        this.promemoria = entr;
    }

    public String getDataUscita() {
        return dataUscita;
    }
    public void setDataEntrata(String datenr) {
        this.dataUscita = datenr;
    }




}

