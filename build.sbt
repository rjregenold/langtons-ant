name := "langtonsant"

organization := "com.binarylion"

version := "0.1"

scalaVersion := "2.9.1"

crossScalaVersions := Seq("2.9.1", "2.9.0-1", "2.9.0", "2.8.1", "2.8.0")

scalacOptions ++= Seq("-deprecation", "-unchecked")

javacOptions ++= Seq("-Xlint:unchecked", "-Xlint:deprecation")

mainClass in (Compile, packageBin) := Some("com.binarylion.langton.Main")

mainClass in (Compile, run) := Some("com.binarylion.langton.Main")

seq(ProguardPlugin.proguardSettings :_*)

proguardOptions ++= Seq (
    "-dontshrink -dontoptimize -dontobfuscate -dontpreverify -dontnote " +
    "-ignorewarnings",
    keepAllScala
)
