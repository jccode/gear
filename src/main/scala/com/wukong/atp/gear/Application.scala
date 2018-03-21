package com.wukong.atp.gear

import com.typesafe.config.ConfigFactory

/**
  * Application
  *
  * @author 01372461
  */
object Application extends App {

  val config = ConfigFactory.load()
  val profile = config.getString("db.profile")
  val tables = config.getString("tables")
  val atables = tables.split(",|;").map { s =>
    val t = s.trim.split(":")
    ATable(t(0), if (t.size > 1 && t(1).trim != "") Some(t(1)) else None)
  }

  val fetcher = new Fetcher(profile, if (tables.trim == "") Seq.empty else atables)
  fetcher.fetch()

}
