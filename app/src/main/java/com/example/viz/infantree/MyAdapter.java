package com.example.viz.infantree;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * Created by viz on 2015. 3. 31..
 */
public class MyAdapter extends RecyclerView.Adapter<MyAdapter.ViewHolder> {

    private static final int TYPE_HEADER = 0;
    private static final int TYPE_ITEM = 1;

    private String mNavTitles[];
    private int mIcons[];

    private String name;
    private String age;
    private int profile;

    MyAdapter(String TITLES[], int ICONS[], String name, String age, int profile) {
        mNavTitles = TITLES;
        mIcons = ICONS;

        this.name = name;
        this.age = age;
        this.profile = profile;
    }

    // 내부 클래스 - 뷰의 아이디와 뷰에 필요한 정보를 가지고 있는 홀더
    public static class ViewHolder extends RecyclerView.ViewHolder {
        int HolderId; // 이 아이디로 type_header 인지 type_item 인지 구분한다

        TextView rowText;
        ImageView rowImage;

        TextView tvName;
        TextView tvAge;
        ImageView ivProfile;

        // 생성자
        public ViewHolder(View itemView, int ViewType) {
            super(itemView);

            if (ViewType == TYPE_ITEM) {
                rowText = (TextView) itemView.findViewById(R.id.rowText);
                rowImage = (ImageView) itemView.findViewById(R.id.rowIcon);
                HolderId = 1;
            } else if (ViewType == TYPE_HEADER) {
                tvName = (TextView) itemView.findViewById(R.id.name);
                tvAge = (TextView) itemView.findViewById(R.id.age);
                ivProfile = (ImageView) itemView.findViewById(R.id.circleView);
                HolderId = 0;
            }
        }
    }

    // 뷰 홀더가 onCreate 될 때 해야 할 일 묘사
    @Override
    public MyAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        // 뷰의 타입이 아이템일 때
        if (viewType == TYPE_ITEM) {

            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_row, parent, false); // 레이아웃 인플레이트하고 리턴값으로 뷰를 받아옴
            ViewHolder vhItem = new ViewHolder(v, viewType); // 아이템 뷰홀더 생성

            return vhItem;

        }

        // 뷰의 타입이 헤더일 때
        else if (viewType == TYPE_HEADER) {

            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.header, parent, false); // 마찬가지
            ViewHolder vhHeader = new ViewHolder(v, viewType); // 헤더 뷰홀더 생성

            return vhHeader;
        }

        return null; // 뷰 타입의 숫자가 잘못되었을 경우 null 리턴
    }

    // 뷰 홀더의 한 아이템이 디스플레이되어야 할 때 실행됨
    @Override
    public void onBindViewHolder(MyAdapter.ViewHolder holder, int position) {

        // 아이템 뷰홀더의 디스플레이
        if (holder.HolderId == 1) {
            // 헤더가 맨 위에 있어서 헤더의 position 값이 0이기 때문에 아이템의 포지션은 1부터 시작이다 그래서 인덱스 값을 position-1 값으로 해줌
            holder.rowText.setText(mNavTitles[position - 1]);
            holder.rowImage.setImageResource(mIcons[position - 1]);
        }

        // 헤더 뷰홀더의 디스플레이
        else if (holder.HolderId == 2) {
            holder.ivProfile.setImageResource(profile);
            holder.tvName.setText(name);
            holder.tvAge.setText(age);
        }
    }

    // 리스트에 있는 아이템 개수를 반환해줌 - 꼭 구현해줘야 하는 듯
    @Override
    public int getItemCount() {
        return mNavTitles.length + 1; // 헤더 뷰까지 합쳐서 +1
    }


    // 어떤 타입의 뷰가 넘어가고 있는지 확인할 수 있음 - 이것도
    @Override
    public int getItemViewType(int position) {
        if (position == 0)
            return TYPE_HEADER;
        else
            return TYPE_ITEM;
    }
}