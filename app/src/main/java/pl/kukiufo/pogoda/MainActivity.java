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

/**
 * Created by kukiufo on 16.02.15.
 */
public class MainActivity extends ActionBarActivity {

    public static final String PREFS_NAME = "pogoda_prefs";
    private PogodaComponent pc;
    private String city_name;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        SharedPreferences settings = getSharedPreferences(PREFS_NAME, DEFAULT_KEYS_DISABLE);
        city_name = settings.getString("city_name", "");

        pc = (PogodaComponent) findViewById(R.id.am_pogoda_component);

        findViewById(R.id.am_im_refresh).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pogodaRefresh();
            }
        });

        findViewById(R.id.am_im_settings).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showCityNameDialog();
            }
        });

        if(city_name == null || city_name.isEmpty())
            showCityNameDialog();
        else
            pogodaRefresh();
    }

    private void pogodaRefresh() {
        new PogodaTask(this, pc, city_name).execute();
    }

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

                SharedPreferences settings = getSharedPreferences(PREFS_NAME, DEFAULT_KEYS_DISABLE);
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
}
