package com.dxj.student.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


import com.dxj.student.R;
import com.dxj.student.base.BaseListAdapter;
import com.dxj.student.bean.CourseSubjectBean;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by kings on 8/30/2015.
 */
public class DelatilAdapter extends BaseListAdapter {
    private LayoutInflater inflater;
    private List<CourseSubjectBean> results = new ArrayList<>();
    public DelatilAdapter(Context context, List<CourseSubjectBean> list) {
        super(context, list);
        inflater = LayoutInflater.from(context);
        this.results = list;
    }

    @Override
    public int getCount() {
        return results.size();
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
        ViewHolder viewHolder = null;
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.item_detail_course, null);
            viewHolder = new ViewHolder();
            viewHolder.name = (TextView) convertView.findViewById(R.id.tv_name);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        viewHolder.name.setText(results.get(position).getRemark());
        return convertView;
    }

    class ViewHolder {
        TextView name;
    }
}

