package com.yahui.collapsingimagetext;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.annotation.NonNull;
import android.support.design.widget.AppBarLayout;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.widget.RelativeLayout;


/**
 * Created by yahui.hu on 2017/4/21.
 */

public class CollapsingImageTextLayout extends RelativeLayout {
    
    private AppBarLayout.OnOffsetChangedListener mOffsetChangedListener;
    
    private int mTitleId, mTextId, mImageId;
    private int mTitleMarginLeft, mTitleMarginTop, mImgMarginLeft, mImgMarginTop;
    private float mTextScale, mImgScale;
    private View mTitle, mImg, mText;
    private boolean isGetView = true;
    private int mTitleHeight = 0;
    
    
    public CollapsingImageTextLayout(Context context) {
        this(context, null);
    }
    
    public CollapsingImageTextLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }
    
    public CollapsingImageTextLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        TypedArray a = context.obtainStyledAttributes(attrs,
                R.styleable.CollapsingImageLayout, defStyleAttr, 0);
        mTitleId = a.getResourceId(R.styleable.CollapsingImageLayout_title_id, 0);
        mTextId = a.getResourceId(R.styleable.CollapsingImageLayout_text_id, 0);
        mImageId = a.getResourceId(R.styleable.CollapsingImageLayout_img_id, 0);
        mTextScale = a.getFloat(R.styleable.CollapsingImageLayout_text_scale, 0.4f);
        mImgScale = a.getFloat(R.styleable.CollapsingImageLayout_img_scale, 0.4f);
        mTitleMarginLeft = a.getDimensionPixelSize(R.styleable.CollapsingImageLayout_text_margin_left, 0);
        mTitleMarginTop = a.getDimensionPixelSize(R.styleable.CollapsingImageLayout_text_margin_top, 0);
        mImgMarginLeft = a.getDimensionPixelSize(R.styleable.CollapsingImageLayout_img_margin_left, 0);
        mImgMarginTop = a.getDimensionPixelSize(R.styleable.CollapsingImageLayout_img_margin_top, 0);
        a.recycle();
        
    }
    
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        getView();
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }
    
    private void getView() {
        if (!isGetView) {
            return;
        }
        if (mTitleId != 0) {
            mTitle = findViewById(mTitleId);
        }
        
        if (mTextId != 0) {
            mText = findViewById(mTextId);
        }
        
        if (mImageId != 0) {
            mImg = findViewById(mImageId);
        }
        isGetView = false;
    }
    
    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        super.onLayout(changed, l, t, r, b);
        
        if (mTitle != null) {
            getViewOffsetHelper(mTitle).onViewLayout(0, 0);
            setMinimumHeight(getHeightWithMargins(mTitle));
            mTitleHeight = mTitle.getHeight();
            this.bringChildToFront(mTitle);
        }
        
        if (mImg != null) {
            getViewOffsetHelper(mImg).onViewLayout(mImgMarginLeft, mImgMarginTop);
            this.bringChildToFront(mImg);
        }
        
        if (mText != null) {
            getViewOffsetHelper(mText).onViewLayout(mTitleMarginLeft, mTitleMarginTop);
            this.bringChildToFront(mText);
        }
    }
    
    static ViewHelper getViewOffsetHelper(View view) {
        ViewHelper offsetHelper = (ViewHelper) view.getTag(R.id.view_helper);
        if (offsetHelper == null) {
            offsetHelper = new ViewHelper(view);
            view.setTag(R.id.view_helper, offsetHelper);
        }
        return offsetHelper;
    }
    
    
    private static int getHeightWithMargins(@NonNull final View view) {
        final ViewGroup.LayoutParams lp = view.getLayoutParams();
        if (lp instanceof MarginLayoutParams) {
            final MarginLayoutParams mlp = (MarginLayoutParams) lp;
            return view.getHeight() + mlp.topMargin + mlp.bottomMargin;
        }
        return view.getHeight();
    }
    
    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        ViewParent viewParent = getParent();
        if (viewParent instanceof AppBarLayout) {
            if (mOffsetChangedListener == null) mOffsetChangedListener = new OffsetListenerImp();
            ((AppBarLayout) viewParent).addOnOffsetChangedListener(mOffsetChangedListener);
        }
    }
    
    @Override
    protected void onDetachedFromWindow() {
        ViewParent viewParent = getParent();
        if (viewParent instanceof AppBarLayout) {
            ((AppBarLayout) viewParent).removeOnOffsetChangedListener(mOffsetChangedListener);
        }
        super.onDetachedFromWindow();
    }
    
    
    final int getMaxOffsetForPinChild(View child) {
        final ViewHelper offsetHelper = getViewOffsetHelper(child);
        final LayoutParams lp = (LayoutParams) child.getLayoutParams();
        return getHeight()
                - offsetHelper.getLayoutTop()
                - child.getHeight()
                - lp.bottomMargin;
    }
    
    static int constrain(int amount, int low, int high) {
        return amount < low ? low : (amount > high ? high : amount);
    }
    
    static int constrain(int amount, int low) {
        return amount < low ? low : amount;
    }
    
    private void setTopAndBottomOffset(View child, int verticalOffset) {
        ViewHelper viewHelper = (ViewHelper) child.getTag(R.id.view_helper);
        viewHelper.setTopAndBottomOffset(
                constrain(-verticalOffset, 0, getMaxOffsetForPinChild(child)));
    }
    
    private void setTopAndBottomOffset(View child, int verticalOffset, float scale) {
        ViewHelper viewHelper = (ViewHelper) child.getTag(R.id.view_helper);
        viewHelper.setTopAndBottomOffset(
                constrain(-verticalOffset - getMaxOffset(viewHelper, scale),
                        0));
    }
    
    private void setLeftAndRightOffset(View child, int verticalOffset, float scale) {
        ViewHelper viewHelper = (ViewHelper) child.getTag(R.id.view_helper);
        int maxOffsetDistance = getMaxOffset(viewHelper, scale);
        int maxLeft = viewHelper.getLayoutLeft()
                + (viewHelper.getViewWidth() - viewHelper.getScaleViewWidth(scale))
                - viewHelper.getMarginTitleLeft();
        int realOffset = (int) (maxLeft * 1.0f / (maxOffsetDistance * 1.0f) * verticalOffset);
        realOffset = constrain(realOffset, -maxLeft, maxLeft);
        viewHelper.setLeftAndRightOffset(realOffset);
    }
    
    private void setViewScale(View child, int verticalOffset, float scale) {
        ViewHelper viewHelper = (ViewHelper) child.getTag(R.id.view_helper);
        int maxOffsetDistance = getMaxOffset(viewHelper, scale);
        float realScale = -verticalOffset - maxOffsetDistance > 0 ? scale : verticalOffset == 0 ? 1f : 0f;
        if (realScale == 0) {
            realScale = (maxOffsetDistance + verticalOffset * (1 - scale)) / (maxOffsetDistance * 1f);
        }
        viewHelper.setViewOffsetScale(realScale);
    }
    
    private int getMaxOffset(ViewHelper viewHelper, float scale) {
        int scaleViewHeight = (int) (scale * viewHelper.getViewHeight());
        int offsetTitleDistance = scaleViewHeight >= mTitleHeight ? 0 : (mTitleHeight - scaleViewHeight) / 2;
        int marginTop = viewHelper.getMarginTitleTop() >= offsetTitleDistance ? offsetTitleDistance : viewHelper.getMarginTitleTop();
        return viewHelper.getLayoutBottom() - viewHelper.getScaleViewHeight(scale) - offsetTitleDistance - marginTop;
    }
    
    private class OffsetListenerImp implements AppBarLayout.OnOffsetChangedListener {
        @Override
        public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
            if (mTitle != null) {
                setTopAndBottomOffset(mTitle, verticalOffset);
            }
            if (mText != null) {
                setTopAndBottomOffset(mText, verticalOffset, mTextScale);
                setLeftAndRightOffset(mText, verticalOffset, mTextScale);
                setViewScale(mText, verticalOffset, mTextScale);
            }
            if (mImg != null) {
                setTopAndBottomOffset(mImg, verticalOffset, mImgScale);
                setLeftAndRightOffset(mImg, verticalOffset, mImgScale);
                setViewScale(mImg, verticalOffset, mImgScale);
            }
        }
    }
    
    public void setImgTitleMarginTop(int top) {
        if (mImg != null) {
            getViewOffsetHelper(mImg).setMarginTitleTop(top);
        }
    }
    
    public void setImgTitleMarginLeft(int left) {
        if (mImg != null) {
            getViewOffsetHelper(mImg).setMarginTitleLeft(left);
        }
    }
    
    public void setTextTitleMarginTop(int top) {
        if (mText != null) {
            getViewOffsetHelper(mText).setMarginTitleTop(top);
        }
    }
    
    public void setImgTextMarginLeft(int left) {
        if (mText != null) {
            getViewOffsetHelper(mText).setMarginTitleLeft(left);
        }
    }
}
