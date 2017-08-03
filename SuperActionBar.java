package com.udofy.ui.view;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.drawable.Drawable;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.Display;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.codetail.graphics.drawables.LollipopDrawablesCompat;
import com.udofy.R;

import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicInteger;


/**
 * Created by abhayalekal on 20/07/17.
 */
public class SuperActionBar extends RelativeLayout {
    private static final AtomicInteger sNextGeneratedId = new AtomicInteger(1);
    private Context context;
    private ImageView leftMostIconView, penultimateRightMostIconView, rightMostIconView, dropdownView;
    private TextView titleView, nextView, subtitleTextView;
    private RelativeLayout subtitleView;
    private ArrayList<View> visibleViews;
    private int DIMENSION, DROPDOWN_ICON_WIDTH, NEXT_BUTTON_WIDTH, RM_DIM, PEN_RM_DIM;
    private int[] titleViewRules;
    private TouchListener touchListener;

    public SuperActionBar(Context context) {
        super(context);
        this.context = context;
        initialize();
    }

    public SuperActionBar(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        initialize();
    }

    private void initialize() {
        DIMENSION = pxFromDp(context, 48);
        RM_DIM = pxFromDp(context, 48);
        PEN_RM_DIM = pxFromDp(context, 48);

        visibleViews = new ArrayList<>();
        ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, DIMENSION);
        setBackgroundColor(Color.parseColor("#333333"));
        setLayoutParams(params);
        setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (touchListener != null) {
                    touchListener.onSuperActionBarClicked();
                }
            }
        });
        invalidate();
        requestLayout();
    }

    public SuperActionBar setTouchListener(TouchListener touchListener) {
        this.touchListener = touchListener;
        return this;
    }

    public SuperActionBar setLeftMostIconView(int drawable) {
        return setLeftMostIconView(drawable, 16);
    }

    public SuperActionBar setLeftMostIconView(int drawable, int paddingInDp) {
        if (leftMostIconView == null) {
            leftMostIconView = new ImageView(context);
            addView(leftMostIconView);
            leftMostIconView.setId(generateViewId());
            LayoutParams params = new LayoutParams(DIMENSION, ViewGroup.LayoutParams.MATCH_PARENT);
            params.addRule(ALIGN_PARENT_LEFT);
            leftMostIconView.setLayoutParams(params);
            int padding = pxFromDp(context, paddingInDp);
            leftMostIconView.setPadding(padding, padding, padding, padding);
            leftMostIconView.setImageResource(drawable);
            leftMostIconView.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (touchListener != null) {
                        touchListener.onLeftMostIconClicked();
                    }
                }
            });
            if (visibleViews.contains(titleView)) {
                LayoutParams titleParams = (LayoutParams) titleView.getLayoutParams();
                titleParams.leftMargin = DIMENSION;
                titleView.setLayoutParams(titleParams);
            }
            if (visibleViews.contains(subtitleView)) {
                LayoutParams subtitleParams = (LayoutParams) subtitleView.getLayoutParams();
                subtitleParams.leftMargin = DIMENSION;
                subtitleView.setLayoutParams(subtitleParams);
            }
            setBackground(leftMostIconView, R.drawable.btn_ripple_drawable, context, R.drawable.alternate_card_background);
            visibleViews.add(leftMostIconView);
            invalidate();
            requestLayout();
        }
        return this;
    }

    public static int generateViewId() {
        for (; ; ) {
            final int result = sNextGeneratedId.get();
            int newValue = result + 1;
            if (newValue > 0x00FFFFFF) newValue = 1;
            if (sNextGeneratedId.compareAndSet(result, newValue)) {
                return result;
            }
        }
    }

    public SuperActionBar setTitle(String title, int color, int endPaddingInDP, int[] rules) {
        if (titleView == null) {
            titleView = new TextView(context);
            addView(titleView);
            titleViewRules = rules;
            titleView.setId(generateViewId());
            LayoutParams params = new LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            titleView.setText(title);
            titleView.setTextColor(color);
            titleView.setMaxLines(1);
            titleView.setEllipsize(TextUtils.TruncateAt.END);
            titleView.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 20);
            if (visibleViews.contains(leftMostIconView)) {
                params.leftMargin = DIMENSION;
            }
            if (visibleViews.contains(subtitleView)) {
                params.topMargin = pxFromDp(context, 8);
            } else {
                params.addRule(CENTER_VERTICAL);
                if (titleViewRules != null) {
                    for (Integer rule : titleViewRules) {
                        params.addRule(rule);
                    }
                }
            }
            params.rightMargin = (visibleViews.contains(rightMostIconView) ? RM_DIM : 0) + (visibleViews.contains(penultimateRightMostIconView) ? PEN_RM_DIM : 0);
            int endPaddingInPixels = pxFromDp(context, endPaddingInDP);
            titleView.setPadding(endPaddingInPixels, 0, endPaddingInPixels, 0);
            titleView.setLayoutParams(params);
            titleView.setVisibility(View.VISIBLE);
            titleView.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (touchListener != null) {
                        touchListener.onTitleClicked();
                    }
                }
            });
            visibleViews.add(titleView);
        } else {
            titleView.setText(title);
        }
        invalidate();
        requestLayout();
        return this;
    }

    public SuperActionBar setTitle(String title) {
        return setTitle(title, Color.WHITE, 0, null);
    }

    public SuperActionBar setTitle(String title, int endPaddingInDP) {
        return setTitle(title, Color.WHITE, endPaddingInDP, null);
    }

    public SuperActionBar setTitle(String title, int endPaddingInDP, int[] rules) {
        return setTitle(title, Color.WHITE, endPaddingInDP, rules);
    }

    public SuperActionBar setSubtitle(String subtitle) {
        return setSubtitle(subtitle, 0);
    }

    public SuperActionBar setSubtitle(String subtitle, int paddingInDp) {
        if (subtitleView == null) {
            subtitleView = new RelativeLayout(context);
            addView(subtitleView);
            subtitleView.setId(generateViewId());
            LayoutParams params = new LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            params.topMargin = pxFromDp(context, 24);
            if (visibleViews.contains(leftMostIconView)) {
                params.leftMargin = DIMENSION;
            }
            if (visibleViews.contains(titleView)) {
                LayoutParams titleParams = (LayoutParams) titleView.getLayoutParams();
                titleParams.addRule(CENTER_VERTICAL, 0);
                titleParams.topMargin = pxFromDp(context, 8);
                titleParams.addRule(RelativeLayout.CENTER_HORIZONTAL, 0);
                titleParams.addRule(RelativeLayout.CENTER_IN_PARENT, 0);
                titleView.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 12);
                titleView.setLayoutParams(titleParams);
            }
            params.rightMargin = (visibleViews.contains(rightMostIconView) ? RM_DIM : 0) + (visibleViews.contains(penultimateRightMostIconView) ? PEN_RM_DIM : 0);
            subtitleView.setLayoutParams(params);
            subtitleTextView = new TextView(context);
            subtitleTextView.setText(subtitle);
            subtitleTextView.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 16);
            subtitleTextView.setMaxLines(1);
            subtitleTextView.setEllipsize(TextUtils.TruncateAt.END);
            subtitleTextView.setId(generateViewId());
            //subtitleTextView.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.arrow_down_white, 0);
            subtitleTextView.setTextColor(Color.WHITE);
            LayoutParams subtitleTextLayoutParams = new LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            dropdownView = new ImageView(context);
            dropdownView.setImageResource(R.drawable.arrow_down_white);
            LayoutParams dropdownParams = new LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            dropdownParams.addRule(RIGHT_OF, subtitleTextView.getId());
            dropdownParams.addRule(ALIGN_TOP, subtitleTextView.getId());
            dropdownParams.addRule(ALIGN_BOTTOM, subtitleTextView.getId());
            dropdownView.setId(generateViewId());
            dropdownView.setPadding(pxFromDp(context, 4), 0, pxFromDp(context, 20), 0);
            dropdownView.setVisibility(GONE);
            DROPDOWN_ICON_WIDTH = measureCellWidth(context, dropdownView);
            //subtitleTextLayoutParams.width = getAdjustedSubtitleWidth();
            subtitleTextView.setLayoutParams(subtitleTextLayoutParams);
            dropdownView.setLayoutParams(dropdownParams);
            subtitleView.setPadding(pxFromDp(context, paddingInDp), 0, 0, 0);
            subtitleView.addView(subtitleTextView);
            subtitleView.addView(dropdownView);
            subtitleView.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (touchListener != null) {
                        touchListener.onSubtitleClicked();
                    }
                }
            });
            subtitleView.setVisibility(View.VISIBLE);
            visibleViews.add(subtitleView);
        } else {
            subtitleTextView.setText(subtitle);
        }
        invalidate();
        requestLayout();
        return this;
    }

    public SuperActionBar hideSubtitle() {
        if (subtitleView != null) {
            visibleViews.remove(subtitleView);
            subtitleView.setVisibility(GONE);
            if (visibleViews.contains(titleView)) {
                LayoutParams titleParams = (LayoutParams) titleView.getLayoutParams();
                titleParams.addRule(CENTER_VERTICAL);
                if (titleViewRules != null) {
                    for (int rule : titleViewRules) {
                        titleParams.addRule(rule);
                    }
                }
                titleView.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 20);
                titleView.setLayoutParams(titleParams);
            }
            invalidate();
            requestLayout();
        }
        return this;
    }

    public SuperActionBar showSubtitle() {
        if (subtitleView != null) {
            visibleViews.add(subtitleView);
            subtitleView.setVisibility(VISIBLE);
            if (visibleViews.contains(titleView)) {
                LayoutParams titleParams = (LayoutParams) titleView.getLayoutParams();
                titleParams.addRule(CENTER_VERTICAL, 0);
                titleParams.topMargin = pxFromDp(context, 8);
                titleParams.addRule(RelativeLayout.CENTER_HORIZONTAL, 0);
                titleParams.addRule(RelativeLayout.CENTER_IN_PARENT, 0);
                titleView.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 12);
                titleView.setLayoutParams(titleParams);
            }
            invalidate();
            requestLayout();
        }
        return this;
    }

    public SuperActionBar setDropdown() {
        if (dropdownView != null) {
            dropdownView.setVisibility(VISIBLE);
            dropdownView.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (touchListener != null) {
                        touchListener.onDropdownClicked();
                    }
                }
            });
        } else {
            throw new IllegalStateException("SuperActionBar: cannot set dropdown before setting subtitle");
        }
        return this;
    }

    public SuperActionBar setPenultimateRightMostIconView(int drawable) {
        return setPenultimateRightMostIconView(drawable, 12);
    }

    public SuperActionBar setPenultimateRightMostIconView(int drawable, int paddingInDp) {
        if (penultimateRightMostIconView == null) {
            penultimateRightMostIconView = new ImageView(context);
            addView(penultimateRightMostIconView);
            visibleViews.add(penultimateRightMostIconView);
            penultimateRightMostIconView.setId(generateViewId());
            if (visibleViews.contains(nextView)) {
                visibleViews.remove(nextView);
                nextView.setVisibility(GONE);
            }
            LayoutParams params = new LayoutParams(PEN_RM_DIM, ViewGroup.LayoutParams.MATCH_PARENT);
            int rightMargin = PEN_RM_DIM;
            if (visibleViews.contains(rightMostIconView)) {
                rightMargin += RM_DIM;
                params.addRule(LEFT_OF, rightMostIconView.getId());
            } else {
                params.addRule(ALIGN_PARENT_RIGHT);
            }
            if (visibleViews.contains(titleView)) {
                LayoutParams titleParams = (LayoutParams) titleView.getLayoutParams();
                titleParams.rightMargin = rightMargin;
                titleView.setLayoutParams(titleParams);
            }
            if (visibleViews.contains(subtitleView)) {
                LayoutParams subtitleParams = (LayoutParams) subtitleView.getLayoutParams();
                subtitleParams.rightMargin = rightMargin;
                subtitleTextView.getLayoutParams().width = getAdjustedSubtitleWidth();
                subtitleView.setLayoutParams(subtitleParams);
            }
            penultimateRightMostIconView.setLayoutParams(params);
            int padding = pxFromDp(context, paddingInDp);
            penultimateRightMostIconView.setPadding(padding, padding, padding, padding);
            penultimateRightMostIconView.setImageResource(drawable);
            penultimateRightMostIconView.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (touchListener != null) {
                        touchListener.onPenultimateRightMostIconClicked();
                    }
                }
            });
            setBackground(penultimateRightMostIconView, R.drawable.btn_ripple_drawable, context, R.drawable.alternate_card_background);
            penultimateRightMostIconView.setVisibility(View.VISIBLE);
        } else if (penultimateRightMostIconView.getVisibility() == GONE) {
            visibleViews.add(penultimateRightMostIconView);
            penultimateRightMostIconView.setVisibility(VISIBLE);
            if (rightMostIconView != null) {
                visibleViews.add(rightMostIconView);
                rightMostIconView.setVisibility(VISIBLE);
            }
        }
        invalidate();
        requestLayout();
        return this;
    }

    private int getAdjustedSubtitleWidth() {
        int rightMostViewsWidth = (visibleViews.contains(penultimateRightMostIconView) ? PEN_RM_DIM : 0) + (visibleViews.contains(rightMostIconView) ? RM_DIM : 0);
        int subtitleTextViewWidth = measureCellWidth(context, subtitleTextView);
        int deviceWidth = getDeviceDimensions((Activity) context).x;
        int leftIconWidth = visibleViews.contains(leftMostIconView) ? DIMENSION : 0;
        if (leftIconWidth + subtitleTextViewWidth + DROPDOWN_ICON_WIDTH > deviceWidth - rightMostViewsWidth) {
            return deviceWidth - leftIconWidth - rightMostViewsWidth - DROPDOWN_ICON_WIDTH;
        }
        return subtitleTextViewWidth;
    }

    public SuperActionBar setRightMostIconView(int drawable) {
        return setRightMostIconView(drawable, 16);
    }

    public SuperActionBar setRightMostIconView(int drawable, int paddingInDp) {
        if (rightMostIconView == null) {
            rightMostIconView = new ImageView(context);
            addView(rightMostIconView);
            visibleViews.add(rightMostIconView);
            rightMostIconView.setId(generateViewId());
            if (visibleViews.contains(nextView)) {
                visibleViews.remove(nextView);
                removeView(nextView);
            }
            LayoutParams params = new LayoutParams(RM_DIM, ViewGroup.LayoutParams.MATCH_PARENT);
            params.addRule(ALIGN_PARENT_RIGHT);
            rightMostIconView.setLayoutParams(params);
            int padding = pxFromDp(context, paddingInDp);
            rightMostIconView.setPadding(padding, padding, padding, padding);
            rightMostIconView.setImageResource(drawable);
            rightMostIconView.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (touchListener != null) {
                        touchListener.onRightMostIconClicked();
                    }
                }
            });
            setBackground(rightMostIconView, R.drawable.btn_ripple_drawable, context, R.drawable.alternate_card_background);
            rightMostIconView.setVisibility(View.VISIBLE);
            int rightMargin = RM_DIM;
            if (visibleViews.contains(penultimateRightMostIconView)) {
                rightMargin += RM_DIM;
                LayoutParams immediateLeftIconParams = (LayoutParams) titleView.getLayoutParams();
                immediateLeftIconParams.rightMargin = RM_DIM;
                immediateLeftIconParams.addRule(ALIGN_PARENT_RIGHT, 0);
                titleView.setLayoutParams(immediateLeftIconParams);
            }
            if (visibleViews.contains(titleView)) {
                LayoutParams titleParams = (LayoutParams) titleView.getLayoutParams();
                titleParams.rightMargin = rightMargin;
                titleView.setLayoutParams(titleParams);
            }
            if (visibleViews.contains(subtitleView)) {
                LayoutParams subtitleParams = (LayoutParams) subtitleView.getLayoutParams();
                subtitleParams.rightMargin = rightMargin;
                subtitleTextView.getLayoutParams().width = getAdjustedSubtitleWidth();
                subtitleView.setLayoutParams(subtitleParams);
            }
        } else if (rightMostIconView.getVisibility() == GONE) {
            visibleViews.add(rightMostIconView);
            rightMostIconView.setVisibility(VISIBLE);
            if (penultimateRightMostIconView != null) {
                visibleViews.add(penultimateRightMostIconView);
                penultimateRightMostIconView.setVisibility(VISIBLE);
            }
        }
        invalidate();
        requestLayout();
        return this;
    }

    public SuperActionBar setNextButton() {
        return setNextButtonWithCustomText("NEXT");
    }

    public SuperActionBar setNextButtonWithCustomText(String text) {
        if (nextView == null) {
            nextView = new TextView(context);
            addView(nextView);
            LayoutParams params = new LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.MATCH_PARENT);
            nextView.setGravity(Gravity.CENTER_VERTICAL);
            nextView.setId(generateViewId());
            if (visibleViews.contains(rightMostIconView)) {
                visibleViews.remove(rightMostIconView);
                rightMostIconView.setVisibility(GONE);
            }
            if (visibleViews.contains(penultimateRightMostIconView)) {
                visibleViews.remove(penultimateRightMostIconView);
                penultimateRightMostIconView.setVisibility(GONE);
            }
            nextView.setText(text);
            nextView.setTextColor(Color.WHITE);
            nextView.setVisibility(View.VISIBLE);
            params.addRule(ALIGN_PARENT_RIGHT);
            nextView.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 16);
            nextView.setLayoutParams(params);
            int padding = pxFromDp(context, 24);
            nextView.setPadding(padding, 0, padding, 0);
            NEXT_BUTTON_WIDTH = measureCellWidth(context, nextView);
            int rightMargin = NEXT_BUTTON_WIDTH;
            if (visibleViews.contains(titleView)) {
                LayoutParams titleParams = (LayoutParams) titleView.getLayoutParams();
                titleParams.rightMargin = rightMargin;
                titleView.setLayoutParams(titleParams);
            }
            if (visibleViews.contains(subtitleView)) {
                LayoutParams subtitleParams = (LayoutParams) subtitleView.getLayoutParams();
                subtitleParams.rightMargin = rightMargin;
                subtitleView.setLayoutParams(subtitleParams);
            }
            nextView.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (touchListener != null) {
                        touchListener.onNextClicked();
                    }
                }
            });
            visibleViews.add(nextView);
            disableNextButton();
            invalidate();
            requestLayout();
        }
        return this;
    }

    public SuperActionBar disableNextButton() {
        if (visibleViews.contains(nextView)) {
            nextView.setEnabled(false);
            nextView.setBackgroundColor(Color.parseColor("#999999"));
        }
        return this;
    }

    public SuperActionBar enableNextButton() {
        if (visibleViews.contains(nextView)) {
            nextView.setEnabled(true);
            nextView.setBackgroundColor(getResources().getColor(R.color.app_green_1));
        }
        return this;
    }

    public void hideRightMostIconView() {
        if (visibleViews.contains(rightMostIconView)) {
            ViewGroup.LayoutParams layoutParams = rightMostIconView.getLayoutParams();
            layoutParams.width = 0;
        }
    }

    public void hidePenultimateIconView() {
        if (visibleViews.contains(penultimateRightMostIconView)) {
            ViewGroup.LayoutParams layoutParams = penultimateRightMostIconView.getLayoutParams();
            layoutParams.width = 0;
        }
    }

    public interface TouchListener {
        void onSuperActionBarClicked();

        void onLeftMostIconClicked();

        void onTitleClicked();

        void onSubtitleClicked();

        void onDropdownClicked();

        void onPenultimateRightMostIconClicked();

        void onRightMostIconClicked();

        void onNextClicked();
    }

    public static int pxFromDp(final Context context, final float dp) {
        return (int) (dp * context.getResources().getDisplayMetrics().density);
    }

    public static void setBackground(View view, int resource, Context context, int alternateResource) {
        if (view == null) {
            return;
        }
        try {
            view.setClickable(true);
            view.setBackgroundDrawable(getDrawable(context, resource));
        } catch (RuntimeException e) {
            e.printStackTrace();
            if (alternateResource > 0) {
                view.setBackgroundResource(alternateResource);
            } else {
                view.setBackgroundResource(R.drawable.alternate_background);
            }
        }
    }

    public static Drawable getDrawable(Context context, int id) {
        return LollipopDrawablesCompat.getDrawable(context.getResources(), id, context.getTheme());
    }

    public static Point getDeviceDimensions(Activity context) {
        Display display = context.getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        return size;
    }

    public static int measureCellWidth(Context context, View cell) {
        if (cell.getParent() == null) {
            FrameLayout buffer = new FrameLayout(context);
            android.widget.AbsListView.LayoutParams layoutParams = new android.widget.AbsListView.LayoutParams(
                    android.view.ViewGroup.LayoutParams.WRAP_CONTENT,
                    android.view.ViewGroup.LayoutParams.WRAP_CONTENT);
            buffer.addView(cell, layoutParams);
            cell.forceLayout();
            cell.measure(1000, 1000);
            int width = cell.getMeasuredWidth();
            buffer.removeAllViews();
            return width;
        } else {
            cell.forceLayout();
            cell.measure(1000, 1000);
            return cell.getMeasuredWidth();
        }
    }
}
