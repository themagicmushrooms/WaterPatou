name := "TestGDXCube"

version := "1.0"

scalaVersion := "2.12.3"

libraryDependencies ++= Seq(
  "com.badlogicgames.gdx" % "gdx-backend-lwjgl" % "1.4.1",
  "com.badlogicgames.gdx" % "gdx-platform" % "1.4.1" classifier "natives-desktop"
)
