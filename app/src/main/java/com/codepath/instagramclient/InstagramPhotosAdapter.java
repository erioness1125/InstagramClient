package com.codepath.instagramclient;

import android.content.Context;
import android.content.Intent;
import android.support.v4.content.ContextCompat;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.format.DateUtils;
import android.text.style.ForegroundColorSpan;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.codepath.instagramclient.models.InstagramPhoto;
import com.codepath.instagramclient.models.InstagramPhotoComment;
import com.makeramen.roundedimageview.RoundedTransformationBuilder;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Transformation;

import java.util.List;

public class InstagramPhotosAdapter extends ArrayAdapter<InstagramPhoto> {

    static long currentTime = System.currentTimeMillis();

    private static final int HOW_MANY_LAST_COMMENTS = 2;

    public InstagramPhotosAdapter(Context context, List<InstagramPhoto> objects) {
        super(context, R.layout.item_photos, objects);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final InstagramPhoto photo = getItem(position);

        final Context context = getContext();
        ViewHolder viewHolder; // view lookup cache stored in tag

        // Check if an existing view is being reused, otherwise inflate the view
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.item_photos, parent, false);

            viewHolder = new ViewHolder();
            viewHolder.ibUserProfilePic = (ImageButton) convertView.findViewById(R.id.ibUserProfilePic);
            viewHolder.tvUserName = (TextView) convertView.findViewById(R.id.tvUserName);
            viewHolder.tvWhenCreated = (TextView) convertView.findViewById(R.id.tvWhenCreated);
            viewHolder.ivPhoto = (ImageView) convertView.findViewById(R.id.ivPhoto);
            viewHolder.tvCaption = (TextView) convertView.findViewById(R.id.tvCaption);
            viewHolder.tvLikes = (TextView) convertView.findViewById(R.id.tvLikes);
            viewHolder.llComments = (LinearLayout) convertView.findViewById(R.id.llComments);
            viewHolder.tvSeeAllComments = (TextView) convertView.findViewById(R.id.tvSeeAllComments);

            convertView.setTag(viewHolder);
        }
        else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        // Populate the data into the each view

        // get and set caption
        viewHolder.tvCaption.setText(photo.getCaption());

        // get and set photo
        // clear out the imageview so that the old image from the recycled view doesn't appear
        viewHolder.ivPhoto.setImageResource(0);
        // insert the image using Picasso (library)
        DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
        Picasso.with(context).load(photo.getImageUrl())
                .resize(displayMetrics.widthPixels, 0)
                .into(viewHolder.ivPhoto);

        // get and set username
        viewHolder.tvUserName.setText(photo.getUserName());

        // get and set user profile pic
        viewHolder.ibUserProfilePic.setImageResource(0);
        // display each user profile image using a RoundedImageViewDisplay each user profile image using a RoundedImageView
        Transformation circleTransformation = new RoundedTransformationBuilder()
                .cornerRadiusDp(30)
                .oval(false)
                .build();
        // insert the image using Picasso (library)
        Picasso.with(context).load(photo.getUserProfilePicUrl())
                .resize(80, 80)
                .transform(circleTransformation)
                .into(viewHolder.ibUserProfilePic);

        // get and set likes count
        viewHolder.tvLikes.setText(photo.getLikesCount() + " likes");

        // get and set createdTime
        viewHolder.tvWhenCreated.setText(
                DateUtils.getRelativeTimeSpanString(
                        photo.getCreatedTime() * 1000, currentTime, DateUtils.SECOND_IN_MILLIS
                )
        );

        // get and set comments
        viewHolder.llComments.removeAllViews();
        int count = 1;
        for (InstagramPhotoComment comment : photo.getComments()) {
            View c = viewHolder.llComments.inflate(context, R.layout.item_comments, null);
            TextView tvCComment = (TextView) c.findViewById(R.id.tvCComment);
            TextView tvCCreatedTime = (TextView) c.findViewById(R.id.tvCCreatedTime);
            String cUserName = comment.getUserName();

            // change color of username only
            SpannableString spannable = new SpannableString(cUserName + " " + comment.getText());
            spannable.setSpan(
                    new ForegroundColorSpan(ContextCompat.getColor(context, R.color.colorPrimary)),
                    0,
                    cUserName.length(),
                    Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            tvCComment.setText(spannable);

            tvCCreatedTime.setText(
                    DateUtils.getRelativeTimeSpanString(
                            comment.getCreatedTime() * 1000, currentTime, DateUtils.SECOND_IN_MILLIS
                    ), TextView.BufferType.SPANNABLE
            );
            viewHolder.llComments.addView(c);

            count++;
            if (count > HOW_MANY_LAST_COMMENTS)
                break;
        }

        // get and set the number of all comments
        viewHolder.tvSeeAllComments.setText("View all " + photo.getCommentsCount() + " comments");
        // set OnClickListener
        viewHolder.tvSeeAllComments.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // first parameter is the context, second is the class of the activity to launch
                Intent i = new Intent(context, CommentsActivity.class);
                // put "extras" into the bundle for access in the second activity
                i.putExtra(context.getString(R.string.id), photo.getId());

                context.startActivity(i);
            }
        });

        return convertView;
    }

    private static class ViewHolder {
        TextView tvUserName;
        TextView tvCaption;
        TextView tvLikes;
        TextView tvWhenCreated;
        TextView tvSeeAllComments;
        ImageView ivPhoto;
        ImageButton ibUserProfilePic;
        LinearLayout llComments;
    }
}
