package com.yahui.collapsingimagetext;

import android.support.v4.view.ViewCompat;
import android.view.View;

/**
 * Created by yahui.hu on 2017/4/21.
 */

public class ViewHelper {
    
    private final View mView;
    
    private int mLayoutTop;
    private int mLayoutLeft;
    private int mLayoutBottom;
    private int mViewHeight;
    private int mViewWidth;
    private int mOffsetTop;
    private int mOffsetLeft;
    
    private int mMarginTitleLeft, mMarginTitleTop;
    private int mMaxOffset;
    private float mSynOffset;
    
    public ViewHelper(View view) {
        mView = view;
    }
    
    public void onViewLayout(int marginTitleLeft, int marginTitleTop) {
        mLayoutTop = mView.getTop();
        mLayoutLeft = mView.getLeft();
        mLayoutBottom = mView.getBottom();
        mViewHeight = mView.getHeight();
        mViewWidth = mView.getWidth();
    
        mMarginTitleLeft = marginTitleLeft;
        mMarginTitleTop = marginTitleTop;
    }
    
    private void updateOffsets() {
        ViewCompat.offsetTopAndBottom(mView, mOffsetTop - (mView.getTop() - mLayoutTop));
        ViewCompat.offsetLeftAndRight(mView, mOffsetLeft - (mView.getLeft() - mLayoutLeft));
    }
    
    
    public void updateOffsetScale(float scale) {
        ViewCompat.setScaleY(mView, scale);
        ViewCompat.setScaleX(mView, scale);
    }
    
    public void setViewOffsetScale(float scale) {
        updateOffsetScale(scale);
    }
    
    public boolean setTopAndBottomOffset(int offset) {
        if (mOffsetTop != offset) {
            mOffsetTop = offset;
            updateOffsets();
            return true;
        }
        return false;
    }
    
    
    public boolean setLeftAndRightOffset(int offset) {
        if (mOffsetLeft != offset) {
            mOffsetLeft = offset;
            updateOffsets();
            return true;
        }
        return false;
    }
    
    public int getTopAndBottomOffset() {
        return mOffsetTop;
    }
    
    public int getLeftAndRightOffset() {
        return mOffsetLeft;
    }
    
    public int getLayoutTop() {
        return mLayoutTop;
    }
    
    public int getLayoutLeft() {
        return mLayoutLeft;
    }
    
    public int getLayoutBottom() {
        return mLayoutBottom;
    }
    
    public int getViewHeight() {
        return mViewHeight;
    }
    
    public int getScaleViewHeight(float scale) {
        return (int) (mViewHeight * (scale + (1 - scale) / 2));
    }
    
    public int getScaleViewWidth(float scale) {
        return (int) (mViewWidth * (scale + (1 - scale) / 2));
    }
    
    public int getViewWidth() {
        return mViewWidth;
    }
    
    
    public int getMarginTitleLeft() {
        return mMarginTitleLeft;
    }
    
    public void setMarginTitleLeft(int marginTitleLeft) {
        this.mMarginTitleLeft = marginTitleLeft;
    }
    
    public int getMarginTitleTop() {
        return mMarginTitleTop;
    }
    
    public void setMarginTitleTop(int mMarginTitleTop) {
        this.mMarginTitleTop = mMarginTitleTop;
    }
    public int getMaxOffset() {
        return mMaxOffset;
    }
    
    public void setMaxOffset(int mMaxOffset) {
        this.mMaxOffset = mMaxOffset;
    }
    
    public float getSynOffset() {
        return mSynOffset;
    }
    
    public void setSynOffset(float mSynOffset) {
        this.mSynOffset = mSynOffset;
    }
}
