package com.invoicera.androidapp;

import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.invoicera.Database.DatabaseHelper;
import com.invoicera.Fragment.ClientList;
import com.invoicera.Fragment.EstimateList;
import com.invoicera.Fragment.EstimatePreviewCreateFragment;
import com.invoicera.Fragment.ExpenseList;
import com.invoicera.Fragment.HomePageFragment;
import com.invoicera.Fragment.InvoiceList;
import com.invoicera.Fragment.InvoicePreviewCreateFragment;
import com.invoicera.Fragment.ProductServicesList;
import com.invoicera.Fragment.RecurringList;
import com.invoicera.GlobalData.Constant;
import com.invoicera.GlobalData.Constant.DrwerListSection;
import com.invoicera.GlobalData.Preferences;
import com.invoicera.ListViewAdpter.LeftDrawer;
import com.invoicera.listener.FragmentChanger;
import com.invoicera.model.LeftDrawerItem;

import java.util.ArrayList;
import java.util.List;

public class Home extends AppCompatActivity implements FragmentChanger {

    Toolbar toolbar;
    ArrayList<LeftDrawerItem> drawerListItem;
    private DrawerLayout mDrawerLayout;
    private ActionBarDrawerToggle mDrawerToggle;
    private ListView mDrawerListView;
    private ImageView open;
    public static Context context;
    FrameLayout frameLayout;
    LeftDrawer draerListAdpter;
    Global global;
    DatabaseHelper db;
    public static TextView toolbarText;
    public static ProgressBar progressBarHome;
    Preferences preferences;
    private Boolean exit = false;

