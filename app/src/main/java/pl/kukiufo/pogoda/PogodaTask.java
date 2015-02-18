package pl.kukiufo.pogoda;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

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
        this.pogoda = pogoda;
    }

    @Override
    protected void onPreExecute() {
        if(pogoda != null)
            pogoda.setBussyVisible(View.VISIBLE);
    }

    @Override
    protected String doInBackground(Void... arg0) {
        if(pogoda == null || city_name == null || city_name.isEmpty())
            return null;

        HttpClient httpClient = new DefaultHttpClient();
        HttpContext httpContext = new BasicHttpContext();

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
            return "error_network";
        } catch (IOException e) {
            e.printStackTrace();
            return "error_network";
        }

        if(jsonStr != null) {
            try {
                JSONObject jsonObj = new JSONObject(jsonStr);
                JSONObject jsonQuery = jsonObj.getJSONObject("query");
                if(jsonQuery.getInt("count") == 1) {
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
                    wind = jsonWind.get("speed").toString() + " " + jsonUnits.get("speed").toString() + ", " + jsonWind.get("direction").toString() + "°";
                    humidity = jsonAtmosphere.get("humidity").toString() + "%";
                    pressure = jsonAtmosphere.get("pressure").toString() + " hPa";
                    visibility = jsonAtmosphere.get("visibility").toString() + " " + jsonUnits.get("distance").toString();
                    sunrise = jsonAstronomy.get("sunrise").toString();
                    sunset = jsonAstronomy.get("sunset").toString();

                    return "ok";
                } else
                    return "error_data";
            } catch (JSONException e) {
                e.printStackTrace();
                return "error_data";
            } catch (NullPointerException e) {
                e.printStackTrace();
                return "error_data";
            }
        }

        return "error_data";
    }

    @Override
    @SuppressLint("SimpleDateFormat")
    protected void onPostExecute(String result) {
        if(pogoda == null)
            return;

        pogoda.setBussyVisible(View.INVISIBLE);

        if(result != null) {
            if (result.equals("ok")) {
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

                pogoda.setPogoda(location, date, temperature, temperature_min, temperature_max,
                        condition, humidity, wind, pressure, visibility, sunrise, sunset);
                return;
            } else if(result.equals("error_network") || result.equals("error_data")) {
                AlertDialog.Builder alertDialog = new AlertDialog.Builder(context);
                LayoutInflater inflater = (LayoutInflater) context.getSystemService( Context.LAYOUT_INFLATER_SERVICE );
                View error_layout = inflater.inflate(R.layout.error_layout, null);
                alertDialog.setView(error_layout);
                final TextView tv_error_details= (TextView) error_layout.findViewById(R.id.tv_error_details);

                String packageName = context.getPackageName();
                int resId = context.getResources().getIdentifier(result, "string", packageName);
                tv_error_details.setText(context.getString(resId));

                alertDialog.setPositiveButton(context.getString(R.string.btn_close), new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog,int which) {
                        dialog.cancel();
                    }
                });
                alertDialog.show();
            }

        }
        pogoda.setPogodaNoData();
    }
}