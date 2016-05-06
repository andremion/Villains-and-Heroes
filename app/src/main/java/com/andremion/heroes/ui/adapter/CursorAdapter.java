package com.andremion.heroes.ui.adapter;

import android.database.Cursor;
import android.provider.BaseColumns;
import android.support.v7.widget.RecyclerView;
import android.view.View;

public abstract class CursorAdapter<VH extends RecyclerView.ViewHolder> extends RecyclerView.Adapter<VH> {

    private final OnAdapterInteractionListener mItemClickListener;
    private Cursor mCursor;

    public CursorAdapter(OnAdapterInteractionListener listener) {
        super();
        mItemClickListener = listener;
        setHasStableIds(true);
    }

    public OnAdapterInteractionListener getItemClickListener() {
        return mItemClickListener;
    }

    @Override
    public long getItemId(int position) {
        Cursor cursor = getItem(position);
        return cursor.getLong(cursor.getColumnIndex(BaseColumns._ID));
    }

    @Override
    public int getItemCount() {
        if (null == mCursor) {
            return 0;
        }
        return mCursor.getCount();
    }

    public Cursor getItem(int position) {
        if (mCursor == null) {
            return null;
        }
        mCursor.moveToPosition(position);
        return mCursor;
    }

    public void swapCursor(Cursor newCursor) {
        if (newCursor == mCursor) {
            return;
        }
        mCursor = newCursor;
        notifyDataSetChanged();
    }

    public interface OnAdapterInteractionListener {
        void onItemClick(CursorAdapter adapter, View view, int position);
    }

    public abstract class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        public final View mView;

        public ViewHolder(View v) {
            super(v);
            mView = v;
            mView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            int position = getAdapterPosition();
            if (position != RecyclerView.NO_POSITION && mItemClickListener != null) {
                mItemClickListener.onItemClick(CursorAdapter.this, v, position);
            }
        }
    }

}
