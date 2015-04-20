package com.collection.next.infantree.home;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.collection.next.infantree.R;

import org.lucasr.twowayview.widget.TwoWayView;


/**
 * Created by chayongbin on 15. 3. 31..
 */
public class HomeAdapter extends RecyclerView.Adapter<HomeAdapter.ViewHolder> {

    private int rowLayout;
    private Context mContext;
    private int COUNT;

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TwoWayView spannable;

        public ViewHolder(View itemView) {
            super(itemView);
            spannable = (TwoWayView) itemView.findViewById(R.id.spannable_grid);
        }
    }

    public HomeAdapter(int rowLayout, Context context) {
        this.rowLayout = rowLayout;
        this.mContext = context;
        COUNT = 10;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(rowLayout, viewGroup, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        /*
         * 수정 필요함 !!!
         */
        holder.spannable.setAdapter(new SpannableAdapter(mContext, holder.spannable, position+1));
        System.out.println("---------------------------");
        System.out.println(position);

    }

    public int getItemCount() {
        return COUNT;
    }

}
