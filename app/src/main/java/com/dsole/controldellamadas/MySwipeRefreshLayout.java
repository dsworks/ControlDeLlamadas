package com.dsole.controldellamadas;

import android.content.Context;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.AttributeSet;

/**
 * Created by dsole on 26/01/2015.
 */
public class MySwipeRefreshLayout extends SwipeRefreshLayout {

    private OnChildScrollUpListener mScrollListenerNeeded;

    public static interface OnChildScrollUpListener {
        public boolean canChildScrollUp();
    }

    public MySwipeRefreshLayout(Context context) {
        super(context);
    }

    public MySwipeRefreshLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }
/*
    @Override
    public boolean canChildScrollUp() {
        return  mListView.getFirstVisiblePosition()!=0;
    }
    */

    public void setOnChildScrollUpListener(OnChildScrollUpListener listener) {
        mScrollListenerNeeded = listener;
    }

    @Override
    public boolean canChildScrollUp() {
        return mScrollListenerNeeded == null ? false : mScrollListenerNeeded.canChildScrollUp();
    }
}
