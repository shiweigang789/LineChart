package com.swg.linechart.progress;

import android.animation.ValueAnimator;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.animation.DecelerateInterpolator;
import android.widget.FrameLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.swg.linechart.R;

/**
 * @ProjectName: LineChart
 * @ClassName: HorizontalProgress
 * @Author: Owen
 * @CreateDate: 2020/11/10 19:45
 * @UpdateUser: 更新者
 * @Description: java类作用描述
 */
public class HorizontalProgress extends FrameLayout {

    private Drawable mProgressDrawable;
    private String mPrefixText;
    private String mDefaultText;
    private boolean mShowAnim;
    private ProgressBar mProgressBar;
    private TextView mTextView;
    private int mTextColor;
    private float mTextSize;

    public HorizontalProgress(@NonNull Context context) {
        this(context, null);
    }

    public HorizontalProgress(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public HorizontalProgress(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    /**
     * 初始化信息
     *
     * @param context
     */
    private void init(Context context, AttributeSet attrs) {
        TypedArray attributes = getContext().obtainStyledAttributes(attrs, R.styleable.HorizontalProgressBar);
        mProgressDrawable = attributes.getDrawable(R.styleable.HorizontalProgressBar_progress_drawable);
        if (mProgressDrawable == null) {
            mProgressDrawable = context.getResources().getDrawable(R.drawable.progress_bar_bg);
        }
        mPrefixText = attributes.getString(R.styleable.HorizontalProgressBar_prefix_text);
        mDefaultText = attributes.getString(R.styleable.HorizontalProgressBar_default_text);
        mShowAnim = attributes.getBoolean(R.styleable.HorizontalProgressBar_progress_anim, true);
        mTextColor = attributes.getColor(R.styleable.HorizontalProgressBar_text_color, Color.WHITE);
        mTextSize = attributes.getDimensionPixelSize(R.styleable.HorizontalProgressBar_text_size, sp2px(12));
        attributes.recycle();
        initView(context);
    }

    /**
     * 初始化View
     *
     * @param context
     */
    private void initView(Context context) {
        inflate(context, R.layout.layout_horizontal_progress, this);
        mProgressBar = findViewById(R.id.progress_bar);
    }

    /**
     * 设置进度
     *
     * @param progress 进度条 0 - 1
     */
    public void setProgress(double progress) {
        setProgress(progress, "");
    }

    /**
     * 设置进度
     *
     * @param progress 进度条 0 - 1
     * @param content  显示的内容
     */
    @SuppressLint("SetTextI18n")
    public void setProgress(double progress, String content) {
        if (mShowAnim) {
//            setProgress((int) (progress * 100));
            setProgress((int) (progress));
        } else {
//            mProgressBar.setProgress((int) (progress * 100));
            mProgressBar.setProgress((int) (progress));
        }
        setText(content);
    }

    /**
     * 设置内容
     *
     * @param content
     */
    private void setText(String content) {
        if (!TextUtils.isEmpty(content)) {
            mTextView.setText(mPrefixText + content);
        }
    }

    /**
     * 动画展示
     *
     * @param progress
     */
    private void setProgress(int progress) {
        ValueAnimator animator = ValueAnimator.ofInt(0, progress);
        //设置动画时长
        animator.setDuration(500);
        //设置插值器，当然你可以不用
        animator.setInterpolator(new DecelerateInterpolator());
        //回调监听
        animator.addUpdateListener(animation -> {
            int value = (int) animation.getAnimatedValue();
            mProgressBar.setProgress(value);
        });
        //启动动画
        animator.start();
    }

    /**
     * dp 2 px
     *
     * @param dpVal
     */
    protected int dp2px(int dpVal) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,
                dpVal, getResources().getDisplayMetrics());
    }

    /**
     * sp 2 px
     *
     * @param spVal
     * @return
     */
    protected int sp2px(int spVal) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP,
                spVal, getResources().getDisplayMetrics());

    }

}
