package com.codepath.instagramclient;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.ListView;

import com.codepath.instagramclient.models.InstagramPhotoComment;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import cz.msebera.android.httpclient.Header;

public class CommentsActivity extends AppCompatActivity {

    private CommentsAdapter adapter;
    private ListView lvAllComments;

    private List<InstagramPhotoComment> commentsList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comments);

        commentsList = new ArrayList<>();
        adapter = new CommentsAdapter(this, commentsList);
        lvAllComments = (ListView) findViewById(R.id.lvAllComments);
        lvAllComments.setAdapter(adapter);

        fetchAllComments(
                getIntent().getStringExtra(
                        getString(R.string.id)
                )
        );
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_comments, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_back:
                finish();
                break;
        }
        return true;
    }

    // trigger API request
    private void fetchAllComments(String id) {
        String url = "https://api.instagram.com/v1/media/"
                + id
                + "/comments?client_id="
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
                JSONArray commentsArr = null;
                try {
                    commentsArr = response.getJSONArray(getString(R.string.data));
                    for (int i = 0; i < commentsArr.length(); i++) {
                        String userName = "", text = "";
                        long createdTime = 0;

                        JSONObject commentJson = commentsArr.optJSONObject(i);
                        if (commentJson != null) {
                            // get created_time
                            createdTime = commentJson.optLong( getString(R.string.created_time) );

                            // get text
                            text = commentJson.optString( getString(R.string.text) );

                            // get username
                            JSONObject fromJson = commentJson.optJSONObject( getString(R.string.from) );
                            if (fromJson != null) {
                                userName = fromJson.optString( getString(R.string.username) );
                            }

                            InstagramPhotoComment comment = new InstagramPhotoComment(userName, text, createdTime);
                            commentsList.add(0, comment);
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
