package com.dxj.student.factory;

import android.support.v4.app.Fragment;

import com.dxj.student.base.BaseFragment;
import com.dxj.student.fragment.HomeFragment;
import com.dxj.student.fragment.MyFragment;

import java.util.HashMap;

/**获取主页面的Fragment，如果有就直接获取它的对象，没有就新建
 * Created by khb on 2015/8/19.
 */
public class FragmentFactory {
    private static HashMap<Integer, Fragment> hashMap = new HashMap<Integer, Fragment>();
    public static BaseFragment getFragment(int position){
        BaseFragment baseFragment = null;
        if (hashMap.containsKey(position)){
            if (hashMap.get(position) != null){
                baseFragment = (BaseFragment) hashMap.get(position);
            }
        }else{
            switch (position){
                case 0: // 首页
                    baseFragment = new HomeFragment();
                    break;
                case 1: //找老师
                    break;
                case 2: //消息
                    break;
                case 3: //我
                    baseFragment = new MyFragment();
                    break;
            }
            hashMap.put(position, baseFragment);
        }
        return  baseFragment;
    }

}
