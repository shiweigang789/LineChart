package com.swg.linechart

import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.widget.SeekBar
import androidx.appcompat.app.AppCompatActivity
import com.swg.linechart.chart.bean.*
import kotlinx.android.synthetic.main.activity_main.*
import java.util.*

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        initLineChart()

        seek_bar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(p0: SeekBar?, p1: Int, p2: Boolean) {
                Log.d("SeekBar", p1.toString())
//                hpb.setProgress(p1.toDouble())
                hpb1.setCurrentProgress(p1.toFloat())
            }

            override fun onStartTrackingTouch(p0: SeekBar?) {

            }

            override fun onStopTrackingTouch(p0: SeekBar?) {

            }
        })

    }


    private fun initLineChart() {
        val axisX = Axis(getAxisValuesX())
        axisX.setAxisColor(Color.parseColor("#33B5E5"))
            .setTextColor(Color.DKGRAY)
            .setShowAxisX(true)
            .setHasLines(true).isShowText = true
        val axisY = Axis(getAxisValuesY())
        axisY.setAxisColor(Color.parseColor("#33B5E5"))
            .setTextColor(Color.DKGRAY)
            .setShowAxisY(false)
            .setHasLines(false)
            .isShowText = true
        move_select_chart.axisX = axisX
        move_select_chart.axisY = axisY
        move_select_chart.setSlideLine(getSlideingLine())
        move_select_chart.chartData = getFoldLine()
        move_select_chart.show()
    }


    var days = 90

    private fun getAxisValuesX(): List<AxisValue>? {
        val axisValues: MutableList<AxisValue> = ArrayList<AxisValue>()
        for (i in 1..days) {
            val value = AxisValue()
            value.setLabel(i.toString() + "日")
            axisValues.add(value)
        }
        return axisValues
    }

    private fun getAxisValuesY(): List<AxisValue>? {
        val axisValues: MutableList<AxisValue> = ArrayList<AxisValue>()
        for (i in 0..4) {
            val value = AxisValue()
            value.setLabel((i * 1000).toString())
            axisValues.add(value)
        }
        return axisValues
    }

    private fun getFoldLine(): Line? {
        val pointValues: MutableList<PointValue> =
            ArrayList<PointValue>()
        for (i in 1..days) {
            val pointValue = PointValue()
            pointValue.setX((i - 1) / (days - 1f))
            val `var` = 5 + i + (Math.random() * 10).toInt()
            pointValue.setLabel(`var`.toString())
            pointValue.setY(`var` / 100f)
            pointValues.add(pointValue)
        }
        val line = Line(pointValues)
        line.setLineColor(Color.parseColor("#33B5E5"))
            .setLineWidth(1.5f)
            .setPointColor(Color.parseColor("#33B5E5"))
            .setCubic(true)
            .setPointRadius(2)
            .setFill(true)
            .setHasPoints(false)
            .setFillColor(Color.parseColor("#33B5E5"))
            .setLabelColor(Color.parseColor("#33B5E5")).isHasLabels = true
        return line
    }

    private fun getSlideingLine(): SlidingLine? {
        val slidingLine = SlidingLine()
        slidingLine.setSlideLineColor(resources.getColor(R.color.colorAccent))
            .setSlidePointColor(resources.getColor(R.color.colorAccent)).slidePointRadius = 4f
        return slidingLine
    }

}