package com.example.soehtet.myapplication.wshelpers;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ActivityManager;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.util.Base64;
import android.util.Log;

import com.example.soehtet.myapplication.MyApplication;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.MySSLSocketFactory;
import com.loopj.android.http.PersistentCookieStore;
import com.loopj.android.http.RequestParams;

import org.apache.http.Header;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.cookie.Cookie;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.net.UnknownHostException;
import java.security.KeyStore;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TimeZone;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

public class WSHelper {

    public String SERVER_PREFIX = "http://127.0.0.1:5001";//server url will be init at WSHelper constructor

    public String REGISTER = "/register";
    public String LOIGIN = "/login";

    private String tag = null;
    private String url = null;
    private WSListener callback = null;
    private ProgressDialog dialog = null;
    private JSONArray result = null;
    private WSHelperState state = WSHelperState.NEW;

    private static PersistentCookieStore cookieStore = new PersistentCookieStore(MyApplication.getInstance());

    public static final String CLIENT_ID = "ww-android";

    public static final String WS_LOGIN_URL = "login_device_adfs/";
    private static final String WS_LOGOUT_URL = "logout_device/";
    public static final String WS_LOGIN_DEVICE_CHECK = "login_device_check/";


    private enum WSHelperState {
        NEW, RUNNING, COMPLETE
    }

    public WSHelper(String tag) {
        this.tag = tag;
    }


    private static String getSoftwareVersion() {
        try {
            PackageInfo packageInfo = MyApplication
                    .getInstance()
                    .getPackageManager()
                    .getPackageInfo(MyApplication.getInstance().getPackageName(),
                            0);
            return packageInfo.versionName;
        } catch (PackageManager.NameNotFoundException e) {
            return "0.0";
        }
    }

    public void resgister(WSListener callback,String username,String password, String email,String img, String phoneno, boolean phstatus) throws FileNotFoundException{
        RequestParams rp = new RequestParams();
        rp.put("usrname", username);
        rp.put("password", password);
        rp.put("email", email);
        rp.put("pic",img);
        rp.put("phoneno",phoneno);
        rp.put("phstatus",phstatus);

        sendRequest(String.format("%s%s", SERVER_PREFIX, REGISTER), HttpPost.METHOD_NAME,rp, callback, true, false);
    }



    String errorResponse;

