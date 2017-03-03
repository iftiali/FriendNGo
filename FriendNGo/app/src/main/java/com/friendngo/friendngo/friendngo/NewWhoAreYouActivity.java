package com.friendngo.friendngo.friendngo;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Matrix;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import com.mikhaellopez.circularimageview.CircularImageView;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Locale;

import io.apptik.widget.multiselectspinner.MultiSelectSpinner;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class NewWhoAreYouActivity extends AppCompatActivity {
    private static final int CAMERA_REQUEST = 1888;
    CircularImageView circularImageView;
    Button nextBtn;
    EditText nameInput;

    private int REQUEST_CAMERA = 0, SELECT_FILE = 1;
    private Button btnSelect;
    EditText ageInput;
    ImageView profilePicture;
    String pictureURL = "";
    File directory;
    File downloadedImage;
    File myFile;
    private String userChoosenTask;
    Bitmap photo;

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

        nameInput = (EditText) findViewById(R.id.name_edit_view);
        ageInput = (EditText) findViewById(R.id.age_edit_view);
        circularImageView = (CircularImageView) findViewById(R.id.profilepicture);
        nextBtn = (Button) findViewById(R.id.profile_continue_button);

        nextBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(NewWhoAreYouActivity.this, MyCity.class);
                NewWhoAreYouActivity.this.startActivity(intent);
                NewWhoAreYouActivity.this.finish();
            }
        });
        Locale[] locales = Locale.getAvailableLocales();
        ArrayList<String> languageList = new ArrayList<String>();
        for (Locale locale : locales) {
            String lang = locale.getDisplayLanguage();

            if (lang.trim().length() > 0 && !languageList.contains(lang)) {
                if (lang.equals("English") || lang.equals("French") || lang.equals("Spanish")) {

                } else {
                    languageList.add(lang);
                }
            }
        }
        Collections.sort(languageList);
        languageList.add(0, "English");
        languageList.add(1, "French");
        languageList.add(2, "Spanish");
        MultiSelectSpinner multiSelectSpinnerLanguage = (MultiSelectSpinner) findViewById(R.id.language_spinner);
        multiSelectSpinnerLanguage.setItems(languageList)

                .setListener(new MultiSelectSpinner.MultiSpinnerListener() {
                    @Override
                    public void onItemsSelected(boolean[] selected) {

                    }
                })
                .setSpinnerItemLayout(R.layout.custom_spinner_item)
                .setAllCheckedText("All types")
                .setAllUncheckedText("Spoken languages")

                .setSelectAll(false)
        ;
        multiSelectSpinnerLanguage.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String item = (String) parent.getItemAtPosition(position);
                // Log.w("TRYTRY",item);
                if (item.equals("Spoken languages"))
                    ((TextView) view).setTextColor(Color.GRAY);

                else
                    ((TextView) view).setTextColor(Color.BLACK);
                //Change selected text color
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        ArrayList<String> countriesList = new ArrayList<String>();
        for (Locale locale : locales) {
            String country = locale.getDisplayCountry();
            if (country.trim().length() > 0 && !countriesList.contains(country)) {
                countriesList.add(country);
            }
        }

        Collections.sort(countriesList);

        Spinner nationalityInputSpinner = (Spinner) findViewById(R.id.citizen_spinner);
        ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(
                this, R.layout.spinner_item, countriesList);

        nationalityInputSpinner.setAdapter(spinnerArrayAdapter);
        nationalityInputSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                if (position == 0) {
                    ((TextView) view).setText("Citizen");
                    ((TextView) view).setTextColor(Color.GRAY);

                } else
                    ((TextView) view).setTextColor(Color.BLACK);
                //Change selected text color
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });



        //Set OnClick Listener for the profile picture pressed
        circularImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Set OnClick Listener for the profile picture button
                selectImage();
            }
        });

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
        //ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        //thumbnail.compress(Bitmap.CompressFormat.JPEG, 180, bytes);

        File destination = new File(Environment.getExternalStorageDirectory(),
                System.currentTimeMillis() + ".jpg");


        Matrix matrix = new Matrix();
        matrix.postRotate(270);

        Bitmap rotated = Bitmap.createBitmap(thumbnail, 0, 0, thumbnail.getWidth(), thumbnail.getHeight(),
                matrix, true);
        circularImageView.setImageBitmap(rotated);
    }

    @SuppressWarnings("deprecation")
    private void onSelectFromGalleryResult(Intent data) {

        Bitmap bm = null;
        if (data != null) {
            try {
                bm = MediaStore.Images.Media.getBitmap(getApplicationContext().getContentResolver(), data.getData());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        Matrix matrix = new Matrix();
        matrix.postRotate(270);

        Bitmap rotated = Bitmap.createBitmap(bm, 0, 0, bm.getWidth(), bm.getHeight(),
                matrix, true);
        circularImageView.setImageBitmap(rotated);
    }

}
