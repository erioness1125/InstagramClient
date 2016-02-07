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
        super(context, R.layout.support_simple_spinner_dropdown_item, objects);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        InstagramPhoto photo = getItem(position);
        if (convertView == null)
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_photos, parent, false);

        // get and set caption
        TextView tvCaption = (TextView) convertView.findViewById(R.id.tvCaption);
        tvCaption.setText(photo.getCaption());

        // get and set photo
        ImageView ivPhoto = (ImageView) convertView.findViewById(R.id.ivPhoto);
        // clear out the imageview so that the old image from the recycled view doesn't appear
        ivPhoto.setImageResource(0);
        // insert the image using Picasso (library)
        DisplayMetrics displayMetrics = getContext().getResources().getDisplayMetrics();
        Picasso.with(getContext()).load(photo.getImageUrl())
                .resize(displayMetrics.widthPixels, 0)
                .into(ivPhoto);

        // get and set username
        TextView tvUserName = (TextView) convertView.findViewById(R.id.tvUserName);
        tvUserName.setText(photo.getUserName());

        // get and set user profile pic
        ImageButton ibUserProfilePic = (ImageButton) convertView.findViewById(R.id.ibUserProfilePic);
        ibUserProfilePic.setImageResource(0);
        // display each user profile image using a RoundedImageViewDisplay each user profile image using a RoundedImageView
        Transformation circleTransformation = new RoundedTransformationBuilder()
                .cornerRadiusDp(30)
                .oval(false)
                .build();
        // insert the image using Picasso (library)
        Picasso.with(getContext()).load(photo.getUserProfilePicUrl())
                .resize(80, 80)
                .transform(circleTransformation)
                .into(ibUserProfilePic);

        // get and set likes count
        TextView tvLikes = (TextView) convertView.findViewById(R.id.tvLikes);
        tvLikes.setText(photo.getLikesCount() + " likes");

        // get and set createdTime
        TextView tvWhenCreated = (TextView) convertView.findViewById(R.id.tvWhenCreated);
        tvWhenCreated.setText(
                DateUtils.getRelativeTimeSpanString(
                        photo.getCreatedTime() * 1000, System.currentTimeMillis(), DateUtils.SECOND_IN_MILLIS
                )
        );

        return convertView;
    }
}
