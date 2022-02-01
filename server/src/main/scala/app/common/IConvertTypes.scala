package app.common

import java.util.Date
import scala.util.Try

/**
 * Created by schepkin on 30.01.2022.
 */

trait IConvertTypes extends IConvertStringToDouble with IConvertStringToDate with IDivideTuple

trait IDivideTuple {
  implicit class DivideTuple2(tuple2: (Double, Int)) {
    private def condition(res: => Option[Double]) = if(tuple2._1 != 0 && tuple2._2 != 0) res else None

    def divideLeftOpt =  condition(Try(tuple2._1 / tuple2._2).toOption)
    def divideRightOpt = condition(Try(tuple2._2 / tuple2._1).toOption)
  }
}

trait IConvertStringToDouble {
  implicit class StringToDouble(num: String) {
    def getDouble = num.map {
      case ',' => '.'
      case c => c
    }.toDouble
  }
}

trait IConvertStringToDate {
  implicit class StringToDate(dateStr: String) {
    private val monthsMap = Map(
      "янв" -> "01", "фев" -> "02", "мар" -> "03", "апр" -> "04",
      "май" -> "05", "июн" -> "06", "июл" -> "07", "авг" -> "08",
      "сен" -> "09", "окт" -> "10", "ноя" -> "11", "дек" -> "12"
    )

    def toDateRU: Date = {
      val format = new java.text.SimpleDateFormat("dd-MM-yyyy")
      format.parse(dateStr.split('.').foldLeft(0, "") {
        case ((count, date), day) if count == 0 => (count + 1, date + "-" + day)
        case ((count, date), month) if count == 1 => (count + 1, date + "-" + monthsMap(month))
        case ((count, date), year) if count == 2 => (count + 1, date + "-20" + year)
      }._2)
    }

    def toDate: Date = {
      val format = new java.text.SimpleDateFormat("yyyy-MM-dd")
      format.parse(dateStr)
    }
  }
}
