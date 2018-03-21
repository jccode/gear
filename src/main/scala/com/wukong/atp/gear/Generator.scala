package com.wukong.atp.gear

import slick.jdbc.meta.MColumn

/**
  * Generator
  *
  * @author 01372461
  */
class Generator(val columns: Vector[MColumn], val alias: Option[String]) {

  val table: String = columns(0).table.name
  val CRLF = "\r\n"

  private def genColumns(): String = {
    val cs = columns.map(c => alias.map(a => s"$a.${c.name} as ${a}_${c.name}").getOrElse(c.name)).mkString("", s",$CRLF    ", "")
    val s =
      s"""
        |<sql id="AliasColumn" >
        |    ${cs}
        |</sql>
      """.stripMargin
    s
  }

  private def genResultMap(): String ={
    val cs = columns.map(c => {
      val xmlTag = if (c.name.toLowerCase == "id") "id" else "result"
      val cName = alias.map(a => s"${a}_${c.name}").getOrElse(c.name)
      val propName = Helper.underscore2Camel(c.name)
      s"""<$xmlTag column="$cName" jdbcType="${c.sqlTypeName.getOrElse("")}" property="$propName" />"""
    })
    val s =
      s"""
         |<resultMap id="AliasResultMap" type="TODO">
         |    ${cs.mkString("", s"$CRLF    ", "")}
         |</resultMap>
        """.stripMargin
    s
  }
}

object Generator {

  def apply(columns: Vector[MColumn], alias: Option[String]): Generator = new Generator(columns, alias)

  def gen(columns: Vector[MColumn], alias: Option[String]): GenResult = {
    val gen = Generator(columns, alias)
    GenResult(gen.table, gen.genColumns(), gen.genResultMap())
  }
}

