package com.sunshine.law.sunshine;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.HttpURLConnection;
import java.text.SimpleDateFormat;
import java.util.Date;


/**
 * A placeholder fragment containing a simple view.
 */
public class MainActivityFragment extends Fragment {

    TaskWeather taskWeather;
    ListView listView;

    public MainActivityFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_main, container, false);

        //Data source
        String[] dummyData = new String[]{"Item 1", "Item 2", "Item 3", "Item 4", "Item 5", "Item 6", "Item 7", "Item 8"};

        taskWeather = new TaskWeather();
        taskWeather.execute();

        ArrayAdapter<String> adapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_list_item_1, dummyData);

        //List view
        listView = (ListView)view.findViewById(R.id.listViewWeather);
        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String listItem = ((TextView)view).getText().toString();
                //Toast.makeText(getActivity(), listItem, Toast.LENGTH_SHORT).show();

                Intent intent = new Intent(getActivity(), DetailsActivity.class);
                intent.putExtra("weatherData", listItem);
                startActivity(intent);
            }
        });

        return view;
    }

    private class TaskWeather extends AsyncTask<Void, Void, String>{
        @Override
        protected String doInBackground(Void... params) {
            String jsonString = "";
            try {
                URL url = new URL("http://api.openweathermap.org/data/2.5/forecast/daily?q=kenya&mode=json&units=metric&cnt=14");
                HttpURLConnection httpConnection = (HttpURLConnection) url.openConnection();
                InputStream inputStream = httpConnection.getInputStream();
                InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
                BufferedReader bufferedReader = new BufferedReader(inputStreamReader);

                StringBuilder stringBuilder = new StringBuilder();
                String line;
                while ((line = bufferedReader.readLine()) != null){
                    stringBuilder.append(line);
                }

                jsonString = stringBuilder.toString();

            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

            return jsonString;
        }

        @Override
        protected void onPostExecute(String jsonString) {
            super.onPostExecute(jsonString);
            String[] weatherData;
            try {
                //Create Json Object
                JSONObject jsonObject = new JSONObject(jsonString);

                //create json array from weather list
                JSONArray jsonArray = new JSONArray(jsonObject.getString("list"));

                //initialize array with list size
                weatherData = new String[jsonArray.length()];
                for(int i = 0; i < jsonArray.length(); i++){
                    StringBuilder sb = new StringBuilder();

                    long theDate = jsonArray.getJSONObject(i).getLong("dt");
                    Date date = new Date(theDate * 1000);
                    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("E, MMM d");
                    sb.append(simpleDateFormat.format(date).toString());
                    sb.append(" - ");

                    JSONArray jsonArrayWeather = new JSONArray(jsonArray.getJSONObject(i).getString("weather"));
                    sb.append(jsonArrayWeather.getJSONObject(0).getString("main"));
                    sb.append(" - ");

                    JSONObject jsonObjectTemp = new JSONObject(jsonArray.getJSONObject(i).getString("temp"));
                    sb.append(jsonObjectTemp.getInt("max"));
                    sb.append("/");
                    sb.append(jsonObjectTemp.getInt("min"));

                    weatherData[i] = sb.toString();
                }

                ArrayAdapter<String> adapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_list_item_1, weatherData);
                listView.setAdapter(adapter);

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }
}
