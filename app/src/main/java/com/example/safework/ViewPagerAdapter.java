package com.example.safework;

import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;


import java.util.List;


/**
 * Created by 赖志林 on 2018/2/4.
 */

public class ViewPagerAdapter extends PagerAdapter{

        private  List<View> viewList;
        public  ViewPagerAdapter(){

         }
        public  ViewPagerAdapter(List<View> views){
            super();
            this.viewList = views;
        }

        public View getItem(int position) {
            return this.viewList.get(position);
        }
        @Override
        public int getCount() {
            return viewList.size();
        }
      @Override
       public boolean isViewFromObject(View view,Object object){
            return view == object;
    }
        @Override
        public Object instantiateItem(ViewGroup viewGroup,int posotion){
            viewGroup.addView(viewList.get(posotion));
            return viewList.get(posotion);
        }
}



