package fractals

import core.DrawingPrimitives._

object Fractals {

  type Fractal = (Int, Int, Int, Point) => List[LineSegment]

  def dragonCurve(
    currentDepth: Int,
    iterations:   Int,
    length:       Int,
    bLeftPoint:   Point
  ): List[LineSegment] = {
    val bottomLeftPoint = bLeftPoint.moveUp(length / 2).moveRight(length / 4)

    val start    = Vec2(bottomLeftPoint)
    val rightDir = Vec2(1, 0)

    Dragon.curve(
      iterations
    )(
      currentDepth,
      start,
      rightDir,
      length / 3
    ).toList
  }

  def h(
    currentDepth: Int,
    iterations:   Int,
    length:       Int,
    bLeftPoint:   Point
  ): List[LineSegment] = {
    val bottomLeftPoint = bLeftPoint.moveUp(length / 4)

    if(currentDepth == iterations) {
      H(bottomLeftPoint, length).toLines
    }  else {
      val topLeftPoint = bottomLeftPoint.moveUp(
        ((length / 3f) - (length / 12f)).toInt
      ).moveLeft(
        (length / 4f + length / 32f).toInt
      )

      val topRightPoint = bottomLeftPoint.moveUp(
        length / 3 - (length / 12)
      ).moveRight(
        length - length / 4 - length / 24
      )

      val newBottomLeftPoint = topLeftPoint.moveDown(
        length  / 3 + length  / 6
      )

      val newBottomRightPoint = topRightPoint.moveDown(
        length / 3 +  length / 6
      )

      val topLeftLines = h(
        currentDepth + 1,
        iterations,
        length / 2,
        topLeftPoint
      )

      val topRightLines = h(
        currentDepth + 1,
        iterations,
        length / 2,
        topRightPoint
      )

      val newBottomLeftLines = h(
        currentDepth + 1,
        iterations,
        length / 2,
        newBottomLeftPoint
      )

      val newBottomRightLines = h(
        currentDepth + 1,
        iterations,
        length / 2,
        newBottomRightPoint
      )

      H(
        bottomLeftPoint,
        length
      ).toLines             :::
        topLeftLines        :::
        newBottomRightLines :::
        topRightLines       :::
        newBottomLeftLines
    }
  }

  def sierpinski(
    currentDepth:    Int,
    iterations:      Int,
    length:          Int,
    bottomLeftPoint: Point
  ): List[LineSegment] = {
    if (currentDepth == iterations) {
      Triangle(bottomLeftPoint, length).toLines
    } else {
      val newDepth  = currentDepth + 1
      val newLength = length / 2

      // Call ourselves again on the three sub-triangles
      val bottomLeftTriangle = sierpinski(
        newDepth,
        iterations,
        newLength,
        bottomLeftPoint
      )

      val bottomRightTriangle = sierpinski(
        newDepth,
        iterations,
        newLength,
        bottomLeftPoint.moveRight(
          newLength
        )
      )

      val topCenterTriangle = sierpinski(
        newDepth,
        iterations,
        newLength,
        bottomLeftPoint.moveRight(
          newLength / 2
        ).moveUp(
          (newLength * .87f).toInt
        )
      )

      // Merge and return the list of lines
      bottomLeftTriangle  :::
      topCenterTriangle   :::
      bottomRightTriangle
    }
  }

  def vicsek(
    currentDepth:    Int,
    iterations:      Int,
    length:          Int,
    bottomLeftPoint: Point
  ): List[LineSegment] = {
    val square = Square(
      bottomLeftPoint,
      length
    )

    if(currentDepth == iterations) {
      square.toLines
    } else {
      val newDepth  = currentDepth + 1
      val newLength = length / 3

      // Call ourselves again on the five sub-squares
      val centerBox = vicsek(
        newDepth,
        iterations,
        newLength,
        bottomLeftPoint.moveUp(
          newLength
        ).moveRight(
          newLength
        )
      )

      val centerLeftBox = vicsek(
        newDepth,
        iterations,
        newLength,
        bottomLeftPoint.moveUp(
          newLength
        )
      )

      val centerRightBox = vicsek(
        newDepth,
        iterations,
        newLength,
        bottomLeftPoint.moveUp(
          newLength
        ).moveRight(
          newLength * 2
        )
      )

      val bottomBox = vicsek(
        newDepth,
        iterations,
        newLength,
        bottomLeftPoint.moveRight(
          newLength
        )
      )

      val topBox = vicsek(
        newDepth,
        iterations,
        newLength,
        bottomLeftPoint.moveRight(
          newLength
        ).moveUp(
          newLength * 2
        )
      )

      // Merge and return the list of lines
      bottomBox      :::
      centerBox      :::
      centerLeftBox  :::
      topBox         :::
      centerRightBox
    }
  }

