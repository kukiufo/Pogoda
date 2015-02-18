package pl.kukiufo.pogoda;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

/**
 * Created by kukiufo on 16.02.15.
 */
public class PogodaComponent extends LinearLayout {

    private ProgressBar pb_bussy;
    private TextView tv_location, tv_date, tv_condition, tv_humidity, tv_wind, tv_pressure, tv_visibility;
    private TextView tv_sunrise, tv_sunset;
    private TextView tv_temperature, tv_temperature_min, tv_temperature_max;

    public PogodaComponent(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        LayoutInflater.from(getContext()).inflate(R.layout.pogoda_component, this);
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
     * @param location
     * @param date
     * @param temperature
     * @param temperature_min
     * @param temperature_max
     * @param condition
     * @param humidity
     * @param wind
     * @param pressure
     * @param visibility
     * @param sunrise
     * @param sunset
     */
    public void setPogoda(String location, String date, String temperature, String temperature_min, String temperature_max,
                          String condition, String humidity, String wind, String pressure, String visibility,
                          String sunrise, String sunset) {
        tv_location.setText(location);
        tv_date.setText(date);
        tv_temperature.setText(temperature);
        tv_temperature_min.setText(temperature_min);
        tv_temperature_max.setText(temperature_max);
        tv_condition.setText(condition);
        tv_humidity.setText(humidity);
        tv_wind.setText(wind);
        tv_pressure.setText(pressure);
        tv_visibility.setText(visibility);
        tv_sunrise.setText(sunrise);
        tv_sunset.setText(sunset);
    }

    /**
     * F-cja wyświetla domyślne wartości dla stanu pogody 'brak danych - b/d'
     */
    public void setPogodaNoData() {
        setPogoda(getContext().getText(R.string.no_data).toString(),
        getContext().getText(R.string.no_data).toString(),
        getContext().getText(R.string.no_data).toString(),
        getContext().getText(R.string.no_data).toString(),
        getContext().getText(R.string.no_data).toString(),
        getContext().getText(R.string.no_data).toString(),
        getContext().getText(R.string.no_data).toString(),
        getContext().getText(R.string.no_data).toString(),
        getContext().getText(R.string.no_data).toString(),
        getContext().getText(R.string.no_data).toString(),
        getContext().getText(R.string.no_data).toString(),
        getContext().getText(R.string.no_data).toString());
    }
}