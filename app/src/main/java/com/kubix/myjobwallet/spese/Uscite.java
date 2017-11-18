package com.kubix.myjobwallet.spese;

public class Uscite {
    private String titolo, uscita, categoria, promemoria, dataUscita, giornoCorrente, numeroCorrente, meseCorrente, annoCorrente;

    public Uscite(String s) {
    }

    public Uscite(String titolo, String uscita, String promemoria, String dataEntrata, String categoria, String giornoCorrente, String numeroCorrente, String meseCorrente, String annoCorrente){
        this.titolo = titolo;
        this.uscita = uscita;
        this.categoria = categoria;
        this.promemoria = promemoria;
        this.dataUscita = dataEntrata;
        this.giornoCorrente = giornoCorrente;
        this.numeroCorrente = numeroCorrente;
        this.meseCorrente = meseCorrente;
        this.annoCorrente = annoCorrente;
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

    public String getCategoriaUscita() {
        return categoria;
    }
    public void setCategoriaUscita(String categusc) {
        this.categoria = categusc;
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

    public String getGiornoCorrente() {
        return giornoCorrente;
    }
    public void setGiornoCorrente(String giorncor) {
        this.giornoCorrente = giorncor;
    }

    public String getNumeroCorrente() {
        return numeroCorrente;
    }
    public void setNumeroCorrente(String numcorr) {
        this.numeroCorrente = numcorr;
    }

    public String getMeseCorrente() {
        return meseCorrente;
    }
    public void setMeseCorrente(String mescorr) {
        this.meseCorrente = mescorr;
    }

    public String getAnnoCorrente() {
        return annoCorrente;
    }
    public void setAnnoCorrente(String anncorr) {
        this.annoCorrente = anncorr;
    }




}

