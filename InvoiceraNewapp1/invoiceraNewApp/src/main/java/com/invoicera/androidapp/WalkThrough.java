package com.invoicera.androidapp;

import java.util.ArrayList;
import java.util.HashMap;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import com.invoicera.GlobalData.Constant;
import com.invoicera.GlobalData.Constant.SERVICE_TYPE;
import com.invoicera.ViewPagerAdpter.WalkthroufgPagerAdpter;
import com.invoicera.ViewPagerFragment.WalkthroufgPagerFragment;
import com.invoicera.InterFace.WebApiResult;
import com.invoicera.Webservices.WebRequest;
import com.viewpagerindicator.CirclePageIndicator;

public class WalkThrough extends FragmentActivity implements WebApiResult {
    static String FRAGMENT_POS = "fragment_pos";

    ViewPager pager;
    Context context;
    SERVICE_TYPE type;
    WebRequest request;
    Button nextBtn, skip;
    int current_possition = 0;
    Global global;
    WalkthroufgPagerAdpter adpter;
    Spinner time_zone, currency, date_format, financial_year, comp_profession;
    public static String contact_name, comp_size, address, country_name, phone,
            prefix, nxt_number, terms, note;
    public static ArrayList<HashMap<String, String>> companyProfessionList,
            Financial_year, timeZoneList, dateFormatList, currencyList;

