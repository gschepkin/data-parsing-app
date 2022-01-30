package app

import akka.stream.alpakka.csv.scaladsl.{CsvParsing, CsvToMap}
import akka.stream.scaladsl.FileIO

import java.io.File
import java.nio.file.Paths

/**
 * Created by schepkin on 30.01.2022.
 */

trait ProcessCsv {
  protected def processFile(file: File) = {
    val csvFile = Paths.get(file.getPath)
    val source = FileIO.fromPath(csvFile)
    source
      .via(CsvParsing.lineScanner(CsvParsing.SemiColon))
      .via(CsvToMap.toMapAsStrings())
  }
}
