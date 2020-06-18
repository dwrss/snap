import sbt._
object Dependencies {
	object Versions {
		val scallop = "3.4.0"
		val enumeratum = "1.6.1"
		val scalaTest = "3.1.2"
		val atto = "0.7.0"
		val cats  = "2.1.1"
	}
	// CLI library
	val scallop = "org.rogach" %% "scallop" % Versions.scallop
	val enumeratum = "com.beachape" %% "enumeratum" % Versions.enumeratum
	val scalactic = "org.scalactic" %% "scalactic" % Versions.scalaTest % "test"
	val scalaTest = "org.scalatest" %% "scalatest" % Versions.scalaTest % "test"
	val cats = "org.typelevel" %% "cats-core" % Versions.cats % "test"

	val atto = Seq(
		"org.tpolecat" %% "atto-core" % Versions.atto
	)
}
