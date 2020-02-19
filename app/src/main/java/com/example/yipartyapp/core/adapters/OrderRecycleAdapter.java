package com.example.yipartyapp.core.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.yipartyapp.R;
import com.example.yipartyapp.bean.MerchantBean;
import com.example.yipartyapp.bean.OrderMsg;

import java.util.List;

public class OrderRecycleAdapter extends RecyclerView.Adapter<OrderRecycleAdapter.ViewHolder>{

    private Context mContext;
    private List<OrderMsg> mList;
    private OrderMsg data;
    private OnItemClikListener mOnItemClikListener;

    public OrderRecycleAdapter(Context context, List<OrderMsg> mList) {
        this.mContext = context;
        this.mList = mList;
    }



    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_order_details, parent, false);
        OrderRecycleAdapter.ViewHolder holder = new OrderRecycleAdapter.ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(final OrderRecycleAdapter.ViewHolder holder, final int position) {
        data = mList.get(position);
        RequestOptions options = new RequestOptions()
                .placeholder(R.drawable.img_default_movie)
                .error(R.drawable.img_default_movie);
        Glide.with(mContext).load(R.drawable.wuhan).apply(options).into(holder.home_item_movie_list_pic);
        holder.home_item_movie_list_title.setText(data.getName());
        holder.textView42.setText(data.getMoney());

        if (mOnItemClikListener != null) {
            // 设置item条目短按点击事件的监听
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int pos = holder.getLayoutPosition();
                    mOnItemClikListener.onItemClik(holder.itemView, pos);
                }
            });

            // 设置item条目长按点击事件的监听
            holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    int pos = holder.getLayoutPosition();
                    mOnItemClikListener.onItemLongClik(holder.itemView, pos);
                    return false;
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }
    public static class ViewHolder extends RecyclerView.ViewHolder {

        private ImageView home_item_movie_list_pic;
        private TextView home_item_movie_list_title;
        private TextView textView42;

        public ViewHolder(View itemView) {
            super(itemView);
            home_item_movie_list_pic = (ImageView) itemView.findViewById(R.id.home_item_movie_list_pic);
            home_item_movie_list_title = (TextView) itemView.findViewById(R.id.home_item_movie_list_title);
            textView42 = (TextView) itemView.findViewById(R.id.textView42);//价格

        }
    }
    public interface OnItemClikListener {
        void onItemClik(View view, int position);

        void onItemLongClik(View view, int position);
    }

    public void setItemClikListener(OrderRecycleAdapter.OnItemClikListener mOnItemClikListener) {
        this.mOnItemClikListener = mOnItemClikListener;
    }
}
