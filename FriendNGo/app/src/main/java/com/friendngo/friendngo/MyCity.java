package com.friendngo.friendngo;

import android.content.Context;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import org.json.JSONArray;
import org.json.JSONObject;
import java.util.List;
import java.util.Locale;
import cz.msebera.android.httpclient.Header;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class MyCity extends AppCompatActivity {
    String current_city;
    TextView my_city_country_name;
    Button my_city_next;
    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_city);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        current_city = "Montreal,Canada";
        my_city_country_name = (TextView)findViewById(R.id.my_city_country_name);
        //my_city_country_name.setText(current_city);
        my_city_country_name.setText(hereLocation(FacebookLogin.clat,FacebookLogin.clon));
        my_city_next = (Button)findViewById(R.id.my_city_next);
        my_city_next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AsyncHttpClient client = new AsyncHttpClient();
                if (SignIn.static_token != null) {
                    client.addHeader("Authorization", "Token " + SignIn.static_token);
                }

                RequestParams params = new RequestParams();
                params.setUseJsonStreamer(true);
                current_city = my_city_country_name.getText().toString();
                final String[] currentCityArray = current_city.split(",");
                params.put("home_city",currentCityArray[0]);
               // Log.w("Home city",currentCityArray[0]);

                if(currentCityArray[0].equals("")){
                    Toast.makeText(MyCity.this,"My city field is empty",Toast.LENGTH_LONG).show();
                }else {
                    client.post(MainActivity.base_host_url + "api/postHomeCity/", params, new JsonHttpResponseHandler() {
                        @Override
                        public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                            Log.w("POST PROFILE CITY", statusCode + ": " + "Response = " + response.toString());
                            MapActivity.other_user_location.setText("Resident "+currentCityArray[0]);
                        }

                        @Override
                        public void onSuccess(int statusCode, Header[] headers, JSONArray timeline) {
                            Log.w("POST PROFILE CITY", statusCode + ": " + timeline.toString());
                            // NewWhoAreYouActivity.this.finish();
                        }

                        @Override
                        public void onRetry(int retryNo) {
                            // called when request is retried
                            Log.w("POST PROFILE CITY RETRY", "" + retryNo);
                        }

                        @Override
                        public void onFailure(int error_code, Header[] headers, String text, Throwable throwable) {
                            Log.w("POST PROFILE  CITY FAIL", "Error Code: " + error_code + "," + text);
                        }

                        @Override
                        public void onFailure(int error_code, Header[] headers, Throwable throwable, JSONObject json) {
                            Log.w("MY PROFILE CITY FAIL", "Error Code: " + error_code + ",  " + json.toString());
                        }
                    });
                }
                Intent intent = new Intent(MyCity.this,MyStatusActivity.class);
                MyCity.this.startActivity(intent);
                MyCity.this.finish();
            }
        });
    }
    public String hereLocation(double lat,double lon){
        String current_city =null;

        Geocoder geocoder = new Geocoder(MyCity.this, Locale.getDefault());
        List<Address> addresses;
        try{
            addresses = geocoder.getFromLocation(lat, lon, 1);
            if (addresses.size() > 0) {
                current_city = addresses.get(0).getLocality();
                if(current_city == null){
                    current_city = addresses.get(0).getSubLocality() +", "+ addresses.get(0).getCountryName();
                }else{
                    current_city = current_city+", "+ addresses.get(0).getCountryName();
                }
            }

        }catch (Exception e){
            e.printStackTrace();

        }
        return  current_city;
    }
}
