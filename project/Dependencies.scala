import sbt._

object Dependencies {
	object Versions {
		val enumeratum = "1.6.1"
		val scalaTest = "3.1.2"
		val atto = "0.7.0"
		val cats = "2.1.1"
	}
	// Better enums
	val enumeratum = "com.beachape" %% "enumeratum" % Versions.enumeratum
	//	Testing
	val scalaTest = "org.scalatest" %% "scalatest" % Versions.scalaTest % "test"
	// Parse commands
	val atto = Seq(
		"org.tpolecat" %% "atto-core" % Versions.atto
	)
	// Transitive dependency of Atto, so used for a couple of operations
	val cats = "org.typelevel" %% "cats-core" % Versions.cats % "test"


}
