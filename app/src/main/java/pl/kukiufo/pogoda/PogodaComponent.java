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
    private TextView tv_city, tv_country, tv_date, tv_condition, tv_humidity;
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

    private void setupViewItems() {
        pb_bussy = (ProgressBar) findViewById(R.id.pb_bussy);
        tv_city = (TextView) findViewById(R.id.tv_city);
        tv_country = (TextView) findViewById(R.id.tv_country);
        tv_date = (TextView) findViewById(R.id.tv_date);
        tv_temperature = (TextView) findViewById(R.id.tv_temperature);
        tv_temperature_min = (TextView) findViewById(R.id.tv_temperature_max);
        tv_temperature_max = (TextView) findViewById(R.id.tv_temperature_min);
        tv_condition = (TextView) findViewById(R.id.tv_condition);
        tv_humidity = (TextView) findViewById(R.id.tv_humidity);

        tv_city.setText("");
        tv_date.setText("");
        tv_temperature.setText("");
        tv_temperature_min.setText("");
        tv_temperature_max.setText("");
        tv_condition.setText("");
        tv_humidity.setText("");
    }

    public void setBussyVisible(int visible) {
        pb_bussy.setVisibility(visible);
    }

    public void setPogoda(String city, String country, String date, String temperature, String temperature_min, String temperature_max, String condition, String humidity) {
        tv_city.setText(city);
        tv_country.setText(country);
        tv_date.setText(date);
        tv_temperature.setText(temperature);
        tv_temperature_min.setText(temperature_min);
        tv_temperature_max.setText(temperature_max);
        tv_condition.setText(condition);
        tv_humidity.setText(humidity);
    }
}