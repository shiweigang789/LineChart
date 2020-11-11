package com.swg.linechart.chart;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.view.View;

import com.swg.linechart.R;
import com.swg.linechart.chart.bean.Axis;
import com.swg.linechart.chart.bean.AxisValue;
import com.swg.linechart.chart.bean.ChartData;
import com.swg.linechart.chart.bean.PointValue;
import com.swg.linechart.chart.renderer.AbsRenderer;

import java.util.List;

/**
 * Created by chenliu on 2016/7/15.<br/>
 * 描述：
 * </br>
 */
public abstract class AbsLeafChart extends View implements Chart {

    static final String TAG = SlideSelectLineChart.class.getName();

    /**
     * 坐标轴相交模式
     */
    protected int coordinateMode;

    /**
     * 第一个点x轴方向起始位置
     */
    protected int startMarginX = 0;
    /**
     * 第一个点y轴方向起始位置
     */
    protected int startMarginY = 0;

    protected Axis axisX;
    protected Axis axisY;
    protected float mYStep;

    protected float mWidth;//控件宽度
    protected float mHeight;//控件高度
    protected float leftPadding, topPadding, rightPadding, bottomPadding;//控件内部间隔

    protected Context mContext;

    private AbsRenderer absRenderer;

    public AbsLeafChart(Context context) {
        this(context, null, 0);
    }

    public AbsLeafChart(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public AbsLeafChart(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mContext = context;

        initRenderer();

        setRenderer();

        initAttrs(attrs);
    }

    protected void initAttrs(AttributeSet attrs) {
        TypedArray ta = mContext.obtainStyledAttributes(attrs, R.styleable.AbsLeafChart);
        try {
            leftPadding = ta.getDimension(R.styleable.AbsLeafChart_lc_leftPadding, LeafUtil.dp2px(mContext, 20));
            topPadding = ta.getDimension(R.styleable.AbsLeafChart_lc_topPadding, LeafUtil.dp2px(mContext, 10));
            rightPadding = ta.getDimension(R.styleable.AbsLeafChart_lc_rightPadding, LeafUtil.dp2px(mContext, 10));
            bottomPadding = ta.getDimension(R.styleable.AbsLeafChart_lc_bottomPadding, LeafUtil.dp2px(mContext, 20));
            startMarginX = (int) ta.getDimension(R.styleable.AbsLeafChart_lc_startMarginX, 0);
            startMarginY = (int) ta.getDimension(R.styleable.AbsLeafChart_lc_startMarginY, 0);
            coordinateMode = ta.getInteger(R.styleable.AbsLeafChart_lc_coordinateMode, Mode.INTERSECT);
        } finally {
            ta.recycle();
        }
    }

    public void setRenderer(AbsRenderer renderer) {
        this.absRenderer = renderer;
    }

    ///////////////////子类必须重写的方法////////////////////////////
    protected abstract void initRenderer();

    protected abstract void setRenderer();

    protected abstract void resetPointWeight();

    /**
     * 不重写改方法，在布局中使用 wrap_content 显示效果是 match_parent
     *
     * @param widthMeasureSpec
     * @param heightMeasureSpec
     */
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int widthSpecMode = MeasureSpec.getMode(widthMeasureSpec);
        int widthSpecSize = MeasureSpec.getSize(widthMeasureSpec);
        int heightSpecMode = MeasureSpec.getMode(heightMeasureSpec);
        int heightSpecSize = MeasureSpec.getSize(heightMeasureSpec);

        if (widthSpecMode == MeasureSpec.AT_MOST && heightSpecMode == MeasureSpec.AT_MOST) {
            setMeasuredDimension((int) LeafUtil.dp2px(mContext, 300), (int) LeafUtil.dp2px(mContext, 300));
        } else if (widthSpecMode == MeasureSpec.AT_MOST) {
            setMeasuredDimension((int) mWidth, heightSpecSize);
        } else if (heightSpecMode == MeasureSpec.AT_MOST) {
            setMeasuredDimension(widthSpecSize, (int) mHeight);
        }

    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);

        initViewSize();

        resetAsixSize();

