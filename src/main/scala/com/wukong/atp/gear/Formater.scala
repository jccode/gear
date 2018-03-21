package com.wukong.atp.gear


import com.typesafe.config.ConfigFactory

/**
  * Formater
  *
  * @author 01372461
  */
object Formater {

  val config = ConfigFactory.load()
  val outFileName = config.getString("out")

  def write(r: Seq[GenResult]): Unit =
    Helper.write2File(outFileName, r.map(format).mkString(""))


  private def format(r: GenResult): String =
    s"""
       |
       | ${r.table}
       | -----------
       |
       | ${r.columns}
       | ${r.resultMap}
       |
       | ===============================
      """.stripMargin

}
