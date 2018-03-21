package com.wukong.atp.gear

case class ATable(val name: String, val alias: Option[String])

case class GenResult(val table: String, val columns: String, val resultMap: String)
