package wichura.de.camperapp.presentation;

import android.content.Context;
import android.util.Log;

import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import wichura.de.camperapp.activity.OpenAdActivity;
import wichura.de.camperapp.http.Service;
import wichura.de.camperapp.mainactivity.Constants;
import wichura.de.camperapp.models.Bookmarks;

import static wichura.de.camperapp.mainactivity.Constants.SHARED_PREFS_USER_INFO;

/**
 * Created by ich on 13.11.2016.
 * CamperApp
 */

public class OpenAdPresenter {

    private Service service;
    private Context context;
    private OpenAdActivity view;
    private Subscription subscription;

    public OpenAdPresenter(OpenAdActivity myAdsActivity, Service service, Context applicationContext) {
        this.service = service;
        this.context = applicationContext;
        this.view = myAdsActivity;
    }

    public void loadBookmarksForUser() {
        service.getBookmarksForUserObserv(getUserId()).subscribeOn(Schedulers.newThread())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<Bookmarks>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.d("CONAN", "error loading bookmarks: " + e.getMessage());
                    }

                    @Override
                    public void onNext(Bookmarks bookmarks) {
                        view.updateBookmarkButton(bookmarks);
                    }
                });
    }

    public void increaseViewCount(String adId) {
        service.increaseViewCount(adId).subscribeOn(Schedulers.newThread())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<String>() {
                    @Override
                    public void onCompleted() {
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.d("CONAN", "error in increase view count: " + e.getMessage());
                    }

                    @Override
                    public void onNext(String result) {//nothing to update
                    }
                });
    }


    private String getUserId() {
        return context.getSharedPreferences(SHARED_PREFS_USER_INFO, 0).getString(Constants.USER_ID, "");
    }
}
