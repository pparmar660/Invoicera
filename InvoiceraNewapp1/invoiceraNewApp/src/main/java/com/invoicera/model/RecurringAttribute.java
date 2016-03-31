package com.invoicera.model;

/**
 * Created by Parvesh on 25/9/15.
 */
public class RecurringAttribute {

    String organization, date, recurring_id, recurring_no, currency, net_balance, status, late_fee, sent, frequency, occurrence;

    public String getOrganization() {
        return organization;
    }

    public void setOrganization(String organization) {
        this.organization = organization;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getRecurring_id() {
        return recurring_id;
    }

    public void setRecurring_id(String recurring_id) {
        this.recurring_id = recurring_id;
    }

    public String getRecurring_no() {
        return recurring_no;
    }

    public void setRecurring_no(String recurring_no) {
        this.recurring_no = recurring_no;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public String getNet_balance() {
        return net_balance;
    }

    public void setNet_balance(String net_balance) {
        this.net_balance = net_balance;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getLate_fee() {
        return late_fee;
    }

    public void setLate_fee(String late_fee) {
        this.late_fee = late_fee;
    }

    public String getSent() {
        return sent;
    }

    public void setSent(String sent) {
        this.sent = sent;
    }

    public String getFrequency() {
        return frequency;
    }

    public void setFrequency(String frequency) {
        this.frequency = frequency;
    }

    public String getOccurrence() {
        return occurrence;
    }

    public void setOccurrence(String occurrence) {
        this.occurrence = occurrence;
    }
}