    @Override
    protected void onCreate(Bundle arg0) {
        super.onCreate(arg0);
        setContentView(R.layout.walkthrough);
        pager = (ViewPager) findViewById(R.id.pager2);
        CirclePageIndicator indicator = (CirclePageIndicator) findViewById(R.id.invoicedescription_indicator2);
        adpter = new WalkthroufgPagerAdpter(getSupportFragmentManager());
        pager.setAdapter(adpter);
        adpter.notifyDataSetChanged();
        context = this;
        companyProfessionList = new ArrayList<HashMap<String, String>>();
        timeZoneList = new ArrayList<HashMap<String, String>>();
        Financial_year = new ArrayList<HashMap<String, String>>();
        dateFormatList = new ArrayList<HashMap<String, String>>();
        currencyList = new ArrayList<HashMap<String, String>>();
        nextBtn = (Button) findViewById(R.id.next);
        skip = (Button) findViewById(R.id.skip);
        WalkthroufgPagerFragment testFragment;
        Bundle arguments;

        for (int i = 0; i < 4; i++) {
            testFragment = new WalkthroufgPagerFragment();
            arguments = new Bundle();
            arguments.putInt(FRAGMENT_POS, i);
            testFragment.setArguments(arguments);
            adpter.add(testFragment);
        }
        indicator.setViewPager(pager);

        indicator.setOnPageChangeListener(new OnPageChangeListener() {

            @Override
            public void onPageSelected(int position) {
                current_possition = position;
            }

            @Override
            public void onPageScrolled(int arg0, float arg1, int arg2) {

            }

            @Override
            public void onPageScrollStateChanged(int arg0) {

            }

        });
        nextBtn.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {

                current_possition++;
                pager.setCurrentItem(current_possition);
                System.out.println("Look Postion:" + current_possition);

                switch (current_possition) {
                    case 4:
                        Intent i = new Intent(WalkThrough.this, Home.class);
                        startActivity(i);
                        View view4 = adpter.fragmentList.get(3).getView();
                        prefix = ((EditText) view4.findViewById(R.id.prefix))
                                .getText().toString();
                        nxt_number = ((EditText) view4
                                .findViewById(R.id.next_number)).getText()
                                .toString();
                        note = ((EditText) view4.findViewById(R.id.note)).getText()
                                .toString();
                        terms = ((EditText) view4.findViewById(R.id.t_d)).getText()
                                .toString();



                        try {
                            sendData();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                        break;
                    case 3:
                        View view3 = adpter.fragmentList.get(2).getView();
                        time_zone = ((Spinner) view3
                                .findViewById(R.id.time_zone_customspinner));
                        comp_profession = ((Spinner) view3
                                .findViewById(R.id.company_profession_customspinner));
                        date_format = ((Spinner) view3
                                .findViewById(R.id.date_format_customspinner));
                        financial_year = ((Spinner) view3
                                .findViewById(R.id.finacialyear_customspinner));
                        currency = ((Spinner) view3
                                .findViewById(R.id.Currency_customspinner));

                        break;
                    case 2:
                        View view2 = adpter.fragmentList.get(1).getView();
                        contact_name = ((EditText) view2.findViewById(R.id.contact_name_frag)).getText().toString();
                        comp_size = ((EditText) view2.findViewById(R.id.comp_size_frag)).getText().toString();
                        address = ((EditText) view2.findViewById(R.id.add_frag)).getText().toString();
                        phone = ((EditText) view2.findViewById(R.id.ph_frag)).getText().toString();
                        country_name = ((EditText) view2.findViewById(R.id.cu_name_frag)).getText().toString();
                        break;
                    default:
                        break;
                }
            }
        });
        pager.setOffscreenPageLimit(adpter.getCount());
    }

    private void sendData() throws JSONException {
        JSONObject obj = new JSONObject();
        obj.put(Constant.KEY_METHOD, "walkThrough");
        obj.put(Constant.KEY_CONTACT_NAME, contact_name);
        obj.put(Constant.KEY_COMPANY_SIZE, comp_size);
        obj.put(Constant.KEY_ADDRESS, address);
        obj.put(Constant.KEY_COUNTRY_NAME, country_name);
        obj.put(Constant.KEY_PHONE, phone);
        obj.put(Constant.KEY_PREFIX, prefix);
        obj.put(Constant.KEY_NEXT_NUMBER, nxt_number);
        obj.put(Constant.KEY_NOTE, note);
        obj.put(Constant.KEY_TERMS, terms);
        obj.put(Constant.KEY_TIME_ZONE_SEND, "117");
        obj.put(Constant.KEY_COMPANY_TYPE, prefix);
        obj.put(Constant.KEY_NEXT_NUMBER, nxt_number);
        obj.put(Constant.KEY_NOTE, note);
        obj.put(Constant.KEY_TERMS, terms);
        WebRequest request = new WebRequest(this, obj, Constant.invoicelistURL, SERVICE_TYPE.SEND_DATA,Constant.token, this, true);
        request.execute();
    }

    private void getData() throws JSONException {
        JSONObject obj = new JSONObject();
        obj.put(Constant.KEY_METHOD, Constant.WALKTHROUGH);
        WebRequest request = new WebRequest(this, obj, Constant.invoicelistURL, SERVICE_TYPE.GET_DATA,Constant.token, this, true);
        request.execute();
    }

    @Override
    public void getWebResult(SERVICE_TYPE type, JSONObject result) {
        switch (type) {
            case GET_DATA:
                try {

                    JSONObject walkthrough = result.getJSONObject("walkThrough");

                    HashMap<String, String> map;

                    // Time Zone
                    for (int i = 0; i < walkthrough.getJSONArray("TimeZone")
                            .length() + 1; i++) {
                        map = new HashMap<String, String>();

                        if (i == 0)
                            map.put(Constant.KEY_NAME, "Select Time Zone");

                        else
                            map.put(Constant.KEY_NAME,
                                    walkthrough.getJSONArray("TimeZone")
                                            .getJSONObject(i - 1)
                                            .getString(Constant.KEY_TIME_ZONE));
                        timeZoneList.add(map);
                    }

                    // Company Profession
                    JSONArray sham = walkthrough.getJSONArray("CompanyProfession");
                    for (int i = 0; i < walkthrough.getJSONArray("CompanyProfession").length() + 1; i++) {
                        map = new HashMap<String, String>();
                        if (i == 0)
                            map.put(Constant.KEY_NAME, "Select Company Profession");
                        else
                            map.put(Constant.KEY_NAME,
                                    walkthrough.getJSONArray("CompanyProfession")
                                            .getJSONObject(i - 1)
                                            .getString(Constant.KEY_COMPANY_TYPE));
                        companyProfessionList.add(map);
                    }

                    // Date Format
                    for (int i = 0; i < walkthrough.getJSONArray("DateFormat")
                            .length() + 1; i++) {
                        map = new HashMap<String, String>();
                        if (i == 0)
                            map.put(Constant.KEY_NAME, "Select Date");
                        else
                            map.put(Constant.KEY_NAME,
                                    walkthrough.getJSONArray("DateFormat")
                                            .getJSONObject(i - 1)
                                            .getString(Constant.KEY_DATE_VALUE));
                        dateFormatList.add(map);
                    }
                    // Currency
                    for (int i = 0; i < walkthrough.getJSONArray("CurrencyList")
                            .length() + 1; i++) {
                        map = new HashMap<String, String>();
                        if (i == 0)
                            map.put(Constant.KEY_NAME, "Select Currency");
                        else
                            map.put(Constant.KEY_NAME,
                                    walkthrough.getJSONArray("CurrencyList")
                                            .getJSONObject(i - 1)
                                            .getString(Constant.KEY_CURRENCY_NAME));
                        currencyList.add(map);
                    }
                    // Financial Year
                    for (int i = 0; i < walkthrough.getJSONArray("Financial")
                            .length() + 1; i++) {
                        map = new HashMap<String, String>();
                        if (i == 0)
                            map.put(Constant.KEY_NAME, "Select Month");
                        else
                            map.put(Constant.KEY_NAME,
                                    walkthrough.getJSONArray("Financial")
                                            .getJSONObject(i - 1)
                                            .getString(Constant.KEY_YEAR));
                        Financial_year.add(map);

                    }
                    adpter.notifyDataSetChanged();
                    ((WalkthroufgPagerFragment)adpter.getItem(2)).getView().invalidate();

                } catch (JSONException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
		/*		WalkthroufgPagerFragment WalkthroufgPagerFragment;
				adpter.fragmentList=new ArrayList<WalkthroufgPagerFragment>();
				Bundle arguments;
				for (int i = 0; i < 4; i++) {
					WalkthroufgPagerFragment = new WalkthroufgPagerFragment();
					arguments = new Bundle();
					arguments.putInt(FRAGMENT_POS, i);
					WalkthroufgPagerFragment.setArguments(arguments);
					adpter.add(WalkthroufgPagerFragment);
				}*/
                adpter.notifyDataSetChanged();

                break;
            case SEND_DATA:
                System.out.println("+++++++++++SEND DATA++++++" + result);

                break;

            default:
                break;
        }

    }

}