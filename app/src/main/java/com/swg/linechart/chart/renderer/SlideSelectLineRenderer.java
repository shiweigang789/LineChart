package com.swg.linechart.chart.renderer;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.DashPathEffect;
import android.graphics.Paint;
import android.graphics.Path;
import android.view.View;

import com.swg.linechart.chart.LeafUtil;
import com.swg.linechart.chart.bean.Axis;
import com.swg.linechart.chart.bean.AxisValue;
import com.swg.linechart.chart.bean.SlidingLine;

import java.util.List;


/**
 * Created by chenliu on 2017/1/9.<br/>
 * 描述：
 * </br>
 */

public class SlideSelectLineRenderer extends LeafLineRenderer {
    /**
     * 移动标尺线
     **/
    private Paint slidePaint;

    public SlideSelectLineRenderer(Context context, View view) {
        super(context, view);
    }

    @Override
    protected void initPaint() {
        super.initPaint();
        slidePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    }

    /**
     * 竖直滑动标尺线
     *
     * @param canvas
     */
    public void drawSlideLine(Canvas canvas, Axis axisX, Axis axisY, SlidingLine slidingLine, float moveX, float moveY, float yStep) {
        slidePaint.setStrokeWidth(LeafUtil.dp2px(mContext, 1));
        slidePaint.setColor(slidingLine.getSlideLineColor());
        if (slidingLine.isDash()) {
            float dash = LeafUtil.dp2px(mContext, 2);
            slidePaint.setPathEffect(new DashPathEffect(new float[]{dash, dash, dash, dash}, 0));
        }

        Path pathX = new Path();
        pathX.moveTo(moveX, axisY.getStopY() + yStep);
        pathX.lineTo(moveX, axisY.getStartY());
        canvas.drawPath(pathX, slidePaint);

        Path pathY = new Path();
        pathY.moveTo(axisX.getStartX(), moveY);
        pathY.lineTo(axisX.getStopX(), moveY);
        canvas.drawPath(pathY, slidePaint);

        slidePaint.setPathEffect(null);
        slidePaint.setStyle(Paint.Style.FILL);
        slidePaint.setColor(slidingLine.getSlidePointColor());
        float slidePointRadius = slidingLine.getSlidePointRadius();
        canvas.drawCircle(moveX, moveY, LeafUtil.dp2px(mContext, slidePointRadius), slidePaint);
        slidePaint.setStyle(Paint.Style.STROKE);
        slidePaint.setStrokeWidth(LeafUtil.dp2px(mContext, 2));
        slidePaint.setColor(Color.WHITE);
        canvas.drawCircle(moveX, moveY, LeafUtil.dp2px(mContext, slidePointRadius), slidePaint);
        if (slidingLine.getSlidePointColor() != 0) {
            slidePaint.setAlpha(100);
            canvas.drawCircle(moveX, moveY, LeafUtil.dp2px(mContext, slidePointRadius), slidePaint);
        }
    }

}
