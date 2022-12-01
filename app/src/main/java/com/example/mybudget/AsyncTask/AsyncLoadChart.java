package com.example.mybudget.AsyncTask;

import android.os.AsyncTask;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;

public class AsyncLoadChart extends AsyncTask<String,Void,String> {

    String url;
    public AsyncLoadChart(String url) {
        this.url = url;
    }


    @Override
    protected String doInBackground(String... strings) {
        try {
            Document document = Jsoup.connect(url).get();
            Element element = document.getElementById("container");
            String p = element.text();

            return document.toString();

        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;

    }
}

