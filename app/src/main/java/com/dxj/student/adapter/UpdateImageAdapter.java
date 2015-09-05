package com.dxj.student.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.dxj.student.R;
import com.dxj.student.bean.HeadUrl;
import com.dxj.student.utils.HttpUtils;
import com.dxj.student.utils.MyAsyn;
import com.google.gson.Gson;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


public class UpdateImageAdapter extends BaseAdapter {
    ExecutorService fixedThreadPool = Executors.newFixedThreadPool(9);
    protected List<String> mDatas = new ArrayList<String>();
    private Context context;
    private LayoutInflater inflater;
    private int selectedPosition = -1;
    private boolean shape;
    private int mImageSize = 60;
    private int index = -1;
    private List<String> list = new ArrayList<>();
    private boolean isShow;
    private List<String> mSelectedImages = new ArrayList<>();
    private List<String> imageUrls = new ArrayList<>();
    /**
     * 选择某个图片，改变选择状态
     * @param image
     */
    public void select(String image) {
        if(mSelectedImages.contains(image)){
            mSelectedImages.remove(image);
        }else{
            mSelectedImages.add(image);
        }
        notifyDataSetChanged();
    }
   public List<String> getmSelectedImages(){
       return mSelectedImages;
   }
    public boolean isShape() {
        return shape;
    }

    public void setShape(boolean shape) {
        this.shape = shape;
        notifyDataSetChanged();
    }

    public UpdateImageAdapter(Context context) {
        this.context = context;
        inflater = LayoutInflater.from(context);
    }

    public int getCount() {
        return mDatas.size();
    }
//    public void setBoolean(boolean isShow){
//        this
//    }
    public void addData(List<String> data) {
        if (mDatas != null && data != null) {
            if (mDatas.size() > 0) {
                mDatas.clear();
            }
            mDatas.addAll(data);
        }
        notifyDataSetChanged();
    }

    public Object getItem(int arg0) {
        return null;
    }

    public long getItemId(int arg0) {
        return 0;
    }

    public void setSelectedPosition(int position) {
        selectedPosition = position;
    }

    public int getSelectedPosition() {
        return selectedPosition;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        Log.i("TAG", "position=" + position);
        ViewHolder holder = null;
        final int sign = position;
        if (convertView == null) {

            convertView = inflater.inflate(R.layout.item_published_grida, parent, false);
            holder = new ViewHolder();
            holder.image = (ImageView) convertView.findViewById(R.id.item_grida_image);
            holder.add_image = (ImageView) convertView.findViewById(R.id.item_grida_addimage);
            holder.tvUpdate = (TextView) convertView.findViewById(R.id.tv);
            holder.checkmark=(ImageView)convertView.findViewById(R.id.checkmark);
            convertView.setTag(holder);

        } else {

            holder = (ViewHolder) convertView.getTag();

        }
        if(shape){
            holder.checkmark.setVisibility(View.VISIBLE);
            if(mSelectedImages.contains(mDatas.get(position))){
                // 设置选中状态
                holder.checkmark.setImageResource(R.mipmap.btn_selected);
//                mask.setVisibility(View.VISIBLE);
            }else{
                // 未选择
                holder.checkmark.setImageResource(R.mipmap.btn_unselected);
//                mask.setVisibility(View.GONE);
            }
        }else{
            holder.checkmark.setVisibility(View.GONE);

        }
        if (mDatas.get(position) != null && !list.contains(mDatas.get(position))) {
            list.add(mDatas.get(position));
            MyAsyn asyn = new MyAsyn(context, getAsynResponse(holder.tvUpdate), mDatas.get(position), HttpUtils.UPADTE_MULT_IMAGE);
            asyn.executeOnExecutor(fixedThreadPool, String.valueOf(position));
            holder.tvUpdate.setVisibility(View.VISIBLE);
        } else {
            holder.tvUpdate.setVisibility(View.GONE);
        }
        Log.i("TAG", "path=" + mDatas.get(position));
        if (position == mDatas.size()) {
            holder.image.setVisibility(convertView.GONE);
//            holder.del_image.setVisibility(convertView.GONE);
            holder.add_image.setVisibility(View.VISIBLE);

            if (position == 9) {
                holder.add_image.setVisibility(View.GONE);
            }

        } else {

            Glide.with(context).load(new File(mDatas.get(position))).error(R.mipmap.default_error).override(mImageSize,mImageSize).centerCrop().into(holder.image);
//	    holder.image.setImageBitmap(Bimp.tempSelectBitmap.get(position).getBitmap());
            holder.image.setVisibility(convertView.VISIBLE);
//            holder.del_imageage.setVisibilitybility(convertView.VISIBLE);
            holder.add_image.setVisibility(View.GONE);
//            holder.del_image.setOnClickListener(new View.OnClickListener() {
//
//                @Override
//                public void onClick(View v) {
//                    // TODO Auto-generated method stub
////		    Bimp.tempSelectBitmap.remove(sign);
//                    // gridviewinit();
//                    notifyDataSetChanged();
//                }
//            });
        }

        return convertView;
    }

    private MyAsyn.AsyncResponse getAsynResponse(final TextView tv) {
        return new MyAsyn.AsyncResponse() {

            @Override
            public void processFinish(String result) {

                // TODO Auto-generated method stub
                Log.i("TAG", "Update+result=" + result);
                Gson gson = new Gson();
                HeadUrl headUrl = gson.fromJson(result,HeadUrl.class);
                Log.i("TAG","headUrl="+headUrl.getImages().get(0));
                imageUrls.add(headUrl.getImages().get(0));
                tv.setVisibility(View.GONE);
            }
        };
    }
   public List<String> getImageUrls(){
       return imageUrls;
   }

    public class ViewHolder {
        public ImageView image;
        //        public ImageView del_image;
        public ImageView add_image;
        public TextView tvUpdate;
        public ImageView checkmark;
    }
}
