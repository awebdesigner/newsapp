package com.example.android.newsapp;

import android.content.res.Resources;
import android.text.TextUtils;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public final class QueryUtils {

    private static final String LOG_TAG = QueryUtils.class.getSimpleName();
    private final static String NO_AUTHOR = "unknown author";

    private QueryUtils() {
    }

    public static List<News> fetchNewsData(String requestUrl) {
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        URL url = createUrl(requestUrl);

        String jsonResponse = null;
        try {
            jsonResponse = makeHttpRequest(url);
        } catch (IOException e) {
            Log.e(LOG_TAG, "Problem making the HTTP request.", e);
        }

        List<News> news = extractFeatureFromJson(jsonResponse);

        return news;
    }

    private static URL createUrl(String stringUrl) {
        URL url = null;
        try {
            url = new URL(stringUrl);
        } catch (MalformedURLException e) {
            Log.e(LOG_TAG, "Problem building the URL ", e);
        }
        return url;
    }

    private static String makeHttpRequest(URL url) throws IOException {
        String jsonResponse = "";

        if (url == null) return jsonResponse;

        HttpURLConnection urlConnection = null;
        InputStream inputStream = null;
        try {
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setReadTimeout(10000 );
            urlConnection.setConnectTimeout(15000);
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();

            if (urlConnection.getResponseCode() == HttpURLConnection.HTTP_OK) {
                inputStream = urlConnection.getInputStream();
                jsonResponse = readFromStream(inputStream);
            } else {
                Log.e(LOG_TAG, "Error response code: " + urlConnection.getResponseCode());
            }
        } catch (IOException e) {
            Log.e(LOG_TAG, "Problem retrieving the news JSON results.", e);
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
            if (inputStream != null) inputStream.close();
        }
        return jsonResponse;
    }

    private static String readFromStream(InputStream inputStream) throws IOException {
        StringBuilder output = new StringBuilder();
        if (inputStream != null) {
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream, Charset.forName("UTF-8"));
            BufferedReader reader = new BufferedReader(inputStreamReader);
            String line = reader.readLine();
            while (line != null) {
                output.append(line);
                line = reader.readLine();
            }
        }
        return output.toString();
    }

    public static List<News> extractFeatureFromJson(String newsJson) {

        if (TextUtils.isEmpty(newsJson)) return null;

        List<News> news = new ArrayList<>();
        try {
            JSONObject baseJsonResponse = new JSONObject(newsJson);
            JSONObject responseObject = baseJsonResponse.optJSONObject("response");

            JSONArray arrayNewsArticles = responseObject.optJSONArray("results");
                int sumNewsArticles = arrayNewsArticles.length();
            for (int i = 0; i < sumNewsArticles; i++) {
                JSONObject currentNewsArticle = arrayNewsArticles.optJSONObject(i);

                String title = currentNewsArticle.optString("webTitle");
                String section = currentNewsArticle.optString("sectionName");
                String date = currentNewsArticle.optString("webPublicationDate");
                String url = currentNewsArticle.optString("webUrl");
                String author = NO_AUTHOR;
                if(currentNewsArticle.has("fields")){
                    JSONObject fieldsObject = currentNewsArticle.optJSONObject("fields");
                    if(fieldsObject.has("byline")){
                        author = fieldsObject.optString("byline");
                    }
                }

                News newsArticle = new News(title,author, section, date, url);

                news.add(newsArticle);
            }
        } catch (JSONException e) {
            Log.e("QueryUtils", "Problem parsing the news JSON response", e);
        }

        return news;
    }
}
