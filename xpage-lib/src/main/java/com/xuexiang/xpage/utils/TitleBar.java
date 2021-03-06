package com.xuexiang.xpage.utils;

import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.xuexiang.xpage.R;

import java.util.LinkedList;

/**
 * 标题栏
 *
 * @author xuexiang
 * @since 2018/5/24 下午3:35
 */
public class TitleBar extends ViewGroup implements View.OnClickListener {
    private static final String STATUS_BAR_HEIGHT_RES_NAME = "status_bar_height";

    private static int DEFAULT_TEXT_COLOR = Color.WHITE;//文字默认白色

    private TextView mLeftText;
    private LinearLayout mRightLayout;
    private LinearLayout mCenterLayout;
    private TextView mCenterText;
    private TextView mSubTitleText;
    private View mCustomCenterView;
    private View mDividerView;

    /**
     * 是否是沉浸模式
     */
    private boolean mImmersive;
    /**
     * 屏幕宽
     */
    private int mScreenWidth;
    /**
     * 标题栏的高度
     */
    private int mBarHeight;
    /**
     * 状态栏的高度
     */
    private int mStatusBarHeight;
    /**
     * 点击动作控件的padding
     */
    private int mActionPadding;
    /**
     * 左右侧文字的padding
     */
    private int mSideTextPadding;

    private int mSideTextSize;
    private int mTitleTextSize;
    private int mSubTitleTextSize;
    private int mActionTextSize;

    private int mSideTextColor;
    private int mTitleTextColor;
    private int mSubTitleTextColor;
    private int mActionTextColor;

    private Drawable mLeftImageResource;
    private String mLeftTextString;
    private String mTitleTextString;
    private String mSubTextString;
    private int mDividerColor;

    public TitleBar(Context context) {
        super(context);
        initAttrs(context, null);
        init(context);
    }

    public TitleBar(Context context, AttributeSet attrs) {
        super(context, attrs);
        initAttrs(context, attrs);
        init(context);
    }

    public TitleBar(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initAttrs(context, attrs);
        init(context);
    }

