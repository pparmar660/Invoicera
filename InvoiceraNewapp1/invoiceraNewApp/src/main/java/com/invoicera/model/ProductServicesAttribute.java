package com.invoicera.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Parvesh on 25/8/15.
 */
public class ProductServicesAttribute implements Parcelable {



    String id, name, unitCost, quantity, type,createBy,description, date, status, tax_one_value, tax_two_value, currency_id, currency_name, tax_one_name, tax_two_name, inventoryCurrentStock, trackInventory, taxId1, taxId2;



    public static final Creator<ProductServicesAttribute> CREATOR = new Creator<ProductServicesAttribute>() {
        @Override
        public ProductServicesAttribute createFromParcel(Parcel in) {
            return new ProductServicesAttribute(in);
        }

        @Override
        public ProductServicesAttribute[] newArray(int size) {
            return new ProductServicesAttribute[size];
        }
    };

    public String getId() {
        return id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCreateBy() {
        return createBy;
    }

    public void setCreateBy(String createBy) {
        this.createBy = createBy;
    }

    public String getName() {

        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUnitCost() {
        return unitCost;
    }

    public void setUnitCost(String unitCost) {
        this.unitCost = unitCost;
    }

    public String getQuantity() {
        return quantity;
    }

    public void setQuantity(String quantity) {
        this.quantity = quantity;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getTax_one_value() {
        return tax_one_value;
    }

    public void setTax_one_value(String tax_one_value) {
        this.tax_one_value = tax_one_value;
    }

    public String getTax_two_value() {
        return tax_two_value;
    }

    public void setTax_two_value(String tax_two_value) {
        this.tax_two_value = tax_two_value;
    }

    public String getCurrency_id() {
        return currency_id;
    }

    public void setCurrency_id(String currency_id) {
        this.currency_id = currency_id;
    }

    public String getCurrency_name() {
        return currency_name;
    }

    public void setCurrency_name(String currency_name) {
        this.currency_name = currency_name;
    }

    public String getTax_one_name() {
        return tax_one_name;
    }

    public void setTax_one_name(String tax_one_name) {
        this.tax_one_name = tax_one_name;
    }

    public String getTax_two_name() {
        return tax_two_name;
    }

    public void setTax_two_name(String tax_two_name) {
        this.tax_two_name = tax_two_name;
    }

    public String getInventoryCurrentStock() {
        return inventoryCurrentStock;
    }

    public void setInventoryCurrentStock(String inventoryCurrentStock) {
        this.inventoryCurrentStock = inventoryCurrentStock;
    }

    public String getTrackInventory() {
        return trackInventory;
    }

    public void setTrackInventory(String trackInventory) {
        this.trackInventory = trackInventory;
    }

    public String getTaxId1() {
        return taxId1;
    }

    public void setTaxId1(String taxId1) {
        this.taxId1 = taxId1;
    }

    public String getTaxId2() {
        return taxId2;
    }

    public void setTaxId2(String taxId2) {
        this.taxId2 = taxId2;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeString(name);
        dest.writeString(unitCost);
        dest.writeString(quantity);
        dest.writeString(type);
        dest.writeString(createBy);
        dest.writeString(description);
        dest.writeString(date);
        dest.writeString(status);
        dest.writeString(tax_one_value);
        dest.writeString(tax_two_value);
        dest.writeString(currency_id);
        dest.writeString(currency_name);
        dest.writeString(tax_one_name);
        dest.writeString(tax_two_name);
        dest.writeString(inventoryCurrentStock);
        dest.writeString(trackInventory);
        dest.writeString(taxId1);
        dest.writeString(taxId2);
    }
public  ProductServicesAttribute(){}
    protected ProductServicesAttribute(Parcel in) {
        id = in.readString();
        name = in.readString();
        unitCost = in.readString();
        quantity = in.readString();
        type = in.readString();
        createBy = in.readString();
        description = in.readString();
        date = in.readString();
        status = in.readString();
        tax_one_value = in.readString();
        tax_two_value = in.readString();
        currency_id = in.readString();
        currency_name = in.readString();
        tax_one_name = in.readString();
        tax_two_name = in.readString();
        inventoryCurrentStock = in.readString();
        trackInventory = in.readString();
        taxId1 = in.readString();
        taxId2 = in.readString();
    }
}