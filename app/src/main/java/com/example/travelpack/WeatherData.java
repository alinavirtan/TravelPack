package com.example.travelpack;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.maps.model.LatLng;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

class ForecastInfo {
    String date;
    double temp;
    double feelsLike;
    double windSpeed;
    int humidity;
    int weather_id;
    String description;

    SimpleDateFormat sdformat;

    public ForecastInfo(String date, double temp, double feelsLike, double windSpeed, int humidity, int weather_id, String description) {
        this.date = date;
        this.temp = temp;
        this.feelsLike = feelsLike;
        this.windSpeed = windSpeed;
        this.humidity = humidity;
        this.weather_id = weather_id;
        this.description = description;
    }
}

public class WeatherData {
    Context context;
    private String key = "cecd2562ff803b5d2574560fd0fd41d5";
    private String url = "https://api.openweathermap.org/data/2.5/onecall";

    private Date startDate;           // day-month-year
    private Date endDate;
    private int daysNum;
    private LatLng latLng;
    private SimpleDateFormat sdformat;

    ArrayList<ForecastInfo> dailyForecast;


    public WeatherData(LatLng latLng, String startDate, String daysNum, Context context) {
        this.latLng = latLng;
        this.daysNum = Integer.parseInt(daysNum);
        this.context = context;
        this.sdformat = new SimpleDateFormat("yyyy-MM-dd");
        computeDates(startDate);
        dailyForecast = new ArrayList<>();
    }

    void computeDates(String startDateString) {
        try {
            Log.d("DATA", " " + startDateString);
            this.startDate = sdformat.parse(startDateString);

            Log.d("DATA DUPA PARSARE:", "" + sdformat.format(this.startDate));

            Calendar c = Calendar.getInstance();
            c.setTime(sdformat.parse(startDateString));

            // nr_zile = daysNum - 1
            c.add(Calendar.DAY_OF_MONTH, this.daysNum-1);

            String endDateString = sdformat.format(c.getTime());

            this.endDate = sdformat.parse(endDateString);

        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    public void ComputeRequest() {
        String tmpURL = "";
        final String[] output = new String[7];

        if (latLng != null) {
            tmpURL += url + "?lat=" + latLng.latitude + "&lon=" + latLng.longitude + "&exclude=current,minutely,hourly,alerts&appid=" + key;
        } else {
            tmpURL += url + "?q=0.00&lon=0.00&appid=" + key;       // nu stiu ce sa bag aici :)))
        }

        StringRequest stringRequest = new StringRequest(Request.Method.POST, tmpURL, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.d("response", response);
                try {
                    JSONObject jsonResponse = new JSONObject(response);
                    JSONArray jsonArray = jsonResponse.getJSONArray("daily");

                    for (int i = 0; i < 7; i++) {
                        JSONObject jsonObjectDaily = jsonArray.getJSONObject(i);

                        // date
                        int dt = jsonObjectDaily.getInt("dt");
                        String dtString = sdformat.format(new Date((long) dt * 1000));
                        Date forecastDate = null;
                        try {
                            forecastDate = sdformat.parse(dtString);
                        } catch (ParseException e) {
                            e.printStackTrace();
                            return;
                        }

                        // temperature
                        JSONObject jsonObjectTemp = jsonObjectDaily.getJSONObject("temp");
                        double temp = jsonObjectTemp.getDouble("day") - 273.15;

                        // feels like
                        JSONObject jsonObjectFeelsLike = jsonObjectDaily.getJSONObject("feels_like");
                        double feelsLike = jsonObjectFeelsLike.getDouble("day") - 273.15;

                        // wind speed
                        double windSpeed = jsonObjectDaily.getDouble("wind_speed");

                        // humidity
                        int humidity = jsonObjectDaily.getInt("humidity");

                        // weather description
                        JSONArray jsonArrayWeather = jsonObjectDaily.getJSONArray("weather");
                        JSONObject jsonObjectWeather = jsonArrayWeather.getJSONObject(0);

                        int id = jsonObjectWeather.getInt("id");
                        String description = jsonObjectWeather.getString("description");

                        if (dayOfTravel(forecastDate)) {
                            dailyForecast.add(new ForecastInfo(dtString, temp, feelsLike, windSpeed, humidity, id, description));
                            int current_index = dailyForecast.size() - 1;
                            output[i] = "Today's weather of " + latLng.latitude + " on " + dailyForecast.get(current_index).date
                                    + "\n Temp: " + dailyForecast.get(current_index).temp + " C"
                                    + "\n Feels like: " + dailyForecast.get(current_index).feelsLike + " C"
                                    + "\n Humidity: " + dailyForecast.get(current_index).humidity + "%"
                                    + "\n Description: " + dailyForecast.get(current_index).description
                                    + "\n Wind Speed: " + dailyForecast.get(current_index).windSpeed + "m/s (meters per second)"
                                    + "\n Humidity: " + dailyForecast.get(current_index).humidity + "%\n";

                            Log.d("WEATHER INFO: ", output[i]);
                        }

                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(context, error.toString().trim(), Toast.LENGTH_SHORT).show();
            }
        });

        RequestQueue requestQueue = Volley.newRequestQueue(context);
        requestQueue.add(stringRequest);
    }

    boolean dayOfTravel(Date currentDate) {
        if (currentDate.compareTo(startDate) == 0 || currentDate.compareTo(endDate) == 0) {
            return true;
        }

        if (currentDate.compareTo(startDate) > 0 && currentDate.compareTo(endDate) < 0) {
            return true;
        }

        return false;
    }
}
