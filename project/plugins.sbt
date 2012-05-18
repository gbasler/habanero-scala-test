resolvers ++= Seq(
  "Nexus Releases" at "http://nexus/nexus/content/repositories/releases",
  "Nexus Snapshots" at "http://nexus/nexus/content/repositories/snapshots",
  "sbt-idea-repo" at "http://mpeltonen.github.com/maven/"
)

//ivyLoggingLevel := UpdateLogging.Full

addSbtPlugin("com.github.mpeltonen" % "sbt-idea" % "1.0.0")

scalacOptions in ThisBuild += "-deprecation"