    public static ImageView backHome, filterImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home);


        //

        // View tool_bar = (View)findViewById(R.id.tool_bar);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        backHome = (ImageView) findViewById(R.id.backHome);
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawerLayout);
        mDrawerListView = (ListView) findViewById(R.id.list);
        drawerListItem = new ArrayList<LeftDrawerItem>();
        toolbarText = (TextView) findViewById(R.id.toolbar_text);
        context = this;
        global = (Global) context.getApplicationContext();
        this.setSupportActionBar(toolbar);
        progressBarHome = (ProgressBar) findViewById(R.id.progress_home);
        progressBarHome.setVisibility(View.GONE);
        progressBarHome.setOnClickListener(null);
        filterImage = (ImageView) findViewById(R.id.filter_home);
        filterImage.setVisibility(View.GONE);

        mDrawerListView.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                int editedPosition = position + 1;
                // Toast.makeText(Home.this, "You Selected item" + editedPosition,
                //      Toast.LENGTH_SHORT).show();
                mDrawerLayout.closeDrawer(mDrawerListView);

                selectItem(position);
            }
        });
        db = new DatabaseHelper(context);
        open = (ImageView) findViewById(R.id.toolbar_button);
        open.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                if (mDrawerLayout.isDrawerOpen(GravityCompat.START)) {
                    mDrawerLayout.closeDrawer(GravityCompat.START);
                } else {
                    mDrawerLayout.openDrawer(GravityCompat.START);
                }
            }
        });
        preferences = new Preferences(context);
        // set


        SetDrawerList();
        //  selectItem(1);


        if (getIntent().getStringExtra(Constant.KEY_CLIENT_ID) != null) {

            if (getIntent().getStringExtra("isAddEstimate") != null) {
                if (getIntent().getStringExtra("isAddEstimate").equalsIgnoreCase("true")) {
                    Bundle arguments = getIntent().getBundleExtra(Constant.KEY_CLIENT);
                    onFragmentReplace(new EstimatePreviewCreateFragment(), arguments);
                } else {
                    Bundle arguments = getIntent().getBundleExtra(Constant.KEY_CLIENT);
                    onFragmentReplace(new InvoicePreviewCreateFragment(), arguments);
                }

            }


        } else selectItem(1);


        backHome.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {

                if (getSupportFragmentManager().getBackStackEntryCount() < 1) {
                    if (exit) {
                        finish(); // finish activity
                    } else {
                        Toast.makeText(context, "Press Back again to Exit.",
                                Toast.LENGTH_SHORT).show();
                        exit = true;
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                exit = false;
                            }
                        }, 3 * 1000);

                    }
                } else {
                    getSupportFragmentManager().popBackStack();
                    getSupportFragmentManager().executePendingTransactions();
                    backHome.setVisibility(View.GONE);
                }
            }


        });

    }


    @Override
    protected void onResume() {
        // TODO Auto-generated method stub
        super.onResume();
        //	SetDrawerList();
        //selectItem(1);
    }

    void SetDrawerList() {

        LeftDrawerItem map = new LeftDrawerItem();

        // 0- Header
        map = new LeftDrawerItem();
        map.setType(DrwerListSection.DRAWER_HEADER.toString());
        map.setResId(R.drawable.invoicera_logo);
        drawerListItem.add(map);

        // 1-Home,2-client,3-product

        map = new LeftDrawerItem();
        map.setType(DrwerListSection.DRAWER_ITEM.toString());
        map.setResId(R.drawable.home_left);
        map.setItemName("Home");
        drawerListItem.add(map);

        map = new LeftDrawerItem();
        map.setType(DrwerListSection.DRAWER_ITEM.toString());
        map.setResId(R.drawable.client_left);
        map.setItemName("Clients");
        drawerListItem.add(map);

        map = new LeftDrawerItem();
        map.setType(DrwerListSection.DRAWER_ITEM.toString());
        map.setResId(R.drawable.products_left);
        map.setItemName("Products/Services");
        drawerListItem.add(map);

        // 4-section
        map = new LeftDrawerItem();
        map.setType(DrwerListSection.DRAWER_SECTION.toString());
        map.setItemName("Sales & Purchases");
        drawerListItem.add(map);

        // 5-invoice,6-estimate,7-Expanse,8-recurring,9-sign out

        map = new LeftDrawerItem();
        map.setType(DrwerListSection.DRAWER_ITEM.toString());
        map.setResId(R.drawable.invoices_left);
        map.setItemName("Invoices");
        drawerListItem.add(map);

        map = new LeftDrawerItem();
        map.setType(DrwerListSection.DRAWER_ITEM.toString());
        map.setResId(R.drawable.estimate_left);
        map.setItemName("Estimates");
        drawerListItem.add(map);

        map = new LeftDrawerItem();
        map.setType(DrwerListSection.DRAWER_ITEM.toString());
        map.setResId(R.drawable.expenses_left);
        map.setItemName("Expenses");
        drawerListItem.add(map);

        map = new LeftDrawerItem();
        map.setType(DrwerListSection.DRAWER_ITEM.toString());
        map.setResId(R.drawable.recurring_invoice_left);
        map.setItemName("Recurring Invoice");
        drawerListItem.add(map);

        map = new LeftDrawerItem();
        map.setType(DrwerListSection.DRAWER_ITEM.toString());
        map.setResId(R.drawable.sign_out);
        map.setItemName("Sign out");
        drawerListItem.add(map);


        draerListAdpter = new LeftDrawer(drawerListItem, context);
        mDrawerListView.setAdapter(draerListAdpter);
        draerListAdpter.notifyDataSetChanged();

    }

    private void selectItem(int position) {

        // mDrawerLayout.closeDrawer(mDrawerLayout);

        // update the main content by replacing fragments

        System.out.println("Look pos" + position);
        Fragment fragment;
        FragmentManager fragmentManager = getSupportFragmentManager();
        Bundle args = new Bundle();
        switch (position) {
            case 1:
                fragment = new HomePageFragment();
                args.putInt(Constant.POSITION_NUMBER, position);
                fragment.setArguments(args);

                onFragmentReplace(fragment, args);
                break;

            case 2:
                fragment = new ClientList();
                args.putInt(Constant.POSITION_NUMBER, position);
                fragment.setArguments(args);

                onFragmentReplace(fragment, args);
                break;

            case 3:
                fragment = new ProductServicesList();
                args.putInt(Constant.POSITION_NUMBER, position);
                fragment.setArguments(args);

                onFragmentReplace(fragment, args);
                break;


            case 5:

                fragment = new InvoiceList();
                args.putInt(Constant.POSITION_NUMBER, position);
                fragment.setArguments(args);

                onFragmentReplace(fragment, args);
                break;
            case 6:

                fragment = new EstimateList();
                args.putInt(Constant.POSITION_NUMBER, position);
                fragment.setArguments(args);

                onFragmentReplace(fragment, args);
                break;

            case 7:
                fragment = new ExpenseList();
                args.putInt(Constant.POSITION_NUMBER, position);
                fragment.setArguments(args);

                onFragmentReplace(fragment, args);
                break;

            case 8:
                fragment = new RecurringList();
                args.putInt(Constant.POSITION_NUMBER, position);
                fragment.setArguments(args);

                onFragmentReplace(fragment, args);
                break;

            case 9:
                Intent myIntent = new Intent(context, Login.class);
                myIntent.putExtra("finish", true); // if you are checking for
                // this in your other
                // Activities
                db.removeAllTable();
                //  preferences.sharedPref.edit().clear().commit();
                preferences.setBoolean(Constant.KEY_USER_LOGED, false);

                myIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP
                        | Intent.FLAG_ACTIVITY_CLEAR_TASK
                        | Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(myIntent);
                finish();
                ;
                break;

            default:
                break;
        }

    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        // mDrawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        // mDrawerToggle.onConfigurationChanged(newConfig);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        // getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        switch (item.getItemId()) {
            case android.R.id.home: {
                if (mDrawerLayout.isDrawerOpen(mDrawerListView)) {
                    mDrawerLayout.closeDrawer(mDrawerListView);
                } else {
                    mDrawerLayout.openDrawer(mDrawerListView);
                }
                return true;
            }
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onFragmentReplaceWithBackStack(Fragment fragment, String TAG, Bundle args) {
        fragment.setArguments(args);
        getSupportFragmentManager().beginTransaction().replace(R.id.content_frame, fragment).addToBackStack(TAG).commit();
        getSupportFragmentManager().executePendingTransactions();
    }

    @Override
    public void onFragmentAddWithBackStack(Fragment fragment, String TAG, Bundle args) {
        fragment.setArguments(args);
        getSupportFragmentManager().beginTransaction()
                .add(R.id.content_frame, fragment).addToBackStack(TAG).commit();
        getSupportFragmentManager().executePendingTransactions();
    }

    @Override
    public void onFragmentAddWithoutBackStack(Fragment fragment, Bundle args) {
        getSupportFragmentManager().beginTransaction()
                .add(R.id.content_frame, fragment).commit();
        getSupportFragmentManager().executePendingTransactions();
    }

    @Override
    public void onFragmentRemove(Fragment fragment) {
        getSupportFragmentManager().beginTransaction().remove(fragment).commit();
        getSupportFragmentManager().executePendingTransactions();
    }

    @Override
    public void onFragmentReplace(Fragment fragment, Bundle args) {
        fragment.setArguments(args);
        getSupportFragmentManager().beginTransaction().replace(R.id.content_frame, fragment).commit();
        getSupportFragmentManager().executePendingTransactions();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (data == null)
            return;

        /* Fragment fragment = (Fragment) getSupportFragmentManager().findFragmentByTag("Invoice List");
         fragment.onActivityResult(requestCode, resultCode, data);*/

        List<Fragment> fragments = getSupportFragmentManager().getFragments();
        if (fragments != null) {
            for (Fragment fragment : fragments) {
                if (fragment != null)
                    fragment.onActivityResult(requestCode, resultCode, data);
            }
        }


    }

    public String getActiveFragment() {
        if (getSupportFragmentManager().getBackStackEntryCount() == 0) {
            return null;
        }
        String tag = getSupportFragmentManager().getBackStackEntryAt(getSupportFragmentManager().getBackStackEntryCount() - 1).getName();
        Log.e("Tag", tag);
        return tag;
    }

    @Override
    public void onBackPressed() {


        if (getSupportFragmentManager().getBackStackEntryCount() < 1) {
            if (exit) {
                finish(); // finish activity
            } else {
                Toast.makeText(this, "Press Back again to Exit.",
                        Toast.LENGTH_SHORT).show();
                exit = true;
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        exit = false;
                    }
                }, 3 * 1000);

            }
        } else {
            getSupportFragmentManager().popBackStack();
            getSupportFragmentManager().executePendingTransactions();
        }

    }
}