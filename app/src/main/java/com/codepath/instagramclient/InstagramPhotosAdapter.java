package com.codepath.instagramclient;

import android.content.Context;
import android.text.format.DateUtils;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.makeramen.roundedimageview.RoundedTransformationBuilder;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Transformation;

import java.util.List;

public class InstagramPhotosAdapter extends ArrayAdapter<InstagramPhoto> {
    public InstagramPhotosAdapter(Context context, List<InstagramPhoto> objects) {
        super(context, R.layout.item_photos, objects);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        InstagramPhoto photo = getItem(position);

        ViewHolder viewHolder; // view lookup cache stored in tag

        // Check if an existing view is being reused, otherwise inflate the view
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_photos, parent, false);

            viewHolder = new ViewHolder();
            viewHolder.ibUserProfilePic = (ImageButton) convertView.findViewById(R.id.ibUserProfilePic);
            viewHolder.tvUserName = (TextView) convertView.findViewById(R.id.tvUserName);
            viewHolder.tvWhenCreated = (TextView) convertView.findViewById(R.id.tvWhenCreated);
            viewHolder.ivPhoto = (ImageView) convertView.findViewById(R.id.ivPhoto);
            viewHolder.tvCaption = (TextView) convertView.findViewById(R.id.tvCaption);
            viewHolder.tvLikes = (TextView) convertView.findViewById(R.id.tvLikes);

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
        DisplayMetrics displayMetrics = getContext().getResources().getDisplayMetrics();
        Picasso.with(getContext()).load(photo.getImageUrl())
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
        Picasso.with(getContext()).load(photo.getUserProfilePicUrl())
                .resize(80, 80)
                .transform(circleTransformation)
                .into(viewHolder.ibUserProfilePic);

        // get and set likes count
        viewHolder.tvLikes.setText(photo.getLikesCount() + " likes");

        // get and set createdTime
        viewHolder.tvWhenCreated.setText(
                DateUtils.getRelativeTimeSpanString(
                        photo.getCreatedTime() * 1000, System.currentTimeMillis(), DateUtils.SECOND_IN_MILLIS
                )
        );

        return convertView;
    }

    private static class ViewHolder {
        TextView tvUserName;
        TextView tvCaption;
        TextView tvLikes;
        TextView tvWhenCreated;
        ImageView ivPhoto;
        ImageButton ibUserProfilePic;
    }
}
