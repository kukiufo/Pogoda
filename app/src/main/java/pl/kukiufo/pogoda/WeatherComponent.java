package pl.kukiufo.pogoda;

import android.content.Context;
import android.content.res.Resources;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import pl.kukiufo.pogoda.model.YChannel;
import pl.kukiufo.pogoda.model.YResponse;

/**
 * Created by kukiufo on 16.02.15.
 */
public class WeatherComponent extends LinearLayout {

    private Context context;
    private ProgressBar pb_bussy;
    private TextView tv_location, tv_date, tv_condition, tv_humidity, tv_wind, tv_pressure, tv_visibility;
    private TextView tv_sunrise, tv_sunset;
    private TextView tv_temperature, tv_temperature_min, tv_temperature_max;

    public WeatherComponent(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        LayoutInflater.from(getContext()).inflate(R.layout.weather_component, this);
        setupViewItems();
    }

    /**
     * F-cja inicjuje kontrolki komponentu
     */
    private void setupViewItems() {
        pb_bussy = (ProgressBar) findViewById(R.id.pb_bussy);
        tv_location = (TextView) findViewById(R.id.tv_location);
        tv_date = (TextView) findViewById(R.id.tv_date);
        tv_temperature = (TextView) findViewById(R.id.tv_temperature);
        tv_temperature_min = (TextView) findViewById(R.id.tv_temperature_max);
        tv_temperature_max = (TextView) findViewById(R.id.tv_temperature_min);
        tv_condition = (TextView) findViewById(R.id.tv_condition);
        tv_humidity = (TextView) findViewById(R.id.tv_humidity);
        tv_wind = (TextView) findViewById(R.id.tv_wind);
        tv_pressure = (TextView) findViewById(R.id.tv_pressure);
        tv_visibility = (TextView) findViewById(R.id.tv_visibility);
        tv_sunrise = (TextView) findViewById(R.id.tv_sunrise);
        tv_sunset = (TextView) findViewById(R.id.tv_sunset);
    }

    /**
     * F-cja ukrywa/pokazuje progressbar informujący o pracy w tle
     * @param visible
     */
    public void setBussyVisible(int visible) {
        pb_bussy.setVisibility(visible);
    }

    /**
     * F-cja wyświetla informacje nt pogody
     * @param yResponse
     */
    public void setPogoda(YResponse yResponse) {
        if(yResponse != null && yResponse.getQuery() != null && yResponse.getQuery().getResults() != null) {
            try {
                YChannel yChannel = yResponse.getQuery().getResults().getChannel();

                String location = yChannel.getLocation().getCity() + ", " + yChannel.getLocation().getCountry();

                String date = yChannel.getItem().getForecast().get(0).getDate();
                String sunrise = yChannel.getAstronomy().getSunrise();
                String sunset = yChannel.getAstronomy().getSunset();
                try {
                    //zmień format otrzymanej daty z np. 18 Feb 2015
                    SimpleDateFormat dtParse = new SimpleDateFormat("dd MMM yyyy", Locale.US);
                    //na datę w postaci np. 18 lutego 2015
                    SimpleDateFormat dtFormat = new SimpleDateFormat("dd MMMM yyyy");
                    Date dt = dtParse.parse(date);
                    date = dtFormat.format(dt);

                    //zmień format czasu z np. 6:25 pm
                    dtParse = new SimpleDateFormat("hh:mm aa", Locale.US);
                    //na czas w postaci 18:25
                    dtFormat = new SimpleDateFormat("H:mm");
                    dt = dtParse.parse(sunrise);
                    sunrise = dtFormat.format(dt);
                    dt = dtParse.parse(sunset);
                    sunset = dtFormat.format(dt);
                } catch (ParseException e) {
                    //jeśli wystąpił błąd parsowania dat ustaw brak danych i wyjdź
                    e.printStackTrace();
                    setPogodaNoData();
                    return;
                }

                tv_location.setText(location);
                tv_date.setText(date);
                tv_temperature.setText(yChannel.getItem().getCondition().getTemp() + "°");
                tv_temperature_min.setText(yChannel.getItem().getForecast().get(0).getLow() + "°");
                tv_temperature_max.setText(yChannel.getItem().getForecast().get(0).getHigh() + "°");

                String condition;
                try {
                    //przygotowanie id tłumaczenia nazwy stanu pogody na podstawie kodu
                    String packageName = context.getPackageName();
                    int resId = context.getResources().getIdentifier("condition_" + yChannel.getItem().getCondition().getCode(), "string", packageName);
                    condition = context.getString(resId);
                } catch (Resources.NotFoundException e) {
                    e.printStackTrace();
                    //jeśli wystąpił błąd podstaw 'brak danych'
                    condition = context.getString(R.string.condition_3200);
                }

                tv_condition.setText(condition);
                tv_humidity.setText(yChannel.getAtmosphere().getHumidity() + "%");
                tv_wind.setText(yChannel.getWind().getSpeed() + " " + yChannel.getUnits().getSpeed() + ", " + yChannel.getWind().getDirection() + "°");
                tv_pressure.setText(yChannel.getAtmosphere().getPressure() + " hPa");
                tv_visibility.setText(yChannel.getAtmosphere().getVisibility() + " " + yChannel.getUnits().getDistance());
                tv_sunrise.setText(sunrise);
                tv_sunset.setText(sunset);
            } catch (Exception e) {
                e.printStackTrace();
                //jeśli wystąpił błąd parsowania dat ustaw brak danych
                e.printStackTrace();
                setPogodaNoData();
            }
        } else
            setPogodaNoData();
    }

    /**
     * F-cja wyświetla domyślne wartości dla stanu pogody 'brak danych - b/d'
     */
    public void setPogodaNoData() {
        String no_data = getContext().getText(R.string.no_data).toString();
        tv_location.setText(no_data);
        tv_date.setText(no_data);
        tv_temperature.setText(no_data);
        tv_temperature_min.setText(no_data);
        tv_temperature_max.setText(no_data);
        tv_condition.setText(no_data);
        tv_humidity.setText(no_data);
        tv_wind.setText(no_data);
        tv_pressure.setText(no_data);
        tv_visibility.setText(no_data);
        tv_sunrise.setText(no_data);
        tv_sunset.setText(no_data);
    }
}