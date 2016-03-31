package com.invoicera.CustomView;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.invoicera.GlobalData.Constant;
import com.invoicera.InterFace.PopUpResult;
import com.invoicera.androidapp.R;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Parvesh on 25/6/15.EEE
 */
public class CustomPopup extends Dialog {


    public Activity c;
    public Dialog d;
    public Button yes, no;
    ArrayList<HashMap<String, String>> list;
    Context context;
    LayoutInflater inflater;
    View view;
    public TextView clearTv;
    Constant.POP_UP type;
    boolean showPopUp;
    ProgressBar progressBar;
    PopUpResult popUpResult;
    LinearLayout linearLayout;
    TextView titleTV;
    String title;


    public CustomPopup(Activity a, ArrayList<HashMap<String, String>> list, Constant.POP_UP type, boolean showPopUp, PopUpResult popUpResult, String title) {
        super(a);
        // TODO Auto-generated constructor stub
        this.c = a;
        this.list = list;
        this.type = type;
        context = a.getApplicationContext();
        this.showPopUp = showPopUp;
        this.popUpResult = popUpResult;
        this.title = title;
        System.out.println("list :" + list);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.custom_popup);
        linearLayout = (LinearLayout) findViewById(R.id.popUpLiner);
        titleTV = (TextView) findViewById(R.id.title);
        titleTV.setText(title);
        clearTv = (TextView) findViewById(R.id.clear);
        progressBar = (ProgressBar) findViewById(R.id.progress);
        clearTv.setText("Clear");
        if (!showPopUp)
            progressBar.setVisibility(View.GONE);
        else
            progressBar.setVisibility(View.VISIBLE);

        for (int i = 0; i < list.size(); i++) {
            inflater = (LayoutInflater) context.getSystemService(Context
                    .LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.custom_popup_adpter, null);
            view.setId(i);
            TextView txv = (TextView) view.findViewById(R.id.name);
            txv.setText(list.get(i).get(Constant.KEY_NAME));
            TextView txv2 = (TextView) view.findViewById(R.id.detail);

            switch (type) {

                case LATE_FEE:

                    txv.setText(list.get(i).get(Constant.KEY_NAME));
                    txv2.setText("");
                    view.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            dismiss();
                            popUpResult.getPopUpResult(type, v.getId(), false);
                        }
                    });

                    break;
                case ADDITIONAL_CHARGES:
                    txv.setText(list.get(i).get(Constant.KEY_NAME));
                    txv2.setText(list.get(i).get(Constant.KEY_TYPE) + " VAL:" + list.get(i).get(Constant.KEY_VALUE));
                    view.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            dismiss();
                            popUpResult.getPopUpResult(type, v.getId(), false);
                        }
                    });
                    clearTv.setText("Cancel");
                    break;
                case PRODUCT_LIST:

                    txv.setText(list.get(i).get(Constant.KEY_NAME));
                    txv2.setText("");
                    view.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            dismiss();
                            popUpResult.getPopUpResult(type, v.getId(), false);
                        }
                    });

                    break;
                case SERVICE_LIST:

                    txv.setText(list.get(i).get(Constant.KEY_NAME));
                    txv2.setText("");
                    view.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            dismiss();
                            popUpResult.getPopUpResult(type, v.getId(), false);
                        }
                    });

                    break;
                case TAX_LIST1:

                    txv.setText(list.get(i).get(Constant.KEY_NAME));
                    txv2.setText(list.get(i).get(Constant.KEY_VALUE) + " %");
                    view.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            dismiss();
                            popUpResult.getPopUpResult(type, v.getId(), false);
                        }
                    });

                    break;
                case TAX_LIST2:

                    txv.setText(list.get(i).get(Constant.KEY_NAME));
                    txv2.setText(list.get(i).get(Constant.KEY_VALUE) + " %");
                    view.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            dismiss();
                            popUpResult.getPopUpResult(type, v.getId(), false);
                        }
                    });

                    break;

                case TAX:

                    txv.setText(list.get(i).get(Constant.KEY_NAME));
                    txv2.setText(list.get(i).get(Constant.KEY_VALUE) + " %");
                    view.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            dismiss();
                            popUpResult.getPopUpResult(type, v.getId(), false);
                        }
                    });
                    clearTv.setText("Cancel");

                    break;

                case COUNTRY:

                    txv.setText(list.get(i).get(Constant.KEY_CountryName));
                    txv2.setText("");
                    view.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            dismiss();
                            popUpResult.getPopUpResult(type, v.getId(), false);
                        }
                    });

                    break;


                case CURRENCY:

                    txv.setText(list.get(i).get(Constant.KEY_CODE));
                    txv2.setText("");
                    view.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            dismiss();
                            popUpResult.getPopUpResult(type, v.getId(), false);
                        }
                    });
