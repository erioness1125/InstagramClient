package com.codepath.instagramclient;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ListView;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import cz.msebera.android.httpclient.Header;

public class PhotosActivity extends AppCompatActivity {

    private static final String CLIENT_ID = "e05c462ebd86446ea48a5af73769b602";

    private ArrayList<InstagramPhoto> iPhotosList;

    private InstagramPhotosAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photos);
        iPhotosList = new ArrayList<>();
        adapter = new InstagramPhotosAdapter(this, iPhotosList);
        ListView lvPhotos = (ListView) findViewById(R.id.lvPhotos);
        lvPhotos.setAdapter(adapter);
        // send out API request to POPULAR PHOTOS
        fetchPopularPhotos();
    }

    // trigger API request
    private void fetchPopularPhotos() {
        String url = "https://api.instagram.com/v1/media/popular?client_id=" + CLIENT_ID;
        // create the network client
        AsyncHttpClient client = new AsyncHttpClient();
        // trigger GET request
        client.get(url, null, new JsonHttpResponseHandler() {
            // onSuccess (worked)
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
//                super.onSuccess(statusCode, headers, response);
                // iterate each of the photo items and decode it into a java object
                JSONArray photosArr = null;
                try {
                    photosArr = response.getJSONArray("data");
                    for (int i = 0; i < photosArr.length(); i++) {
                        JSONObject photoJson = photosArr.getJSONObject(i);
                        if ( "image".equals( photoJson.getString("type") ) ) {
                            String userName = "",
                                    caption = "",
                                    imageUrl = "";
                            int imageHeight = 0,
                                    likesCount = 0;

                            JSONObject userJson = photoJson.optJSONObject("user");
                            if (userJson != null)
                                userName = userJson.optString("username");

                            JSONObject captionJson = photoJson.optJSONObject("caption");
                            if (captionJson != null)
                                caption = captionJson.optString("text");

                            JSONObject likesJson = photoJson.optJSONObject("likes");
                            if (likesJson != null)
                                likesCount = likesJson.optInt("count");

                            JSONObject imagesJson = photoJson.optJSONObject("images");
                            JSONObject stdResImgJson = null;
                            if (imagesJson != null)
                                stdResImgJson = imagesJson.optJSONObject("standard_resolution");
                            if (stdResImgJson != null) {
                                imageUrl = stdResImgJson.optString("url");
                                imageHeight = stdResImgJson.optInt("height");
                            }

                            InstagramPhoto photo = new InstagramPhoto(userName, caption, imageUrl, imageHeight, likesCount);
                            iPhotosList.add(photo);
                        }
                    }

                    adapter.notifyDataSetChanged();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            // onFailure
            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                super.onFailure(statusCode, headers, responseString, throwable);
            }
        });
    }
}
