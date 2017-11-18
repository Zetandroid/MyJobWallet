package com.kubix.myjobwallet.entrate;

public class Entrate {
    private String titolo, entrata, categoria, promemoria, dataEntrata, giornoCorrente, numeroCorrente, meseCorrente, annoCorrente;

    public Entrate(String s) {

    }

    public Entrate(String titolo, String entrata, String promemoria, String dataEntrata, String categoria, String giornoCorrente, String numeroCorrente, String meseCorrente, String annoCorrente) {
        this.titolo = titolo;
        this.entrata = entrata;
        this.categoria = categoria;
        this.promemoria = promemoria;
        this.dataEntrata = dataEntrata;
        this.giornoCorrente = giornoCorrente;
        this.numeroCorrente=numeroCorrente;
        this.meseCorrente = meseCorrente;
        this.annoCorrente = annoCorrente;
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

    public String getCategoria() {
        return categoria;
    }
    public void setCategoria(String categ) {
        this.categoria = categ;
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

    public String getGiornoCorrente() {
        return giornoCorrente;
    }
    public void setGiornoCorrente(String giorncor) {
        this.giornoCorrente = giorncor;
    }

    public String getNumeroCorrente() {
        return numeroCorrente;
    }
    public void setNumeroCorrente(String numcor) {
        this.numeroCorrente = numcor;
    }

    public String getMeseCorrente() {
        return meseCorrente;
    }
    public void setMeseCorrente(String mescor) {
        this.meseCorrente = mescor;
    }

    public String getAnnoCorrente() {
        return annoCorrente;
    }
    public void setAnnoCorrente(String annocor) {
        this.annoCorrente = annocor;
    }
}

