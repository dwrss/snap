import Dependencies._

name := "Snap"
organization := "org.dwrs.blink"
scalaVersion := "2.13.2"

scalacOptions ++= Seq(
	"-deprecation",
	"-encoding", "utf-8",
	"-explaintypes",
	"-unchecked",
//	"-feature",
//	"-language:postfixOps",
//	"-language:reflectiveCalls",
//	"-language:existentials",
//	"-language:implicitConversions",
	"-Xlint",
//	"-Ypartial-unification",             // Enable partial unification in type constructor inference
	"-Ywarn-dead-code",                  // Warn when dead code is identified.
	"-Ywarn-extra-implicit",             // Warn when more than one implicit parameter section is defined.
	"-Ywarn-numeric-widen",              // Warn when numerics are widened.
	"-Ywarn-unused:implicits",           // Warn if an implicit parameter is unused.
	"-Ywarn-unused:imports",             // Warn if an import selector is not referenced.
	"-Ywarn-unused:locals",              // Warn if a local definition is unused.
	"-Ywarn-unused:params",              // Warn if a value parameter is unused.
	"-Ywarn-unused:patvars",             // Warn if a variable bound in a pattern is unused.
	"-Ywarn-unused:privates",            // Warn if a private member is unused.
	"-Ywarn-value-discard"               // Warn when non-Unit expression results are unused.
)

libraryDependencies += enumeratum
libraryDependencies += scalaTest
libraryDependencies ++= atto
libraryDependencies += cats