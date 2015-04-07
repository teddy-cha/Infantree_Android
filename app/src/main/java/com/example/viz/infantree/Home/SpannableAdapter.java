package com.example.viz.infantree.home;

/**
 * Created by chayongbin on 15. 3. 30..
 */

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.viz.infantree.R;

import org.lucasr.twowayview.TwoWayLayoutManager;
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

    public SpannableAdapter(Context context, TwoWayView recyclerView, int COUNT) {
        // <<초기 구성요소 init>>
        mContext = context;
        mItems = new ArrayList<Integer>(COUNT);
        for (int i = 0; i < COUNT; i++) {
            addItem(i);
        }
        mRecyclerView = recyclerView;
    }

    // <<아이템 추가>>
    public void addItem(int position) {
        final int id = mCurrentItemId++;
        mItems.add(position, id);
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

        // 테스트를 위한 아이템 배치 방식
        holder.title.setText(mItems.get(position).toString());

        boolean isVertical = (mRecyclerView.getOrientation() == TwoWayLayoutManager.Orientation.VERTICAL);
        final View itemView = holder.itemView;

        final int itemId = mItems.get(position);

        final SpannableGridLayoutManager.LayoutParams lp =
                (SpannableGridLayoutManager.LayoutParams) itemView.getLayoutParams();

        final int span1 = (itemId == 0 || itemId == 3 ? 2 : 1);
        final int span2 = (itemId == 0 ? 2 : (itemId == 3 ? 3 : 1));

        final int colSpan = (isVertical ? span2 : span1);
        final int rowSpan = (isVertical ? span1 : span2);

        if (lp.rowSpan != rowSpan || lp.colSpan != colSpan) {
            lp.rowSpan = rowSpan;
            lp.colSpan = colSpan;
            itemView.setLayoutParams(lp);
        }


        // <<실제 앱의 아이템 배치 방식>>
        // * 아이템 갯수에 따른 배치방식

        if (mItems.size() == 0) {

            // 아이템이 없을 경우

        } else if (mItems.size() == 1) {

            // 아이템이 1개

            // 오늘의 사진이 등록이 된 경우

            // 오늘의 사진이 등록이 안 된 경우

        } else if (mItems.size() == 2) {

            // 아이템이 2개

            // 오늘의 사진이 등록이 된 경우

            // 오늘의 사진이 등록이 안 된 경우

        } else if (mItems.size() == 3) {

            // 아이템이 3개

            // 오늘의 사진이 등록이 된 경우

            // 오늘의 사진이 등록이 안 된 경우

        } else {

            // 아이템이 4개 이상

            // 오늘의 사진이 등록이 된 경우

            // 오늘의 사진이 등록이 안 된 경우

        }


    }

    @Override
    public int getItemCount() {
        return mItems.size();
    }
}
