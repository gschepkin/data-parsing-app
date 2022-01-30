package app

import akka.actor.{Actor, ActorLogging, ActorRef, Props, Stash}
import akka.pattern.ask
import akka.stream.scaladsl.Flow
import akka.util.Timeout

import java.io.File
import java.util.Date
import scala.collection.mutable.ListBuffer
import scala.concurrent.{ExecutionContextExecutor, Future}
import scala.util.{Failure, Success}

/**
 * Created by schepkin on 30.01.2022.
 */

object Storage {
  def props(implicit timeout: Timeout) = Props(new Storage)

  def name = "storage"

  case class ProcessFile(file: File)

  case object ChangeState

  case object GetCells
  case class GetByDate(date: Date)
  case class GetByDates(start: Date, end: Date)
  case class GetMaxMinByDates(start: Date, end: Date)
}

class Storage extends Actor with ActorLogging with Stash with ProcessCsv with IConvertTypes {

  import Storage._

  implicit val system = context.system
  implicit val executionContext = system.dispatcher

  override def receive: Receive = receiveProcessing

  private val cached = ListBuffer[Cell]()

  def cache(data: Option[Cell] = None) = this.synchronized {
    data foreach { data => cached += data }
    cached
  }

  def process(data: Map[String, String]) = Cell(
    data("Начало периода мониторинга цен на нефть").toDate,
    data("Конец периода мониторинга цен на нефть").toDate,
    data("Средняя цена на нефть сырую марки «Юралс» на мировых рынках нефтяного сырья (средиземноморском и роттердамском)")
      .getDouble
  )

  def conditionByDates(targetStart: Date, targetEnd: Date, start: Date, end: Date) =
    targetStart.getTime <= start.getTime && start.getTime <= targetEnd.getTime ||
      targetStart.getTime <= end.getTime && end.getTime <= targetEnd.getTime

  def receiveProcessing: Receive = {
    case ChangeState => unstashAll(); context.become(receiveProcessed)
    case ProcessFile(file) =>
      processFile(file)
        .via(Flow[Map[String, String]] map process)
        .via(Flow[Cell] map { Some(_) }) runForeach { cache(_) } onComplete {
        case Success(_) => log.info(s"Successfully processed the file ${file.getName}")
          self ! ChangeState
        case Failure(ex) => log.error(s"Failure processed the file ${file.getName}: $ex")
      }
    case _ => stash()
  }

  def receiveProcessed: Receive = {
    case GetCells => log.info("GetCells")
      sender() ! Cells(cache().toList)

    case GetByDate(date) => log.info(s"GetByDate($date)")
      cache().collectFirst {
        case Cell(start, end, price) if start.getTime <= date.getTime && date.getTime <= end.getTime => price
      } match {
        case Some(price) => sender() ! Price(date, price)
        case None => sender() ! BadAction("Sorry, storage doesn't have the price of the date")
      }

    case GetByDates(targetStart, targetEnd) => log.info(s"GetByDates($targetStart, $targetEnd)")
      cache().foldRight(0.0d, 0) {
        case (Cell(start, end, price), (sum, count)) if conditionByDates(targetStart, targetEnd, start, end)=>
          (sum + price, count + 1)
        case (_, acc) => acc
      }.divideLeftOpt match {
        case Some(price) => sender() ! AveragePrice(targetStart, targetEnd, price)
        case None => sender() ! BadAction("Sorry, storage doesn't have the average price of the dates")
      }

    case GetMaxMinByDates(targetStart, targetEnd) => log.info(s"GetMaxMinByDates($targetStart, $targetEnd)")
      cache().foldRight(Double.MaxValue, Double.MinValue) {
        case (Cell(start, end, price), (min, max)) if conditionByDates(targetStart, targetEnd, start, end) && (price < min || (price > max)) =>
          (math.min(min, price), math.max(max, price))
        case (_, acc) => acc
      } match {
        case (min, max) if min != Double.MaxValue && max != Double.MinValue =>
          sender() ! MaxMinPrices(targetStart, targetEnd, min, max)
        case _ =>
          sender() ! BadAction("Sorry, storage doesn't have min and max prices of the dates")
      }

    case _ => log.error("Bad message")
  }
}

trait StorageApi {

  import Storage._

  def createStorage: ActorRef

  implicit def executionContext: ExecutionContextExecutor

  implicit def requestTimeout: Timeout

  lazy val storage = createStorage

  def startProcess(file: File) = Future[Unit] {
    storage ! ProcessFile(file)
  }

  def getCells() =
    storage.ask(GetCells).mapTo[StorageResponse]

  def getByDate(date: Date) =
    storage.ask(GetByDate(date)).mapTo[StorageResponse]

  def getByDates(start: Date, end: Date) =
    storage.ask(GetByDates(start, end)).mapTo[StorageResponse]

  def getMinMaxByDates(start: Date, end: Date) =
    storage.ask(GetMaxMinByDates(start, end)).mapTo[StorageResponse]
}