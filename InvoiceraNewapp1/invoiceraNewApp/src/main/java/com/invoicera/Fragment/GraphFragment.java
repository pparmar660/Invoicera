package com.invoicera.Fragment;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.invoicera.CustomView.CanvasView;
import com.invoicera.GlobalData.Constant;
import com.invoicera.ViewPagerAdpter.GraphPagerAdapter;
import com.invoicera.ViewPagerFragment.ExpenseListPagerFragment;
import com.invoicera.androidapp.ExpenseCreateEdit;
import com.invoicera.androidapp.Home;
import com.invoicera.androidapp.R;
import com.invoicera.listener.FragmentChanger;
import com.invoicera.model.GraphItemModel;

import java.util.ArrayList;

public class GraphFragment extends BaseFragment {
    // Store instance variables
    private String title;
    private int page;
    ArrayList<GraphItemModel> model;
    static String DATA_LIST = "data_list";
    FragmentChanger fragmentChanger;

/*	// newInstance constructor for creating fragment with arguments
    public GraphFragment newInstance(int page, ArrayList<GraphItemModel> model) {
		this.model = model;
		GraphFragment fragmentFirst = new GraphFragment();
		Bundle args = new Bundle();
		args.putInt("someInt", page);

		fragmentFirst.setArguments(args);
		return fragmentFirst;
	}*/

    // Store instance variables based on arguments passed
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //	page = getArguments().getInt("someInt", 0);
        //title = getArguments().getString("someTitle");
    }

    // Inflate the view for the fragment based on layout XML
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.graph, container, false);

        model = getArguments().getParcelableArrayList(DATA_LIST);

        LinearLayout liner = (LinearLayout) view.findViewById(R.id.linerlayout);

        GraphPagerAdapter.centerX = (int) (HomePageFragment.global.screenWidth / 2);
        GraphPagerAdapter.centerY = ((HomePageFragment) getParentFragment()).pager
                .getLayoutParams().height / 2;

        GraphPagerAdapter.radiusOfOuter = (GraphPagerAdapter.centerX * 90) / 100;

        if (((GraphPagerAdapter.centerY * 90) / 100) < GraphPagerAdapter.radiusOfOuter) {

            GraphPagerAdapter.radiusOfOuter = ((GraphPagerAdapter.centerY * 70) / 100);
            GraphPagerAdapter.radiusOfInner = GraphPagerAdapter.radiusOfOuter * 80 / 100;

        }

        GraphPagerAdapter.outerCircleStork = (int) (3 * getResources().getDisplayMetrics().density);
        GraphPagerAdapter.innerCircleStork = (int) (6 * getResources().getDisplayMetrics().density);
        GraphPagerAdapter.radiusOfCircleOnArc = (int) (13 * getResources().getDisplayMetrics().density);
        GraphPagerAdapter.arcTextSize = (int) (11 * getResources().getDisplayMetrics().density);
        GraphPagerAdapter.centerTextSize = (int) (15 * getResources().getDisplayMetrics().density);
        GraphPagerAdapter.hintCircleTextSize = (int) (8 * getResources().getDisplayMetrics().density);
        GraphPagerAdapter.hintCircleRadius = (int) (4 * getResources().getDisplayMetrics().density);
        GraphPagerAdapter.gabBetweenHint = (int) (8 * getResources().getDisplayMetrics().density);

        View myCanView = new CanvasView(Home.context, model);
        liner.addView(myCanView);
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (HomePageFragment.selectedPosition) {
                    case 0:
                        if (HomePageFragment.totalItem.get(0) > 0)
                            fragmentChanger.onFragmentReplace(new InvoiceList(), new Bundle());
                        else
                            fragmentChanger.onFragmentReplaceWithBackStack(
                                    new InvoicePreviewCreateFragment(),
                                    Constant.InvoicePreviewCreateFragmentTag, new Bundle());
                        break;
                    case 1:
                        if (HomePageFragment.totalItem.get(1) > 0)
                            fragmentChanger.onFragmentReplace(new EstimateList(), new Bundle());
                        else
                            fragmentChanger.onFragmentReplace(new EstimatePreviewCreateFragment(), new Bundle());
                        break;
                    case 2:

                        if (HomePageFragment.totalItem.get(1) > 0)
                            fragmentChanger.onFragmentReplace(new ExpenseList(), new Bundle());
                        else {
                            fragmentChanger.onFragmentReplace(new ExpenseCreateEdit(), new Bundle());
                        }
                        break;


                }


            }
        });

        return view;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        fragmentChanger = (FragmentChanger) getActivity();
    }
}