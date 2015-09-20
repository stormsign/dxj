package com.dxj.student.dialogplus;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.dxj.student.R;

import java.util.ArrayList;


/**
 * @author alessandro.balocco
 */
public class PhotoAdapter extends BaseAdapter {

    private LayoutInflater layoutInflater;
    public ArrayList<String> strings = new ArrayList<>();

    public PhotoAdapter(Context context, ArrayList<String> strings) {
        this.strings = strings;
        layoutInflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return strings.size();
    }

    @Override
    public Object getItem(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        View view = convertView;

        if (view == null) {

            view = layoutInflater.inflate(R.layout.simple_grid_item, parent, false);
            viewHolder = new ViewHolder();
            viewHolder.textView = (TextView) view.findViewById(R.id.text_view);
            viewHolder.imageView = (ImageView) view.findViewById(R.id.image_view);
            view.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) view.getTag();
        }

        Context context = parent.getContext();
//    switch (position) {
//      case 0:
//        viewHolder.textView.setText(context.getString(R.string.google_plus_title));
//        viewHolder.imageView.setImageResource(R.mipmap.ic_google_plus_icon);
//        break;
//      case 1:
//        viewHolder.textView.setText(context.getString(R.string.google_maps_title));
//        viewHolder.imageView.setImageResource(R.mipmap.ic_google_maps_icon);
//        break;
//      default:
//        viewHolder.textView.setText(strings[position]);
//        viewHolder.imageView.setImageResource(R.mipmap.ic_google_messenger_icon);
//        break;
//    }
        Glide.with(context).load(strings.get(position)).into(viewHolder.imageView);
        return view;
    }

    static class ViewHolder {
        TextView textView;
        ImageView imageView;
    }
}
