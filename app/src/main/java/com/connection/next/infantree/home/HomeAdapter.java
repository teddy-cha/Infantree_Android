package com.connection.next.infantree.home;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.Button;
import android.widget.Toast;

import com.connection.next.infantree.R;
import com.connection.next.infantree.db.PhotoDBHelper;
import com.connection.next.infantree.model.PhotoModel;
import com.connection.next.infantree.photos.FullImageActivity;
import com.urqa.clientinterface.URQAController;

import org.lucasr.twowayview.ItemClickSupport;
import org.lucasr.twowayview.widget.TwoWayView;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;


/**
 * Created by chayongbin on 15. 3. 31..
 */
public class HomeAdapter extends RecyclerView.Adapter<HomeAdapter.ViewHolder> {

    private int rowLayout;
    private Context mContext;
    private int COUNT;
    private int TOTAL;
    private PhotoDBHelper dao;
    private ArrayList<PhotoModel> photoList;
    private ArrayList<String> photoListByDate;

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TwoWayView spannable;
        Button allPhotosButton;
        Button diaryButton;

        public ViewHolder(View itemView) {
            super(itemView);
            spannable = (TwoWayView) itemView.findViewById(R.id.spannable_grid);
            this.allPhotosButton = (Button) itemView.findViewById(R.id.see_all_photos_button);
            this.diaryButton = (Button) itemView.findViewById(R.id.see_diary_button);
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
    public void onBindViewHolder(final ViewHolder holder, final int position) {

        try {
            final String date = photoList.get(position).getDate();
            photoListByDate = dao.getPhotoListByDateOfThree(date);
            int size = photoListByDate.size();
            holder.spannable.setAdapter(new SpannableAdapter(mContext, holder.spannable, date, size));

            holder.allPhotosButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(mContext, date + "'s all Photos", Toast.LENGTH_SHORT).show();
                    Intent allPhotosIntent = new Intent("com.connection.next.infantree.photos.AllPhotosActivity");
                    allPhotosIntent.putExtra("date", date);
                    mContext.startActivity(allPhotosIntent);

                }
            });

            holder.diaryButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(mContext, date + "'s diary", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent("com.connection.next.infantree.diary.SeeDiaryActivity");
                    intent.putExtra("date", date);
                    mContext.startActivity(intent);
                }
            });

            final ItemClickSupport itemClick = ItemClickSupport.addTo(holder.spannable);

            itemClick.setOnItemClickListener(new ItemClickSupport.OnItemClickListener() {
                @Override
                public void onItemClick(RecyclerView recyclerView, View view, int i, long l) {
                    Toast.makeText(mContext, date + "'s " + i, Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent("com.connection.next.infantree.photos.FullImageActivity");
                    // passing array index
                    intent.putExtra("id", i);
                    intent.putExtra("date", date);
                    mContext.startActivity(intent);
                }
            });
        } catch (Exception e) {
            URQAController.SendException(e);
        }

    }

    public int getItemCount() {
        return COUNT;
    }

}
