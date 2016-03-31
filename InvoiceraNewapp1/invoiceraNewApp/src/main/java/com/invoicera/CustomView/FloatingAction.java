package com.invoicera.CustomView;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;

import com.invoicera.Fragment.CreateEditProductServices;
import com.invoicera.Fragment.EstimatePreviewCreateFragment;
import com.invoicera.Fragment.InvoicePreviewCreateFragment;
import com.invoicera.Fragment.RecurringPreviewCreateFragment;
import com.invoicera.GlobalData.Constant;
import com.invoicera.androidapp.CreateEditClient;
import com.invoicera.androidapp.ExpenseCreateEdit;
import com.invoicera.androidapp.Global;
import com.invoicera.androidapp.R;
import com.invoicera.listener.FragmentChanger;
import com.wangjie.androidbucket.utils.ABTextUtil;
import com.wangjie.rapidfloatingactionbutton.RapidFloatingActionButton;
import com.wangjie.rapidfloatingactionbutton.RapidFloatingActionHelper;
import com.wangjie.rapidfloatingactionbutton.RapidFloatingActionLayout;
import com.wangjie.rapidfloatingactionbutton.contentimpl.labellist.RFACLabelItem;
import com.wangjie.rapidfloatingactionbutton.contentimpl.labellist.RapidFloatingActionContentLabelList;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Parvesh on 14/8/15.
 */
public class FloatingAction {


    private RapidFloatingActionHelper rfabHelper;
    FragmentChanger fragmentChanger;
    ;
    RapidFloatingActionButton rfaButton;
    Context context;
    Global global;

    public FloatingAction(Context context, FragmentChanger fragmentChanger, RapidFloatingActionLayout rfaLayout, RapidFloatingActionButton rfaButton, RapidFloatingActionContentLabelList rfaContent) {


        this.context = context;
        this.fragmentChanger = fragmentChanger;
        this.rfaButton = rfaButton;
        global = (Global) context.getApplicationContext();
        List<RFACLabelItem> items = new ArrayList<>();
        items.add(new RFACLabelItem<Integer>()
                        .setLabel(" Invoice ")
                        .setLabelBackgroundDrawable(context.getResources().getDrawable(R.drawable.box_background))
                        .setResId(R.drawable.invoice)
                        .setLabelColor(Color.parseColor("#000000"))
                        .setIconNormalColor(Color.parseColor("#4191EA"))
                        .setWrapper(0)
        );
        items.add(new RFACLabelItem<Integer>()
                        .setLabel(" Estimate ")


                        .setLabelBackgroundDrawable(context.getResources().getDrawable(R.drawable.box_background))
                        .setResId(R.drawable.estimate)
                        .setLabelColor(Color.parseColor("#000000"))
                        .setIconNormalColor(Color.parseColor("#57D7Df"))

                        .setWrapper(1)
        );
        items.add(new RFACLabelItem<Integer>()
                        .setLabel(" Client ").setLabelBackgroundDrawable(context.getResources().getDrawable(R.drawable.box_background))
                        .setResId(R.drawable.create_client)
                        .setLabelColor(Color.parseColor("#000000"))
                        .setIconNormalColor(Color.parseColor("#5CB200"))
                        .setWrapper(2)
        );
        items.add(new RFACLabelItem<Integer>()
                        .setLabel(" Item ")
                        .setLabelBackgroundDrawable(context.getResources().getDrawable(R.drawable.box_background))
                        .setResId(R.drawable.product)
                        .setLabelColor(Color.parseColor("#000000"))
                        .setIconNormalColor(Color.parseColor("#2CACA6"))

                        .setWrapper(3)
        );


        items.add(new RFACLabelItem<Integer>()
                        .setLabel(" Expense ")
                        .setResId(R.drawable.expense)
                        .setLabelBackgroundDrawable(context.getResources().getDrawable(R.drawable.box_background))

                        .setLabelColor(Color.parseColor("#000000"))
                        .setIconNormalColor(Color.parseColor("#7960E7"))

                        .setWrapper(3)
        );

        items.add(new RFACLabelItem<Integer>()
                        .setLabel(" Recurring ")
                        .setResId(R.drawable.recurring)
                        .setLabelBackgroundDrawable(context.getResources().getDrawable(R.drawable.box_background))

                        .setLabelColor(Color.parseColor("#000000"))
                        .setIconNormalColor(Color.parseColor("#EE6495"))

                        .setWrapper(5)
        );


        rfaContent
                .setItems(items)
                .setIconShadowRadius(ABTextUtil.dip2px(context, 5))

                .setIconShadowDy(ABTextUtil.dip2px(context, 5))
        ;

        rfabHelper = new RapidFloatingActionHelper(
                context,
                rfaLayout,
                rfaButton,
                rfaContent
        ).build();


    }


    public void createFunction(int position) {

        rfabHelper.collapseContent();

        Intent i;
        switch (position) {

            case 0:


                fragmentChanger.onFragmentReplaceWithBackStack(
                        new InvoicePreviewCreateFragment(),
                        Constant.InvoicePreviewCreateFragmentTag, new Bundle());
                break;

            case 1:


                fragmentChanger.onFragmentReplaceWithBackStack(
                        new EstimatePreviewCreateFragment(),
                        Constant.EstimatePreviewCreateFragmentTag, new Bundle());
                break;


            case 2:
                if (!global.isNetworkAvailable()) {
                    global.showAlert(Constant.NO_CONNECTION_MESSAGE, context);
                    return;
                }
                i = new Intent(context, CreateEditClient.class);
                context.startActivity(i);
                break;


            case 3:
                if (!global.isNetworkAvailable()) {
                    global.showAlert(Constant.NO_CONNECTION_MESSAGE, context);
                    return;
                }

                fragmentChanger.onFragmentAddWithBackStack(
                        new CreateEditProductServices(),
                        Constant.CreateProductServicesFragmentTag, new Bundle());
                break;

            case 4:
                if (!global.isNetworkAvailable()) {
                    global.showAlert(Constant.NO_CONNECTION_MESSAGE, context);
                    return;
                }
                fragmentChanger.onFragmentAddWithBackStack(
                        new ExpenseCreateEdit(),
                        Constant.CreateExpenseFragmentTag, new Bundle());

                break;

            case 5:
           /*     if (!global.isNetworkAvailable()) {
                    global.showAlert(Constant.NO_CONNECTION_MESSAGE, context);
                    return;
                }*/

                fragmentChanger.onFragmentAddWithBackStack(
                        new RecurringPreviewCreateFragment(),
                        Constant.CreateProductServicesFragmentTag, new Bundle());
                break;


        }
    }
}
