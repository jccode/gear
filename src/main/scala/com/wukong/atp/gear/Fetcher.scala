package com.wukong.atp.gear

import com.typesafe.scalalogging.LazyLogging
import slick.dbio.DBIO
import slick.jdbc.JdbcProfile
import slick.jdbc.meta.{MColumn, MQName}

import scala.concurrent.Await
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.duration._

/**
  * Generator
  *
  * @author 01372461
  */
class Fetcher(val profile: String, atables: Seq[ATable]) extends LazyLogging {

  val profileInstance: JdbcProfile =
    Class.forName(profile + "$").getField("MODULE$").get(null).asInstanceOf[JdbcProfile]

  val dbFactory: profileInstance.backend.DatabaseFactoryDef = profileInstance.api.Database

  val db: profileInstance.backend.DatabaseDef = dbFactory.forConfig("db")

  val alias: Map[String, Option[String]] = atables.map(x => x.name -> x.alias).toMap


  def fetch(): Unit = {
    rawCreateMode()
  }

  private def rawCreateMode(): Unit = {
    /*
    val action = profileInstance.defaultTables.flatMap {list => {
      val list2 = if (atables.nonEmpty) list.filter(x=>atables.map(_.name).contains(x.name.name)) else list
      DBIO.sequence(list2.map(_.getColumns))
    }}*/
    val action = DBIO.sequence(atables.map(x => MColumn.getColumns(MQName.local(x.name), "%")))
    val future = db.run(action).map {r: Seq[Vector[MColumn]] => {
      val results = r.map { columns =>
        val c = columns(0)
        Generator.gen(columns, alias(c.table.name))
      }
      Formater.write(results)
      logger.info(">>>>  Finish. <<<< ")
    }}
    Await.result(future, Duration.Inf)
  }


  private def slickModelMode(): Unit = {
    val modelAction = profileInstance.createModel(Some(profileInstance.defaultTables))
    val future = db.run(modelAction) map { model =>
      val tables = if (atables.isEmpty) model.tables else model.tables.filter(t => atables.map(_.name).contains(t.name.table))
       tables.foreach(println(_))
    }
    Await.result(future, 10 seconds)
  }

}
