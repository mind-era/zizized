// Turn this project into a Scala.js project by importing these settings
//enablePlugins(ScalaJSPlugin)

organization := "com.mind_era"

organizationName := "Mind Eratosthenes Kft."

organizationHomepage := Some(url("http://mind-era.com"))

homepage := Some(url("http://github.com/mind-era/zizized"))

startYear := Some(2015)

description := "A z3 inspired theorem prover for Scala."

licenses += "AGPLv3" -> url("http://www.gnu.org/licenses/agpl-3.0.en.html")

name := "zizized"

version := "0.0.1"

scalaVersion := "2.11.7"

scalacOptions += "-target:jvm-1.8"

wartremoverWarnings ++= Warts.all

wartremoverWarnings --= Seq(Warts.Var, Warts.DefaultArguments)

//persistLauncher in Compile := true

//persistLauncher in Test := false

//Macros

libraryDependencies <+= (scalaVersion)("org.scala-lang" % "scala-reflect" % _)

//End macros

//OSGi

//sbtOsgi 0.8 enablePlugins(SbtOsgi)

libraryDependencies += "org.osgi" % "org.osgi.core" % "4.3.0" % "provided"

osgiSettings

//sbtOsgi 0.8 OsgiKeys.bundleRequiredExecutionEnvironment := Some("JavaSE-1.8")

OsgiKeys.exportPackage := Seq("com.mind_era.zizized")

//End OSGi

//Testing

libraryDependencies += "org.scalatest" %% "scalatest" % "3.0.0-M5" % "test"

libraryDependencies += "org.scalamock" %% "scalamock-scalatest-support" % "3.2" % "test"

libraryDependencies += "org.scalacheck" %% "scalacheck" % "1.12.4" % "test"

//specs2

libraryDependencies ++= Seq("org.specs2" %% "specs2-core" % "3.6.2" % "test")

resolvers += "scalaz-bintray" at "http://dl.bintray.com/scalaz/releases"

scalacOptions in Test ++= Seq("-Yrangepos")

//end specs2

//End testing

//Utilities

libraryDependencies += "org.scalaz" %% "scalaz-core" % "7.1.3"

libraryDependencies += "net.codingwell" %% "scala-guice" % "4.0.0"

libraryDependencies += "com.chuusai" %% "shapeless" % "2.2.4"

libraryDependencies += "org.scala-lang.modules" %% "scala-async" % "0.9.3"

libraryDependencies += "com.twitter" %% "algebird-core" % "0.10.2"

//libraryDependencies += "org.scala-miniboxing.plugins" %%
//                       "miniboxing-runtime" % "0.4-SNAPSHOT"

//addCompilerPlugin("org.scala-miniboxing.plugins" %%
//                  "miniboxing-plugin" % "0.4-SNAPSHOT")

//End utilities

//Math/ML

libraryDependencies += "org.spire-math" %% "spire" % "0.10.1"

libraryDependencies  ++= Seq(
  // other dependencies here
  "org.scalanlp" %% "breeze" % "0.11.2",
  // native libraries are not included by default. add this if you want them (as of 0.7)
  // native libraries greatly improve performance, but increase jar sizes. 
  // It also packages various blas implementations, which have licenses that may or may not
  // be compatible with the Apache License. No GPL code, as best I know.
  "org.scalanlp" %% "breeze-natives" % "0.11.2"
)

resolvers ++= Seq(
  // other resolvers here
  // if you want to use snapshot builds (currently 0.12-SNAPSHOT), use this.
  "Sonatype Snapshots" at "https://oss.sonatype.org/content/repositories/snapshots/",
  "Sonatype Releases" at "https://oss.sonatype.org/content/repositories/releases/"
)

//End math/ML

//Parsing

libraryDependencies ++=
    Seq (
        "com.googlecode.kiama" %% "kiama" % "1.8.0",
        "com.googlecode.kiama" %% "kiama" % "1.8.0" % "test" classifier ("tests"))

resolvers ++= Seq (
    Resolver.sonatypeRepo ("releases"),
    Resolver.sonatypeRepo ("snapshots")
)

//libraryDependencies += "org.parboiled" %% "parboiled" % "2.1.0"

libraryDependencies += "com.lihaoyi" %% "fastparse" % "0.2.1"

//End parsing

//Logging

//libraryDependencies ++= Seq(
//  "org.slf4s" %% "slf4s-api" % "1.7.12",
//  "ch.qos.logback" % "logback-classic" % "1.1.2"
//)

//or

//seq(bintrayResolverSettings:_*)

//libraryDependencies += "org.clapper" %% "grizzled-slf4j" % "1.0.2"

//or

libraryDependencies += "org.log4s" %% "log4s" % "1.1.5"

//or

//libraryDependencies ++= Seq(
//   "org.meerkat" % "meerkat_2.11" % "0.1.0",
//   "org.bitbucket.inkytonik.dsinfo" %% "dsinfo" % "0.4.0"
//)

//End logging

//Concurrency

//libraryDependencies += "com.typesafe.akka" %% "akka-actor" % "2.3.11"

libraryDependencies += "com.typesafe.akka" %% "akka-stream-experimental" % "1.0-RC4"

libraryDependencies += "io.reactivex" %% "rxscala" % "0.25.0"

//End concurrency

//Documentation

//scalatex.SbtPlugin.projectSettings

//End documentation

//skip in packageJSDependencies := false

scalacOptions ++= Seq(
  "-deprecation",
  "-encoding", "UTF-8",
  "-unchecked",
  "-feature",
  "-language:implicitConversions",
  "-language:postfixOps",
  "-language:higherKinds",
  "-language:reflectiveCalls",
  "-Xlint",
//  "-Xfatal-warnings",
  "-Yno-adapted-args",
  "-Ywarn-dead-code",
  "-Ywarn-numeric-widen",
  "-Ywarn-value-discard",
  "-Xfuture"
)

pomIncludeRepository := { x => false }
