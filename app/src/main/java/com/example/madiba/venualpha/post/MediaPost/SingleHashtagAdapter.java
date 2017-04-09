package com.example.madiba.venualpha.post.MediaPost;

import android.content.Context;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.RadioButton;
import android.widget.TextView;

import com.example.madiba.venualpha.R;
import com.example.madiba.venualpha.models.MdMemoryItem;

import java.util.List;

public class SingleHashtagAdapter extends RecyclerView.Adapter<SingleHashtagAdapter.SingleCheckViewHolder> {

    private int mSelectedItem = -1;
    private List<MdMemoryItem> mSingleCheckList;
    private Context mContext;
    private AdapterView.OnItemClickListener onItemClickListener;

    public SingleHashtagAdapter(Context context, List<MdMemoryItem> listItems) {
        mContext = context;
        mSingleCheckList = listItems;
    }

    @Override
    public SingleCheckViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        LayoutInflater inflater = LayoutInflater.from(mContext);
        final View view = inflater.inflate(R.layout.item_addhashtag_item, viewGroup, false);
        return new SingleCheckViewHolder(view, this);
    }

    @Override
    public void onBindViewHolder(SingleCheckViewHolder viewHolder, final int position) {
        MdMemoryItem item = mSingleCheckList.get(position);
        try {
            viewHolder.setDateToView(item, position);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public int getItemCount() {
        return mSingleCheckList.size();
    }

    public void setOnItemClickListener(AdapterView.OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }


    public void onItemHolderClick(SingleCheckViewHolder holder) {
        if (onItemClickListener != null)
            onItemClickListener.onItemClick(null, holder.itemView, holder.getAdapterPosition(), holder.getItemId());
    }

    class SingleCheckViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private SingleHashtagAdapter mAdapter;
        private RadioButton mRadio;
        private TextView mText,mTags;

        public SingleCheckViewHolder(View itemView, final SingleHashtagAdapter mAdapter) {
            super(itemView);
            this.mAdapter = mAdapter;

            mText = (TextView) itemView.findViewById(R.id.hashtag);
            mRadio = (RadioButton) itemView.findViewById(R.id.radio);
            itemView.setOnClickListener(this);
            mRadio.setOnClickListener(this);

        }

        public void setDateToView(MdMemoryItem item, int position) throws Exception {
            mRadio.setChecked(position == mSelectedItem);
//            mText.setText(item.getHashTag());
//            if (item.getExtraAvailable())
//                mTags.setText(item.getExtraInfo());
        }

        @Override
        public void onClick(View v) {
            mSelectedItem = getAdapterPosition();
            notifyItemRangeChanged(0, mSingleCheckList.size());
            mAdapter.onItemHolderClick(SingleCheckViewHolder.this);
        }
    }



    private void showOverflow(View view, MdMemoryItem item,Context context) {
        PopupMenu popupMenu = new PopupMenu(context, view);
        popupMenu.getMenu().add(Menu.NONE, 1, 1, "Remove");
        popupMenu.show();

        popupMenu.setOnMenuItemClickListener(menuItem -> {
            int i = menuItem.getItemId();
            return false;
        });

        popupMenu.show();
    }

}
