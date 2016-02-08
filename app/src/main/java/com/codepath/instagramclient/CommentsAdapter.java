package com.codepath.instagramclient;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.format.DateUtils;
import android.text.style.ForegroundColorSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.codepath.instagramclient.models.InstagramPhotoComment;

import java.util.List;

public class CommentsAdapter extends ArrayAdapter<InstagramPhotoComment> {

    static long currentTime = System.currentTimeMillis();

    public CommentsAdapter(Context context, List<InstagramPhotoComment> objects) {
        super(context, R.layout.item_comments, objects);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final InstagramPhotoComment comment = getItem(position);

        final Context context = getContext();
        CommentsViewHolder viewHolder; // view lookup cache stored in tag

        // Check if an existing view is being reused, otherwise inflate the view
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.item_comments, parent, false);

            viewHolder = new CommentsViewHolder();
            viewHolder.tvCComment = (TextView) convertView.findViewById(R.id.tvCComment);
            viewHolder.tvCCreatedTime = (TextView) convertView.findViewById(R.id.tvCCreatedTime);

            convertView.setTag(viewHolder);
        }
        else {
            viewHolder = (CommentsViewHolder) convertView.getTag();
        }

        String cUserName = comment.getUserName();

        // change color of username only
        SpannableString spannable = new SpannableString(cUserName + " " + comment.getText());
        spannable.setSpan(
                new ForegroundColorSpan(ContextCompat.getColor(context, R.color.colorPrimary)),
                0,
                cUserName.length(),
                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        viewHolder.tvCComment.setText(spannable);

        viewHolder.tvCCreatedTime.setText(
                DateUtils.getRelativeTimeSpanString(
                        comment.getCreatedTime() * 1000, currentTime, DateUtils.SECOND_IN_MILLIS
                ), TextView.BufferType.SPANNABLE
        );

        return convertView;
    }

    private static class CommentsViewHolder {
        TextView tvCComment;
        TextView tvCCreatedTime;
    }
}
