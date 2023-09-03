package com.example.origin;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Patterns;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import java.util.ArrayList;
import java.util.Locale;

@SuppressWarnings("deprecation")
public class MainActivity extends AppCompatActivity {
    public static final int HIS_REQ_CODE = 7;
    public static final int BOOK_REQ_CODE = 8;
    Toolbar toolbar;
    ImageView home_btn,mic,cut;
    WebView webView;
    EditText editText;
    ProgressBar progressBar;
    MenuItem myItem;
    private final int mic_req_code=10;
    private  boolean desktop_mode_flag=false;
    @SuppressLint("SetJavaScriptEnabled")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        myItem=findViewById(R.id.history);
        home_btn=findViewById(R.id.home_btn);
        toolbar=findViewById(R.id.toolbar);
        webView=findViewById(R.id.webView);
        mic=findViewById(R.id.mic);
        cut=findViewById(R.id.cut);
        progressBar=findViewById(R.id.progressBar);
        setSupportActionBar(toolbar);
        editText=findViewById(R.id.search_editText);
        WebSettings webSettings= webView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webSettings.setBuiltInZoomControls(true);
        webSettings.setDisplayZoomControls(false);

        webView.setWebViewClient(new MyWebViewClient());


        webView.setWebChromeClient(new WebChromeClient(){
            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                super.onProgressChanged(view, newProgress);
                progressBar.setProgress(newProgress);
            }
        });

        webView.loadUrl("https://www.google.com");

        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if(editText.getText().toString().isEmpty()){
                    cut.setVisibility(View.INVISIBLE);
                    mic.setVisibility(View.VISIBLE);
                }else{
                    cut.setVisibility(View.VISIBLE);
                    mic.setVisibility(View.INVISIBLE);}
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        editText.setOnEditorActionListener((textView, i, keyEvent) -> i == EditorInfo.IME_ACTION_GO || i == EditorInfo.IME_ACTION_DONE);


        home_btn.setOnClickListener(view -> {
            Intent go_home_intent=new Intent(MainActivity.this,MainActivity.class);
            startActivity(go_home_intent);
            finish();
        });



        cut.setOnClickListener(view -> editText.setText(""));

        mic.setOnClickListener(view -> {
            Intent intent_mic=new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
            intent_mic.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
            intent_mic.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault());
//            editText.setClickable(false);

            startActivityForResult(intent_mic,mic_req_code);

        });
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode==RESULT_OK ){
            if(requestCode==mic_req_code && data!=null){
                ArrayList<String> get_mic_input=data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
                search_url(get_mic_input.get(0));
            }
            if(requestCode==HIS_REQ_CODE && data!=null){
                String getHistory=data.getStringExtra("hisUrl");
                search_url(getHistory);
            }
            if(requestCode==BOOK_REQ_CODE && data!=null){
                String getBookmark=data.getStringExtra("BookUrl");
                search_url(getBookmark);
            }
        }
    }

    public void search_url(String url){
        boolean is_url= Patterns.WEB_URL.matcher(url).matches();
        if(is_url){
            webView.loadUrl(url);
        }
        else{
            webView.loadUrl("https://google.com/search?q="+url);
        }

    }











    class MyWebViewClient extends WebViewClient{
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
            return super.shouldOverrideUrlLoading(view, request);
        }

        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            DbHelper dbHelper=new DbHelper(MainActivity.this);
            dbHelper.AddUrl(url);
            super.onPageStarted(view, url, favicon);

            //disable focus from editText and remove keyboard on go
            InputMethodManager go_manager=(InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
            go_manager.hideSoftInputFromWindow(editText.getWindowToken(),0);
            editText.clearFocus();
            editText.setText(webView.getUrl());
            progressBar.setVisibility(View.VISIBLE);
        }

        @Override
        public void onPageFinished(WebView view, String url) {
            super.onPageFinished(view, url);

            progressBar.setVisibility(View.GONE);
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        new MenuInflater(this).inflate(R.menu.toolbar_menu,menu);
        return super.onCreateOptionsMenu(menu);

    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id=item.getItemId();
        Intent intent;
        if(id==R.id.refresh) {
            webView.reload();
        }
        else if(id==R.id.history) {
                intent=new Intent(MainActivity.this,HistoryActivity.class);
                startActivityForResult(intent,HIS_REQ_CODE);
        }
        else if(id==R.id.addBookMark) {
            BookmarkDbHelper bookmarkDbHelper=new BookmarkDbHelper(MainActivity.this);
            bookmarkDbHelper.AddUrlBookmark(webView.getUrl());
        }
        else if(id==R.id.bookmarks) {
                intent=new Intent(MainActivity.this,BookmarksActivity.class);
                startActivityForResult(intent,BOOK_REQ_CODE);
        }
        else if(id==R.id.desktop_mode) {
           desktopDefineFun(item);
        }
        else if(id==R.id.incognito) {
            Intent intentIncognito=new Intent(MainActivity.this,IncognitoActivity.class);
            startActivity(intentIncognito);
        }
        else{
            aboutUs();
        }
        return super.onOptionsItemSelected(item);
    }

    public void aboutUs() {
        Dialog dialog=new Dialog(MainActivity.this);
        dialog.setContentView(R.layout.about_us);
        TextView emailBtn=dialog.findViewById(R.id.emailBtn);
        emailBtn.setOnClickListener(view -> {
            Intent emailIntent=new Intent();
            emailIntent.setAction(Intent.ACTION_SEND);
            emailIntent.setType("message/rfc822");
            emailIntent.putExtra(Intent.EXTRA_EMAIL,new String[]{"abc@gmail.com","def@gmail.com"});
            emailIntent.putExtra(Intent.EXTRA_SUBJECT,"Alert");
            emailIntent.putExtra(Intent.EXTRA_TEXT,"please contact us!");
            startActivity(emailIntent);
            dialog.dismiss();
        });
        dialog.show();
    }

    public void desktopDefineFun(MenuItem item) {
        if(!desktop_mode_flag){
            desktop_mode_flag=true;
            setDesktopMode(webView, true);
            item.setTitle("Mobile Mode");
        }
        else{
            desktop_mode_flag=false;
            setDesktopMode(webView, false);
            item.setTitle("Desktop Mode");
        }
    }

    public void setDesktopMode(WebView webView, boolean b) {
        String newUsrAgent=webView.getSettings().getUserAgentString();
        if(b) {

            try {
                String uas = webView.getSettings().getUserAgentString();
                String androidDosString = webView.getSettings().getUserAgentString().substring(uas.indexOf("("), uas.indexOf(")")+1);
                newUsrAgent = webView.getSettings().getUserAgentString().replace(androidDosString, "(X11; Linux x86_64)");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        else{
            newUsrAgent=null;
        }
        webView.getSettings().setUserAgentString(newUsrAgent);
        webView.getSettings().setUseWideViewPort(b);
        webView.getSettings().setLoadWithOverviewMode(b);
        webView.reload();
    }

    @Override
    public void onBackPressed() {
        if(webView.canGoBack()){
            webView.goBack();

//            if(desktop_mode_flag=true){
//                myItem.setTitle("Mobile Mode");
//            }
//            else{
//                myItem.setTitle("Desktop Mode");
//            }
        }
        else{
            super.onBackPressed();
        }
    }



}