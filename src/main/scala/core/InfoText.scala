package core

import sdl2.SDL
import sdl2.Extras._
import sdl2.SDL._
import sdl2.ttf.SDL_ttf._

import scala.scalanative.native._


class InfoText(fontColor: SDL_Color) {

  // Constructor OverLoad. If InfoText is instantiated without a color, fontColor is white.
  def this(){
    this(SDL_Color(255.toUByte, 255.toUByte, 255.toUByte, 255.toUByte)) // RGB color white: (255, 255, 255)
  }

  var names: Array[String] = Array("Sierpinski", "Vicsek", "Vicsekx", "Cantor Dust", "Koch Curve", "Koch Snowflake", "Tree", "Sierpinski Carpet")


  var infoName: String = "Sierpinski" // Default value
  var isAnimationOn: String = "off"
  var depth: Int = 0
  var linesNumber: Int = 1
  var text: CString = c"hello world"

  def updateFractalName(currentFractal: Int) = infoName = names(currentFractal)

  def updateDepth(d: Int) = depth = d

  def updateAnimationState(b: Boolean) = if (b) isAnimationOn = "On" else isAnimationOn = "Off"

  def updateLinesNumber(n: Int) = linesNumber = n


  def draw(font: Ptr[TTF_Font], renderer: Ptr[SDL.SDL_Renderer]) = {
    // Prepare the texture with the text

    Zone { implicit z =>
      //var info = string.strcat(infoName, c" Depth: ")
      var info : CString = toCString(infoName + " Depth: " + depth + " Animation: " + isAnimationOn + " Total Lines: " + linesNumber)(z)
      val message = TTF_RenderText_Solid(font, info, fontColor)
      var texture = SDL_CreateTextureFromSurface(renderer, message)
      val w = stackalloc[CInt]
      val h = stackalloc[CInt]
      SDL_QueryTexture(texture, null, null, w, h)
      val rect = stackalloc[SDL_Rect].init(0, 0, !w, !h)
      SDL_RenderCopy(renderer, texture, null, rect)

    }


  }


}
