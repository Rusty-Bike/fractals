scalaVersion := "2.11.12"

// Set to false or remove if you want to show stubs as linking errors
nativeLinkStubs := true

enablePlugins(ScalaNativePlugin)
libraryDependencies += "com.regblanc" %%% "native-sdl2" % "0.1"
libraryDependencies += "com.regblanc" %%% "native-sdl2-ttf" % "0.1"
