package com.invoicera.Fragment;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.invoicera.GlobalData.Constant;
import com.invoicera.ViewPagerAdpter.RecurringCreateAndDetailPagerAdapter;
import com.invoicera.ViewPagerFragment.RecurringCreateEditPagerFragment;
import com.invoicera.ViewPagerFragment.RecurringInformationPagerFragment;
import com.invoicera.ViewPagerFragment.RecurringPreviewPagerFragment;
import com.invoicera.androidapp.R;

import java.util.HashMap;
import java.util.List;

/**
 * Created by Parvesh on 25/9/15.
 */
public class RecurringPreviewCreateFragment extends BaseFragment implements
        ViewPager.OnPageChangeListener {

    RecurringCreateEditPagerFragment createFragment;
    RecurringPreviewPagerFragment previewFragment;
    RecurringInformationPagerFragment informationFragment;

    public RecurringCreateAndDetailPagerAdapter pagerAdapter;
    Bundle argument;
    static public ViewPager pager;

    HorizontalScrollView scrollView;
    LinearLayout scrollLiner;
    String content[] = {"CREATE", "PREVIEW", "HISTORY"};
    int pagerCurrentPosition = 0;
    public String recurring_id = "";
    public String recurringStatus = "";

    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.detail_fragment,
                container, false);

        Bundle bundle = getArguments();
        if (bundle != null)
            if (bundle.getString(Constant.KEY_RECURRING_ID) != null) {
                content = new String[]{"EDIT", "PREVIEW", "HISTORY"};
                pagerCurrentPosition = bundle.getInt(Constant.KEY_POSITION);
                recurring_id = bundle.getString(Constant.KEY_RECURRING_ID);
                recurringStatus = bundle.getString(Constant.KEY_RECURRING_STATUS);

            }


        pagerAdapter = new RecurringCreateAndDetailPagerAdapter(getChildFragmentManager());

        pager = (ViewPager) view.findViewById(R.id.pager);
        pager.setOffscreenPageLimit(3);

        // -------------------View Pager
        scrollView = (HorizontalScrollView) view
                .findViewById(R.id.horizontalScroll);
        scrollLiner = (LinearLayout) view.findViewById(R.id.scrollLiner);

        pager.setAdapter(pagerAdapter);

        // --------
        // Create Fragment------------
        createFragment = new RecurringCreateEditPagerFragment();
        argument = new Bundle();
        argument.putString(Constant.KEY_RECURRING_ID, recurring_id);
        argument.putString(Constant.KEY_RECURRING_STATUS, recurringStatus);
        if (bundle.getString(Constant.KEY_CLIENT_ID) != null)
            createFragment.setArguments(bundle);
        else
            createFragment.setArguments(argument);
        pagerAdapter.add(createFragment);
        // preview Fragment-------------------------

        previewFragment = new RecurringPreviewPagerFragment();
        argument = new Bundle();

        argument.putString(Constant.KEY_RECURRING_ID, recurring_id);

        argument.putString(Constant.KEY_RECURRING_STATUS, recurringStatus);
        previewFragment.setArguments(argument);
        pagerAdapter.add(previewFragment);
        // ---------------------------

        // information Fragment-------------------------

        informationFragment = new RecurringInformationPagerFragment();
        argument = new Bundle();

        argument.putString(Constant.KEY_RECURRING_ID, recurring_id);

        argument.putString(Constant.KEY_RECURRING_STATUS, recurringStatus);
        informationFragment.setArguments(argument);
        pagerAdapter.add(informationFragment);
        // ---------------------------
        pager.setOnPageChangeListener(this);

        // ----

        for (int i = 0; i < 3; i++) {
            scrollLiner.addView(addIndicater(i));
        }// ----
        invalidateTabs(0);


        pager.setCurrentItem(pagerCurrentPosition);


        return view;

    }

    public View addIndicater(int position) {

        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.indicater_text, scrollLiner,
                false);
        TextView textView = (TextView) view.findViewById(R.id.indicater_text);

        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                global.screenWidth / 3, LinearLayout.LayoutParams.WRAP_CONTENT);

		/*
         * layoutParams.setMargins((int) global.convertDpToPixel(10, context),
		 * (int) global.convertDpToPixel(10, context), (int)
		 * global.convertDpToPixel(10, context), (int)
		 * global.convertDpToPixel(10, context));
		 */// margins as you
        // wish

        textView.setLayoutParams(layoutParams);

        // imageView.setScaleType(ImageView.ScaleType.FIT_CENTER);
        // imageView.setImageResource(R.mipmap.ic_launcher);
        textView.setText(content[position]);
        textView.setTextColor(Color.WHITE);
        view.setTag(position);
        textView.setTag(position);
        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    int position = Integer.parseInt(view.getTag().toString());
                    pager.setCurrentItem(position);
                } catch (NumberFormatException e) {

                    e.getMessage();
                }

            }
        });

        return view;

    }

    @Override
    public void onPageScrollStateChanged(int arg0) {

    }

    @Override
    public void onPageScrolled(int arg0, float arg1, int arg2) {

    }

    @Override
    public void onPageSelected(int arg0) {
        invalidateTabs(arg0);

    }

    private void invalidateTabs(int currentItem) {
        View view;
        int childCount = scrollLiner.getChildCount();
        for (int i = 0; i < childCount; i++) {
            view = scrollLiner.getChildAt(i);
            if (currentItem == Integer.parseInt(view.getTag().toString())) {
                view.setBackground(context.getResources().getDrawable(R.drawable.tab_indicator_list_back));
                int vLeft = view.getLeft();
                int vRight = view.getRight();
                int sWidth = scrollView.getWidth();
                scrollView.smoothScrollTo(((vLeft + vRight - sWidth) / 2), 0);
            } else {
                view.setBackgroundColor(Color.parseColor("#E56E27"));
            }
        }
    }

    public void getChildFragment(Constant.FRAGMENT_RESULT type, HashMap<String, String> result) {

        switch (type) {


            case CLIENT:
                RecurringCreateEditPagerFragment fragment = (RecurringCreateEditPagerFragment) pagerAdapter.getItem(0);
                fragment.clientAdd = result.get(Constant.KEY_ADDRESS);
                fragment.clientName = result.get(Constant.KEY_CLIENT_NAME);
                fragment.clientId = result.get(Constant.KEY_CLIENT_ID);
                fragment.clientTV.setText(fragment.clientName);


                break;
        }

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        List<Fragment> fragments = getChildFragmentManager().getFragments();
        if (fragments != null) {
            for (Fragment fragment : fragments) {
                fragment.onActivityResult(requestCode, resultCode, data);
            }
        }

    }
}
