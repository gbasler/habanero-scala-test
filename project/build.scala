import java.io.Serializable
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

  lazy val os = SettingKey[String]("os", "This is the targeted OS for the build. Defaults to the running OS.")

  def defineOs = System.getProperty("os.name").toLowerCase.take(3).toString match {
    case "lin"         => "linux"
    case "mac" | "dar" => "osx"
    case "win"         => "windows"
    case "sun"         => "solaris"
    case _             => "unknown"
  }

  lazy val runPlacesTaskSettings: Seq[Setting[_]] = Seq(
    fullRunTask(runPlacesTask, Test, "Places"),
    fork in runPlacesTask := true,
    os := defineOs,
    javaOptions in runPlacesTask += "-Dhs.places=4:1",
    javaOptions in runPlacesTask += "-Dhs.time=true",
    javaOptions in runPlacesTask += "-Dhs.stats=true",
    javaOptions in runPlacesTask += "-Dhs.threadBindingDiagnostics=true",
    javaOptions in runPlacesTask <++= (baseDirectory, os) {
      case (dir, "linux")   => Seq("-Djava.library.path=%s/lib/native/linux_x86".format(dir))
      case (dir, "windows") => Seq("-Djava.library.path=%s/lib/native/win32_x86".format(dir))
      case (dir, _)         => Seq.empty
    }
  )
}
