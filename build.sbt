name := "TestGDXCube"

version := "1.0"

scalaVersion := "2.12.3"

libraryDependencies ++= Seq(
  "com.badlogicgames.gdx" % "gdx-backend-lwjgl" % "1.9.6",
  "com.badlogicgames.gdx" % "gdx-platform" % "1.9.6" classifier "natives-desktop",
  "com.badlogicgames.gdx" % "gdx-bullet" % "1.9.6",
  "com.badlogicgames.gdx" % "gdx-bullet-platform" % "1.9.6" classifier "natives-desktop"
)
