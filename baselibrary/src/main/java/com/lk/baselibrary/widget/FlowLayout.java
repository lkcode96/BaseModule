package com.lk.baselibrary.widget;

import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

/**
 * @author: Administrator
 * @date: 2021/6/7
 */
public class FlowLayout extends ViewGroup {

    public FlowLayout(Context context) {
        this(context, null);
    }

    public FlowLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public FlowLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    private int currposition = 0;
    private List<List<View>> mLineViews = new ArrayList<List<View>>();
    private List<Integer> mLineHeight = new ArrayList<Integer>();

    /**
     * 测量所有子View大小,确定ViewGroup的宽高
     *
     * @param widthMeasureSpec
     * @param heightMeasureSpec
     */
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        //由于onMeasure会执行多次,避免重复的计算控件个数和高度,这里需要进行清空操作
        mLineViews.clear();
        mLineHeight.clear();

        //获取测量的模式和尺寸大小
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int widthSize = MeasureSpec.getSize(widthMeasureSpec) - getPaddingLeft() - getPaddingRight();
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        int heightSize = MeasureSpec.getSize(heightMeasureSpec) + getPaddingTop() + getPaddingBottom();


        //记录ViewGroup真实的测量宽高
        int viewGroupWidth = 0 - getPaddingLeft() - getPaddingRight();
        int viewGroupHeight = getPaddingTop() + getPaddingBottom();

        if (widthMode == MeasureSpec.EXACTLY && heightMode == MeasureSpec.EXACTLY) {
            viewGroupWidth = widthSize;
            viewGroupHeight = heightSize;
        } else {
            //当前所占的宽高
            int currentLineWidth = 0;
            int currentLineHeight = 0;

            //用来存储每一行上的子View
            List<View> lineView = new ArrayList<View>();
            int childViewsCount = getChildCount();
            for (int i = 0; i < childViewsCount; i++) {
                View childView = getChildAt(i);
                //对子View进行测量
                measureChild(childView, widthMeasureSpec, heightMeasureSpec);
                MarginLayoutParams marginLayoutParams = (MarginLayoutParams) childView.getLayoutParams();
                int childViewWidth = childView.getMeasuredWidth() + marginLayoutParams.leftMargin + marginLayoutParams.rightMargin;
                int childViewHeight = childView.getMeasuredHeight() + marginLayoutParams.topMargin + marginLayoutParams.bottomMargin;

                if (currentLineWidth + childViewWidth > widthSize) {
                    //当前行宽+子View+左右外边距>ViewGroup的宽度,换行
                    viewGroupWidth = Math.max(currentLineWidth, widthSize);
                    viewGroupHeight += currentLineHeight;
                    //添加行高
                    mLineHeight.add(currentLineHeight);
                    //添加行对象
                    mLineViews.add(lineView);

                    //new新的一行
                    lineView = new ArrayList<View>();
                    //添加行对象里的子View
                    lineView.add(childView);
                    currentLineWidth = childViewWidth;

                } else {
                    //当前行宽+子View+左右外边距<=ViewGroup的宽度,不换行
                    currentLineWidth += childViewWidth;
                    currentLineHeight = Math.max(currentLineHeight, childViewHeight);
                    //添加行对象里的子View
                    lineView.add(childView);
                }


                if (i == childViewsCount - 1) {
                    //最后一个子View的时候
                    //添加行对象
                    mLineViews.add(lineView);
                    viewGroupWidth = Math.max(childViewWidth, viewGroupWidth);
                    viewGroupHeight += childViewHeight;
                    //添加行高
                    mLineHeight.add(currentLineHeight);

                }


            }

        }

        setMeasuredDimension(viewGroupWidth, viewGroupHeight);

    }

    public void setDefaultView(int position) {
        currposition = position;
    }

    /**
     * 设置ViewGroup里子View的具体位置
     *
     * @param changed
     * @param l
     * @param t
     * @param r
     * @param b
     */
    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {

        int left = getPaddingLeft();
        int top = getPaddingTop();
        //一共有几行
        int lines = mLineViews.size();
        for (int i = 0; i < lines; i++) {
            //每行行高
            int lineHeight = mLineHeight.get(i);
            //行内有几个子View
            List<View> viewList = mLineViews.get(i);
            int views = viewList.size();

            for (int j = 0; j < views; j++) {
                View view = viewList.get(j);
                MarginLayoutParams marginLayoutParams = (MarginLayoutParams) view.getLayoutParams();
                int vl = left + marginLayoutParams.leftMargin;
                int vt = top + marginLayoutParams.topMargin;
                int vr = vl + view.getMeasuredWidth();
                int vb = vt + view.getMeasuredHeight();
                view.layout(vl, vt, vr, vb);
                left += view.getMeasuredWidth() + marginLayoutParams.leftMargin + marginLayoutParams.rightMargin;
            }
            left = getPaddingLeft();
            top += lineHeight;

        }


    }

    private void createTagView(String str, int position) {
        TextView tv = new TextView(getContext());
        MarginLayoutParams lp = new MarginLayoutParams(100,100);
        lp.setMargins(20, 30, 20, 30);
        tv.setLayoutParams(lp);
        tv.setText(str);
        tv.setTextSize(14);
        tv.setPadding(36, 10, 36, 10);
        tv.setTextColor(Color.BLACK);
    //    tv.setBackground(getContext().getResources().getDrawable(R.drawable.tag_bg, null));
        tv.setTag(position);
//        if (position == currposition) {
//            tv.setSelected(true);
//        } else {
//            tv.setSelected(false);
//        }
        addView(tv);
    }

    public void setTags(List<String> list) {
        if (getChildCount() > 0) {
            removeAllViews();
        }
        if (null != list) {
            int size = list.size();
            for (int i = 0; i < size; i++) {
                createTagView(list.get(i), i);
            }
        }
        invalidate();
    }

    /**
     * 接收自定义类型标签
     *
     * @param list   标签列表
     * @param method 能描述标签的字符串方法
     * @param <T>
     */
    public <T> void setTags(List<T> list, String method) {
        if (getChildCount() > 0) {
            removeAllViews();
        }
        if (null != list) {
            int size = list.size();
            for (int i = 0; i < size; i++) {
                T t = list.get(i);
                Class cls = t.getClass();
                try {
                    Method m = cls.getDeclaredMethod(method);
                    String str = (String) m.invoke(t);
                    createTagView(str, i);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            invalidate();
        }
    }

    /**
     * 指定ViewGroup的LayoutParams
     *
     * @param attrs
     * @return
     */
    @Override
    public LayoutParams generateLayoutParams(AttributeSet attrs) {
        return new MarginLayoutParams(getContext(), attrs);
    }

}
