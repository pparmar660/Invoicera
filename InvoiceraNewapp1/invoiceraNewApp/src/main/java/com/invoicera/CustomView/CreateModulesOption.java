package com.invoicera.CustomView;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TableLayout;
import android.widget.TableRow;

import com.invoicera.GlobalData.Constant;
import com.invoicera.InterFace.CreateModule;
import com.invoicera.androidapp.R;

/**
 * Created by Parvesh on 12/6/15.
 */
public class CreateModulesOption implements OnClickListener {


    Activity activity;
    View view;
    LayoutInflater inflater;
    // MyAnimation animatioon;
    View MyView;
    ViewGroup contentFrame;
    ImageView removeImage;
    CreateModule createModule;
    TableRow invoiceRow,estimateRow;

    public void addView(Activity activity, final CreateModule createModule) {

        this.createModule = createModule;

        this.activity = activity;
        //  animatioon = new MyAnimation(activity.getApplicationContext());
        contentFrame = (ViewGroup) activity.getWindow().getDecorView().findViewById(android.R.id.content);
        // view = activity.getWindow().getDecorView().findViewById(android.R.id.content);
        inflater = LayoutInflater.from(activity.getApplicationContext());
        MyView = inflater.inflate(R.layout.cretae_all_module, contentFrame, false);
        contentFrame.addView(MyView);
        invoiceRow = (TableRow) MyView.findViewById(R.id.invoiceRow);
        estimateRow=(TableRow)MyView.findViewById(R.id.estimateRow);
        estimateRow.setOnClickListener(this);
        invoiceRow.setOnClickListener(this);
        removeImage = (ImageView) MyView.findViewById(R.id.close);
        removeImage.setOnClickListener(this);
        animationStart((TableLayout) MyView.findViewById(R.id.tableLayout), R.anim.animation_sub_menu_open);


    }


    @Override
    public void onClick(View v) {

        switch (v.getId()) {

            case R.id.close:
                animationEnd((TableLayout) MyView.findViewById(R.id.tableLayout), R.anim.animation_sub_menu_close, Constant.CREATE_MODULE.CANCEL);

                break;
            case R.id.invoiceRow:
                animationEnd((TableLayout) MyView.findViewById(R.id.tableLayout), R.anim.animation_sub_menu_close, Constant.CREATE_MODULE.INVOICE);

                break;


            case R.id.estimateRow:
                animationEnd((TableLayout) MyView.findViewById(R.id.tableLayout), R.anim.animation_sub_menu_close, Constant.CREATE_MODULE.ESTIMATE);
                break;
        }


    }

    public void animationEnd(TableLayout layout, int animationId, final Constant.CREATE_MODULE type) {

        android.view.animation.Animation animationInfo;

        animationInfo = AnimationUtils.loadAnimation(activity.getApplicationContext(),
                animationId);

        animationInfo.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {

                switch (type) {

                    case INVOICE:
                        contentFrame.removeView(MyView);
                        createModule.getResultOfCreateModule(Constant.CREATE_MODULE.INVOICE);

                        break;


                    case ESTIMATE:
                        contentFrame.removeView(MyView);
                        createModule.getResultOfCreateModule(Constant.CREATE_MODULE.ESTIMATE);

                        break;

                    case CANCEL:
                        contentFrame.removeView(MyView);
                        createModule.getResultOfCreateModule(Constant.CREATE_MODULE.CANCEL);
                        break;

                }

            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });

        layout.startAnimation(animationInfo);


    }

    public void animationStart(TableLayout layout, int animationId) {

        android.view.animation.Animation animationInfo;

        animationInfo = AnimationUtils.loadAnimation(activity.getApplicationContext(),
                animationId);

        animationInfo.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {

            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });

        layout.startAnimation(animationInfo);


    }


}