    private void sendRequest(String url, final String methodName,
                             RequestParams params, final WSListener callback, boolean showDialog,
                             final boolean useCache) {


        try {
            this.url = url;
            this.callback = callback;
            Log.e("WSHelper", "sendRequest url is " + url);


            // resume activity when needed
            if (state == WSHelperState.RUNNING) {
                if (showDialog)
                    showDialog(callback);
                return;
            } else if (state == WSHelperState.COMPLETE) {
                handleCallback(callback, url, result, methodName);
                return;
            }

            // retain the current WSHelper
            FragmentManager fm = getFragmentManagerFromCallback(callback);
            if (fm != null) {
                WSFragment wsf = new WSFragment();
                wsf.ws = this;
                fm.beginTransaction().add(wsf, "WSFragment_" + tag).commitAllowingStateLoss();
            }

            this.state = WSHelperState.RUNNING;

            if (useCache) {
                // JSONArray cachedResult = (JSONArray)
                // cache.getCachedData(url);
                // // Log.v("WSHelper", "cache: " + cachedResult);
                // if (cachedResult != null) {
                // state = WSHelperState.COMPLETE;
                // result = cachedResult;
                // handleCallback(callback, url, cachedResult);
                // return;
                // }
            }

            if (showDialog)
                showDialog(callback);
           // Log.d(WSHelper.class.getName(), "Calling " + url);

//            if (Helpers.checkNetworkConnection(callback.getContext())) {
//                dismissDialog();
//                errorResponse = "No internet connection";
//                callback.onWSError(errorResponse, url);
//                return;
//            }

            final WWAsyncHttpClient request = createRequest(url);
            request.addHeader("Content-Type", "application/json");
            AsyncHttpResponseHandler responseHandler = new AsyncHttpResponseHandler() {
                @Override
                public void onSuccess(int statusCode, cz.msebera.android.httpclient.Header[] headers, byte[] responseBody) {
                    dismissDialog();
                    state = WSHelperState.COMPLETE;

                    handleCallback(WSHelper.this.callback,
                            WSHelper.this.url, null, methodName);
                }

                @Override
                public void onFailure(int statusCode, cz.msebera.android.httpclient.Header[] headers, byte[] responseBody, Throwable error) {
                    dismissDialog();
                    state = WSHelperState.COMPLETE;

                }
            };





            Log.d(WSHelper.class.getName(), methodName);
            if (HttpPost.METHOD_NAME.equals(methodName)) {
                Log.d(WSHelper.class.getName(), "POST");
                // add csrf cookie
                for (cz.msebera.android.httpclient.cookie.Cookie cookie : cookieStore.getCookies()) {
                    if ("csrftoken".equals(cookie.getName())) {
                        if (params == null)
                            params = new RequestParams();
                        params.add("csrfmiddlewaretoken", cookie.getValue());
                    }
                }
                request.post(url, params, responseHandler);
            } else if (HttpGet.METHOD_NAME.equals(methodName)) {
                Log.d(WSHelper.class.getName(), "GET");
                for (cz.msebera.android.httpclient.cookie.Cookie cookie : cookieStore.getCookies()) {
                    if ("csrftoken".equals(cookie.getName())) {
                        if (params == null)
                            params = new RequestParams();
                        params.add("csrfmiddlewaretoken", cookie.getValue());
                    }
                }
                Log.d("sending request", url);
                request.get(url, params, responseHandler);
            } else if (HttpDelete.METHOD_NAME.equals(methodName)) {
                Log.d(WSHelper.class.getName(), "DELETE");
                for (cz.msebera.android.httpclient.cookie.Cookie cookie : cookieStore.getCookies()) {
                    if ("csrftoken".equals(cookie.getName())) {
                        if (params == null)
                            params = new RequestParams();
                        // params.add("csrfmiddlewaretoken", cookie.getValue()); //ktl

                        Log.i("WSHelper", "X-CSRFToken");
                    }
                }
                request.delete(getContextFromCallback(callback), url, null,
                        params, responseHandler);
            } else if (HttpPut.METHOD_NAME.equals(methodName)) {
                Log.d(WSHelper.class.getName(), "PUT");
                for (cz.msebera.android.httpclient.cookie.Cookie cookie : cookieStore.getCookies()) {
                    if ("csrftoken".equals(cookie.getName())) {
                        if (params == null)
                            params = new RequestParams();
                        params.add("csrfmiddlewaretoken", cookie.getValue());
                    }
                }

                request.put(url, params, responseHandler);
            } else {
                throw new Exception("Unsupported HTTP method.");
            }
        } catch (Exception e) {
            state = WSHelperState.NEW;
            result = null;
            Log.d("CAGX-SEND_REQUEST", e.getMessage());
//            if(e.getMessage().equals("Synchronous ResponseHandler used in AsyncHttpClient. You should create your response handler in a looper thread or use SyncHttpClient instead."))

            Log.d("CAGX-ERROR","error is " + errorResponse);
            dismissDialog();
            e.printStackTrace();
            callback.onWSError(errorResponse, url);
        }
    }

//    public WWAsyncHttpClient createRequest(String url) {
//        WWAsyncHttpClient httpClient = new WWAsyncHttpClient();
//        httpClient.setUserAgent(getUserAgent());
//        httpClient.setCookieStore(cookieStore);
//
//// add referer to header to allow ssl redirection to work properly in django
//        httpClient.addHeader("Referer", SERVER_PREFIX);
//        try {
//            KeyStore trustStore = KeyStore.getInstance(KeyStore.getDefaultType());
//            trustStore.load(null, null);
//            SSLSocketFactory sf = new TrustAllSSLSocketFactory(trustStore);
//            sf.setHostnameVerifier(MySSLSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER);
//            httpClient.setSSLSocketFactory(sf);
//        } catch (Exception e) {
//            Log.d(WSHelper.class.getName(), "SSL/keystore error.", e);
//        }
//
//        return httpClient;
//    }

