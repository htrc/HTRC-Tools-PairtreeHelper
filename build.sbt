import com.typesafe.sbt.{GitBranchPrompt, GitVersioning}

showCurrentGitBranch

git.useGitDescribe := true

lazy val commonSettings = Seq(
  organization := "org.hathitrust.htrc",
  organizationName := "HathiTrust Research Center",
  organizationHomepage := Some(url("https://www.hathitrust.org/htrc")),
  scalaVersion := "2.12.5",
  scalacOptions ++= Seq(
    "-feature",
    "-deprecation",
    "-language:postfixOps",
    "-language:implicitConversions"
  ),
  resolvers ++= Seq(
    "I3 Repository" at "http://nexus.htrc.illinois.edu/content/groups/public",
    Resolver.mavenLocal
  ),
  packageOptions in (Compile, packageBin) += Package.ManifestAttributes(
    ("Git-Sha", git.gitHeadCommit.value.getOrElse("N/A")),
    ("Git-Branch", git.gitCurrentBranch.value),
    ("Git-Version", git.gitDescribedVersion.value.getOrElse("N/A")),
    ("Git-Dirty", git.gitUncommittedChanges.value.toString),
    ("Build-Date", new java.util.Date().toString)
  ),
  description := "Tool and library that provides various APIs for managing HT IDs and the Pairtree structure.",
  licenses += "Apache2" -> url("http://www.apache.org/licenses/LICENSE-2.0"),
  wartremoverErrors ++= Warts.unsafe.diff(Seq(
    Wart.DefaultArguments,
    Wart.NonUnitStatements
  ))
)

lazy val `pairtree-helper` = (project in file("."))
  .settings(
    publish      := {},
    publishLocal := {}
  )
  .aggregate(lib, app)

lazy val lib = (project in file("lib")).
  enablePlugins(GitVersioning, GitBranchPrompt).
  settings(commonSettings).
  settings(
    name := "pairtree-helper",
    publishTo := {
      val nexus = "https://nexus.htrc.illinois.edu/"
      if (isSnapshot.value)
        Some("HTRC Snapshots Repository" at nexus + "content/repositories/snapshots")
      else
        Some("HTRC Releases Repository"  at nexus + "content/repositories/releases")
    },
    credentials += Credentials(Path.userHome / ".ivy2" / ".credentials"),
    libraryDependencies ++= Seq(
      "gov.loc"                       %  "pairtree"             % "1.1.2",
      "org.scalacheck"                %% "scalacheck"           % "1.13.5"      % Test,
      "org.scalatest"                 %% "scalatest"            % "3.0.5"       % Test
    ),
    crossScalaVersions := Seq("2.12.5", "2.11.12")
  )

lazy val app = (project in file("app")).dependsOn(lib).
  enablePlugins(GitVersioning, GitBranchPrompt, JavaAppPackaging).
  settings(commonSettings).
  settings(
    name := "pairtree-helper-app",
    libraryDependencies ++= Seq(
      "org.rogach"                    %% "scallop"              % "3.1.2",
      "org.scalacheck"                %% "scalacheck"           % "1.13.5"      % Test,
      "org.scalatest"                 %% "scalatest"            % "3.0.5"       % Test
    ),
    publish      := {},
    publishLocal := {},
    executableScriptName := "pairtree-helper"
  )
