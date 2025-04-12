package com.livescore.soccerscore.matchlive.dialog

import android.annotation.SuppressLint
import android.content.Context
import android.util.Log
import android.view.View
import com.livescore.soccerscore.matchlive.base.BaseDialog
import com.livescore.soccerscore.matchlive.databinding.DialogNoteDateTimeBinding
import com.livescore.soccerscore.matchlive.util.calendar_collapse.widget.CollapsibleCalendar
import java.text.SimpleDateFormat

class NoteDateTimeDialog(context: Context, private var onSave: (Int, Int, Int, Int, Int) -> Unit) :
    BaseDialog<DialogNoteDateTimeBinding>(context, false) {

    override fun setBinding(): DialogNoteDateTimeBinding {
        return DialogNoteDateTimeBinding.inflate(layoutInflater)
    }

    var hour = 11
    var minute = 0

    @SuppressLint("SimpleDateFormat", "SetTextI18n")
    override fun initView() {


    }

    @SuppressLint("SetTextI18n")
    override fun bindView() {
        var day = 17
        var month = 2
        var year = 2025

        binding.viewCalendar.setCalendarListener(object : CollapsibleCalendar.CalendarListener {

            override fun onDaySelect() {
                val date =
                    SimpleDateFormat("dd/MM/yyyy").format(binding.viewCalendar.selectedDay!!.toUnixTime())
                year = date.split("/")[2].toInt()
                month = date.split("/")[1].toInt()
                day = date.split("/")[0].toInt()
                Log.e("check_time", "onDaySelect: $date")
            }

            override fun onItemClick(v: View) {}

            override fun onDataUpdate() {}

            override fun onMonthChange() {}

            override fun onWeekChange(position: Int) {}

            override fun onClickListener() {}

            override fun onDayChanged() {}
        })

        binding.tvCancel.setOnClickListener {
            dismiss()
        }
        binding.tvSave.setOnClickListener {
            dismiss()
            onSave.invoke(day, month, year, hour, minute)
        }
    }

}