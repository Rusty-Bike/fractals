import DrawingPrimitives.{LineSegment, Point, Triangle}

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
      val bottomRightTriangle = sierpinski(newDepth, iterations, newLength, bottomLeftPoint.incX(newLength))
      val topCenterTriangle   = sierpinski(newDepth, iterations, newLength, bottomLeftPoint.incX(newLength / 2).decY(newLength))

      // Merge and return the list of lines
      bottomLeftTriangle ::: topCenterTriangle ::: bottomRightTriangle
    }
  }
}
