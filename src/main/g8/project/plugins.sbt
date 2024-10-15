// sbt-revolver is a plugin for SBT enabling a super-fast development turnaround for your Scala applications
// more info: https://github.com/spray/sbt-revolver
addSbtPlugin("io.spray" % "sbt-revolver" % "0.10.0")
// bloop is used for faster compilation
addSbtPlugin("ch.epfl.scala" % "sbt-bloop" % "1.5.11")

$if(include_backend.truthy) $
  addSbtPlugin("com.eed3si9n" % "sbt-assembly" % "2.1.3")
$endif$
$if(include_frontend.truthy) $
  addSbtPlugin("org.scala-js" % "sbt-scalajs" % "1.17.0")
$endif$
$if(include_shared.truthy) $
  addSbtPlugin("org.portable-scala" % "sbt-scalajs-crossproject" % "1.0.0")
$endif$