break;
                case PAYMENT_GATEWAY:

                    txv.setText(list.get(i).get(Constant.KEY_NAME));
                    txv2.setText("");
                    view.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            dismiss();
                            popUpResult.getPopUpResult(type, v.getId(), false);
                        }
                    });

                    break;

                case CREDIT_CARD_TYPE:

                    txv.setText(list.get(i).get(Constant.KEY_NAME));
                    txv2.setText("");
                    view.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            dismiss();
                            popUpResult.getPopUpResult(type, v.getId(), false);
                        }
                    });

                    break;

                case CATEGORY_LIST:

                    txv.setText(list.get(i).get(Constant.KEY_NAME));
                    txv2.setText("");
                    view.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            dismiss();
                            popUpResult.getPopUpResult(type, v.getId(), false);
                        }
                    });

                    break;
                case STAFF_LIST:

                    txv.setText(list.get(i).get(Constant.KEY_NAME));
                    txv2.setText("");
                    view.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            dismiss();
                            popUpResult.getPopUpResult(type, v.getId(), false);
                        }
                    });

                    break;

                case PROJECT_LIST:

                    txv.setText(list.get(i).get(Constant.KEY_NAME));
                    txv2.setText("");
                    view.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            dismiss();
                            popUpResult.getPopUpResult(type, v.getId(), false);
                        }
                    });

                    break;


                case CLIENT_LIST:

                    txv.setText(list.get(i).get(Constant.KEY_NAME));
                    txv2.setText("");
                    view.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            dismiss();
                            popUpResult.getPopUpResult(type, v.getId(), false);
                        }
                    });

                    break;



            }


            linearLayout.addView(view, i);
        }

        clearTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
                popUpResult.getPopUpResult(type, v.getId(), true);
            }
        });


    }

    public void updateList(boolean showPopUp, ArrayList<HashMap<String, String>> list1) {
        this.showPopUp = showPopUp;
        clearTv.setText("Clear");
        this.list = list1;
        linearLayout.removeAllViews();
        if (!showPopUp)
            progressBar.setVisibility(View.GONE);
        else
            progressBar.setVisibility(View.VISIBLE);


        for (int i = 0; i < list.size(); i++) {
            inflater = (LayoutInflater) context.getSystemService(Context
                    .LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.custom_popup_adpter, null);
            view.setId(i);
            TextView txv = (TextView) view.findViewById(R.id.name);
            txv.setText(list.get(i).get(Constant.KEY_NAME));
            TextView txv2 = (TextView) view.findViewById(R.id.detail);

            switch (type) {

                case LATE_FEE:

                    txv.setText(list.get(i).get(Constant.KEY_NAME));
                    txv2.setText("");
                    view.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            dismiss();
                            popUpResult.getPopUpResult(type, v.getId(), false);
                        }
                    });

                    break;
                case ADDITIONAL_CHARGES:
                    txv.setText(list.get(i).get(Constant.KEY_NAME));
                    txv2.setText(list.get(i).get(Constant.KEY_TYPE) + " VAL:" + list.get(i).get(Constant.KEY_VALUE));
                    view.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            dismiss();
                            popUpResult.getPopUpResult(type, v.getId(), false);
                        }
                    });
                    clearTv.setText("Cancel");

                    break;

                case PRODUCT_LIST:

                    txv.setText(list.get(i).get(Constant.KEY_NAME));
                    txv2.setText("");
                    view.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            dismiss();
                            popUpResult.getPopUpResult(type, v.getId(), false);
                        }
                    });

                    break;
                case SERVICE_LIST:

                    txv.setText(list.get(i).get(Constant.KEY_NAME));
                    txv2.setText("");
                    view.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            dismiss();
                            popUpResult.getPopUpResult(type, v.getId(), false);
                        }
                    });

                    break;
                case TAX_LIST1:

                    txv.setText(list.get(i).get(Constant.KEY_NAME));
                    txv2.setText(list.get(i).get(Constant.KEY_VALUE) + " %");
                    view.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            dismiss();
                            popUpResult.getPopUpResult(type, v.getId(), false);
                        }
                    });

                    break;
                case TAX_LIST2:

                    txv.setText(list.get(i).get(Constant.KEY_NAME));
                    txv2.setText(list.get(i).get(Constant.KEY_VALUE) + " %");
                    view.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            dismiss();
                            popUpResult.getPopUpResult(type, v.getId(), false);
                        }
                    });

                    break;

                case TAX:

                    txv.setText(list.get(i).get(Constant.KEY_NAME));
                    txv2.setText(list.get(i).get(Constant.KEY_VALUE) + " %");
                    view.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            dismiss();
                            popUpResult.getPopUpResult(type, v.getId(), false);
                        }
                    });
                    clearTv.setText("Cancel");
                    break;

                case COUNTRY:

                    txv.setText(list.get(i).get(Constant.KEY_COUNTRY_NAME));
                    txv2.setText("");
                    view.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            dismiss();
                            popUpResult.getPopUpResult(type, v.getId(), false);
                        }
                    });

                    break;

                case CURRENCY:

                    txv.setText(list.get(i).get(Constant.KEY_CODE));
                    txv2.setText("");
                    view.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            dismiss();
                            popUpResult.getPopUpResult(type, v.getId(), false);
                        }
                    });

                    break;
                case PAYMENT_GATEWAY:

                    txv.setText(list.get(i).get(Constant.KEY_NAME));
                    txv2.setText("");
                    view.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            dismiss();
                            popUpResult.getPopUpResult(type, v.getId(), false);
                        }
                    });

                    break;
                case CREDIT_CARD_TYPE:

                    txv.setText(list.get(i).get(Constant.KEY_NAME));
                    txv2.setText("");
                    view.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            dismiss();
                            popUpResult.getPopUpResult(type, v.getId(), false);
                        }
                    });

                    break;

                case CATEGORY_LIST:

                    txv.setText(list.get(i).get(Constant.KEY_NAME));
                    txv2.setText("");
                    view.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            dismiss();
                            popUpResult.getPopUpResult(type, v.getId(), false);
                        }
                    });
                    break;

                case STAFF_LIST:

                    txv.setText(list.get(i).get(Constant.KEY_NAME));
                    txv2.setText("");
                    view.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            dismiss();
                            popUpResult.getPopUpResult(type, v.getId(), false);
                        }
                    });
                    break;


                case PROJECT_LIST:

                    txv.setText(list.get(i).get(Constant.KEY_NAME));
                    txv2.setText("");
                    view.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            dismiss();
                            popUpResult.getPopUpResult(type, v.getId(), false);
                        }
                    });
                    break;


                case CLIENT_LIST:

                    txv.setText(list.get(i).get(Constant.KEY_NAME));
                    txv2.setText("");
                    view.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            dismiss();
                            popUpResult.getPopUpResult(type, v.getId(), false);
                        }
                    });
                    break;
            }


            linearLayout.addView(view, i);
        }
    }
}



