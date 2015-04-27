package com.connection.next.infantree.home;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.connection.next.infantree.R;
import com.connection.next.infantree.db.PhotoDBHelper;
import com.connection.next.infantree.model.PhotoModel;

import org.lucasr.twowayview.widget.TwoWayView;

import java.sql.Date;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;


/**
 * Created by chayongbin on 15. 3. 31..
 */
public class HomeAdapter extends RecyclerView.Adapter<HomeAdapter.ViewHolder> {

    private int rowLayout;
    private Context mContext;
    private int COUNT;
    private PhotoDBHelper dao;
    private ArrayList<PhotoModel> photoList;
    private ArrayList<String> photoListByDate;

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TwoWayView spannable;

        public ViewHolder(View itemView) {
            super(itemView);
            spannable = (TwoWayView) itemView.findViewById(R.id.spannable_grid);
        }
    }

    public HomeAdapter(int rowLayout, Context context) {
        dao = new PhotoDBHelper(HomeActivity.getAppContext());
        this.rowLayout = rowLayout;
        this.mContext = context;
        COUNT = dao.getDateCounts();
        photoList = dao.getPhotoList();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(rowLayout, viewGroup, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

        String date = photoList.get(position).getDate();
        photoListByDate = dao.getPhotoListByDate(date);
        System.out.println("photoListByDate : " + photoListByDate);
        int size = photoListByDate.size();
        holder.spannable.setAdapter(new SpannableAdapter(mContext, holder.spannable, date, size));
        Log.i("Data is ",date);
    }

    public int getItemCount() {
        return COUNT;
    }

}
