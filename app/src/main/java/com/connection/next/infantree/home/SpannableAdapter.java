package com.connection.next.infantree.home;

/**
 * Created by chayongbin on 15. 3. 30..
 */

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.connection.next.infantree.R;
import com.connection.next.infantree.db.PhotoDBHelper;

import org.lucasr.twowayview.widget.TwoWayView;
import org.lucasr.twowayview.widget.SpannableGridLayoutManager;

import java.util.ArrayList;
import java.util.List;


public class SpannableAdapter extends RecyclerView.Adapter<SpannableAdapter.SimpleViewHolder> {

    // <<Spannable Grid View Layout Adapter>>

    private final Context mContext;
    private final TwoWayView mRecyclerView;
    private final List<Integer> mItems;
    private int mCurrentItemId = 0;
    private int MAX_ITEM = 3;
    private int size;
    private String date;
    private PhotoDBHelper dao;
    private ArrayList<String> photoModelArrayList;

    public static class SimpleViewHolder extends RecyclerView.ViewHolder {
        public final TextView title;
        public final ImageView image;

        // <<home_item.xml 구성요소>>
        public SimpleViewHolder(View view) {
            super(view);
            // <<임시적으로 사용하는 TextView>>
            title = (TextView) view.findViewById(R.id.title);
            image = (ImageView) view.findViewById(R.id.image);
        }
    }

    public SpannableAdapter(Context context, TwoWayView recyclerView, String date, int size) {
        // <<초기 구성요소 init>>
        dao = new PhotoDBHelper(HomeActivity.getAppContext());
        mContext = context;
        this.size = size;
        this.date = date;
        photoModelArrayList = dao.getPhotoListByDateOfThree(date);
        mItems = new ArrayList<Integer>(size);
        for (int i = 0; i < size; i++) {
            if (i <= MAX_ITEM - 1) {
                addItem(i);
            }
        }
        mRecyclerView = recyclerView;
    }

    // <<아이템 추가>>
    public void addItem(int position) {
        if (mItems != null) {
            mCurrentItemId += 1;
            final int itemId = mCurrentItemId;
            mItems.add(position, itemId);
        }
        notifyItemInserted(position);
    }

    // <<아이템 제거>>
    public void removeItem(int position) {
        mItems.remove(position);
        notifyItemRemoved(position);
    }

    @Override
    public SimpleViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        final View view = LayoutInflater.from(mContext).inflate(R.layout.home_item, parent, false);
        return new SimpleViewHolder(view);
    }

    @Override
    public void onBindViewHolder(SimpleViewHolder holder, int position) {
        final View itemView = holder.itemView;
        final int itemId = mItems.get(position);
        final SpannableGridLayoutManager.LayoutParams lp =
                (SpannableGridLayoutManager.LayoutParams) itemView.getLayoutParams();

        String img_path = HomeActivity.getAppContext().getFilesDir().getPath() + "/" + photoModelArrayList.get(position);
//        String img_path = HomeActivity.getAppContext().getExternalFilesDir(Environment.DIRECTORY_PICTURES).getPath() + "/" + photoModelArrayList.get(position);



        Bitmap bitmap = BitmapFactory.decodeFile(img_path);

        holder.image.setImageBitmap(bitmap);

//        BitmapFactory.Options options = new BitmapFactory.Options();
//        options.inSampleSize = 4;
//        options.inPurgeable = true;
//
//        Bitmap bitmap = BitmapFactory.decodeFile(img_path, options);
//        Bitmap resized = Bitmap.createScaledBitmap(bitmap, 1000, 1000, true);
//        holder.image.setImageBitmap(resized);

        if (position == 0){
            holder.title.setText(date);
        }

        // <<실제 앱의 아이템 배치 방식>>
        //   아이템 갯수에 따른 배치방식

        if (mItems.size() == 1) {


            final int one_colSpan = 6;
            final int one_rowSpan = 4;
            lp.rowSpan = one_rowSpan;
            lp.colSpan = one_colSpan;
            itemView.setLayoutParams(lp);

        } else if (mItems.size() == 2) {
            final int two_colSpan = 3;
            final int two_rowSpan = 4;

            lp.rowSpan = two_rowSpan;
            lp.colSpan = two_colSpan;
            itemView.setLayoutParams(lp);


        } else {
            final int three_span1;
            final int three_span2;

            three_span1 = (itemId == 3 || itemId == 2 ? 2 : 4);
            three_span2 = (itemId == 3 || itemId == 2 ? 2 : 4);

            lp.rowSpan = three_span1;
            lp.colSpan = three_span2;

            itemView.setLayoutParams(lp);
        }

    }

    @Override
    public int getItemCount() {
        return mItems.size();
    }
}
