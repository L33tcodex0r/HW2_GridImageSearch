package com.udroidw.gridimagesearch.adapters;

import android.content.Context;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.udroidw.gridimagesearch.R;
import com.udroidw.gridimagesearch.models.ImageResult;

import java.util.List;


public class ImageResultsAdapter extends ArrayAdapter<ImageResult> {

    public ImageResultsAdapter(Context context, List<ImageResult> images) {
        super(context, android.R.layout.simple_list_item_1, images);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ImageResult imageInfo = getItem(position);
        ViewHolder viewHolder;
        if (convertView == null) {
            viewHolder = new ViewHolder();
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_image_result, parent, false);
            viewHolder.ivImage = (ImageView) convertView.findViewById(R.id.ivImage);
            viewHolder.tvTitle = (TextView) convertView.findViewById(R.id.tvTitle);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        viewHolder.ivImage.setImageResource(0);
        //Populate title and remote download image url
        viewHolder.tvTitle.setText(Html.fromHtml(imageInfo.title));
        //Remotely download the image data in the background (with Picasso)
        Picasso.with(getContext()).load(imageInfo.thumbUrl).into(viewHolder.ivImage);
        //Return the completed view to be displayed
        return convertView;
    }

    public static class ViewHolder {
        ImageView ivImage;
        TextView tvTitle;
    }

}
