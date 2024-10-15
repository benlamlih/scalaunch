name := "$name$"

version := "0.1.0-SNAPSHOT"

scalaVersion := "$scala_version$"

ThisBuild / organization := "$package$"

lazy val root = (project in file("."))
  .aggregate(
    $if(include_backend.truthy) $ backend $endif$
      $if(include_frontend.truthy) $,
    frontend $endif$
      $if(include_shared.truthy) $,
    shared $endif$,
  )
  .settings(
    publish / skip := true,
  )

$if(include_backend.truthy) $
lazy val backend = {
  val backendProject = (project in file("./backend"))
    .settings(commonSettings)
    .settings(
      name         := "backend",
      scalaVersion := "$scala_version$",
      // TODO: Fix dependencies versions
      libraryDependencies ++= Seq(
        $if(backend_framework == "cask") $
          "com.lihaoyi" %% "cask" % "0.9.0",
        $elseif(backend_framework == "akka") $
          "com.typesafe.akka" %% "akka-actor-typed" % "2.8.0",
        "com.typesafe.akka"   %% "akka-stream"      % "2.8.0",
        $elseif(backend_framework == "http4s") $
          "org.http4s" %% "http4s-dsl"          % "0.23.22",
        "org.http4s"   %% "http4s-blaze-server" % "0.23.22",
        $endif$
          $if (include_cats.truthy) $
          "org.typelevel" %% "cats-core" % "2.10.0",
        $endif$,
      ),
    )
    .settings(
      assembly / mainClass := Some("$package.server.Server"),
      assembly / assemblyJarName := "app.jar"

      // Gets rid of "(server / assembly) deduplicate: different file contents found in the following" errors
      // https://stackoverflow.com/questions/54834125/sbt-assembly-deduplicate-module-info-class
        assembly / assemblyMergeStrategy := {
          case path if path.endsWith("module-info.class") => MergeStrategy.discard
          case path =>
            val oldStrategy = (assembly / assemblyMergeStrategy).value
            oldStrategy(path)
        },
    )
  $if(include_shared.truthy) $
    backendProject.dependsOn(shared)
  $else$
  backendProject
  $endif$
}
$endif$

$if(include_frontend.truthy) $
lazy val frontend = {
  val frontendProject = (project in file("./frontend"))
    .enablePlugins(ScalaJSPlugin)
    .settings(commonSettings)
    .settings(
      scalaVersion                    := "$scala_version$",
      name                            := "frontend",
      scalaJSUseMainModuleInitializer := true,
      scalaJSLinkerConfig ~= {
        _.withModuleKind(ModuleKind.ESModule)
          .withModuleSplitStyle(
            ModuleSplitStyle.SmallModulesFor(List("$package$")),
          )
      },
      libraryDependencies ++= List(
        "org.scala-js" %%% "scalajs-dom" % "2.8.0",
        "com.raquo"    %%% "laminar"     % "17.1.0",
      ),
      idePackagePrefix := Some("$package$"),
    )
  $if(include_shared.truthy) $
    frontendProject.dependsOn(shared)
  $else$
  frontendProject
  $endif$
}
$endif$

$if(include_shared.truthy) $
lazy val shared = crossProject(JSPlatform, JVMPlatform)
  .in(file("."))
  .enablePlugins(BuildInfoPlugin)
  .settings(commonSettings)
  .settings(
    // sbt-BuildInfo plugin can write any (simple) data available in sbt at
    // compile time to a `case class BuildInfo` that it makes available at runtime.
    buildInfoKeys := Seq[BuildInfoKey](scalaVersion, sbtVersion, BuildInfoKey("laminarVersion" -> "17.1.0")),
    // The BuildInfo case class is located in target/scala<version>/src_managed,
    // and with this setting, you'll need to `import $package.buildinfo.BuildInfo`
    // to use it.
    buildInfoPackage := ".buildinfo",
    // Because we add BuildInfo to the `shared` project, this will be available
    // on both the client and the server, but you can also make it e.g. server-only.
  )
  .settings(
    libraryDependencies ++= List(
      // Here we add dependencies that are shared between JS and JVM
      // (e.g. case classes that are serialized to JSON)
      // JSON codec
      // "io.bullet" %%% "borer-core"       % Versions.Borer,
      // "io.bullet" %%% "borer-derivation" % Versions.Borer,
    ),
  )
  .jvmSettings(
    libraryDependencies ++= List(
      // This dependency lets us put @JSExportAll and similar Scala.js
      // annotations on data structures shared between JS and JVM.
      // With this library, on the JVM, these annotations compile to
      // no-op, which is exactly what we need.
      "org.scala-js" %% "scalajs-stubs" % "1.0.0",
    ),
  )
$endif$

lazy val commonSettings = Seq(
  scalacOptions ++= Seq(
    "-deprecation",
    // "-feature",
    "-language:implicitConversions",
  ),
) ++ intellijTargetSettings

// Use Vim :) Otherwise you'll have to deal with this:

// -- IntelliJ workarounds

// https://youtrack.jetbrains.com/issue/SCL-21917/Trivial-changes-to-build.sbt-cause-IDEA-to-forget-excluded-directories
// https://github.com/JetBrains/sbt-ide-settings
SettingKey[Seq[File]]("ide-excluded-directories").withRank(KeyRanks.Invisible) := Seq(
  file(".idea"),
  file("project/project/target"),
  file("target"),
  file("target-idea"),
  file("client/target"),
  file("client/target-idea"),
  file("server/target"),
  file("server/target-idea"),
  file("shared/js/target"),
  file("shared/js/target-idea"),
  file("shared/jvm/target"),
  file("shared/jvm/target-idea"),
  file("shared/shared/target"),
  file("dist"),
  file("client/dist"),
  file("client/public/assets/shoelace"),
  file("client/src/main/scala/com/raquo/app/codesnippets/generated"), // Not 100% sure if it's good UX to exclude this
  file("server/src/main/resources/static"),
)

// https://youtrack.jetbrains.com/issue/SCL-21839/Intellij-refactor-causes-external-incremental-sbt-compilation-to-fail-consistently
val intellijTargetSettings = {
  if (System.getenv("IDEA_INITIAL_DIRECTORY") ne null)
    Seq(
      target := baseDirectory.value / "target-idea",
    )
  else Seq.empty
}