  def vicsekx(
    currentDepth:    Int,
    iterations:      Int,
    length:          Int,
    bottomLeftPoint: Point
  ): List[LineSegment] = {
    val square = Square(
      bottomLeftPoint,
      length
    )

    if(currentDepth == iterations) {
      square.toLines
    } else {
      val newDepth  = currentDepth + 1
      val newLength = length / 3

      // Call ourselves again on the five sub-squares
      val centerBox = vicsekx(
        newDepth,
        iterations,
        newLength,
        bottomLeftPoint.moveUp(
          newLength
        ).moveRight(
          newLength
        )
      )

      val topLeftBox = vicsekx(
        newDepth,
        iterations,
        newLength,
        bottomLeftPoint.moveUp(
          newLength * 2
        )
      )

      val topRightBox = vicsekx(
        newDepth,
        iterations,
        newLength,
        bottomLeftPoint.moveUp(
          newLength * 2
        ).moveRight(
          newLength * 2
        )
      )

      val bottomLeftBox = vicsekx(
        newDepth,
        iterations,
        newLength,
        bottomLeftPoint
      )

      val bottomRightBox = vicsekx(
        newDepth,
        iterations,
        newLength,
        bottomLeftPoint.moveRight(
          newLength * 2
        )
      )

      // Merge and return the list of lines
      centerBox      :::
      bottomLeftBox  :::
      topRightBox    :::
      topLeftBox     :::
      bottomRightBox
    }
  }

  def cantorDust(
    currentDepth:    Int,
    iterations:      Int,
    length:          Int,
    bottomLeftPoint: Point
  ): List[LineSegment] = {
    val square = Square(
      bottomLeftPoint,
      length
    )

    if(currentDepth == iterations) {
      square.toLines
    } else {
      val newDepth  = currentDepth + 1
      val newLength = length / 3

      // Call ourselves again on the four inner squares
      val bottomLeft = cantorDust(
        newDepth,
        iterations,
        newLength,
        bottomLeftPoint
      )

      val topLeft = cantorDust(
        newDepth,
        iterations,
        newLength,
        bottomLeftPoint.moveUp(
          newLength * 2
        )
      )

      val topRight = cantorDust(
        newDepth,
        iterations,
        newLength,
        bottomLeftPoint.moveUp(
          newLength * 2
        ).moveRight(
          newLength * 2
        )
      )

      val bottomRight = cantorDust(
        newDepth,
        iterations,
        newLength,
        bottomLeftPoint.moveRight(
          newLength * 2
        )
      )

      // Merge and return the list of lines
      bottomLeft  :::
      topLeft     :::
      topRight    :::
      bottomRight
    }
  }

  def kochCurve(
    currentDepth:    Int,
    iterations:      Int,
    length:          Int,
    bottomLeftPoint: Point
  ): List[LineSegment] = {
    val start    = Vec2(bottomLeftPoint)
    val rightDir = Vec2(1, 0)

    Koch.curve(
      iterations
    )(
      currentDepth,
      start,
      rightDir,
      length
    ).toList
  }

  def kochSnowflake(
    currentDepth:    Int,
    iterations:      Int,
    length:          Int,
    bottomLeftPoint: Point
): List[LineSegment] = {

    val rightDir     = Vec2(1, 0)
    val leftDir      = Vec2(-1, 0)
    val upRightDir   = rightDir.rotate(-60)
    val downRightDir = rightDir.rotate(60)

    val actualBottomLeft =
      Vec2(bottomLeftPoint) +
      upRightDir            *
      (length / 3)

    val newLength = length * 2 / 3

    val kochCurve = Koch.curve(iterations) _

    kochCurve(
      currentDepth,
      actualBottomLeft + rightDir * newLength,
      leftDir,
      newLength
    ).toList ++
    kochCurve(
      currentDepth,
      actualBottomLeft,
      upRightDir,
      newLength
    ) ++
    kochCurve(
      currentDepth,
      actualBottomLeft + upRightDir * newLength,
      downRightDir,
      newLength
    )
  }

  def tree(
    currentDepth:    Int,
    iterations:      Int,
    length:          Int,
    bottomLeftPoint: Point
): List[LineSegment] = {
    val branchAngle = 20

    def loop(
      depth:  Int,
      length: Int,
      start:  Vec2,
      dir:    Vec2
    ): List[LineSegment] =
      if (depth == iterations)
        List(LineSegment(
          start.toPoint,
          (start + (dir * length)).toPoint)
        )
      else {
        val newLength = length * 2 / 3
        val end       = start + dir * length

        val trunk = LineSegment(
          start.toPoint,
          end.toPoint
        )

        val right = loop(
          depth + 1,
          newLength,
          end,
          dir.rotate(branchAngle)
        )

        val left = loop(
          depth + 1,
          newLength,
          end,
          dir.rotate(-branchAngle)
        )

        trunk +: right ::: left
      }

    val up    = Vec2(0, -1)
    val start = Vec2(bottomLeftPoint.copy(x = length / 2))

    loop(
      currentDepth,
      length / 3,
      start,
      up
    )
  }

