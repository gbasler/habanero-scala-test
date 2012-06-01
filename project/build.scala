import sbt._
import Keys._
import sbt.Reference._

object build extends Build {

  import DependencyManagement._

  // Settings shared by all sub-projects.
  val standardSettings: Seq[Project.Setting[_]] =
    Seq[Project.Setting[_]](
      ivyXML := DependencyManagement.ivyExclusionsAndOverrides,
      scalaVersion := "2.9.2"
    )

  //
  // PROJECTS
  //

  // Parent Project, it aggregates all others.
  lazy val jnaTest = Project(
    id = "habanero-scala-test",
    base = file("."),
    settings = Defaults.defaultSettings ++ standardSettings,
    aggregate = Seq[ProjectReference](scalazSbtSandboxCore)
  )

  lazy val scalazSbtSandboxCore = Project(
    id = "habanero-scala-test-core",
    base = file("habanero-scala-test-core"),
    settings = Defaults.defaultSettings ++ standardSettings ++ runPlacesTaskSettings ++ Seq(
      libraryDependencies ++= Seq(Specs, SpecsInternalScalaz, JUnit, Scalacheck, MockitoAll, CommonsIo)
    )
  )

  lazy val runPlacesTask = TaskKey[Unit]("run-places")

  lazy val runPlacesTaskSettings: Seq[Setting[_]] = Seq(
    fullRunTask(runPlacesTask, Test, "Places"),
    fork in runPlacesTask := true,
    javaOptions in runPlacesTask += "-Dhs.places=4:1"
  )

}
