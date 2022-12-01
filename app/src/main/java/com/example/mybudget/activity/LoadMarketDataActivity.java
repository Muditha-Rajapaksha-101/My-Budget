package com.example.mybudget.activity;

import androidx.appcompat.app.AppCompatActivity;

import com.example.mybudget.AsyncTask.AsyncExchangeRateScrape;
import com.example.mybudget.AsyncTask.AsyncLoadChart;
import com.example.mybudget.controllers.DatePickerController;
import com.example.mybudget.R;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.concurrent.ExecutionException;

public class LoadMarketDataActivity extends AppCompatActivity {

    private static final String TAG = "XXXXX";
    WebView wbChart;
    Spinner spExchange;

    TextView txtUSDBuy;
    TextView txtUSDSell;

    TextView txtGBPBuy;
    TextView txtGBPSell;

    TextView txtEURBuy;
    TextView txtEURSell;

    TextView txtAUDBuy;
    TextView txtAUDSell;

    TextView txtExchangeRateDate;

    final String linkUSD = "https://www.cbsl.gov.lk/cbsl_custom/charts/usd/indexsmall.php";
    final String linkGBP = "https://www.cbsl.gov.lk/cbsl_custom/charts/gbp/indexsmall.php";
    final String linkEUR = "https://www.cbsl.gov.lk/cbsl_custom/charts/eur/indexsmall.php";
    final String linkAUD = "https://www.cbsl.gov.lk/cbsl_custom/charts/aud/indexsmall.php";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_load_market_data);

        //References
        wbChart = (WebView) findViewById(R.id.wbChart);
        spExchange = findViewById(R.id.spExchange);

        txtUSDBuy = findViewById(R.id.txtUSDBuy);
        txtUSDSell = findViewById(R.id.txtUSDSell);

        txtGBPBuy = findViewById(R.id.txtGBPBuy);
        txtGBPSell = findViewById(R.id.txtGBPSell);

        txtEURBuy = findViewById(R.id.txtEURBuy);
        txtEURSell = findViewById(R.id.txtEURSell);

        txtAUDBuy = findViewById(R.id.txtAUDBuy);
        txtAUDSell = findViewById(R.id.txtAUDSell);

        txtExchangeRateDate = findViewById(R.id.txtExchangeRateDate);


        //init
        this.setTitle("Market Data");

        //Loading Exchange Rates
        loadExchangeRates(linkUSD,txtUSDBuy,txtUSDSell);
        loadExchangeRates(linkGBP,txtGBPBuy,txtGBPSell);
        loadExchangeRates(linkEUR,txtEURBuy,txtEURSell);
        loadExchangeRates(linkAUD,txtAUDBuy,txtAUDSell);



        //wbChart.setWebViewClient(new WebViewClient());
        wbChart.setWebChromeClient(new WebChromeClient());

        wbChart.getSettings().setJavaScriptEnabled(true);
        WebSettings webSettings = wbChart.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webSettings.setDomStorageEnabled(true);
        webSettings.setLoadWithOverviewMode(true);
        webSettings.setUseWideViewPort(true);
        webSettings.setBuiltInZoomControls(true);
        webSettings.setDisplayZoomControls(false);
        webSettings.setSupportZoom(true);
        webSettings.setDefaultTextEncodingName("utf-8");
        //webSettings.setPluginState(WebSettings.PluginState.ON);
        //webSettings.setRenderPriority(WebSettings.RenderPriority.HIGH);


        //Load Spinner
        setExchangeSpinner();

        //Load Chart
        spExchange.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                loadChartOnSelectedString(adapterView,view,i,l);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        //Set Today Date
        String date = DatePickerController.getTodaysDate();
        txtExchangeRateDate.setText(date);

    }

    private void loadChartOnSelectedString(AdapterView<?> adapterView, View view, int i, long l) {
        String selectedExchange = spExchange.getItemAtPosition(i).toString();

        if(selectedExchange.equals("USD/LKR")){
            wbChart.loadUrl(linkUSD) ;
        }else if(selectedExchange.equals("GBP/LKR")){
            wbChart.loadUrl(linkGBP);
        }else if(selectedExchange.equals("EUR/LKR")){
            wbChart.loadUrl(linkEUR);
        }else{
            wbChart.loadUrl(linkAUD);
        }
    }

    private void loadChart(String link) {
        AsyncLoadChart asyncLoadChart = new AsyncLoadChart(link);
        try {
            String htmlString = asyncLoadChart.execute().get();
            //wbChart.loadDataWithBaseURL(null,htmlString, "text/html", "UTF-8",null);
            Log.i(TAG,htmlString);
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }

    private void loadExchangeRates(String url , TextView buy , TextView sell) {


        AsyncExchangeRateScrape asyncExchangeRateScrape = new AsyncExchangeRateScrape(url);
        try {
            String strEx = asyncExchangeRateScrape.execute("").get();
            String[] lstEx = strEx.split(" ");

            buy.setText(lstEx[3]);
            sell.setText(lstEx[5]);


        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }



    }

    private void setExchangeSpinner() {
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,R.array.strExhange, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spExchange.setAdapter(adapter);
        spExchange.setSelection(0);
    }


}