package app

import java.util.Date
import scala.util.Try

/**
 * Created by schepkin on 30.01.2022.
 */

trait IConvertTypes extends IConvertStringToDouble with IConvertStringToDate with IDivideTuple

trait IDivideTuple {
  implicit class DivideTuple2(tuple2: (Double, Int)) {
    def divideLeftOpt = Try(tuple2._1 / tuple2._2).toOption
    def divideRightOpt = Try(tuple2._2 / tuple2._1).toOption
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
  implicit class StringToDate(date: String) {
    private val monthsMap = Map(
      "янв" -> 1, "фев" -> 2, "мар" -> 3, "апр" -> 4,
      "май" -> 5, "июн" -> 6, "июл" -> 7, "авг" -> 8,
      "сен" -> 9, "окт" -> 10, "ноя" -> 11, "дек" -> 12
    )

    def toDate: Date = {
      val Array(day, month, year) = date.split('.') map {
        case month if month.length == 3 => monthsMap(month)
        case value => value.toInt
      } // 100 + year => 1921 => 2021
      new Date(100 + year, month, day)
    }
  }
}
