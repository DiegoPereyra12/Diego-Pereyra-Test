package com.example.axosnettestjava;

public class ReceiptModel {

    public Integer id;
    public String provider;
    public Double amount;
    public String comment;
    public String emission_date;
    public String currency_code;

    public ReceiptModel(Integer id, String provider, Double amount, String comment, String emission_date, String currency_code) {
        this.id = id;
        this.provider = provider;
        this.amount = amount;
        this.emission_date = emission_date;
        this.comment = comment;
        this.currency_code = currency_code;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getProvider() {
        return provider;
    }

    public void setProvider(String provider) {
        this.provider = provider;
    }

    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    public String getEmission_date() {
        return emission_date;
    }

    public void setEmission_date(String emission_date) {
        this.emission_date = emission_date;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getCurrency_code() {
        return currency_code;
    }

    public void setCurrency_code(String currency_code) {
        this.currency_code = currency_code;
    }
}
