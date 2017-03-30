package com.hcloud.apm.analysis.utils

import java.text.SimpleDateFormat
import java.util.Date

/**
  * Created by 陈志民 on 2016/11/29.
  */
object CurrentTimeUtil {
  def getNowDate(): String = {
    val now: Date = new Date()
    val dateFormat: SimpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
    val currentTime = dateFormat.format(now)
    currentTime
  }
}
