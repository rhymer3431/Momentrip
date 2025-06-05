package com.mp.momentrip.util.schedule

import com.mp.momentrip.data.schedule.Activity
import com.mp.momentrip.data.schedule.Day

object ScheduleUtil{
    fun isAvailable(day: Day, newActivity: Activity) : Boolean{

        val newStart = newActivity.startTime
        val newEnd = newActivity.endTime

        // 시작/종료 시간이 없는 경우는 무조건 추가 불가
        if (newStart == null || newEnd == null || newStart >= newEnd) return false

        for (existing in day.timeTable) {
            val existingStart = existing.startTime
            val existingEnd = existing.endTime

            if (existingStart == null || existingEnd == null) continue

            // 겹치는지 검사: [start, end) 기준
            if (newStart < existingEnd && newEnd > existingStart) {
                return false
            }
        }
        return true
    }
}