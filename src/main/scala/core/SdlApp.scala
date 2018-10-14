package core

import sdl2.Extras.{SDL_INIT_VIDEO, SDL_RENDERER_PRESENTVSYNC, SDL_WINDOWPOS_CENTERED, SDL_WINDOW_SHOWN}
import sdl2.SDL._
import sdl2.ttf.SDL_ttf._

import scala.scalanative.native.{Ptr, UInt, stackalloc, _}

abstract class SdlApp(title: CString, height: Int, width: Int) extends App {
  var window:   Ptr[SDL_Window]   = _
  var renderer: Ptr[SDL_Renderer] = _

  def init(): Unit = {

    SDL_Init(SDL_INIT_VIDEO)
    window   = SDL_CreateWindow(title, SDL_WINDOWPOS_CENTERED, SDL_WINDOWPOS_CENTERED, width, height, SDL_WINDOW_SHOWN)
    renderer = SDL_CreateRenderer(window, -1, SDL_RENDERER_PRESENTVSYNC)
  }

  def onEvent(event: Ptr[SDL_Event]): Unit

  def loop(): Unit = {
    val event = stackalloc[SDL_Event]

    while (true) {
      while (SDL_PollEvent(event) != 0) {
        onEvent(event)
      }

      onDraw()
      onIdle()
      delay((1000 / 12).toUInt)
    }
  }

  def delay(ms: UInt): Unit =
    SDL_Delay(ms)

  def onDraw(): Unit
  def onIdle(): Unit
  def onCleanup(): Unit

  def cleanup(): Unit = {
    SDL_DestroyRenderer(renderer)
    SDL_DestroyWindow(window)
    onCleanup()
    SDL_Quit()
  }

  init()
  loop()
  cleanup()
}
