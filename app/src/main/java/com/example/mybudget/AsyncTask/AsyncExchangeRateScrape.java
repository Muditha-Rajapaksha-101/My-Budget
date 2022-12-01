package com.example.mybudget.AsyncTask;

import android.os.AsyncTask;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;

public class AsyncExchangeRateScrape extends AsyncTask<String,Void,String> {

    String url;
    public AsyncExchangeRateScrape(String url) {
        this.url = url;
    }

    @Override
    protected String doInBackground(String... strings) {

        try {
            Document document = Jsoup.connect(url).get();
            Elements elements = document.getElementsByTag("p");
            String p = elements.text();

            return p;

        } catch (IOException e) {
            e.printStackTrace();
        }


        return null;
    }

}