        resetPointWeight();

    }

    protected void initViewSize() {
        mWidth = getMeasuredWidth();
        mHeight = getMeasuredHeight() - bottomPadding;
        absRenderer.setWH(mWidth, mHeight);
        absRenderer.setPadding(leftPadding, topPadding, rightPadding, bottomPadding);
    }

    /**
     * 设置坐标轴位置
     */
    public void resetAsixSize() {

        resetAsixX();

        resetAsixY();

    }

    protected void resetAsixY() {
        if (axisY != null) {
            List<AxisValue> values = axisY.getValues();
            int sizeY = values.size(); //几条x轴
            float yStep = (mHeight - topPadding - bottomPadding - startMarginY) / sizeY;
            mYStep = yStep;
            for (int i = 0; i < sizeY; i++) {
                AxisValue axisValue = values.get(i);
                axisValue.setPointX(leftPadding);
                if (i == 0) {
                    axisValue.setPointY(mHeight - bottomPadding - startMarginY);
                } else {
                    axisValue.setPointY(mHeight - bottomPadding - startMarginY - yStep * i);
                }
            }
            switch (coordinateMode) {
                case Mode.ACROSS:
                case Mode.Y_ACROSS:
                    axisY.setStartY(mHeight - bottomPadding * 0.5f);
                    break;

                case Mode.INTERSECT:
                case Mode.X_ACROSS:
                    axisY.setStartY(mHeight - bottomPadding);
                    break;
            }
            axisY.setStartX(leftPadding).setStopX(leftPadding).setStopY(topPadding);

        }
    }

    protected void resetAsixX() {
        if (axisX != null) {
            List<AxisValue> values = axisX.getValues();
            int sizeX = values.size(); //几条y轴
            float xStep = (mWidth - leftPadding - startMarginX) / sizeX;
            for (int i = 0; i < sizeX; i++) {
                AxisValue axisValue = values.get(i);
                axisValue.setPointY(mHeight);
                if (i == 0) {
                    axisValue.setPointX(leftPadding + startMarginX);
                } else {
                    axisValue.setPointX(leftPadding + startMarginX + xStep * i);
                }
            }


            switch (coordinateMode) {
                case Mode.ACROSS:
                case Mode.X_ACROSS:
                    axisX.setStartX(leftPadding * 0.5f);
                    break;

                case Mode.INTERSECT:
                case Mode.Y_ACROSS:
                    axisX.setStartX(leftPadding);
                    break;
            }
            axisX.setStartY(mHeight - bottomPadding - startMarginY).setStopX(mWidth).setStopY(mHeight - bottomPadding - startMarginY);
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        absRenderer.drawCoordinateLines(canvas, axisX, axisY);
        absRenderer.drawCoordinateText(canvas, axisX, axisY);
    }

    protected void resetPointWeight(ChartData chartData) {
        if (chartData != null && axisX != null && axisY != null) {
            List<PointValue> values = chartData.getValues();
            int size = values.size();

            List<AxisValue> axisValuesX = axisX.getValues();
            List<AxisValue> axisValuesY = axisY.getValues();
            float totalWidth = Math.abs(axisValuesX.get(0).getPointX() - axisValuesX.get(axisValuesX.size() - 1).getPointX());

            float totalHeight = Math.abs(axisValuesY.get(0).getPointY() - axisValuesY.get(axisValuesY.size() - 1).getPointY());
            for (int i = 0; i < size; i++) {
                PointValue pointValue = values.get(i);
                float diffX = pointValue.getX() * totalWidth;
                pointValue.setDiffX(diffX);

                float diffY = pointValue.getY() * totalHeight;
                pointValue.setDiffY(diffY);

                float originX1 = diffX + leftPadding + startMarginX;
                float originY1 = mHeight - bottomPadding - diffY - startMarginY;
                pointValue.setOriginX(originX1).setOriginY(originY1);
            }
        }
    }

    @Override
    public void setAxisX(Axis axisX) {
        this.axisX = axisX;
        resetAsixX();
        invalidate();
    }

    @Override
    public void setAxisY(Axis axisY) {
        this.axisY = axisY;
        resetAsixY();
        invalidate();
    }

    @Override
    public Axis getAxisX() {
        return axisX;
    }

    @Override
    public Axis getAxisY() {
        return axisY;
    }
}
