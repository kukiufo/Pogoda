package pl.kukiufo.pogoda;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import pl.kukiufo.pogoda.model.YResponse;
import pl.kukiufo.pogoda.rest.service.YService;
import retrofit.RestAdapter;
import retrofit.RetrofitError;

/**
 * Created by kukiufo on 16.02.15.
 */
public class WeatherTask extends AsyncTask<Void, String, YResponse> {

    Context mContext;
    String mCityName;
    WeatherComponent mWeatherComp;

    public WeatherTask(Context context, WeatherComponent weatherComp, String city_name) {
        this.mContext = context;
        this.mCityName = city_name;
        this.mWeatherComp = weatherComp;
    }

    @Override
    protected void onPreExecute() {
        if(mWeatherComp != null)
            mWeatherComp.setBussyVisible(View.VISIBLE);
    }

    /**
     * F-cja łączy się z serwisem pogodowym yahoo, parsuje wynik
     * @param arg0
     * @return
     */
    @Override
    protected YResponse doInBackground(Void... arg0) {
        if(mWeatherComp == null || mCityName == null || mCityName.isEmpty())
            return null;

        //zakoduj podaną nazwę miasta
        String url_city_name;
        try {
            url_city_name = URLEncoder.encode(mCityName, "UTF-8");
            url_city_name = url_city_name.replaceAll("\\+", "%20");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            return new YResponse();//"error_data";
        }

        try {
            RestAdapter restAdapter = new RestAdapter.Builder()
                    .setEndpoint("https://query.yahooapis.com")
                    .setLogLevel(RestAdapter.LogLevel.FULL)
                    .build();
            YService yService = restAdapter.create(YService.class);
            String service_query = "?q=select%20*%20from%20weather.forecast%20where%20woeid%20in%20(select%20woeid%20from%20geo.places(1)%20where%20text%3D%22" + url_city_name + "%22)%20and%20u%20%3D%20'c'&format=json&env=store%3A%2F%2Fdatatables.org%2Falltableswithkeys";
            YResponse yResponse = yService.getResponce(service_query);
            return yResponse;
        } catch (RetrofitError e) {
            e.printStackTrace();
        }

        return null;//"error_network"
    }

    /**
     * F-cja po zakończeniu wczytywania danych z serwisu wyświetla dane nt pogody lub okno z błędem
     * @param result
     */
    @Override
    @SuppressLint("SimpleDateFormat")
    protected void onPostExecute(YResponse result) {
        if(mWeatherComp == null)
            return;

        mWeatherComp.setBussyVisible(View.INVISIBLE);

        String error_name = "error_network"; //nazwa błędu
        if(result != null) {
            if (result.getQuery() != null && result.getQuery().getResults() != null) {
                mWeatherComp.setPogoda(result);
                return;
            } else
                error_name = "error_data";
        }

        //jeśli wystąpił błąd podczas czytania informacji z serwisu wyświet okno i informacją nt błędu
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(mContext);
        LayoutInflater inflater = (LayoutInflater) mContext.getSystemService( Context.LAYOUT_INFLATER_SERVICE );
        View error_layout = inflater.inflate(R.layout.error_layout, null);
        alertDialog.setView(error_layout);
        final TextView tv_error_details= (TextView) error_layout.findViewById(R.id.tv_error_details);

        //przygotowanie id tłumaczenia opisu błędu pogody na podstawie nazwy błędu
        String packageName = mContext.getPackageName();
        int resId = mContext.getResources().getIdentifier(error_name, "string", packageName);
        tv_error_details.setText(mContext.getString(resId));

        alertDialog.setPositiveButton(mContext.getString(R.string.btn_close), new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog,int which) {
                dialog.cancel();
            }
        });
        alertDialog.show();

        mWeatherComp.setPogodaNoData();
    }
}