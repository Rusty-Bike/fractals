# Fractals
A Scala native fractal generator.

Sierpinski Triangle
![Sierpinski
Triangle](docs/images/sierpinski.png)

## First runners
You need to install llvm, sdl2 and bdw-gc to your system.

### MacOS

```
brew install llvm sdl2 sdl2_ttf bdw-gc
```

### Linux (tested on Ubuntu 18.04)

```
sudo apt install llvm
sudo apt install libsdl2-dev
sudo apt install libgc-dev
```

It is also needed clang and libunwind-dev.
```
sudo apt install clang
sudo apt install libunwind-dev
```

### Running

After that `sbt run` will start and run the project.

For the implemented fractals you can add the number of the implementation as command line parameter. (for ex. `sbt "run 3"`)

You can use left mouse button to add more detail/depth to the actual fractal.
You can use right mouse button to iterate between the fractals.
You can use middle mouse button to animate the actual fractal.

## List of implemented fractals

- 0: sierpinski
![SierpinskiTriangle](docs/images/sierpinski.png)

- 1: vicsek
![Vicsek](docs/images/vicsek.png)

- 2: vicsekx
![VicsekX](docs/images/vicsek-x.png)

- 3: cantorDust
![CantorDust](docs/images/cantor-dust.png)

- 4: kochCurve
![KochCurve](docs/images/koch-curve.png)

- 5: kochSnowflake
![KochSnowFlake](docs/images/koch-snowflake.png)

- 6: Tree
![Tree](docs/images/tree.png)

- 7: sierpinski carpet
![SierpinskiCarpet](docs/images/sierpinski-carpet.png)
 
- 8: dragon curve 
![DragonCurve](docs/images/dragon-curve.png)

- 9: H fractal 
![Hfractal](docs/images/H-fractal.png)

- 10: Minkowski Sausage 
![MinkowskiSausage](docs/images/minkowski-sausage.png)

- 11: Cesaro 
![Cesaro](docs/images/cesaro.png)
