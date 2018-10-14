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

  var names: Array[CString] = Array(c"Sierpinski", c"Vicsek", c"Vicsekx", c"Cantor Dust", c"Koch Curve", c"Koch Snowflake", c"Tree", c"Sierpinski Carpet")

  def getFractalName(index: Int) = names(index)

  var infoName = c"Sierpinski" // Default value

  def changeFractalName(currentFractal: Int) = {
    infoName = getFractalName(currentFractal)
  }

  def draw(font: Ptr[TTF_Font], renderer: Ptr[SDL.SDL_Renderer]) = {
    // Prepare the texture with the text
    val message = TTF_RenderText_Solid(font, infoName, fontColor)
    var texture = SDL_CreateTextureFromSurface(renderer, message)
    val w = stackalloc[CInt]
    val h = stackalloc[CInt]
    SDL_QueryTexture(texture, null, null, w, h)
    val rect = stackalloc[SDL_Rect].init(0, 0, !w, !h)
    SDL_RenderCopy(renderer, texture, null, rect)
  }


}