    private void initAttrs(Context context, AttributeSet attrs) {
        if (isInEditMode()) {
            return;
        }
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.TitleBar);
        if (typedArray != null) {
            mBarHeight = typedArray.getDimensionPixelOffset(R.styleable.TitleBar_tb_barHeight, Utils.resolveDimension(context, R.attr.xpage_actionbar_height, Utils.getDimensionPixelOffset(context, R.dimen.xpage_default_titlebar_height)));
            mImmersive = typedArray.getBoolean(R.styleable.TitleBar_tb_immersive, false);

            mActionPadding = typedArray.getDimensionPixelOffset(R.styleable.TitleBar_tb_actionPadding, Utils.resolveDimension(context, R.attr.xpage_actionbar_action_padding, Utils.getDimensionPixelOffset(context, R.dimen.xpage_default_action_padding)));
            mSideTextPadding = typedArray.getDimensionPixelOffset(R.styleable.TitleBar_tb_sideTextPadding, Utils.resolveDimension(context, R.attr.xpage_actionbar_sidetext_padding, Utils.getDimensionPixelOffset(context, R.dimen.xpage_default_sidetext_padding)));

            mSideTextSize = typedArray.getDimensionPixelSize(R.styleable.TitleBar_tb_sideTextSize, Utils.resolveDimension(context, R.attr.xpage_actionbar_action_text_size, Utils.getDimensionPixelSize(context, R.dimen.xpage_default_action_text_size)));
            mTitleTextSize = typedArray.getDimensionPixelSize(R.styleable.TitleBar_tb_titleTextSize, Utils.resolveDimension(context, R.attr.xpage_actionbar_title_text_size, Utils.getDimensionPixelSize(context, R.dimen.xpage_default_title_text_size)));
            mSubTitleTextSize = typedArray.getDimensionPixelSize(R.styleable.TitleBar_tb_subTitleTextSize, Utils.resolveDimension(context, R.attr.xpage_actionbar_sub_text_size, Utils.getDimensionPixelSize(context, R.dimen.xpage_default_sub_text_size)));
            mActionTextSize = typedArray.getDimensionPixelSize(R.styleable.TitleBar_tb_actionTextSize, Utils.resolveDimension(context, R.attr.xpage_actionbar_action_text_size, Utils.getDimensionPixelSize(context, R.dimen.xpage_default_action_text_size)));

            mSideTextColor = typedArray.getColor(R.styleable.TitleBar_tb_sideTextColor, DEFAULT_TEXT_COLOR);
            mTitleTextColor = typedArray.getColor(R.styleable.TitleBar_tb_titleTextColor, DEFAULT_TEXT_COLOR);
            mSubTitleTextColor = typedArray.getColor(R.styleable.TitleBar_tb_subTitleTextColor, DEFAULT_TEXT_COLOR);
            mActionTextColor = typedArray.getColor(R.styleable.TitleBar_tb_actionTextColor, DEFAULT_TEXT_COLOR);

            mLeftImageResource = typedArray.getDrawable(R.styleable.TitleBar_tb_leftImageResource);
            mLeftTextString = typedArray.getString(R.styleable.TitleBar_tb_leftText);
            mTitleTextString = typedArray.getString(R.styleable.TitleBar_tb_titleText);
            mSubTextString = typedArray.getString(R.styleable.TitleBar_tb_subTitleText);
            mDividerColor = typedArray.getColor(R.styleable.TitleBar_tb_dividerColor, Color.TRANSPARENT);

            typedArray.recycle();
        }
    }

    private void init(Context context) {
        mScreenWidth = getResources().getDisplayMetrics().widthPixels;
        if (mImmersive) {
            mStatusBarHeight = getStatusBarHeight();
        }
        initView(context);
    }

    private void initView(Context context) {
        mLeftText = new TextView(context);
        mCenterLayout = new LinearLayout(context);
        mRightLayout = new LinearLayout(context);
        mDividerView = new View(context);

        LayoutParams layoutParams = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.MATCH_PARENT);

        mLeftText.setTextSize(TypedValue.COMPLEX_UNIT_PX, mSideTextSize);
        mLeftText.setTextColor(mSideTextColor);
        mLeftText.setText(mLeftTextString);
        if (mLeftImageResource != null) {
            mLeftText.setCompoundDrawablesWithIntrinsicBounds(mLeftImageResource, null, null, null);
        }
        mLeftText.setSingleLine();

        mLeftText.setGravity(Gravity.CENTER_VERTICAL);
        mLeftText.setPadding(mSideTextPadding, 0, mSideTextPadding, 0);

        mCenterText = new TextView(context);
        mSubTitleText = new TextView(context);

        if (!TextUtils.isEmpty(mSubTextString)) {
            mCenterLayout.setOrientation(LinearLayout.VERTICAL);
        }
        mCenterLayout.addView(mCenterText);
        mCenterLayout.addView(mSubTitleText);
        mCenterLayout.setGravity(Gravity.CENTER);

        mCenterText.setTextSize(TypedValue.COMPLEX_UNIT_PX, mTitleTextSize);
        mCenterText.setTextColor(mTitleTextColor);
        mCenterText.setText(mTitleTextString);
        mCenterText.setSingleLine();
        mCenterText.setGravity(Gravity.CENTER);
        mCenterText.setEllipsize(TextUtils.TruncateAt.END);

        mSubTitleText.setTextSize(TypedValue.COMPLEX_UNIT_PX, mSubTitleTextSize);
        mSubTitleText.setTextColor(mSubTitleTextColor);
        mSubTitleText.setText(mSubTextString);
        mSubTitleText.setSingleLine();
        mSubTitleText.setGravity(Gravity.CENTER);
        mSubTitleText.setPadding(0, Utils.dip2px(getContext(), 2), 0, 0);
        mSubTitleText.setEllipsize(TextUtils.TruncateAt.END);

        mRightLayout.setPadding(mSideTextPadding, 0, mSideTextPadding, 0);

        mDividerView.setBackgroundColor(mDividerColor);

        addView(mLeftText, layoutParams);
        addView(mCenterLayout);
        addView(mRightLayout, layoutParams);
        addView(mDividerView, new LayoutParams(LayoutParams.MATCH_PARENT, 1));
    }

    public TitleBar setImmersive(boolean immersive) {
        mImmersive = immersive;
        if (mImmersive) {
            mStatusBarHeight = getStatusBarHeight();
        } else {
            mStatusBarHeight = 0;
        }
        return this;
    }

    public TitleBar setHeight(int height) {
        mBarHeight = height;
        setMeasuredDimension(getMeasuredWidth(), mBarHeight);
        return this;
    }

    public TitleBar setLeftImageResource(int resId) {
        mLeftText.setCompoundDrawablesWithIntrinsicBounds(resId, 0, 0, 0);
        return this;
    }

    public TitleBar setBackImageResource(int resId) {
        if (resId != 0) {
            mLeftImageResource = Utils.getDrawable(getContext(), resId);
            mLeftImageResource.setBounds(0, 0, Utils.dip2px(getContext(), 12), Utils.dip2px(getContext(), 22));
            mLeftText.setCompoundDrawables(mLeftImageResource, null, null, null);
        } else {
            mLeftImageResource = null;
            mLeftText.setCompoundDrawables(null, null, null, null);
        }
        return this;
    }

    public TitleBar setLeftClickListener(OnClickListener l) {
        mLeftText.setOnClickListener(l);
        return this;
    }

    public TitleBar setLeftText(CharSequence title) {
        mLeftText.setText(title);
        return this;
    }

    public TitleBar setLeftText(int resid) {
        mLeftText.setText(resid);
        return this;
    }

    public TitleBar setLeftTextSize(float size) {
        mLeftText.setTextSize(TypedValue.COMPLEX_UNIT_PX, size);
        return this;
    }

    public TitleBar setLeftTextColor(int color) {
        mLeftText.setTextColor(color);
        return this;
    }

    public TitleBar setLeftVisible(boolean visible) {
        mLeftText.setVisibility(visible ? View.VISIBLE : View.GONE);
        return this;
    }

    public TitleBar setTitle(CharSequence title) {
        int index = title.toString().indexOf("\n");
        if (index > 0) {
            setTitle(title.subSequence(0, index), title.subSequence(index + 1, title.length()), LinearLayout.VERTICAL);
        } else {
            index = title.toString().indexOf("\t");
            if (index > 0) {
                setTitle(title.subSequence(0, index), "  " + title.subSequence(index + 1, title.length()), LinearLayout.HORIZONTAL);
            } else {
                mCenterText.setText(title);
                mSubTitleText.setVisibility(View.GONE);
            }
        }
        return this;
    }

    private TitleBar setTitle(CharSequence title, CharSequence subTitle, int orientation) {
        mCenterLayout.setOrientation(orientation);
        mCenterText.setText(title);

        mSubTitleText.setText(subTitle);
        mSubTitleText.setVisibility(View.VISIBLE);
        return this;
    }

    public TitleBar setCenterClickListener(OnClickListener l) {
        mCenterLayout.setOnClickListener(l);
        return this;
    }

    public TitleBar setTitle(int resid) {
        setTitle(getResources().getString(resid));
        return this;
    }

    public TitleBar setTitleColor(int resid) {
        mCenterText.setTextColor(resid);
        return this;
    }

    public TitleBar setTitleSize(float size) {
        mCenterText.setTextSize(TypedValue.COMPLEX_UNIT_PX, size);
        return this;
    }

    public TitleBar setTitleBackground(int resid) {
        mCenterText.setBackgroundResource(resid);
        return this;
    }

    public TitleBar setSubTitleColor(int resid) {
        mSubTitleText.setTextColor(resid);
        return this;
    }

    public TitleBar setSubTitleSize(float size) {
        mSubTitleText.setTextSize(TypedValue.COMPLEX_UNIT_PX, size);
        return this;
    }

    public TitleBar setCustomTitle(View titleView) {
        if (titleView == null) {
            mCenterText.setVisibility(View.VISIBLE);
            if (mCustomCenterView != null) {
                mCenterLayout.removeView(mCustomCenterView);
            }

        } else {
            if (mCustomCenterView != null) {
                mCenterLayout.removeView(mCustomCenterView);
            }
            LayoutParams layoutParams = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
            mCustomCenterView = titleView;
            mCenterLayout.addView(titleView, layoutParams);
            mCenterText.setVisibility(View.GONE);
        }
        return this;
    }

    public TitleBar setDivider(Drawable drawable) {
        mDividerView.setBackgroundDrawable(drawable);
        return this;
    }

    public TitleBar setDividerColor(int color) {
        mDividerView.setBackgroundColor(color);
        return this;
    }

    public TitleBar setDividerHeight(int dividerHeight) {
        mDividerView.getLayoutParams().height = dividerHeight;
        return this;
    }

    public TitleBar setActionTextColor(int colorResId) {
        mActionTextColor = colorResId;
        return this;
    }

    /**
     * Function to set a click listener for Title TextView
     *
     * @param listener the onClickListener
     */
    public TitleBar setOnTitleClickListener(OnClickListener listener) {
        mCenterText.setOnClickListener(listener);
        return this;
    }

    @Override
    public void onClick(View view) {
        final Object tag = view.getTag();
        if (tag instanceof Action) {
            final Action action = (Action) tag;
            action.performAction(view);
        }
    }

    /**
     * Adds a list of {@link Action}s.
     * @param actionList the actions to add
     */
    public TitleBar addActions(ActionList actionList) {
        int actions = actionList.size();
        for (int i = 0; i < actions; i++) {
            addAction(actionList.get(i));
        }
        return this;
    }

    /**
     * Adds a new {@link Action}.
     * @param action the action to add
     */
    public View addAction(Action action) {
        final int index = mRightLayout.getChildCount();
        return addAction(action, index);
    }

    /**
     * Adds a new {@link Action} at the specified index.
     * @param action the action to add
     * @param index the position at which to add the action
     */
    public View addAction(Action action, int index) {
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT,
                LayoutParams.MATCH_PARENT);
        View view = inflateAction(action);
        mRightLayout.addView(view, index, params);
        return view;
    }

    /**
     * Removes all action views from this action bar
     */
    public void removeAllActions() {
        mRightLayout.removeAllViews();
    }

    /**
     * Remove a action from the action bar.
     * @param index position of action to remove
     */
    public void removeActionAt(int index) {
        mRightLayout.removeViewAt(index);
    }

    /**
     * Remove a action from the action bar.
     * @param action The action to remove
     */
    public void removeAction(Action action) {
        int childCount = mRightLayout.getChildCount();
        for (int i = 0; i < childCount; i++) {
            View view = mRightLayout.getChildAt(i);
            if (view != null) {
                final Object tag = view.getTag();
                if (tag instanceof Action && tag.equals(action)) {
                    mRightLayout.removeView(view);
                }
            }
        }
    }

    /**
     * Returns the number of actions currently registered with the action bar.
     * @return action count
     */
    public int getActionCount() {
        return mRightLayout.getChildCount();
    }

    /**
     * Inflates a {@link View} with the given {@link Action}.
     * @param action the action to inflate
     * @return a view
     */
    private View inflateAction(Action action) {
        View view = null;
        if (TextUtils.isEmpty(action.getText())) {
            ImageView img = new ImageView(getContext());
            img.setImageResource(action.getDrawable());
            view = img;
        } else {
            TextView text = new TextView(getContext());
            text.setGravity(Gravity.CENTER);
            text.setText(action.getText());
            text.setTextSize(TypedValue.COMPLEX_UNIT_PX, mActionTextSize);
            if (mActionTextColor != 0) {
                text.setTextColor(mActionTextColor);
            }
            view = text;
        }

        view.setPadding(mActionPadding, 0, mActionPadding, 0);
        view.setTag(action);
        view.setOnClickListener(this);
        return view;
    }

    public View getViewByAction(Action action) {
        View view = findViewWithTag(action);
        return view;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        int height;
        if (heightMode != MeasureSpec.EXACTLY) {
            height = mBarHeight + mStatusBarHeight;
            heightMeasureSpec = MeasureSpec.makeMeasureSpec(mBarHeight, MeasureSpec.EXACTLY);
        } else {
            height = MeasureSpec.getSize(heightMeasureSpec) + mStatusBarHeight;
        }

        measureChild(mLeftText, widthMeasureSpec, heightMeasureSpec);
        measureChild(mRightLayout, widthMeasureSpec, heightMeasureSpec);
        if (mLeftText.getMeasuredWidth() > mRightLayout.getMeasuredWidth()) {
            mCenterLayout.measure(
                    MeasureSpec.makeMeasureSpec(mScreenWidth - 2 * mLeftText.getMeasuredWidth(), MeasureSpec.EXACTLY)
                    , heightMeasureSpec);
        } else {
            mCenterLayout.measure(
                    MeasureSpec.makeMeasureSpec(mScreenWidth - 2 * mRightLayout.getMeasuredWidth(), MeasureSpec.EXACTLY)
                    , heightMeasureSpec);
        }
        measureChild(mDividerView, widthMeasureSpec, heightMeasureSpec);
        setMeasuredDimension(MeasureSpec.getSize(widthMeasureSpec), height);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        mLeftText.layout(0, mStatusBarHeight, mLeftText.getMeasuredWidth(), mLeftText.getMeasuredHeight() + mStatusBarHeight);
        mRightLayout.layout(mScreenWidth - mRightLayout.getMeasuredWidth(), mStatusBarHeight,
                mScreenWidth, mRightLayout.getMeasuredHeight() + mStatusBarHeight);
        if (mLeftText.getMeasuredWidth() > mRightLayout.getMeasuredWidth()) {
            mCenterLayout.layout(mLeftText.getMeasuredWidth(), mStatusBarHeight,
                    mScreenWidth - mLeftText.getMeasuredWidth(), getMeasuredHeight());
        } else {
            mCenterLayout.layout(mRightLayout.getMeasuredWidth(), mStatusBarHeight,
                    mScreenWidth - mRightLayout.getMeasuredWidth(), getMeasuredHeight());
        }
        mDividerView.layout(0, getMeasuredHeight() - mDividerView.getMeasuredHeight(), getMeasuredWidth(), getMeasuredHeight());
    }

    /**
     * 计算状态栏高度高度
     * getStatusBarHeight
     * @return
     */
    public static int getStatusBarHeight() {
        return getInternalDimensionSize(Resources.getSystem(), STATUS_BAR_HEIGHT_RES_NAME);
    }

    private static int getInternalDimensionSize(Resources res, String key) {
        int result = 0;
        int resourceId = res.getIdentifier(key, "dimen", "android");
        if (resourceId > 0) {
            result = res.getDimensionPixelSize(resourceId);
        }
        return result;
    }


    /**
     * A {@link LinkedList} that holds a list of {@link Action}s.
     */
    @SuppressWarnings("serial")
    public static class ActionList extends LinkedList<Action> {
    }

    /**
     * Definition of an action that could be performed, along with a icon to
     * show.
     */
    public interface Action {
        String getText();
        int getDrawable();
        void performAction(View view);
    }

    public static abstract class ImageAction implements Action {
        private int mDrawable;

        public ImageAction(int drawable) {
            mDrawable = drawable;
        }

        @Override
        public int getDrawable() {
            return mDrawable;
        }

        @Override
        public String getText() {
            return null;
        }
    }

    public static abstract class TextAction implements Action {
        final private String mText;

        public TextAction(String text) {
            mText = text;
        }

        @Override
        public int getDrawable() {
            return 0;
        }

        @Override
        public String getText() {
            return mText;
        }
    }

}
