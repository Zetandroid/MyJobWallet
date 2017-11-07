package com.kubix.myjobwallet.entrate;

public class Entrate {
    private String titolo, entrata, promemoria, dataEntrata;

    public Entrate(String s) {
    }

    public Entrate(String titolo, String entrata, String promemoria, String dataEntrata) {
        this.titolo = titolo;
        this.entrata = entrata;
        this.promemoria = promemoria;
        this.dataEntrata = dataEntrata;
    }

    public String getTitolo() {
        return titolo;
    }
    public void setTitle(String name) {
        this.titolo = name;
    }


    public String getEntrata() {
        return entrata;
    }
    public void setEntrata(String genre) {
        this.entrata = genre;
    }

    public String getPromemoria() {
        return promemoria;
    }
    public void setPromemoria(String entr) {
        this.promemoria = entr;
    }

    public String getDataEntrata() {
        return dataEntrata;
    }
    public void setDataEntrata(String datenr) {
        this.dataEntrata = datenr;
    }




}

