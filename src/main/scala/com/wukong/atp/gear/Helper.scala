package com.wukong.atp.gear

import java.io.FileWriter

/**
  * Helper
  *
  * @author 01372461
  */
object Helper {

  //  https://gist.github.com/sidharthkuruvila/3154845
  def camel2Underscore(text: String) = text.drop(1).foldLeft(text.headOption.map(_.toLower + "") getOrElse "") {
    case (acc, c) if c.isUpper => acc + "_" + c.toLower
    case (acc, c) => acc + c
  }

  def underscore2Camel(name: String) = "_([a-z\\d])".r.replaceAllIn(name, {m =>
    m.group(1).toUpperCase()
  })


  /**
    * Used for reading/writing to database, files, etc.
    * Code From the book "Beginning Scala"
    * http://www.amazon.com/Beginning-Scala-David-Pollak/dp/1430219890
    */
  def using[A <: {def close(): Unit}, B](param: A)(f: A => B): B =
    try { f(param) } finally { param.close() }

  def write2File(fileName: String, content: String): Unit = {
    using(new FileWriter(fileName)) { f => f.write(content) }
  }
}
