package pl.kukiufo.pogoda;

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
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.xml.sax.SAXException;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

/**
 * Created by kukiufo on 16.02.15.
 */
public class PogodaTask extends AsyncTask<Void, String, String> {

    Context context;
    String new_line;
    String location, date, condition, humidity, wind, pressure, visibility;
    String sunrise, sunset;
    String temperature, temperature_min, temperature_max;
    PogodaComponent pogoda;

    public PogodaTask(Context context, PogodaComponent pogoda) {
        this.context = context;
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
        String result = null;
        HttpClient httpClient = new DefaultHttpClient();
        HttpContext httpContext = new BasicHttpContext();
        HttpGet httpGet = new HttpGet("http://weather.yahooapis.com/forecastrss?w=12866469&u=c");

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
                result = stringBuilder.toString();
                System.out.println("result: " + result);
            }

        } catch (ClientProtocolException e) {
            e.printStackTrace();
            Toast.makeText(context, e.toString(), Toast.LENGTH_LONG).show();
            result = null;
        } catch (IOException e) {
            e.printStackTrace();
            Toast.makeText(context, e.toString(), Toast.LENGTH_LONG).show();
            result = null;
        }

        if(result != null && !result.isEmpty()) {
            Document dest;
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder parser;
            try {
                parser = dbFactory.newDocumentBuilder();
                dest = parser.parse(new ByteArrayInputStream(result.getBytes()));
            } catch (ParserConfigurationException e1) {
                e1.printStackTrace();
                Toast.makeText(context, e1.toString(), Toast.LENGTH_LONG).show();
                dest = null;
            } catch (SAXException e) {
                e.printStackTrace();
                Toast.makeText(context, e.toString(), Toast.LENGTH_LONG).show();
                dest = null;
            } catch (IOException e) {
                e.printStackTrace();
                Toast.makeText(context, e.toString(), Toast.LENGTH_LONG).show();
                dest = null;
            }

            if(dest != null) {
                Node unitsNode = dest.getElementsByTagName("yweather:units").item(0);

                Node locationNode = dest.getElementsByTagName("yweather:location").item(0);
                location = locationNode.getAttributes().getNamedItem("city").getNodeValue();
                location += ", " + locationNode.getAttributes().getNamedItem("country").getNodeValue();

                Node temperatureNode = dest.getElementsByTagName("yweather:condition").item(0);
                temperature = temperatureNode.getAttributes().getNamedItem("temp").getNodeValue() + "°";
                //
                //temperature += tempUnitNode.getAttributes().getNamedItem("temperature").getNodeValue();

                Node dateNode = dest.getElementsByTagName("yweather:forecast").item(0);
                date = dateNode.getAttributes().getNamedItem("date").getNodeValue();
                temperature_min = dateNode.getAttributes().getNamedItem("low").getNodeValue() + "°";
                temperature_max = dateNode.getAttributes().getNamedItem("high").getNodeValue() + "°";

                Node conditionNode = dest.getElementsByTagName("yweather:condition").item(0);
                condition = conditionNode.getAttributes().getNamedItem("text").getNodeValue();

                Node atmosphereNode = dest.getElementsByTagName("yweather:atmosphere").item(0);
                humidity = atmosphereNode.getAttributes().getNamedItem("humidity").getNodeValue() + "%";

                Node windNode = dest.getElementsByTagName("yweather:wind").item(0);
                wind = windNode.getAttributes().getNamedItem("speed").getNodeValue() + " "  + unitsNode.getAttributes().getNamedItem("speed").getNodeValue();
                wind += ", " + windNode.getAttributes().getNamedItem("direction").getNodeValue() + "°";

                pressure = atmosphereNode.getAttributes().getNamedItem("pressure").getNodeValue()  + " hPa";

                visibility = atmosphereNode.getAttributes().getNamedItem("visibility").getNodeValue()  + " " + unitsNode.getAttributes().getNamedItem("distance").getNodeValue();

                Node astronomyNode = dest.getElementsByTagName("yweather:astronomy").item(0);
                sunrise = astronomyNode.getAttributes().getNamedItem("sunrise").getNodeValue();
                sunset = astronomyNode.getAttributes().getNamedItem("sunset").getNodeValue();
            }
        }
        return result;
    }

    @Override
    protected void onPostExecute(String result) {
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

        if(pogoda != null) {
            pogoda.setBussyVisible(View.INVISIBLE);
            pogoda.setPogoda(location, date, temperature, temperature_min, temperature_max,
                            condition, humidity, wind, pressure, visibility, sunrise, sunset);
        }
    }
}