package com.dxj.student.fragment;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;


import com.dxj.student.R;
import com.dxj.student.base.BaseFragment;
import com.dxj.student.dialogplus.PhotoAdapter;

import java.util.ArrayList;

/**
 * Created by khb on 2015/8/19.
 * 相册
 */
public class DetailPhotoFragment extends BaseFragment {
    private GridView photo;
    private ArrayList<String> photos;
    private PhotoAdapter photoAdapter;

    @Override
    public void initData() {
        photos = getArguments().getStringArrayList("photos");
        photoAdapter = new PhotoAdapter(getActivity(), photos);
        photo.setAdapter(photoAdapter);
    }

    @Override
    public View initView(LayoutInflater inflater) {
        view = inflater.inflate(R.layout.fragment_photo, null);
        photo = (GridView) view.findViewById(R.id.gridview_photo);
        photo.setOnItemClickListener(getGridViewItemClickListener());
        return view;
    }

    private AdapterView.OnItemClickListener getGridViewItemClickListener() {
        return new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

//                Intent intent = new Intent(getActivity(), GalleryActivity.class);
//                intent.putStringArrayListExtra("imgPath", photos);
//                intent.putExtra("index", position);
//                startActivity(intent);
            }
        };
    }

}
