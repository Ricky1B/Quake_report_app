package com.example.android.quakereport;

import android.text.TextUtils;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


public class QueryUtils {

    public static final String USGS_REQUEST_URL =
            "https://earthquake.usgs.gov/fdsnws/event/1/query";



    /** Sample JSON response for a USGS query */

        /**
         * Create a private constructor because no one should ever create a {@link QueryUtils} object.
         * This class is only meant to hold static variables and methods, which can be accessed
         * directly from the class name QueryUtils (and an object instance of QueryUtils is not needed).
         */
        private QueryUtils() {
        }

        /**
         * Return a list of {@link Earthquake} objects that has been built up from
         * parsing a JSON response.
         */
        public static List<Earthquake> extractFeatureFromJson( String earthquakeJSON)  {
            if (TextUtils.isEmpty(earthquakeJSON)) {
                return null;
            }

            // Create an empty ArrayList that we can start adding earthquakes to
            List<Earthquake> earthquakes = new ArrayList<>();


            // Try to parse the SAMPLE_JSON_RESPONSE. If there's a problem with the way the JSON
            // is formatted, a JSONException exception object will be thrown.
            // Catch the exception so the app doesn't crash, and print the error message to the logs.
            try{

                // TODO: Parse the response given by the SAMPLE_JSON_RESPONSE string and
                // build up a list of Earthquake objects with the corresponding data.
                JSONObject jsonObject = new JSONObject(earthquakeJSON);
                JSONArray featuresArray = jsonObject.getJSONArray("features");

                for(int i=0; i<featuresArray.length(); i++){
                    JSONObject jsonObject1 = featuresArray.getJSONObject(i);
                    JSONObject properties = jsonObject1.getJSONObject("properties");
                    double magnitude = properties.getDouble("mag");
                    String place = properties.getString("place");

                    String website = properties.getString("url");

                    //Time recieved fromJSON object is unix time. Needed to covert in normal time zones
                    long timeInMilliseconds = properties.getLong("time");
                    Date dateObject = new Date(timeInMilliseconds);

                    SimpleDateFormat dateFormatter = new SimpleDateFormat("MMM dd, yyyy");
                    String dateToDisplay = dateFormatter.format(dateObject);

                    Earthquake earthquake = new Earthquake(magnitude,place,dateToDisplay, timeInMilliseconds, website);
                    earthquakes.add(earthquake);
                }

            } catch (Exception e) {
                // If an error is thrown when executing any of the above statements in the "try" block,
                // catch the exception here, so the app doesn't crash. Print a log message
                // with the message from the exception.
                Log.e("QueryUtils", "Problem parsing the earthquake JSON results", e);
            }

            // Return the list of earthquakes
            return earthquakes;
        }


    private static URL createUrl(String stringUrl){
        URL url = null;
        try{
            url = new URL(stringUrl);
        }catch(MalformedURLException e){
            Log.e("error", "Error with creating URL", e);
            return null;
        }
        return url;
    }

    private static String makeHttpRequest(URL url) throws IOException {
        String jsonResponse ="";
        if(url ==null){
            return jsonResponse;
        }
        HttpURLConnection urlConnection = null;
        InputStream inputStream = null;
        try{
            urlConnection =(HttpURLConnection)url.openConnection();
            urlConnection.setRequestMethod("GET");
            urlConnection.setReadTimeout(10000 /* milliseconds */);
            urlConnection.setConnectTimeout(15000 /* milliseconds */);
            urlConnection.connect();
            if(urlConnection.getResponseCode() ==200){
                inputStream = urlConnection.getInputStream();
                jsonResponse = readFromStream(inputStream);
            }

        }
        catch (IOException e) {
            e.printStackTrace();
        }finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
            if (inputStream != null) {
                // function must handle java.io.IOException here
                inputStream.close();
            }
        }
        return jsonResponse;
    }

    private static String readFromStream(InputStream inputStream) throws IOException {
        StringBuilder output = new StringBuilder();
        if (inputStream != null) {
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream, Charset.forName("UTF-8"));
            BufferedReader reader = new BufferedReader(inputStreamReader);
            String line = reader.readLine();
            while (line != null) {
                output.append(line);
                line = reader.readLine();
            }
        }
        return output.toString();
    }
    public static List<Earthquake> fetchEarthquakeData(String requestUrl) {
        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        // Create URL object
        URL url = createUrl(requestUrl);

        // Perform HTTP request to the URL and receive a JSON response back
        String jsonResponse = null;
        try {
            jsonResponse = makeHttpRequest(url);
        } catch (IOException e) {
            Log.e("hey", "Problem making the HTTP request.", e);
        }

        // Extract relevant fields from the JSON response and create a list of {@link Earthquake}s
        List<Earthquake> earthquakes = extractFeatureFromJson(jsonResponse);

        // Return the list of {@link Earthquake}s
        return earthquakes;
    }
}
