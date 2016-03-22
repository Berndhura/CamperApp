package wichura.de.camperapp.ad;

import android.app.Activity;
import android.graphics.Point;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.squareup.picasso.Picasso;

import wichura.de.camperapp.R;
import wichura.de.camperapp.app.AppController;
import wichura.de.camperapp.http.Urls;

public class OpenAdActivity extends Activity {

    private String pictureUri;
    private TextView mTitleText;
    private TextView mDescText;
    private TextView mLocationText;
    private TextView mPhoneText;
    private Button mDelButton;
    private String mAdId;

    private ImageView imgView;

    //TODO
    //use swipe function from CustomSwipeAdapter to swipe pictures
    private CustomSwipeAdapter adapter;
    private ImageView viewPager;

    ImageLoader imageLoader = AppController.getInstance().getImageLoader();

    @Override
    protected void onCreate(final Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        setContentView(R.layout.open_ad_activity);

        //TODO
        //swipe the pictures, preparation
        adapter = new CustomSwipeAdapter(this);
        //error! this has to be a viewpager obj in layout xml file! TODO!
        viewPager = (ImageView) findViewById(R.id.icon);
        //viewPager.setAdapter(adapter);


        TextView titelHeader = (TextView) findViewById(R.id.headerTitel);
        mTitleText = (TextView) findViewById(R.id.title);
        TextView desHeader = (TextView) findViewById(R.id.headerDesciption);
        mDescText = (TextView) findViewById(R.id.description);
        mLocationText = (TextView) findViewById(R.id.location);
        mPhoneText = (TextView) findViewById(R.id.phone);
        imgView = (ImageView) findViewById(R.id.icon);
        mDelButton = (Button) findViewById(R.id.delButton);


        //get data from Intent
        pictureUri = getIntent().getStringExtra("uri");

        mTitleText.setText(getIntent().getStringExtra("title"));
        mDescText.setText(getIntent().getStringExtra("description"));
        mLocationText.setText(getIntent().getStringExtra("location"));
        mPhoneText.setText(getIntent().getStringExtra("phone"));
        mAdId = getIntent().getStringExtra("id");

        //Displaygroesse
        Display display = getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        Log.i("CONAN", "X " +  size.x);
        Log.i("CONAN", "Y " +  size.y);


        ImageView picture = (ImageView) imgView
                .findViewById(R.id.icon);

        Picasso.with(getApplicationContext())
                .load(pictureUri)
                .resize(100, 100)
                .centerCrop()
                .into(imgView);

        mDelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //get ad id and send delete request
                String adId = getIntent().getStringExtra("id");
                Log.i("CONAN", "ApId: " + mAdId);
                deleteAdRequest(adId);
            }
        });

        Log.i("CONAN", "MyClass.getView() OPEN " + pictureUri);
    }

    private void deleteAdRequest(String adId) {
        // Instantiate the RequestQueue.
        RequestQueue queue = Volley.newRequestQueue(this);
        String url = Urls.MAIN_SERVER_URL + Urls.DELETE_AD_WITH_APID + "?adid=" + adId;

        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        finish();
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(OpenAdActivity.this, "Something went wrong...", Toast.LENGTH_LONG).show();
            }
        });

        queue.add(stringRequest);
    }


}
