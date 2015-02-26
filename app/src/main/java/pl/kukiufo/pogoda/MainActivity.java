package pl.kukiufo.pogoda;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

/**
 * Created by kukiufo on 16.02.15.
 */
public class MainActivity extends ActionBarActivity {

    //nazwa pod którą szukane są dane z poprzedniej sesji
    public static final String PREFERENSES_NAME = "weather_preferences";
    private WeatherComponent weatherComp;
    private String city_name;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //wczytaj nazwę miasta zapisaną w poprzedniej sesji
        SharedPreferences settings = getSharedPreferences(PREFERENSES_NAME, DEFAULT_KEYS_DISABLE);
        city_name = settings.getString("city_name", "");

        weatherComp = (WeatherComponent) findViewById(R.id.am_weather_component);

        //inicjalizacja obsługi przycisku odświeżania pogody
        findViewById(R.id.am_im_refresh).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pogodaRefresh();
            }
        });

        //inicjalizacja obsługi przycisku ustawień
        findViewById(R.id.am_im_settings).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showCityNameDialog();
            }
        });

        //jeśli brak nazwy miasta wyświetl okno konfiguracji
        if(city_name == null || city_name.isEmpty())
            showCityNameDialog();
        else
            //wczytaj pogodę
            pogodaRefresh();
    }

    /**
     * F-cja wczytuje pogodę
     */
    private void pogodaRefresh() {
        new WeatherTask(this, weatherComp, city_name).execute();
    }

    /**
     * F-cja wyświetla okno konfiguracji
     */
    @SuppressLint("InflateParams")
    private void showCityNameDialog() {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(MainActivity.this);
        LayoutInflater inflater = MainActivity.this.getLayoutInflater();
        View settings_layout = inflater.inflate(R.layout.settings_layout, null);
        alertDialog.setView(settings_layout);
        final EditText et_settings_city_name= (EditText) settings_layout.findViewById(R.id.et_settings_city_name);
        et_settings_city_name.setText(city_name);
        alertDialog.setPositiveButton(getString(R.string.btn_save), new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog,int which) {
                city_name = et_settings_city_name.getText().toString();

                //zapisz nazwę miasta
                SharedPreferences settings = getSharedPreferences(PREFERENSES_NAME, DEFAULT_KEYS_DISABLE);
                SharedPreferences.Editor editor = settings.edit();
                editor.putString("city_name", city_name);
                editor.apply();

                dialog.cancel();

                pogodaRefresh();
            }
        });
        alertDialog.setNegativeButton(getString(R.string.btn_cancel), new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        alertDialog.show();
    }

    private Toast toast;
    private long lastBackPressTime = 0;

    /**
     * F-cja obsługuje zamknięcie aplikacji po dwukrotnym kliknięciu przycisku back
     */
    @Override
    public void onBackPressed() {
        //wyświetl komunikat
        if (this.lastBackPressTime < System.currentTimeMillis() - 4000) {
            toast = Toast.makeText(this, getString(R.string.toast_app_exit), Toast.LENGTH_SHORT);
            toast.show();
            this.lastBackPressTime = System.currentTimeMillis();
        } else {
            //wyłącz powiadomienie i zamknij aplikację
            if (toast != null)
                toast.cancel();

            super.onBackPressed();
        }
    }
}