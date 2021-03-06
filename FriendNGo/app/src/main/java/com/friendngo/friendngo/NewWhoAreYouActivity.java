package com.friendngo.friendngo;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.MultiAutoCompleteTextView;
import android.widget.TextView;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.FileAsyncHttpResponseHandler;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.mikhaellopez.circularimageview.CircularImageView;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.Random;

import cz.msebera.android.httpclient.Header;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class NewWhoAreYouActivity extends AppCompatActivity {

    CircularImageView circularImageView;
    Button nextBtn;
    Boolean finishProfileFlag = false;
    Boolean finishPictureFlag = false;
    EditText nameInput;
    AutoCompleteTextView citizenAuto;
    MultiAutoCompleteTextView spokenLanguage;
    private int REQUEST_CAMERA = 0, SELECT_FILE = 1;
    EditText ageInput;
    Boolean selectImageFlag = false;
    EditText bioField;
    String pictureURL = "";
    Boolean checkValidation = true;
    EditText codeEditText, phoneEditText;
    File downloadedImage;
    private String userChoosenTask;
    private String nationality;
    String imageName;
    String errorMessage = "Some fields are empty";
    Locale[] locales;

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_who_are_you);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        citizenAuto = (AutoCompleteTextView) findViewById(R.id.citizen_spinner);
        spokenLanguage = (MultiAutoCompleteTextView) findViewById(R.id.language_spinner);
        nameInput = (EditText) findViewById(R.id.name_edit_view);
        ageInput = (EditText) findViewById(R.id.age_edit_view);
        bioField = (EditText) findViewById(R.id.bio_edit_view);
        circularImageView = (CircularImageView) findViewById(R.id.profilepicture);
        nextBtn = (Button) findViewById(R.id.profile_continue_button);
        codeEditText = (EditText) findViewById(R.id.code_phone_edit_view);
        phoneEditText = (EditText) findViewById(R.id.phone_edit_view);
        nationality = "";

        if (FacebookLogin.facebook_profile_pic != null) {
            circularImageView.setImageURI(FacebookLogin.facebook_profile_pic);
            //MapActivity.other_user_picture.setImageURI(FacebookLogin.facebook_profile_pic);
        }

        locales = Locale.getAvailableLocales();
        final ArrayList<String> languageList = new ArrayList<String>();
        for (Locale locale : locales) {
            String lang = locale.getDisplayLanguage();

            if (lang.trim().length() > 0 && !languageList.contains(lang)) {

                languageList.add(lang);
            }
        }
        Collections.sort(languageList);

        ArrayList<String> countriesList = new ArrayList<String>();
        for (Locale locale : locales) {
            String country = locale.getDisplayCountry();
            if (country.trim().length() > 0 && !countriesList.contains(country)) {
                countriesList.add(country);
            }
        }

        Collections.sort(countriesList);
        ArrayAdapter<String> citizenAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, countriesList);

        citizenAuto.setAdapter(citizenAdapter);
        citizenAuto.setThreshold(1);
        ArrayAdapter<String> languageAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, languageList);
        spokenLanguage.setTokenizer(new MultiAutoCompleteTextView.CommaTokenizer());
        spokenLanguage.setAdapter(languageAdapter);
        //Set OnClick Listener for the profile picture pressed
        circularImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Set OnClick Listener for the profile picture button
                selectImage();
            }
        });

        //SETUP GET user profile
        AsyncHttpClient client = new AsyncHttpClient();
        if (SignIn.static_token != null) {
            client.addHeader("Authorization", "Token " + SignIn.static_token);
        }

        //GET user profile
        client.get(MainActivity.base_host_url + "api/getProfile/", new JsonHttpResponseHandler() {

            @Override
            public void onSuccess(int statusCode, Header[] headers, final JSONObject response) {
                Log.w("GET PROFILE SUCCESS", statusCode + ": " + "Response = " + response.toString());
                Log.w("Response", response.toString());
                String langName = null;
                try {
                    String phoneNumber = response.getString("phone");
                    if (phoneNumber.equals("555-555-5555")) {
                        phoneEditText.setText("");
                    } else {
                        MainActivity.new_user = false;
                        Intent intent = new Intent(NewWhoAreYouActivity.this, MapActivity.class);
                        NewWhoAreYouActivity.this.startActivity(intent);
                        NewWhoAreYouActivity.this.finish();
                        phoneEditText.setText(phoneNumber);
                    }
                    String firstNameString = response.getString("first_name");
                    if (firstNameString.equals("anonymous")) {
                        nameInput.setText("");
                    } else {
                        nameInput.setText(firstNameString);

                    }

                    int age = response.getInt("age");
                    if (age > 0) {
                        ageInput.setText("" + age);
                    } else if (age == 0) {
                        ageInput.setText("");
                    }

                    String bio = response.getString("bio");
                    if (bio.equals("Message Me To Find Out")) {
                        bioField.setText("");
                    } else {
                        bioField.setText(bio);
                    }

                    nationality = response.getString("home_nationality");
                    if (nationality.equals("Choose Citizenship")) {
                        citizenAuto.setText("");
                    } else {
                        citizenAuto.setText(nationality);
                    }
                    // Log.w("Language",response.getString("languages"));
                    JSONArray languagesArray = response.getJSONArray("languages");
                    for (int x = 0; x < languagesArray.length(); x++) {
                        JSONObject languageNames = languagesArray.getJSONObject(x);
                        if (x == 0) {
                            langName = languageNames.getString("name");
                            if (langName.equals("mute")) {
                                langName = "";
                            }
                        } else {
                            langName = langName + "," + languageNames.getString("name");
                        }
                        //  Log.w("langName",langName);
                        spokenLanguage.setText(langName);
                    }

                } catch (JSONException e) {
                    Log.w("JSON EXCEPTION", e.getMessage());
                }


                //GET Profile image from backend if not available from Facebook
                if (FacebookLogin.facebook_profile_pic == null) {
                    //GET The image file at the pictureURL
                    AsyncHttpClient client = new AsyncHttpClient();
                    try {
                        pictureURL = response.getString("picture");
                    } catch (JSONException e) {
                        Log.w("GET PROFILE JSON FAIL", e.getMessage().toString());
                    }

                    Picasso.with(getApplicationContext())
                            .load(MainActivity.base_host_url + pictureURL)
                            .into(circularImageView);
//                    Uri uri = Uri.fromFile(response);
//                    MapActivity.other_user_picture.setImageURI(uri);
//                    client.get(MainActivity.base_host_url + pictureURL, new FileAsyncHttpResponseHandler(getApplicationContext()) {
//
//                        @Override
//                        public void onSuccess(int statusCode, Header[] headers, File response) {
//                            Log.w("GET IMAGE SUCCESS", "Successfully Retrieved The Image");
//                            //Use the downloaded image as the profile picture
//                            Uri uri = Uri.fromFile(response);
//                            downloadedImage = response;
//                            circularImageView.setImageURI(uri);
//                            MapActivity.other_user_picture.setImageURI(uri);
//                        }
//
//                        @Override
//                        public void onFailure(int statusCode, Header[] headers, Throwable throwable, File file) {
//                            Log.w("GET IMAGE FAIL", "Could not retrieve image");
//                        }
//                    });
                } else {
                    Log.d("HELLO", "HELLO");
                    circularImageView.setImageURI(FacebookLogin.facebook_profile_pic);
                }
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONArray timeline) {
                Log.w("PROFILE SUCCESS ARRAY", statusCode + ": " + timeline.toString());

            }

            @Override
            public void onRetry(int retryNo) {
                // called when request is retried
            }

            @Override
            public void onFailure(int error_code, Header[] headers, String text, Throwable throwable) {
                Log.w("GET PROFILE FAIL", "Error Code: " + error_code + ",  " + text);
            }
        });

        nextBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //POST Profile

                submitApi();
            }
        });
        nameInput.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    submitApi();
                    return true;
                }
                return false;
            }
        });
        citizenAuto.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    submitApi();
                    return true;
                }
                return false;
            }
        });
        spokenLanguage.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    submitApi();
                    return true;
                }
                return false;
            }
        });
        ageInput.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    submitApi();
                    return true;
                }
                return false;
            }
        });
        bioField.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    submitApi();
                    return true;
                }
                return false;
            }
        });
        phoneEditText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    submitApi();
                    return true;
                }
                return false;
            }
        });
        codeEditText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    submitApi();
                    return true;
                }
                return false;
            }
        });
    }
    private void submitApi(){
        AsyncHttpClient client = new AsyncHttpClient();
        if (SignIn.static_token != null) {
            client.addHeader("Authorization", "Token " + SignIn.static_token);
        }

        RequestParams params = new RequestParams();
        params.setUseJsonStreamer(true);


        String name_input = nameInput.getText().toString();

        if (name_input.equals("")) {
            GradientDrawable gd = new GradientDrawable();
            gd.setCornerRadius(10);
            gd.setStroke(2, Color.rgb(255, 117, 0));
            nameInput.setBackground(gd);
        } else {
            GradientDrawable gd = new GradientDrawable();
            gd.setCornerRadius(10);
            gd.setStroke(2, Color.rgb(255, 255, 255));
            nameInput.setBackground(gd);
        }
        if (ageInput.getText().toString().equals("")) {
            GradientDrawable gd = new GradientDrawable();
            gd.setCornerRadius(10);
            gd.setStroke(2, Color.rgb(255, 117, 0));
            ageInput.setBackground(gd);
        } else {
            GradientDrawable gd = new GradientDrawable();
            gd.setCornerRadius(10);
            gd.setStroke(2, Color.rgb(255, 255, 255));
            ageInput.setBackground(gd);
        }
        if (citizenAuto.getText().toString().equals("")) {
            GradientDrawable gd = new GradientDrawable();
            gd.setCornerRadius(10);
            gd.setStroke(2, Color.rgb(255, 117, 0));
            citizenAuto.setBackground(gd);
        } else {
            GradientDrawable gd = new GradientDrawable();
            gd.setCornerRadius(10);
            gd.setStroke(2, Color.rgb(255, 255, 255));
            citizenAuto.setBackground(gd);
        }
        if (spokenLanguage.getText().toString().equals("")) {
            GradientDrawable gd = new GradientDrawable();
            gd.setCornerRadius(10);
            gd.setStroke(2, Color.rgb(255, 117, 0));
            spokenLanguage.setBackground(gd);
        } else {
            GradientDrawable gd = new GradientDrawable();
            gd.setCornerRadius(10);
            gd.setStroke(2, Color.rgb(255, 255, 255));
            spokenLanguage.setBackground(gd);
        }
        if (bioField.getText().toString().equals("")) {
            GradientDrawable gd = new GradientDrawable();
            gd.setCornerRadius(10);
            gd.setStroke(2, Color.rgb(255, 117, 0));
            bioField.setBackground(gd);
        } else {
            GradientDrawable gd = new GradientDrawable();
            gd.setCornerRadius(10);
            gd.setStroke(2, Color.rgb(255, 255, 255));
            bioField.setBackground(gd);
        }
        if (phoneEditText.getText().toString().equals("")) {
            GradientDrawable gd = new GradientDrawable();
            gd.setCornerRadius(10);
            gd.setStroke(2, Color.rgb(255, 117, 0));
            phoneEditText.setBackground(gd);
        } else {
            GradientDrawable gd = new GradientDrawable();
            gd.setCornerRadius(10);
            gd.setStroke(2, Color.rgb(255, 255, 255));
            phoneEditText.setBackground(gd);
        }
        if (codeEditText.getText().toString().equals("")) {
            GradientDrawable gd = new GradientDrawable();
            gd.setCornerRadius(10);
            gd.setStroke(2, Color.rgb(255, 117, 0));
            codeEditText.setBackground(gd);
        } else {
            GradientDrawable gd = new GradientDrawable();
            gd.setCornerRadius(10);
            gd.setStroke(2, Color.rgb(255, 255, 255));
            codeEditText.setBackground(gd);
        }
        if (name_input.equals("") || ageInput.getText().toString().equals("") || citizenAuto.getText().toString().equals("") || spokenLanguage.getText().toString().equals("") || bioField.getText().toString().equals("") || codeEditText.getText().toString().equals("") || phoneEditText.getText().toString().equals("")) {
            Toast.makeText(getApplicationContext(), errorMessage, Toast.LENGTH_LONG).show();
            errorMessage = "Some fields are empty";
        } else {

            //   Log.d("Phone Number", codeEditText.getText().toString() + phoneEditText.getText().toString());
            String codePhone = codeEditText.getText().toString() + phoneEditText.getText().toString();
            params.put("phone", codePhone);
            params.put("first_name", name_input);
            //MapActivity.other_user_name.setText(name_input);
            boolean checkAgeValidation = false;

            String bio_input = bioField.getText().toString();
            params.put("bio", bio_input);
            // MapActivity.other_user_about.setText(bio_input);

            nationality = citizenAuto.getText().toString();
            Locale[] nameCompare = Locale.getAvailableLocales();
            boolean compareCountryName = false;
            for (Locale locale : nameCompare) {
                String cc = locale.getDisplayCountry();
                if (cc.equals(nationality)) {
                    compareCountryName = true;
                    params.put("home_nationality", nationality);
                    //     MapActivity.other_user_citizenship.setText(nationality);
                    break;
                } else {
                    // Log.d("Profile message",errorMessage);
                    compareCountryName = false;
                    errorMessage = "Country name in not valid";
                }
            }
            int age_input = 0;
            age_input = Integer.parseInt(ageInput.getText().toString());
            // Log.d("Profile",age_input+"");
            if (age_input < 13 || age_input > 120) {
                checkAgeValidation = false;

                errorMessage = "Age should be between 13 and 120 ";
            } else {
                checkAgeValidation = true;
                params.put("age", age_input);
                //MapActivity.other_user_age.setText(age_input + "y-o");
            }

            String str = spokenLanguage.getText().toString();
            List<String> elephantList = Arrays.asList(str.split(","));
            boolean languageCountCheck = false;
            Log.d("Hello", spokenLanguage.getText().toString());
            Log.d("hello", elephantList.size() + "");
            if (elephantList.size() > 4) {
                languageCountCheck = false;
                errorMessage = "Cannot select more than three language";
            } else {
                JSONArray languagesArray = new JSONArray();
                try {

                    languageCountCheck = true;
                    for (int i = 0; i < elephantList.size(); i++) {
                        JSONObject json_i = new JSONObject();
                        if (elephantList.get(i).replaceAll("\\s+", "").equals("")) {

                        } else {
                            json_i.put("name", elephantList.get(i));
                            // Log.w("Language count",elephantList.get(i));
                            languagesArray.put(json_i);
                            //  Log.w("Language count",languagesArray.toString());
                        }
                    }
                } catch (JSONException e) {
                    Log.w("JSON Exception", e.toString());
                }
                params.put("languages", languagesArray);
                // Log.d("languages123",languagesArray.toString());
            }
            Log.d("Profile", checkAgeValidation + ":" + compareCountryName + ":" + languageCountCheck);
            if (compareCountryName && checkAgeValidation && languageCountCheck) {

                client.post(MainActivity.base_host_url + "api/postProfile2/", params, new JsonHttpResponseHandler() {
//                client.post("http://requestb.in/zzmhq6zz", params, new JsonHttpResponseHandler() {

                    @Override
                    public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                        Log.w("POST PROFILE SUCCESS", statusCode + ": " + "Response = " + response.toString());
//                        NewWhoAreYouActivity.this.finish();
                        finishProfileFlag = true;
                        if (finishPictureFlag && finishProfileFlag) {

                            NewWhoAreYouActivity.this.finish();
                        }
                    }

                    @Override
                    public void onSuccess(int statusCode, Header[] headers, JSONArray timeline) {
                        Log.w("POST PROFILE SUCCESS2", statusCode + ": " + timeline.toString());
                        // NewWhoAreYouActivity.this.finish();
                    }

                    @Override
                    public void onRetry(int retryNo) {
                        // called when request is retried
                        Log.w("POST PROFILE RETRY", "" + retryNo);
                    }

                    @Override
                    public void onFailure(int error_code, Header[] headers, String text, Throwable throwable) {
                        Log.w("POST PROFILE FAIL", "Error Code: " + error_code + "," + text);
                    }

                    @Override
                    public void onFailure(int error_code, Header[] headers, Throwable throwable, JSONObject json) {
                        Log.w("MY PROFILE FAIL", "Error Code: " + error_code + ",  " + json.toString());
                    }
                });

                if (selectImageFlag) {
                    String root = Environment.getExternalStorageDirectory().toString();
                    File myDir = new File(root + "/FriendnGo");

                    File file = new File(myDir, imageName);
                    //Log.w("PICTURE PATH",myFile.toString());
                    RequestParams paramsProfilePicture = new RequestParams();
                    // paramsProfilePicture.setUseJsonStreamer(true);
                    try {
                        paramsProfilePicture.put("picture", file);

                    } catch (FileNotFoundException e) {
                    }
                    client.post(MainActivity.base_host_url + "api/uploadProfilePicture/", paramsProfilePicture, new JsonHttpResponseHandler() {
                        // client.post("http://requestb.in/zzmhq6zz", params, new JsonHttpResponseHandler() {

                        @Override
                        public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                            Log.w("POST PROFILE PICTURE", statusCode + ": " + "Response = " + response.toString());
                            finishPictureFlag = true;
                            if (finishPictureFlag && finishProfileFlag) {
                                NewWhoAreYouActivity.this.finish();
                            }
                        }

                        @Override
                        public void onSuccess(int statusCode, Header[] headers, JSONArray timeline) {
                            Log.w("POST PROFILE PICTURE2", statusCode + ": " + timeline.toString());
                            //  NewWhoAreYouActivity.this.finish();
                        }

                        @Override
                        public void onRetry(int retryNo) {
                            // called when request is retried
                            Log.w("POST PROFILE PICTURE", "" + retryNo);
                        }

                        @Override
                        public void onFailure(int error_code, Header[] headers, String text, Throwable throwable) {
                            Log.w("POST PROFILE PICTURE", "Error Code: " + error_code + "," + text);
                        }

                        @Override
                        public void onFailure(int error_code, Header[] headers, Throwable throwable, JSONObject json) {
                            Log.w("MY PROFILE PICTURE", "Error Code: " + error_code + ",  " + json.toString());
                        }
                    });
                } else {

                    finishPictureFlag = true;
                    if (finishPictureFlag && finishProfileFlag) {


                    }
                }
                Intent intent = new Intent(getApplicationContext(), MyCity.class);
                NewWhoAreYouActivity.this.startActivity(intent);
                NewWhoAreYouActivity.this.finish();
            } else {
                Toast.makeText(getApplicationContext(), errorMessage, Toast.LENGTH_LONG).show();
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case ImagePhotoPermission.MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    if (userChoosenTask.equals("Take Photo"))
                        cameraIntent();
                    else if (userChoosenTask.equals("Choose from Library"))
                        galleryIntent();
                } else {
                    //code for deny
                }
                break;
        }
    }

    private void selectImage() {
        final CharSequence[] items = {"Take Photo", "Choose from Library",
                "Cancel"};

        AlertDialog.Builder builder = new AlertDialog.Builder(NewWhoAreYouActivity.this);
        builder.setTitle("Add Photo!");
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                boolean result = ImagePhotoPermission.checkPermission(NewWhoAreYouActivity.this);

                if (items[item].equals("Take Photo")) {
                    userChoosenTask = "Take Photo";
                    if (result)
                        cameraIntent();

                } else if (items[item].equals("Choose from Library")) {
                    userChoosenTask = "Choose from Library";
                    if (result)
                        galleryIntent();

                } else if (items[item].equals("Cancel")) {
                    dialog.dismiss();
                }
            }
        });
        builder.show();
    }

    private void galleryIntent() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);//
        startActivityForResult(Intent.createChooser(intent, "Select File"), SELECT_FILE);
    }

    private void cameraIntent() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(intent, REQUEST_CAMERA);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == SELECT_FILE)
                onSelectFromGalleryResult(data);
            else if (requestCode == REQUEST_CAMERA)
                onCaptureImageResult(data);
        }
    }

    private void onCaptureImageResult(Intent data) {
        Bitmap thumbnail = (Bitmap) data.getExtras().get("data");
//        imageName =  "profile" + ".jpg";
//        destination = new File(Environment.getExternalStorageDirectory(),
//               imageName);
        SaveImage(thumbnail);
        circularImageView.setImageBitmap(thumbnail);
    }

    @SuppressWarnings("deprecation")
    private void onSelectFromGalleryResult(Intent data) {
        Bitmap bm = null;
        if (data != null) {
            try {
                bm = MediaStore.Images.Media.getBitmap(getApplicationContext().getContentResolver(), data.getData());
                SaveImage(bm);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
//        imageName =  "profile" + ".jpg";
//        destination = new File(Environment.getExternalStorageDirectory(),
//                imageName);
//        Matrix matrix = new Matrix();
////        matrix.postRotate(270);
//
//        Bitmap rotated = Bitmap.createBitmap(bm, 0, 0, bm.getWidth(), bm.getHeight(),
//                matrix, true);
        circularImageView.setImageBitmap(bm);
    }

    private void SaveImage(Bitmap finalBitmap) {
        //MapActivity.other_user_picture.setImageBitmap(finalBitmap);
        selectImageFlag = true;
        String root = Environment.getExternalStorageDirectory().toString();
        File myDir = new File(root + "/FriendnGo");
        myDir.mkdirs();
        Random generator = new Random();
        int n = 10000;
        n = generator.nextInt(n);
        imageName = "Image-" + n + ".jpg";
        File file = new File(myDir, imageName);
        Log.d("path", file.toString());

        if (file.exists()) file.delete();
        try {
            FileOutputStream out = new FileOutputStream(file);
            finalBitmap.compress(Bitmap.CompressFormat.JPEG, 90, out);
            out.flush();
            out.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
