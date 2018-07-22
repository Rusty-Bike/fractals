object DrawingPrimitives {
  case class Point(x: Int, y: Int) {
    def incX(amount: Int): Point = copy(x = x + amount)
    def decX(amount: Int): Point = copy(x = x - amount)

    def incY(amount: Int): Point = copy(y = y + amount)
    def decY(amount: Int): Point = copy(y = y - amount)
  }

  class LineSegment(val start: Point, val end: Point)

  case class Triangle(bottomLeftPoint: Point, length: Int) {
    def toLines: List[LineSegment] = {
      val topPoint         = Point(bottomLeftPoint.x + (length / 2), bottomLeftPoint.y - length)
      val bottomRightPoint = Point(bottomLeftPoint.x + length, bottomLeftPoint.y)

      val leftLine   = new LineSegment(bottomLeftPoint,  topPoint)
      val rightLine  = new LineSegment(bottomRightPoint, topPoint)
      val bottomLine = new LineSegment(bottomLeftPoint,  bottomRightPoint)

      List(bottomLine, leftLine, rightLine)
    }
  }

}

