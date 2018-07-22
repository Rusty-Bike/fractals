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
      val bottomRightTriangle = sierpinski(newDepth, iterations, newLength, bottomLeftPoint.incX(newLength))
      val topCenterTriangle   = sierpinski(newDepth, iterations, newLength, bottomLeftPoint.incX(newLength / 2).decY(newLength))

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

      val centerBox = vicsek(newDepth, iterations, newLength, bottomLeftPoint.decY(newLength).incX(newLength))
      val centerLeftBox = vicsek(newDepth, iterations, newLength, bottomLeftPoint.decY(newLength))
      val centerRightBox = vicsek(newDepth, iterations, newLength, bottomLeftPoint.decY(newLength).incX(newLength * 2))
      val bottomBox = vicsek(newDepth, iterations, newLength, bottomLeftPoint.incX(newLength))
      val topBox = vicsek(newDepth, iterations, newLength, bottomLeftPoint.incX(newLength).decY(newLength * 2))

      bottomBox ::: centerBox ::: centerLeftBox ::: topBox ::: centerRightBox 
    }
  }


}
