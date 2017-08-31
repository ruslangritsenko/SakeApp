package com.sakewiz.android.common;

import android.app.Activity;
import android.content.Context;
import android.content.CursorLoader;
import android.database.Cursor;
import android.graphics.Point;
import android.graphics.Typeface;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.provider.MediaStore;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.Display;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;

import com.sakewiz.android.BaseApplication;
import com.sakewiz.android.common.constants.ApplicationConstants;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

/**
 * Created by dilshan_e on 29/05/2017.
 */
public class CommonUtils {

    private static CommonUtils instance = null;
    private static String pinCode = "";
    private static boolean isPinCodeSet = false;
    private static final int REQUEST_CODE_ENABLE = 11;

    private static final int SECOND_MILLIS = 1000;
    private static final int MINUTE_MILLIS = 60 * SECOND_MILLIS;
    private static final int HOUR_MILLIS = 60 * MINUTE_MILLIS;
    private static final int DAY_MILLIS = 24 * HOUR_MILLIS;

    private CommonUtils() {}

    public static CommonUtils getInstance() {
        if (instance == null) instance = new CommonUtils();
        return instance;
    }

    public boolean isNetworkConnected() {
        ConnectivityManager cm = (ConnectivityManager) BaseApplication.getBaseApplication().getSystemService(Context.CONNECTIVITY_SERVICE);
        return cm.getActiveNetworkInfo() != null;
    }

    public void hideKeyboard(Activity activity) {
        InputMethodManager imm = (InputMethodManager) activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
        //Find the currently focused view, so we can grab the correct window token from it.
        View view = activity.getCurrentFocus();
        //If no view currently has focus, create a new one, just so we can grab a window token from it
        if (view == null) {
            view = new View(activity);
        }
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    public SimpleDateFormat getDateTimeFormatter() {
        return new SimpleDateFormat(ApplicationConstants.DEFAULT_DATE_AND_TIME_FORMAT, Locale.getDefault());
    }

    public Date getCurrentDateAndTime() throws ParseException {
        SimpleDateFormat fmtDateAndTime = getDateTimeFormatter();
        return fmtDateAndTime.parse(fmtDateAndTime.format(Calendar.getInstance().getTime()));
    }

    public String getCurrentDateAndTimeString() {
        SimpleDateFormat fmtDateAndTime = getDateTimeFormatter();
        return fmtDateAndTime.format(Calendar.getInstance().getTime());
    }

    public Date formatDate(String date) throws ParseException {
        SimpleDateFormat fmtDateAndTime = getDateTimeFormatter();
        return fmtDateAndTime.parse(date);
    }

    public Date formatDate(String date, SimpleDateFormat simpleDateFormat) throws ParseException {
        return simpleDateFormat.parse(date);
    }

    public int[] calculateScreenDimens() {
        WindowManager windowManager = (WindowManager) BaseApplication.getBaseApplication().getSystemService(Context.WINDOW_SERVICE);
        Display display = windowManager.getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);

        int dimension[] = new int[2];
        dimension[0] = size.x;  //width
        dimension[1] = size.y;  //height

        return dimension;
    }

    public float dpToPx(float valueInDp) {
        try {
            DisplayMetrics metrics = BaseApplication.getBaseApplication().getResources().getDisplayMetrics();
            return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, valueInDp, metrics);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return 0.0f;
    }

    public Typeface getFont(Context mContext, String font) {
        switch (font) {
            case ApplicationConstants.FONT_SF_UI_TEXT_REGULAR:
                return Typeface.createFromAsset(mContext.getAssets(), "font/SF-UI-Text-Regular.otf");
            case ApplicationConstants.FONT_SF_UI_DISPLAY_REGULAR:
                return Typeface.createFromAsset(mContext.getAssets(), "font/SF-UI-Display-Regular.otf");
            case ApplicationConstants.FONT_ROBOTO_SLAB_BOLD:
                return Typeface.createFromAsset(mContext.getAssets(), "font/RobotoSlab-Bold.ttf");
            case ApplicationConstants.FONT_ROBOTO_SLAB_REGULAR:
                return Typeface.createFromAsset(mContext.getAssets(), "font/RobotoSlab-Regular.ttf");
            default:
                return null;
        }
    }

    public String getPathFromUri(Context context, Uri uri) {
        Cursor cursor = null;
        try {
        String[] data = {MediaStore.Images.Media.DATA};
        CursorLoader loader = new CursorLoader(context, uri, data, null, null, null);
        cursor = loader.loadInBackground();
        int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        cursor.moveToFirst();
        return cursor.getString(column_index);
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
    }

    public static String formatDateFromString(String inputFormat, String outputFormat, String inputDate) {

        Date parsed = null;
        String outputDate = "";

        SimpleDateFormat df_input = new SimpleDateFormat(inputFormat, Locale.getDefault());
        SimpleDateFormat df_output = new SimpleDateFormat(outputFormat, Locale.getDefault());

        try {
            parsed = df_input.parse(inputDate);
            outputDate = df_output.format(parsed);
        } catch (ParseException e) {}
        return outputDate;
    }


    private Date getCurrentUTCTime() throws ParseException {
        String fromTimeZone = "UTC";
        SimpleDateFormat dateFormatGmt = getDateTimeFormatter();
        dateFormatGmt.setTimeZone(TimeZone.getTimeZone(fromTimeZone));
        return formatDate(dateFormatGmt.format(new Date()));
    }

    public String getFormattedDate(long milliSeconds, String dateFormat)
    {
        // Create a DateFormatter object for displaying date in specified format.
        SimpleDateFormat formatter = new SimpleDateFormat(dateFormat);

        // Create a calendar object that will convert the date and time value in milliseconds to date.
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(milliSeconds);
        return formatter.format(calendar.getTime());
    }

    public boolean isEmptyString(String object) {
        return (object == null || object.trim().equals("null") || object.trim().length() <= 0);
    }

    public String getColoredSpanned(String text, String color) {
        String input = "<font color=" + color + ">" + text + "</font>";
        return input;
    }
}
