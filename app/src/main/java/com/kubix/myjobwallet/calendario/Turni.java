package com.kubix.myjobwallet.calendario;

public class Turni {
    private String giornosettimana, numerogiorno, mese, anno, oraentrata, orauscita, ordinarie, straordinarie;

    public Turni(String s) {

    }

    public Turni(String giornosettimana, String numerogiorno, String mese, String anno, String oraentrata, String orauscita, String ordinarie, String straordinarie) {
        this.giornosettimana = giornosettimana;
        this.numerogiorno = numerogiorno;
        this.mese = mese;
        this.anno = anno;
        this.oraentrata = oraentrata;
        this.orauscita = orauscita;
        this.ordinarie = ordinarie;
        this.straordinarie = straordinarie;
    }

    public String getGiornoSettimana() {
        return giornosettimana;
    }
    public void setGiornosettimana(String giorset) {
        this.giornosettimana = giorset;
    }

    public String getNumeroGiorno() {
        return numerogiorno;
    }
    public void setNumerogiorno(String numgior) {
        this.numerogiorno = numgior;
    }

    public String getMese() {
        return mese;
    }
    public void setMese(String mes) {
        this.mese = mes;
    }

    public String getAnno() {
        return anno;
    }
    public void setAnno(String ann) {
        this.anno = ann;
    }

    public String getOraEntrata() {
        return oraentrata;
    }
    public void setOraEntrata(String oraentr) {
        this.oraentrata = oraentr;
    }

    public String getOraUscita() {
        return orauscita;
    }
    public void setOraUscita(String orausc) {
        this.orauscita = orausc;
    }

    public String getOrdinarie() {
        return ordinarie;
    }
    public void setOrdinarie(String ordin) {
        this.ordinarie = ordin;
    }

    public String getStraordinarie() {
        return straordinarie;
    }
    public void setStraordinarie(String straord) {
        this.straordinarie = straord;
    }
}

