# Fractals 
A Scala native fractal generator.

Sierpinski Triangle
![Sierpinski 
Triangle](https://github.com/smithandrewl/fractals/raw/master/docs/images/sierpinski.png)

## First runners
You need to install llvm and sdl2 to your system. (on mac `brew install llvm sdl2` works)
After that `sbt run` will start and run the project.

For the implemented fractals you can add the number of the implementation as command line parameter. (for ex. `sbt "run 3"`)

You can use left mouse button to add more detail/depth to the actual fractal.
You can use right mouse button to iterate between the fractals.
You can use middle mouse button to animate the actual fractal.

## List of implemented fractals

 - 0: sierpinski
 - 1: vicsek
 - 2: vicsekx
 - 3: cantorDust
 - 4: kochCurve
 - 5: kochSnowflake
 - 6: Tree
