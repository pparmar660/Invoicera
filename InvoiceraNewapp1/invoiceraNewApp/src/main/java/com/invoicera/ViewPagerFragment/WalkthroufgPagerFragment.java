package com.invoicera.ViewPagerFragment;

import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;

import com.invoicera.Fragment.BaseFragment;
import com.invoicera.GlobalData.Constant;
import com.invoicera.GlobalData.Constant.SERVICE_TYPE;
import com.invoicera.GlobalData.Constant.WALK_THROUGH_SPINNERS;
import com.invoicera.SpinnerAdpter.WalkThroughSpinerAdpter;
import com.invoicera.InterFace.WebApiResult;
import com.invoicera.Webservices.WebRequest;
import com.invoicera.androidapp.R;
import com.invoicera.androidapp.WalkThrough;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
//import com.invoicera.model.WalkthroughModel;

public class WalkthroufgPagerFragment extends BaseFragment implements WebApiResult{
    int pos;
    ImageView image_frm_gallery;
    String KEY_CONTENT = "TestFragment:Content";
    static String FRAGMENT_POS = "fragment_pos";
   // WalkthroughModel WalkthroughModel;

    // ArrayList<HashMap<String, String[]>> FinancialList;
    WalkThroughSpinerAdpter timeZoneAdapter, companyAdapter, dateAdapter, currencyAdapter, yearAdapter;
    Spinner compny_profession_Spiner, time_zone_Spiner, financial_year_Spiner,
            date_format_Spiner, currency_spiner;
    EditText comp_name, comp_size, address, country_name, phone;
    String comp_Name, comp_Size, Address, country_Name, Phone;

