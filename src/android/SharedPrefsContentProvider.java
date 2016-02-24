package com.example.plugin;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.SharedPreferences;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.MatrixCursor;
import android.database.SQLException;
import android.net.Uri;

public class SharedPrefsContentProvider extends ContentProvider {
    public static final Uri CONTENT_URI = Uri.parse("content://com.Android.provider.Afaria/seeddata");
    public static final String KEY_PACKAGENAME = "packagename";
    public static final String KEY_SEEDDATAURL = "seeddataurl";
    public static final String APP_SERVER_IP_KEY = "appserverip";
    public static final String APP_SERVER_IP_VALUE = "appserveripvalue";
    public static final int SEEDDATACOLUMN = 0;
    public static final int APPSERVERIPCOLUMN = 0;
    private static final UriMatcher uriMatcher = new UriMatcher(-1);

    static
    {
        uriMatcher.addURI("com.Android.provider.Afaria", "seeddata/*", 1);
    }

    public int delete(Uri arg0, String arg1, String[] arg2)
    {
        return 0;
    }

    public String getType(Uri uri)
    {
        switch (uriMatcher.match(uri))
        {
            case 1:
                return "vnd.android.cursor.item/vnd.Android.seeddata";
            case 2:
                return "vnd.android.cursor.item/vnd.Android.appserverip";
        }
        throw new IllegalArgumentException("Unsupported URI: " + uri);
    }

    public Uri insert(Uri uri, ContentValues initialValues)
    {
        SharedPreferences settings = getContext().getSharedPreferences("AppSeedDataPreferences", 1);
        if (null != settings)
        {
            SharedPreferences.Editor editor = settings.edit();
            if (null != editor)
            {
                String packagename = initialValues.getAsString("packagename");
                String seeddataurl = initialValues.getAsString("seeddataurl");
                if ((null != packagename) && (null != seeddataurl))
                {
                    editor.putString(packagename, seeddataurl);
                    editor.commit();

                    Uri _uri = Uri.withAppendedPath(CONTENT_URI, packagename);
                    getContext().getContentResolver().notifyChange(_uri, null);

                    return _uri;
                }
                String appserveripvalue = initialValues.getAsString("appserveripvalue");
                if (null != appserveripvalue)
                {
                    editor.putString("appserverip", appserveripvalue);
                    editor.commit();

                    Uri _uri = Uri.withAppendedPath(CONTENT_URI, "appserverip");
                    getContext().getContentResolver().notifyChange(_uri, null);

                    return _uri;
                }
            }
        }
        throw new SQLException("Failed to insert row into " + uri);
    }

    public boolean onCreate()
    {
        return false;
    }

    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sort)
    {
        MatrixCursor matrixCursor = null;
        switch (uriMatcher.match(uri))
        {
            case 1:
                String keyString = (String)uri.getPathSegments().get(1);
                SharedPreferences settings = getContext().getSharedPreferences("AppSeedDataPreferences", 1);
                String value = null;
                if ((null != settings) && (null != keyString))
                {
                    value = settings.getString(keyString, null);
                    //Log.d("SharedPrefsContentProvider.query", "value: " + value);
                }
                String[] cols = { "SEEDDATACOLUMN" };
                matrixCursor = new MatrixCursor(cols, 1);
                matrixCursor.addRow(new Object[] { value });
                break;
        }
        return matrixCursor;
    }

    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs)
    {
        return 0;
    }
}