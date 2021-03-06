name := "gear"

organization := "com.wukong.atp"

version := "0.1"

scalaVersion := "2.12.5"


val slickVersion = "3.2.1"

libraryDependencies ++= Seq(
  "org.scalatest" %% "scalatest" % "3.0.1" % "test",
  "ch.qos.logback" % "logback-classic" % "1.2.3",
  "com.typesafe.scala-logging" %% "scala-logging" % "3.7.1",
  "com.typesafe" % "config" % "1.3.1",
  "com.typesafe.slick" %% "slick" % slickVersion,
  "com.typesafe.slick" %% "slick-hikaricp" % slickVersion,
  "com.typesafe.slick" %% "slick-codegen" % slickVersion,
  "mysql" % "mysql-connector-java" % "5.1.36",
)

mappings in (Compile, packageBin) ~= { _.filter(!_._1.getName.matches(".*\\.(conf|xml)$")) }

mainClass in Compile := Some("com.wukong.atp.gear.Application")

enablePlugins(JavaAppPackaging)

mappings in Universal ++= {
  val resDir = (resourceDirectory in Compile).value
  List(
    resDir / "application.conf" -> "conf/application.conf",
    resDir / "logback.xml" -> "conf/logback.xml"
  )
}

bashScriptExtraDefines +=
  """
    |addJava "-Dconfig.file=${app_home}/../conf/application.conf"
    |addJava "-Dlogback.configurationFile=${app_home}/../conf/logback.xml"
  """.stripMargin

batScriptExtraDefines +=
  """
    |call :add_java "-Dconfig.file=%APP_HOME%\conf\application.conf"
    |call :add_java "-Dlogback.configurationFile=%APP_HOME%\conf\logback.xml"
  """.stripMargin
