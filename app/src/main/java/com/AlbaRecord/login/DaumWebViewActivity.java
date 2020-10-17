package com.AlbaRecord.login;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.webkit.JavascriptInterface;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.AlbaRecord.Employ.EmployeeMypageActivity;
import com.AlbaRecord.R;

public class DaumWebViewActivity extends AppCompatActivity {

    private WebView daum_webView;
    Dialog loadingDialog;
    private TextView result;
    private Handler handler;
    String flag;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_daum_web_view);
        result = (TextView) findViewById(R.id.result);
        flag=getIntent().getStringExtra("flag");


        // WebView 초기화
        init_webView();


        // 핸들러를 통한 JavaScript 이벤트 반응
        handler = new Handler();

    }


    public void init_webView() {
        // WebView 설정
        daum_webView = (WebView) findViewById(R.id.webView);
        daum_webView.setWebChromeClient(new WebChromeClient());

        // JavaScript 허용

        daum_webView.getSettings().setJavaScriptEnabled(true);

        // JavaScript의 window.open 허용

        daum_webView.getSettings().setJavaScriptCanOpenWindowsAutomatically(true);


        // JavaScript이벤트에 대응할 함수를 정의 한 클래스를 붙여줌

        daum_webView.addJavascriptInterface(new AndroidBridge(), "testApp");

        /* 2019.03.21 sjwiq200 Cross App Scripting 대비 */
        daum_webView.getSettings().setDatabaseEnabled(false);
        daum_webView.getSettings().setAllowFileAccess(false);
        daum_webView.getSettings().setDomStorageEnabled(false);
        daum_webView.getSettings().setAppCacheEnabled(false);

        // web client 를 chrome 으로 설정

        daum_webView.setWebChromeClient(new WebChromeClient());


        // webview url load.


        daum_webView.loadUrl("https://IcyCompatiblePlane.seongyeolyang7.repl.co");

    }

    private class AndroidBridge extends WebViewClient {

        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
//            if (!url.startsWith(BuildConfig.BASEURL)) {
//                url = BuildConfig.BASEURL + "postCode.html";
//            }
            view.loadUrl(url);
            return true;
        }

        @JavascriptInterface
        public void setAddress(final String arg1, final String arg2, final String arg3) {

            handler.post(new Runnable() {

                @Override

                public void run() {
                    result.setText(String.format("(%s) %s / %s", arg1, arg2, arg3));

                    //Log.e("test address : "," 1번주소 : " +  arg1 +  " ,2번주소 :  " + arg2 + " ,3번주소 : " + arg3);

                    // WebView를 재사용하기위해 초기화를 해줍시다.

                   // init_webView();
                    finish();
                    if(flag.equals("사장")){
                        Intent intent=new Intent(DaumWebViewActivity.this,BossSignupActivity.class);
                        //intent.putExtra("주소","("+arg1+")"+arg2+arg3);
                        intent.putExtra("주소",arg2+arg3);
                        startActivity(intent);
                    }else if(flag.equals("직원")){
                        Intent intent=new Intent(DaumWebViewActivity.this,EmployeeSignupActivity.class);
                        //intent.putExtra("주소","("+arg1+")"+arg2+arg3);
                        intent.putExtra("주소",arg2+arg3);
                        startActivity(intent);
                    }else if(flag.equals("직원MyPage")){
                        Intent intent=new Intent(DaumWebViewActivity.this, EmployeeMypageActivity.class);
                        //intent.putExtra("주소","("+arg1+")"+arg2+arg3);
                        intent.putExtra("주소",arg2+arg3);
                        startActivity(intent);
                    }



                }

            });

        }

    }

}