  def sierpinskiCarpet(
    currentDepth:    Int,
    iterations:      Int,
    length:          Int,
    bottomLeftPoint: Point
): List[LineSegment] = {
    def getActualBottomLeftPoint(
      currentDepth: Int,
      bottomLeftPoint: Point
    ) = {
      if(currentDepth == 0) {
        Point(
          length / 3,
          length * 2 / 3
        )
      } else {
        bottomLeftPoint
      }
    }
    def getActualLength(
     currentDepth: Int,
     length: Int
    ) = {
      if(currentDepth == 0) {
        length / 3
      } else {
        length
      }
    }

    //In the very first iteration we need to calculate the proper beginning position and size of the first square
    val actualBottomLeftPoint = getActualBottomLeftPoint(
      currentDepth,
      bottomLeftPoint
    )

    val actualLength = getActualLength(
      currentDepth,
      length
    )

    val square = Square(
      actualBottomLeftPoint,
      actualLength
    )

    //Second condition ensures we don't try to calculate squares that are too small to form
    if(currentDepth == iterations || actualLength < 3) {
      square.toLines
    } else {
      val newDepth  = currentDepth + 1
      val newLength = actualLength / 3

      // Call ourselves again on the eight sub-squares
      val leftBox = sierpinskiCarpet(
        newDepth,
        iterations,
        newLength,
        actualBottomLeftPoint.moveUp(
          actualLength * 1 / 3
        ).moveLeft(
          actualLength * 2 / 3
        )
      )

      val topLeftBox = sierpinskiCarpet(
        newDepth,
        iterations,
        newLength,
        actualBottomLeftPoint.moveUp(
          actualLength * 4 / 3
        ).moveLeft(
          actualLength * 2 / 3
        )
      )

      val topBox = sierpinskiCarpet(
        newDepth,
        iterations,
        newLength,
        actualBottomLeftPoint.moveUp(
          actualLength * 4 / 3
        ).moveRight(
          actualLength * 1 / 3
        )
      )

      val topRightBox = sierpinskiCarpet(
        newDepth,
        iterations,
        newLength,
        actualBottomLeftPoint.moveUp(
          actualLength * 4 / 3
        ).moveRight(
          actualLength * 4 / 3
        )
      )

      val rightBox = sierpinskiCarpet(
        newDepth,
        iterations,
        newLength,
        actualBottomLeftPoint.moveUp(
          actualLength * 1 / 3
        ).moveRight(
          actualLength * 4 / 3
        )
      )

      val bottomRightBox = sierpinskiCarpet(
        newDepth,
        iterations,
        newLength,
        actualBottomLeftPoint.moveDown(
          actualLength * 2 / 3
        ).moveRight(
          actualLength * 4 / 3
        )
      )

      val bottomBox = sierpinskiCarpet(
        newDepth,
        iterations,
        newLength,
        actualBottomLeftPoint.moveDown(
          actualLength * 2 / 3
        ).moveRight(
          actualLength * 1 / 3
        )
      )

      val bottomLeftBox = sierpinskiCarpet(
        newDepth,
        iterations,
        newLength,
        actualBottomLeftPoint.moveDown(
          actualLength * 2 / 3
        ).moveLeft(
          actualLength * 2 / 3
        )
      )

      //Merge and return the list of lines
      square.toLines :::
      leftBox        :::
      topLeftBox     :::
      topBox         :::
      topRightBox    :::
      rightBox       :::
      bottomRightBox :::
      bottomBox      :::
      bottomLeftBox
    }
  }

  def minkowskiSausage(
    currentDepth:    Int,
    iterations:      Int,
    length:          Int,
    bottomLeftPoint: Point
  ): List[LineSegment] = {
    val start    = Vec2(
      bottomLeftPoint.moveUp(
        length / 2
      )
    )

    val rightDir = Vec2(1, 0)

    MinkowskiSausage.curve(
      iterations
    )(
      currentDepth,
      start,
      rightDir,
      length
    ).toList
  }

  def cesaro(
    currentDepth:    Int,
    iterations:      Int,
    length:          Int,
    bottomLeftPoint: Point
  ): List[LineSegment] = {
    val rightDir        = Vec2(1, 0)
    val leftDir         = Vec2(-1, 0)
    val upDir           = Vec2(0, -1)
    val downDir         = Vec2(0, 1)
    val bottomLeftVec2  = Vec2(bottomLeftPoint)
    val topLeftVec2     = Vec2(bottomLeftPoint.moveUp(length))
    val topRightVec2    = Vec2(bottomLeftPoint.moveUp(length).moveRight(length))
    val bottomRightVec2 = Vec2(bottomLeftPoint.moveRight(length))
    val kochCurve       = Koch.curve(iterations, 85) _

    (
      kochCurve(
        currentDepth,
        bottomRightVec2,
        upDir,
        length
      ) ++
      kochCurve(
        currentDepth,
        bottomLeftVec2,
        rightDir,
        length
      ) ++
      kochCurve(
        currentDepth,
        topRightVec2,
        leftDir,
        length
      ) ++
      kochCurve(
        currentDepth,
        topLeftVec2,
        downDir,
        length
      )
    ).toList
  }
}
