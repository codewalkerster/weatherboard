package com.hardkernel.odroid.weatherboard;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.widget.CompoundButton;
import android.widget.TextView;
import android.widget.ToggleButton;

public class MainActivity extends Activity {

    private final static String TAG = "weather";
    private final static String I2C_1_NODE = "/dev/i2c-1";
    private boolean mStopWeather;
    private ToggleButton mBtn_Weather;
    private TextView mTV_Temperature;
    private TextView mTV_Humidity;
    private TextView mTV_Pressure;
    private TextView mTV_Altitude;
    private Handler handler = new Handler();
    Runnable mRunnableWeather = new Runnable() {

        @Override
        public void run() {
            // TODO Auto-generated method stub
            updateWeather();
        }
    };

    private TextView mTV_UV_index;
    private TextView mTV_Visible;
    private TextView mTV_IR;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mBtn_Weather = (ToggleButton) findViewById(R.id.tb_weather);
        mBtn_Weather.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                // TODO Auto-generated method stub
                if (isChecked) {
                    if (openWeatherBoard(I2C_1_NODE) == -1) {
                        Log.e(TAG, "failed");
                        return;
                    }
                    mStopWeather = false;
                    handler.postDelayed(mRunnableWeather, 300);
                } else {
                    mStopWeather = true;
                    closeWeatherBoard();
                    mTV_Temperature.setText("Temperature :");
                    mTV_Humidity.setText("Humidity :");
                    mTV_Pressure.setText("Pressure :");
                    mTV_Altitude.setText("Altitude :");
                    mTV_UV_index.setText("UV index :");
                    mTV_Visible.setText("Visible :");
                    mTV_IR.setText("IR :");
                }
            }
        });

        mTV_Temperature = (TextView) findViewById(R.id.tv_temperature);
        mTV_Humidity = (TextView) findViewById(R.id.tv_humidity);
        mTV_Pressure = (TextView) findViewById(R.id.tv_pressure);
        mTV_Altitude = (TextView) findViewById(R.id.tv_altitude);

        mTV_UV_index = (TextView) findViewById(R.id.tv_uv_index);
        mTV_Visible = (TextView) findViewById(R.id.tv_visible);
        mTV_IR = (TextView) findViewById(R.id.tv_ir);
    }

    @Override
    protected void onPause() {
        // TODO Auto-generated method stub
        super.onPause();
        mBtn_Weather.setChecked(false);
    }

    private void updateWeather() {
        if (mStopWeather == true)
            return;
        mTV_UV_index.setText("UV index : "
                + String.format("%.2f", (double)getUVindex() / 100.0));
        mTV_Visible.setText("Visible : "
                + String.format("%.0f", (double)getVisible()) + " Lux");
        mTV_IR.setText("IR : "
                + String.format("%.0f", (double)getIR()) + " Lux");
        readyData();
        mTV_Temperature.setText("Temperature : "
                + String.format("%.2f", getTemperature() / 100.0) + " °C");
        mTV_Humidity.setText("Humidity : "
                + String.format("%.2f", getHumidity() / 1024.0) + " %");
        mTV_Pressure.setText("Pressure : "
                + String.format("%.2f", getPressure() / 100.0) + " hPa");
        mTV_Altitude.setText("Altitude : " + getAltitude() + " m");

        if (!mStopWeather)
            handler.postDelayed(mRunnableWeather, 1000);
    }

    public native int openWeatherBoard(String dev);
    public native int closeWeatherBoard();
    public native void readyData();
    public native int getUVindex();
    public native float getVisible();
    public native float getIR();
    public native int getTemperature();
    public native int getPressure();
    public native int getHumidity();
    public native int getAltitude();

    static {
        System.loadLibrary("weather");
    }
}