    private WWAsyncHttpClient createRequest(String url) {
        Log.d(WSHelper.class.getName(), "Create request");
        WWAsyncHttpClient httpClient = new WWAsyncHttpClient();
        httpClient.setTimeout(100 * 1000);
        httpClient.setCookieStore(cookieStore);

        Log.d(WSHelper.class.getName(), "Return request");
        return httpClient;
    }

    private void handleCallback(WSListener callbackActivity, String url,
                                JSONArray response, String methodName) {
        Log.d("WSHelper", "Url is " + url + " and response:" + response);

        if (callbackActivity == null || url == null) {
            Log.d(WSHelper.class.getName(), "no callback aa");
            return;
        }
        if (url.contains(REGISTER)){
            callback.onRegisterSuccess(response.toString());
        }
    }

    private static String hmac(String s, String keyString) {
        try {
            SecretKeySpec key = new SecretKeySpec(
                    (keyString).getBytes("UTF-8"), "HmacSHA1");
            Mac mac = Mac.getInstance("HmacSHA1");
            mac.init(key);

            byte[] bytes = mac.doFinal(s.getBytes("UTF-8"));

            return Base64.encodeToString(bytes, Base64.DEFAULT).trim();
        } catch (Exception e) {
            return "";
        }
    }

    public boolean hasStarted() {
        return state != WSHelperState.NEW;
    }

    public boolean isComplete() {
        return state == WSHelperState.COMPLETE;
    }

    private void showDialog(WSListener callback) {
        Context act = getContextFromCallback(callback);
        if (act != null && act instanceof Activity
                && !((Activity) act).isFinishing()) {
            Log.e("Dialog", "Show dialog" + (act));
            if (dialog != null) return;
            dialog = ProgressDialog.show(act, "",
                    "Loading...", true);
        }
    }

    private void dismissDialog() {
        if(callback != null){
            Context act = getContextFromCallback(callback);
            if (act != null && act instanceof Activity
                    && !((Activity) act).isFinishing()) {
                if (dialog != null
                        && dialog.isShowing()) {
                    dialog.dismiss();
                    dialog = null;
                }
            }
        }
        else {

            if (dialog != null
                && dialog.isShowing()) {
                dialog.dismiss();
                dialog = null;
            }
        }

    }

    public boolean isRunning(Context ctx) {
        ActivityManager activityManager = (ActivityManager) ctx.getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningTaskInfo> tasks = activityManager.getRunningTasks(Integer.MAX_VALUE);

        for (ActivityManager.RunningTaskInfo task : tasks) {
            if (ctx.getPackageName().equalsIgnoreCase(task.baseActivity.getPackageName()))
                return true;
        }

        return false;
    }

    private static FragmentManager getFragmentManagerFromCallback(
            WSListener callback) {
        Context ctx = callback.getContext();
        if (ctx == null) return null;

        if (ctx instanceof FragmentActivity) {
            return ((FragmentActivity) callback.getContext()).getSupportFragmentManager();
        }

        return null;
    }

    private static Context getContextFromCallback(WSListener callback) {
        return callback.getContext();
    }

    private static String signUrl(String url, String timestamp) {
        if (url.indexOf("?") >= 0)
            url = url + "&client_id=" + CLIENT_ID + "&ts=" + timestamp;
        else
            url = url + "?client_id=" + CLIENT_ID + "&ts=" + timestamp;

        return hmac(url, "ag8w6EMSUvr7Lw68mnBGXArpDEKuw8NmHGrPKn7qWmeajDk2zU");
    }

    @SuppressLint("ValidFragment")
    private class WSFragment extends Fragment {
        WSHelper ws = WSHelper.this;

        private void WSHelper() {
        }

        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setRetainInstance(true);
        }

        @Override
        public void onDetach() {
            super.onDetach();
            suspendActivity();
        }
    }

    private void suspendActivity() {
        this.callback = null;
    }

}
