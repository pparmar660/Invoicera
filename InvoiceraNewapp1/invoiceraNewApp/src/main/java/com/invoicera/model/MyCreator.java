package com.invoicera.model;

import android.os.Parcel;
import android.os.Parcelable;

public class MyCreator implements Parcelable.Creator<GraphItemModel> {
    public GraphItemModel createFromParcel(Parcel source) {
          return new GraphItemModel(source);
    }
    public GraphItemModel[] newArray(int size) {
          return new GraphItemModel[size];
    }
}