    // private OnGetFromUserClickListener mListener;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    private void getData() throws JSONException {
        JSONObject obj = new JSONObject();
        obj.put(Constant.KEY_METHOD, Constant.WALKTHROUGH);
        WebRequest request = new WebRequest(getActivity(), obj, Constant.invoicelistURL, SERVICE_TYPE.GET_DATA,Constant.token, this, true);
        request.execute();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = null;
        pos = getArguments().getInt(FRAGMENT_POS);
        switch (pos) {
            case 0:
                view = inflater.inflate(R.layout.walkthrough_pager_1, container,
                        false);
                image_frm_gallery = (ImageView) view.findViewById(R.id.upload);
                image_frm_gallery.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        openGallery();
                    }
                });
                break;
            case 1:
                view = inflater.inflate(R.layout.walkthrough_pager_2, container, false);
                break;
            case 2:
                view = inflater.inflate(R.layout.walkthrough_pager_3, container, false);
                time_zone_Spiner = (Spinner) view.findViewById(R.id.time_zone_customspinner);
                compny_profession_Spiner = (Spinner) view.findViewById(R.id.company_profession_customspinner);
                date_format_Spiner = (Spinner) view.findViewById(R.id.date_format_customspinner);
                financial_year_Spiner = (Spinner) view.findViewById(R.id.finacialyear_customspinner);
                currency_spiner = (Spinner) view.findViewById(R.id.Currency_customspinner);
                // time zone
				/*adapter = new WalkThroughSpinerAdpter(getActivity(), WalkThrough.timeZoneList, WALK_THROUGH_SPINNERS.TIMEZONE);
				time_zone_Spiner.setAdapter(adapter);
				// time_zone_Spiner.getSelectedItemId();
				// company type
				adapter = new WalkThroughSpinerAdpter(getActivity(), WalkThrough.companyProfessionList, WALK_THROUGH_SPINNERS.COMPANY_LIST);
				compny_profession_Spiner.setAdapter(adapter);
				// date type
				adapter = new WalkThroughSpinerAdpter(getActivity(), WalkThrough.dateFormatList, WALK_THROUGH_SPINNERS.DATE_FORMAT_LIST);
				date_format_Spiner.setAdapter(adapter);
				// Currency type
				adapter = new WalkThroughSpinerAdpter(getActivity(), WalkThrough.currencyList, WALK_THROUGH_SPINNERS.CURRENCY_LIST);
				currency_spiner.setAdapter(adapter);
				// Financial year
				adapter = new WalkThroughSpinerAdpter(getActivity(), WalkThrough.Financial_year, WALK_THROUGH_SPINNERS.FINANCIAL_YEAR);
				financial_year_Spiner.setAdapter(adapter);*/
                timeZoneAdapter = new WalkThroughSpinerAdpter(getActivity(), WALK_THROUGH_SPINNERS.TIMEZONE);
                time_zone_Spiner.setAdapter(timeZoneAdapter);

                companyAdapter = new WalkThroughSpinerAdpter(getActivity(), WalkThrough.companyProfessionList, WALK_THROUGH_SPINNERS.COMPANY_LIST);
                compny_profession_Spiner.setAdapter(companyAdapter);

                dateAdapter = new WalkThroughSpinerAdpter(getActivity(), WalkThrough.dateFormatList, WALK_THROUGH_SPINNERS.DATE_FORMAT_LIST);
                date_format_Spiner.setAdapter(dateAdapter);

                currencyAdapter = new WalkThroughSpinerAdpter(getActivity(), WalkThrough.currencyList, WALK_THROUGH_SPINNERS.CURRENCY_LIST);
                currency_spiner.setAdapter(currencyAdapter);

                yearAdapter = new WalkThroughSpinerAdpter(getActivity(), WalkThrough.Financial_year, WALK_THROUGH_SPINNERS.FINANCIAL_YEAR);
                financial_year_Spiner.setAdapter(yearAdapter);

                try {
                    getData();
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                break;
            case 3:
                view = inflater.inflate(R.layout.walkthrough_pager_4, container, false);
                break;
            default:
                break;
        }

        return view;
    }

    // For Walkthrough 1

    private void openGallery() {
        if (Build.VERSION.SDK_INT < 19) {
            Intent intent = new Intent();
            intent.setType("image/*");
            intent.setAction(Intent.ACTION_GET_CONTENT);
            startActivityForResult(
                    Intent.createChooser(intent, "Select Picture"), 1);
        } else {
            Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
            intent.addCategory(Intent.CATEGORY_OPENABLE);
            intent.setType("image/*");
            startActivityForResult(intent, 2);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (data != null) {
            switch (pos) {
                case 0:
                    Uri selectedImageUri = data.getData();
                    image_frm_gallery.setImageURI(selectedImageUri);
                    break;
                default:
                    break;
            }
        }
    }

    @Override
    public void getWebResult(SERVICE_TYPE type, JSONObject result) {
        switch (type) {
            case GET_DATA:
                try {

                    JSONObject walkthrough = result.getJSONObject("walkThrough");

                    HashMap<String, String> map;

                    // Time Zone
                    for (int i = 0; i < walkthrough.getJSONArray("TimeZone").length() + 1; i++) {
                        map = new HashMap<String, String>();
                        if (i == 0)
                            map.put(Constant.KEY_NAME, "Select Time Zone");
                        else
                            map.put(Constant.KEY_NAME, walkthrough.getJSONArray("TimeZone").getJSONObject(i - 1).getString(Constant.KEY_TIME_ZONE));
                        timeZoneAdapter.add(map);
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
                        companyAdapter.add(map);
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
                        dateAdapter.add(map);
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
                        currencyAdapter.add(map);
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
                        yearAdapter.add(map);
                    }
                } catch (JSONException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
		/*		WalkthroufgPagerFragment WalkthroufgPagerFragment;
				adapter.fragmentList=new ArrayList<WalkthroufgPagerFragment>();
				Bundle arguments;
				for (int i = 0; i < 4; i++) {
					WalkthroufgPagerFragment = new WalkthroufgPagerFragment();
					arguments = new Bundle();
					arguments.putInt(FRAGMENT_POS, i);
					WalkthroufgPagerFragment.setArguments(arguments);
					adapter.add(WalkthroufgPagerFragment);
				}*/
                //adapter.notifyDataSetChanged();

                break;
            case SEND_DATA:
                System.out.println("+++++++++++SEND DATA++++++" + result);

                break;

            default:
                break;
        }
    }

}