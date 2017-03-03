package wichura.de.camperapp.http;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;
import android.util.Log;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.FileAsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import java.io.File;
import java.io.FileNotFoundException;

import cz.msebera.android.httpclient.Header;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import wichura.de.camperapp.activity.NewAdActivity;
import wichura.de.camperapp.mainactivity.Constants;
import wichura.de.camperapp.models.RowItem;
import wichura.de.camperapp.util.BitmapHelper;

/**
 * Created by ich on 25.10.2016.
 * Camper App
 */

public class FileUploadService {

    private Context context;
    private NewAdActivity view;
    private Integer adId;

    public FileUploadService(Context context, NewAdActivity view) {
        this.context = context;
        this.view = view;

    }


    public void multiPost(Intent data) {

        //view.showProgress();

        Service service = new Service();

        String picture = data.getStringExtra(Constants.FILENAME);

        RowItem item = new RowItem();
        item.setTitle(data.getStringExtra(Constants.TITLE));
        item.setDescription(data.getStringExtra(Constants.DESCRIPTION));
        item.setPrice(data.getStringExtra(Constants.PRICE));
        item.setDate(data.getLongExtra(Constants.DATE, 0));
        item.setLat(getLat());
        item.setLng(getLng());

        /*double[] latlng = {getLat(), getLng()};
        Location location = new Location();
        location.setCoordinates(latlng);
        item.setLocation(location);*/

        service.saveNewAdObserv(getUserToken(), item)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<RowItem>() {
                    @Override
                    public void onCompleted() {
                        multiPost_old(adId, picture);
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.d("CONAN", "error in upload Ad1 " + e.toString());
                    }

                    @Override
                    public void onNext(RowItem rowItem) {
                        adId = rowItem.getId();
                        Log.d("CONAN", "neue id " + adId);
                    }
                });
    }

    private void multiPost_old(Integer adId, String picture) {

        Uri fileUri = Uri.parse(picture.toString());
        Log.i("CONAN", "fileURI: " + fileUri);

        //thumbnail?
        // Bitmap ThumbImage = ThumbnailUtils.extractThumbnail(BitmapFactory.decodeFile(imagePath), THUMBSIZE, THUMBSIZE);

        String fileString = getRealPathFromUri(context, fileUri);
        Log.i("CONAN", "fileString: " + fileString);


        File file = new File(fileString.toString());
        Log.d("CONAN", "file: " + file);
        BitmapHelper bitmapHelper = new BitmapHelper(context);
        final File reducedPicture = bitmapHelper.saveBitmapToFile(file);
        Log.d("CONAN", "newFile: " + reducedPicture);
        Log.d("CONAN", "adId: " + adId);


        //todo: add more pics here??
        RequestParams params = new RequestParams();
        try {
            params.put("file", reducedPicture);
        } catch (FileNotFoundException e) {
        }

        params.put("articleId", adId);
        params.put("token", getUserToken());
        Log.d("CONAN", "userid bei upload: " + getUserId());
        Log.d("CONAN", "url for upload: " + Urls.MAIN_SERVER_URL_V3 + "ads/" + adId + "\u002F" + "addPicture");

        AsyncHttpClient client = new AsyncHttpClient();
        client.post(Urls.MAIN_SERVER_URL_V3 + "articles/" + adId + "\u002F" + "addPicture", params, new FileAsyncHttpResponseHandler(reducedPicture) {
            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, File file) {
                Toast.makeText(context, "Upload did not work!", Toast.LENGTH_SHORT).show();
                Log.d("CONAN", "error in upload file to server: " + throwable.getMessage());
                //view.hideProgress();
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, File file) {
                Log.d("CONAN", "Picture uploaded");
                Toast.makeText(context, "Upload...done!", Toast.LENGTH_SHORT).show();
                //jetzt bilder neu laden!! finish() geht zu früh zurück in mainactivity
                Boolean deletetd = reducedPicture.delete();
                if (!deletetd)
                    Toast.makeText(context, "Delete tempFile not possible", Toast.LENGTH_SHORT).show();
                // view.hideProgress();
                view.finish();
            }
        });
    }

    private static String getRealPathFromUri(Context context, Uri contentUri) {
        Cursor cursor = null;
        try {
            String[] proj = {MediaStore.Images.Media.DATA};
            cursor = context.getContentResolver().query(contentUri, proj, null, null, null);
            int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            cursor.moveToFirst();
            return cursor.getString(column_index);
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
    }

    private String getUserId() {
        SharedPreferences settings = context.getSharedPreferences(Constants.SHARED_PREFS_USER_INFO, 0);
        return settings.getString(Constants.USER_ID, "");
    }

    private Double getLat() {
        SharedPreferences settings = context.getSharedPreferences(Constants.USERS_LOCATION, 0);
        return Double.longBitsToDouble(settings.getLong(Constants.LAT, 0));
    }

    private Double getLng() {
        SharedPreferences settings = context.getSharedPreferences(Constants.USERS_LOCATION, 0);
        return Double.longBitsToDouble(settings.getLong(Constants.LNG, 0));
    }

    private String getUserToken() {
        SharedPreferences settings = context.getSharedPreferences(Constants.SHARED_PREFS_USER_INFO, 0);
        return settings.getString(Constants.USER_TOKEN, "");
    }
}
