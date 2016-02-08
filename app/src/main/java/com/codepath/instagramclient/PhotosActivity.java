package com.codepath.instagramclient;

import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.widget.ListView;

import com.codepath.instagramclient.models.InstagramPhoto;
import com.codepath.instagramclient.models.InstagramPhotoComment;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import cz.msebera.android.httpclient.Header;

public class PhotosActivity extends AppCompatActivity implements SwipeRefreshLayout.OnRefreshListener {

    private ArrayList<InstagramPhoto> iPhotosList;
    private InstagramPhotosAdapter adapter;
    private ListView lvPhotos;
    private SwipeRefreshLayout swipeContainer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photos);

        swipeContainer = (SwipeRefreshLayout) findViewById(R.id.swipeContainer);
        swipeContainer.setOnRefreshListener(this);

        iPhotosList = new ArrayList<>();
        adapter = new InstagramPhotosAdapter(this, iPhotosList);
        lvPhotos = (ListView) findViewById(R.id.lvPhotos);
        lvPhotos.setAdapter(adapter);
        // send out API request to POPULAR PHOTOS
        fetchPopularPhotos();
    }

    // trigger API request
    private void fetchPopularPhotos() {
        String url = "https://api.instagram.com/v1/media/popular?client_id="
                + getString(R.string.instagramClientId);
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
                    photosArr = response.getJSONArray( getString(R.string.data) );
                    for (int i = 0; i < photosArr.length(); i++) {
                        JSONObject photoJson = photosArr.getJSONObject(i);
                        if ( "image".equals( photoJson.getString( getString(R.string.type) ) ) ) {
                            String userName = "",
                                    userProfilePicUrl = "",
                                    caption = "",
                                    imageUrl = "";
                            int imageHeight = 0,
                                    likesCount = 0;
                            long createdTime = 0;

                            JSONObject userJson = photoJson.optJSONObject( getString(R.string.user) );
                            if (userJson != null) {
                                userName = userJson.optString( getString(R.string.username) );
                                userProfilePicUrl = userJson.optString( getString(R.string.profile_picture) );
                            }

                            JSONObject captionJson = photoJson.optJSONObject( getString(R.string.caption) );
                            if (captionJson != null)
                                caption = captionJson.optString( getString(R.string.text) );

                            JSONObject likesJson = photoJson.optJSONObject( getString(R.string.likes) );
                            if (likesJson != null)
                                likesCount = likesJson.optInt( getString(R.string.count) );

                            JSONObject imagesJson = photoJson.optJSONObject( getString(R.string.images) );
                            JSONObject stdResImgJson = null;
                            if (imagesJson != null)
                                stdResImgJson = imagesJson.optJSONObject( getString(R.string.standard_resolution) );
                            if (stdResImgJson != null) {
                                imageUrl = stdResImgJson.optString( getString(R.string.url) );
                                imageHeight = stdResImgJson.optInt( getString(R.string.height) );
                            }

                            createdTime = photoJson.optLong( getString(R.string.created_time) );

                            // get comments
                            List<InstagramPhotoComment> comments = new ArrayList<>();
                            int commentsCount = 0;
                            JSONObject commentsJson = photoJson.optJSONObject( getString(R.string.comments) );
                            if (commentsJson != null) {
                                commentsCount = commentsJson.optInt( getString(R.string.count) );
                                JSONArray commentsDataArr = commentsJson.optJSONArray( getString(R.string.data) );
                                if (commentsDataArr != null) {
                                    for (int j = 0; j < commentsDataArr.length(); j++) {
                                        JSONObject jo = commentsDataArr.getJSONObject(j);
                                        long cCreatedTime = jo.getLong(getString(R.string.created_time));
                                        String cText = jo.getString(getString(R.string.text));
                                        String cUserName = "";
                                        JSONObject cFromJson = jo.optJSONObject( getString(R.string.from) );
                                        if (cFromJson != null)
                                            cUserName = cFromJson.optString( getString(R.string.username) );

                                        InstagramPhotoComment ipc = new InstagramPhotoComment(cUserName, cText, cCreatedTime);
                                        // comments in the response already sorted in ascending order
                                        // if putting each at the beginning, the first item is always the latest one
                                        comments.add(0, ipc);
                                    }
                                }
                            }

                            InstagramPhoto photo = new InstagramPhoto(
                                    userName, userProfilePicUrl, caption, imageUrl, imageHeight, likesCount, createdTime);
                            photo.setComments(comments);
                            photo.setCommentsCount(commentsCount);

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

    @Override
    public void onRefresh() {
        new android.os.Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                iPhotosList = new ArrayList<>();
                adapter = new InstagramPhotosAdapter(PhotosActivity.this, iPhotosList);
                lvPhotos = (ListView) findViewById(R.id.lvPhotos);
                lvPhotos.setAdapter(adapter);
                fetchPopularPhotos();

                swipeContainer.setRefreshing(false);
            }
        }, 1000);
    }
}
