package com.softprodigy.librarymsspro;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.content.Context;
import android.view.Gravity;
import android.widget.Toast;

public class Validator {
	public static void displyToast(Context c, String str) {
		Toast msg = Toast.makeText(c, str, Toast.LENGTH_SHORT);
		msg.setGravity(Gravity.CENTER, msg.getXOffset() / 2,
				msg.getYOffset() / 2);
		msg.show();
	}

	public static boolean isEmailValid(Context context,String email,boolean showValidationToast) {
		String regExpn = "^(([\\w-]+\\.)+[\\w-]+|([a-zA-Z]{1}|[\\w-]{2,}))@"
				+ "((([0-1]?[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\.([0-1]?"
				+ "[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\."
				+ "([0-1]?[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\.([0-1]?"
				+ "[0-9]{1,2}|25[0-5]|2[0-4][0-9])){1}|"
				+ "([a-zA-Z]+[\\w-]+\\.)+[a-zA-Z]{2,4})$";

		CharSequence inputStr = email;

		Pattern pattern = Pattern.compile(regExpn, Pattern.CASE_INSENSITIVE);
		Matcher matcher = pattern.matcher(inputStr);

		if (matcher.matches())
			return true;
		else
		{
			if(showValidationToast)
			{
				Toast.makeText(context, getStringFromResource(R.string.Valid_Email_Error,context), Toast.LENGTH_LONG).show();
			}
			return false;
		}
	}
	
	
	// check blank or null
	
	public static boolean checkBlankOrNull(Context context,String checkString,boolean showValidationToast) {
		if(checkString==null || checkString.length()<=0)
		{
			if(showValidationToast)
			{
				Toast.makeText(context, getStringFromResource(R.string.mobileno_blank,context), Toast.LENGTH_LONG).show();
			}
			return false;
		}else
		{
			return true;
		}
	}
	
	
	
	
	
	
	
	
	
	static String getStringFromResource(int id,Context context)
	{
		try{
		return context.getResources().getString(id);
		}catch(Exception e)
		{
			return "";
		}
	}
}
