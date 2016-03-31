package com.invoicera.model;

/**
 * Created by Parvesh on 28/9/15.
 */
public class ExpenseAttribute {

    String expense_id,date,status,expense_status,amount,category,client_id,currency,client_name,project_id,notes,tax1_id,tax2_id ,isExpenseInvoiced,isExpenseRecurring,frequency,recurringEndDate;

    public String getExpense_id() {
        return expense_id;
    }

    public String getIsExpenseInvoiced() {
        return isExpenseInvoiced;
    }

    public void setIsExpenseInvoiced(String isExpenseInvoiced) {
        this.isExpenseInvoiced = isExpenseInvoiced;
    }

    public String getIsExpenseRecurring() {
        return isExpenseRecurring;
    }

    public void setIsExpenseRecurring(String isExpenseRecurring) {
        this.isExpenseRecurring = isExpenseRecurring;
    }

    public String getFrequency() {
        return frequency;
    }

    public void setFrequency(String frequency) {
        this.frequency = frequency;
    }

    public String getRecurringEndDate() {
        return recurringEndDate;
    }

    public void setRecurringEndDate(String recurringEndDate) {
        this.recurringEndDate = recurringEndDate;
    }

    public String getCurrency() {
        return currency;
    }

    public String getClient_id() {
        return client_id;
    }

    public void setClient_id(String client_id) {
        this.client_id = client_id;
    }

    public String getProject_id() {
        return project_id;
    }

    public void setProject_id(String project_id) {
        this.project_id = project_id;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public String getTax1_id() {
        return tax1_id;
    }

    public void setTax1_id(String tax1_id) {
        this.tax1_id = tax1_id;
    }

    public String getTax2_id() {
        return tax2_id;
    }

    public void setTax2_id(String tax2_id) {
        this.tax2_id = tax2_id;
    }

    public String getClient_name() {
        return client_name;
    }

    public void setClient_name(String client_name) {
        this.client_name = client_name;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }


    public void setExpense_id(String expense_id) {
        this.expense_id = expense_id;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }



    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getExpense_status() {
        return expense_status;
    }

    public void setExpense_status(String expense_status) {
        this.expense_status = expense_status;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }
}
