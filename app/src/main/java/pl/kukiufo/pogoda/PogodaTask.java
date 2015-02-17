package pl.kukiufo.pogoda;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.AsyncTask;
import android.view.View;
import android.widget.Toast;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.protocol.BasicHttpContext;
import org.apache.http.protocol.HttpContext;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Created by kukiufo on 16.02.15.
 */
public class PogodaTask extends AsyncTask<Void, String, String> {

    Context context;
    String city_name;
    String new_line;
    String location, date, condition, humidity, wind, pressure, visibility;
    String sunrise, sunset;
    String temperature, temperature_min, temperature_max;
    PogodaComponent pogoda;

    public PogodaTask(Context context, PogodaComponent pogoda, String city_name) {
        this.context = context;
        this.city_name = city_name;
        new_line = System.getProperty("line.separator");
        if(pogoda != null)
            this.pogoda = pogoda;
    }

    @Override
    protected void onPreExecute() {
        pogoda.setBussyVisible(View.VISIBLE);
    }

    @Override
    protected String doInBackground(Void... arg0) {
        if(pogoda == null || city_name == null || city_name.isEmpty())
            return null;

        HttpClient httpClient = new DefaultHttpClient();
        HttpContext httpContext = new BasicHttpContext();
        //HttpGet httpGet1 = new HttpGet("https://query.yahooapis.com/v1/public/yql?q=select%20woeid%20from%20geo.places(1)%20where%20text%3D%22" + city + "%22&format=json&env=store%3A%2F%2Fdatatables.org%2Falltableswithkeys");
        HttpGet httpGet = new HttpGet("https://query.yahooapis.com/v1/public/yql?q=select%20*%20from%20weather.forecast%20where%20woeid%20in%20(select%20woeid%20from%20geo.places(1)%20where%20text%3D%22" + city_name + "%2C%20ak%22)%20and%20u%20%3D%20'c'&format=json&env=store%3A%2F%2Fdatatables.org%2Falltableswithkeys");
        String jsonStr = null;

        try {
            HttpResponse response = httpClient.execute(httpGet, httpContext);
            HttpEntity entity = response.getEntity();

            if (entity != null) {
                InputStream inputStream = entity.getContent();
                Reader in = new InputStreamReader(inputStream);
                BufferedReader bufferedreader = new BufferedReader(in);
                StringBuilder stringBuilder = new StringBuilder();
                String stringReadLine;
                while ((stringReadLine = bufferedreader.readLine()) != null) {
                    stringBuilder.append(stringReadLine).append(new_line);
                }
                jsonStr = stringBuilder.toString();
                System.out.println("jsonStr: " + jsonStr);
            }

        } catch (ClientProtocolException e) {
            e.printStackTrace();
            Toast.makeText(context, e.toString(), Toast.LENGTH_LONG).show();
            jsonStr = null;
        } catch (IOException e) {
            e.printStackTrace();
            Toast.makeText(context, e.toString(), Toast.LENGTH_LONG).show();
            jsonStr = null;
        }
        if(jsonStr != null) {
            try {
                JSONObject jsonObj = new JSONObject(jsonStr);
                JSONObject jsonQuery = jsonObj.getJSONObject("query");
                JSONObject jsonResults = jsonQuery.getJSONObject("results");
                JSONObject jsonChannel = jsonResults.getJSONObject("channel");

                JSONObject jsonLocation = jsonChannel.getJSONObject("location");
                JSONObject jsonUnits = jsonChannel.getJSONObject("units");
                JSONObject jsonWind = jsonChannel.getJSONObject("wind");
                JSONObject jsonAtmosphere = jsonChannel.getJSONObject("atmosphere");
                JSONObject jsonAstronomy = jsonChannel.getJSONObject("astronomy");
                JSONObject jsonItem = jsonChannel.getJSONObject("item");
                JSONObject jsonCondition = jsonItem.getJSONObject("condition");

                JSONArray jsonForecasts = jsonItem.getJSONArray("forecast");
                JSONObject jsonForecast = jsonForecasts.getJSONObject(0);

                date = jsonForecast.get("date").toString();
                location = jsonLocation.get("city").toString() + ", " + jsonLocation.get("country").toString();

                String packageName = context.getPackageName();
                int resId = context.getResources().getIdentifier("condition_" + jsonCondition.get("code").toString(), "string", packageName);
                condition = context.getString(resId);
                temperature = jsonCondition.get("temp").toString() + "°";
                temperature_min = jsonForecast.get("low").toString() + "°";
                temperature_max = jsonForecast.get("high").toString() + "°";
                wind = jsonWind.get("speed").toString() + " "  + jsonUnits.get("speed").toString() + ", " + jsonWind.get("direction").toString() + "°";
                humidity = jsonAtmosphere.get("humidity").toString() + "%";
                pressure = jsonAtmosphere.get("pressure").toString()  + " hPa";
                visibility = jsonAtmosphere.get("visibility").toString()  + " " + jsonUnits.get("distance").toString();
                sunrise = jsonAstronomy.get("sunrise").toString();
                sunset = jsonAstronomy.get("sunset").toString();
            } catch (JSONException e) {
                e.printStackTrace();
                Toast.makeText(context, e.toString(), Toast.LENGTH_LONG).show();
            }
        }

        return jsonStr;
    }

    @Override
    @SuppressLint("SimpleDateFormat")
    protected void onPostExecute(String result) {
        if(pogoda == null || result == null || result.isEmpty())
            return;

        try {
            SimpleDateFormat dtParse = new SimpleDateFormat("dd MMM yyyy", Locale.US);
            SimpleDateFormat dtFormat = new SimpleDateFormat("dd MMMM yyyy");
            Date dt = dtParse.parse(date);
            date = dtFormat.format(dt);

            dtParse = new SimpleDateFormat("hh:mm aa", Locale.US);
            dtFormat = new SimpleDateFormat("H:mm");
            dt = dtParse.parse(sunrise);
            sunrise = dtFormat.format(dt);
            dt = dtParse.parse(sunset);
            sunset = dtFormat.format(dt);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        pogoda.setBussyVisible(View.INVISIBLE);
        pogoda.setPogoda(location, date, temperature, temperature_min, temperature_max,
                        condition, humidity, wind, pressure, visibility, sunrise, sunset);
    }
}