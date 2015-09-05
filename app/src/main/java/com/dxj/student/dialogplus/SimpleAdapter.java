package com.dxj.student.dialogplus;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.dxj.student.R;


/**
 * @author alessandro.balocco
 */
public class SimpleAdapter extends BaseAdapter {

  private LayoutInflater layoutInflater;
  private boolean isGrid;
  public  String[] strings ;
  public SimpleAdapter(Context context, boolean isGrid, String[] strings) {
    this.strings=strings;
    layoutInflater = LayoutInflater.from(context);
    this.isGrid = isGrid;
  }

  @Override
  public int getCount() {
    return strings.length;
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
      if (isGrid) {
        view = layoutInflater.inflate(R.layout.simple_grid_item, parent, false);
      } else {
        view = layoutInflater.inflate(R.layout.simple_list_item, parent, false);
      }

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
        viewHolder.textView.setText(strings[position]);
        viewHolder.imageView.setImageResource(R.mipmap.ic_google_messenger_icon);
//        break;
//    }

    return view;
  }

  static class ViewHolder {
    TextView textView;
    ImageView imageView;
  }
}
