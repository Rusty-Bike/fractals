import DrawingPrimitives.{LineSegment, Point, Square, Triangle}

object Fractals {

  type Fractal = (Int, Int, Int, Point) => List[LineSegment]

  def sierpinski(currentDepth: Int, iterations: Int, length: Int, bottomLeftPoint: Point): List[LineSegment] = {
    val triangle = Triangle(bottomLeftPoint, length)

    if (currentDepth == iterations) {
      triangle.toLines
    } else {
      val newDepth  = currentDepth + 1
      val newLength = length / 2

      // Call ourselves again on the three sub-triangles
      val bottomLeftTriangle  = sierpinski(newDepth, iterations, newLength, bottomLeftPoint)
      val bottomRightTriangle = sierpinski(newDepth, iterations, newLength, bottomLeftPoint.moveRight(newLength))
      val topCenterTriangle   = sierpinski(newDepth, iterations, newLength, bottomLeftPoint.moveRight(newLength / 2).moveUp(newLength))

      // Merge and return the list of lines
      bottomLeftTriangle ::: topCenterTriangle ::: bottomRightTriangle
    }
  }

  def vicsek(currentDepth: Int, iterations: Int, length: Int, bottomLeftPoint: Point): List[LineSegment] = {
    val square = Square(bottomLeftPoint, length)
    
    if(currentDepth == iterations) {
      square.toLines
    } else {
      val newDepth = currentDepth + 1
      val newLength = length / 3

      // Call ourselves again on the five sub-squares
      val centerBox      = vicsek(newDepth, iterations, newLength, bottomLeftPoint.moveUp(newLength).moveRight(newLength))
      val centerLeftBox  = vicsek(newDepth, iterations, newLength, bottomLeftPoint.moveUp(newLength))
      val centerRightBox = vicsek(newDepth, iterations, newLength, bottomLeftPoint.moveUp(newLength).moveRight(newLength * 2))
      val bottomBox      = vicsek(newDepth, iterations, newLength, bottomLeftPoint.moveRight(newLength))
      val topBox         = vicsek(newDepth, iterations, newLength, bottomLeftPoint.moveRight(newLength).moveUp(newLength * 2))

      // Merge and return the list of lines
      bottomBox ::: centerBox ::: centerLeftBox ::: topBox ::: centerRightBox 
    }
  }

  def vicsekx(currentDepth: Int, iterations: Int, length: Int, bottomLeftPoint: Point): List[LineSegment] = {
    val square = Square(bottomLeftPoint, length)

    if(currentDepth == iterations) {
      square.toLines
    } else {
      val newDepth = currentDepth + 1
      val newLength = length / 3

      // Call ourselves again on the five sub-squares
      val centerBox      = vicsekx(newDepth, iterations, newLength, bottomLeftPoint.moveUp(newLength).moveRight(newLength))
      val topLeftBox     = vicsekx(newDepth, iterations, newLength, bottomLeftPoint.moveUp(newLength * 2))
      val topRightBox    = vicsekx(newDepth, iterations, newLength, bottomLeftPoint.moveUp(newLength * 2).moveRight(newLength * 2))
      val bottomLeftBox  = vicsekx(newDepth, iterations, newLength, bottomLeftPoint)
      val bottomRightBox = vicsekx(newDepth, iterations, newLength, bottomLeftPoint.moveRight(newLength * 2))

      // Merge and return the list of lines
      centerBox ::: bottomLeftBox ::: topRightBox ::: topLeftBox ::: bottomRightBox
    }
  }


}
