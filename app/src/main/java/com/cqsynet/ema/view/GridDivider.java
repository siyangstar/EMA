package com.cqsynet.ema.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.RecyclerView;
import android.view.View;

/**
 * RecyclerView的Grid分隔线
 */
public class GridDivider extends RecyclerView.ItemDecoration {

    private Drawable mDividerDarwable;
    private int mDividerSize = 1;
    private Paint mColorPaint;
    private int mSpanCount; //列数


    public final int[] ATRRS = new int[]{android.R.attr.listDivider};

    public GridDivider(Context context) {
        final TypedArray ta = context.obtainStyledAttributes(ATRRS);
        this.mDividerDarwable = ta.getDrawable(0);
        ta.recycle();
    }

    /*
     int dividerSize  分割线的线宽
     int dividerColor  分割线的颜色
     */
    public GridDivider(Context context, int dividerSize, int dividerColor, int spanCount) {
        this(context);
        mDividerSize = dividerSize;
        mColorPaint = new Paint();
        mColorPaint.setColor(dividerColor);
        mSpanCount = spanCount;
    }

    /*
     int dividerSize  分割线的线宽
     Drawable dividerDrawable  图片分割线
     */
    public GridDivider(Context context, int dividerSize, Drawable dividerDrawable, int spanCount) {
        this(context);
        mDividerSize = dividerSize;
        mDividerDarwable = dividerDrawable;
        mSpanCount = spanCount;
    }

    @Override
    public void onDraw(Canvas c, RecyclerView parent, RecyclerView.State state) {
        super.onDraw(c, parent, state);
        //画水平和垂直分割线
        drawHorizontalDivider(c, parent);
        drawVerticalDivider(c, parent);
    }

    public void drawVerticalDivider(Canvas c, RecyclerView parent) {
        final int childCount = parent.getChildCount();
        for (int i = 0; i < childCount; i++) {
            final View child = parent.getChildAt(i);
            final RecyclerView.LayoutParams params = (RecyclerView.LayoutParams) child.getLayoutParams();

            final int top = child.getTop() - params.topMargin;
            final int bottom = child.getBottom() + params.bottomMargin;

            int left = 0;
            int right = 0;

            //左边第一列需要左分割线
            if ((i % mSpanCount) == 0) {
                //item左边分割线
                left = child.getLeft() - params.leftMargin;
                right = left + mDividerSize;
                mDividerDarwable.setBounds(left, top, right, bottom);
                mDividerDarwable.draw(c);
                if (mColorPaint != null) {
                    c.drawRect(left, top, right, bottom, mColorPaint);
                }
            }
            //右边分割线
            left = child.getRight() + params.rightMargin;
            right = left + mDividerSize;
            mDividerDarwable.setBounds(left, top, right, bottom);
            mDividerDarwable.draw(c);
            if (mColorPaint != null) {
                c.drawRect(left, top, right, bottom, mColorPaint);
            }

        }
    }

    public void drawHorizontalDivider(Canvas c, RecyclerView parent) {

        final int childCount = parent.getChildCount();
        for (int i = 0; i < childCount; i++) {
            final View child = parent.getChildAt(i);
            RecyclerView.LayoutParams params = (RecyclerView.LayoutParams) child.getLayoutParams();

            final int left = child.getLeft() - params.leftMargin - mDividerSize;
            final int right = child.getRight() + params.rightMargin;
            int top = 0;
            int bottom = 0;

            // 最上面一行
            if (i < mSpanCount) {
                //当前item最上面的分割线
                top = child.getTop() - params.topMargin;
                bottom = top + mDividerSize;
                mDividerDarwable.setBounds(left, top, right, bottom);
                mDividerDarwable.draw(c);
                if (mColorPaint != null) {
                    c.drawRect(left, top, right, bottom, mColorPaint);
                }
            }
            //底部分割线
            top = child.getBottom() + params.bottomMargin;
            bottom = top + mDividerSize;
            mDividerDarwable.setBounds(left, top, right, bottom);
            mDividerDarwable.draw(c);
            if (mColorPaint != null) {
                c.drawRect(left, top, right, bottom, mColorPaint);
            }
        }
    }
}
