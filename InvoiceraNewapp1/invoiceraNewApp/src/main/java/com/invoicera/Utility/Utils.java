package com.invoicera.Utility;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;

import com.invoicera.GlobalData.Constant;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;

public class Utils {

	static String message = "";
	static String title = "";

	public static String FloatToStringLimits(double pNumero) {
		// the function is call with the values Redondear(625.3f, 2)
		int limit = 2;
		BigDecimal roundfinalPrice = new BigDecimal(pNumero);
		try {
			// limit = Integer.parseInt(ConstantList.userCurrencyDecimal);
		} catch (Exception ex) {
		}
		DecimalFormatSymbols symbols = DecimalFormatSymbols.getInstance();
		symbols.setGroupingSeparator(',');

		DecimalFormat formatter = new DecimalFormat(
				"###,###,###,###,###,###.##", symbols);
		formatter.setMinimumFractionDigits(2);
		formatter.setMaximumFractionDigits(2);
		//
		try {
			roundfinalPrice = new BigDecimal(pNumero).setScale(limit,
					BigDecimal.ROUND_HALF_UP);
		} catch (Exception e) {
			e.printStackTrace();
		}
		// return df.format(roundfinalPrice);
		return formatter.format(roundfinalPrice);

	}

	public static String FloatToStringLimits(String pNumeroStr) {
		// the function is call with the values Redondear(625.3f, 2)

		double pNumero = 0.0f;

		try {
			if (!pNumeroStr.equals("")) {
				// pNumero = Float.parseFloat(pNumeroStr);

				pNumero = Double.parseDouble(pNumeroStr.replaceAll(",",""));

			}
		} catch (NumberFormatException e) {
			e.printStackTrace();
		}

		// BigDecimal roundfinalPrice = new
		// BigDecimal(pNumero).setScale(limit,BigDecimal.ROUND_HALF_UP);
		// BigDecimal value = new BigDecimal(pNumero);
		// roundfinalPrice = roundfinalPrice.setScale(pCantidadDecimales,
		// RoundingMode.HALF_EVEN); // here the value is correct (625.30)
		return FloatToStringLimits(pNumero);
	}

	public static double round(double d) {
		int decimalPlace = 0;
		try {
			decimalPlace = Integer.parseInt(Constant.userCurrencyDecimal);
			if (decimalPlace < 0) {
				throw new IllegalArgumentException();
			}
		} catch (Exception ex) {

		}

		BigDecimal bd = new BigDecimal(Double.toString(d));
		bd = bd.setScale(decimalPlace, BigDecimal.ROUND_HALF_UP);
		return bd.doubleValue();
	}

	public static double round(float d) {
		int decimalPlace = 0;
		try {
			decimalPlace = Integer.parseInt(Constant.userCurrencyDecimal);
			if (decimalPlace < 0) {
				throw new IllegalArgumentException();
			}
		} catch (Exception ex) {

		}
		BigDecimal bd = new BigDecimal(Float.toString(d));
		bd = bd.setScale(decimalPlace, BigDecimal.ROUND_HALF_UP);
		return bd.doubleValue();
	}

	public static double round(String d) {
		int decimalPlace = 0;
		try {
			decimalPlace = Integer.parseInt(Constant.userCurrencyDecimal);
			if (decimalPlace < 0) {
				throw new IllegalArgumentException();
			}

		} catch (Exception ex) {

		}
		double dou = 0.00;
		try {
			dou = Double.parseDouble(d);
		} catch (Exception ex) {
			// ex.printStackTrace();
		}
		BigDecimal bd = new BigDecimal((Double.toString(dou)));
		bd = bd.setScale(decimalPlace, BigDecimal.ROUND_HALF_UP);

		return bd.doubleValue();

	}



	private static void alertDialog(Context ctx) {
		AlertDialog alertDialog = new AlertDialog.Builder(((Activity) ctx))
				.create();
		alertDialog.setTitle(title);
		alertDialog.setMessage(message);
		alertDialog.setCancelable(false);
		alertDialog.setButton("OK", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {

			}
		});
		alertDialog.show();
	}
}
