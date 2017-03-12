package wichura.de.camperapp.presentation;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import rx.Observable;
import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import wichura.de.camperapp.activity.SearchActivity;
import wichura.de.camperapp.http.GoogleService;
import wichura.de.camperapp.mainactivity.Constants;

import static android.content.Context.MODE_PRIVATE;

/**
 * Created by ich on 18.02.2017.
 * deSurf
 */

public class LocationPresenter {

    private Context context;
    private GoogleService googleService;
    private Subscription subscription;
    private SearchActivity view;

    public LocationPresenter(Context applicationContext, SearchActivity view) {
        this.context = applicationContext;
        this.googleService = new GoogleService();
        this.view = view;
    }

    public void saveUsersLocation(Double lat, Double lng) {

        Observable<JsonObject> getCityNameFromLatLng = googleService.getCityNameFrimLatLngObserv(lat, lng, false);

        subscription = getCityNameFromLatLng
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<JsonObject>() {
                    @Override
                    public void onCompleted() {
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.d("CONAN", "error in getting city name from google maps api: " + e.toString());
                    }

                    @Override
                    public void onNext(JsonObject location) {
                        JsonElement city = location.get("results").getAsJsonArray()
                                .get(0).getAsJsonObject().get("address_components").getAsJsonArray()
                                .get(2).getAsJsonObject().get("long_name");

                        Log.d("CONAN", "city name from google maps api: " + city);
                        storeCityName(lat, lng, city.getAsString());
                    }
                });
    }

    private void storeCityName(Double lat, Double lng, String location) {

        SharedPreferences sp = context.getSharedPreferences(Constants.USERS_LOCATION, MODE_PRIVATE);
        SharedPreferences.Editor ed = sp.edit();
        ed.putLong(Constants.LAT, Double.doubleToRawLongBits(lat));
        ed.putLong(Constants.LNG, Double.doubleToRawLongBits(lng));
        ed.putString(Constants.LOCATION, location);
        ed.apply();

        //view.updateCity(location);
    }
}
