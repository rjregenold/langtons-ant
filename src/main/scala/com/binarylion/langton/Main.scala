package com.binarylion
package langton

import java.awt._

import scala.annotation.tailrec
import scala.collection.mutable.ArrayBuffer
import scala.math.sqrt

object Main {
  type Point = Pair[Int, Int]
  type Ant = Tuple2[Point, Int]

  val blackColor = new Color(0, 0, 0)
  val whiteColor = new Color(255, 255, 255)
  val redColor = new Color(255, 0, 0)
  val pixelSize = 30

  class PointWrapper(point:Point) {
    def +(that:Point) =
      Pair(point._1 + that._1, point._2 + that._2)
  }

  implicit def pointToWrapper(point:Point) = 
    new PointWrapper(point)

  def sq(num:Int) = 
    num * num

  def emptyBoard(size:Int) = 
    Seq.fill(sq(size))(0)

  def pointToIndex(point:Point, boardSize:Int) = 
    boardSize * point._1 + point._2

  def indexToPoint(index:Int, boardSize:Int) =
    (index / boardSize, index % boardSize)

  def centerPoint(boardSize:Int) =
    ((boardSize / 2), (boardSize / 2))

  def emptyFrame(boardSize:Int) = {
    val screenSize = Toolkit.getDefaultToolkit.getScreenSize
    val frame = new Frame
    frame.setSize(new Dimension(boardSize * pixelSize, boardSize * pixelSize))
    frame.setLocation((screenSize.width - frame.getSize.width) / 2, (screenSize.height - frame.getSize.height) / 2)
    frame.setUndecorated(true)
    frame.setResizable(false)
    frame.setVisible(true)
    frame
  }

  def drawPoint(gfx:Graphics)(color:Color, point:Point) = {
    gfx.setColor(color)
    gfx.fillRect(point._1 * pixelSize, point._2 * pixelSize, pixelSize, pixelSize)
  }

  def drawBoard(gfx:Graphics, boardSize:Int)(board:Seq[Int], ant:Ant) = {
    val draw = drawPoint(gfx) _
    board.zipWithIndex.map { case (color, index) =>
      val point = indexToPoint(index, boardSize)
      draw(if(color == 0) whiteColor else blackColor, point)
    }
    draw(redColor, ant._1)
  }

  // 0 - up, 1 - right, 2 - bottom, 3 - left

  val rules:Map[Int, Map[Int, (Point, Int)]] = Map(
    0 -> Map(0 -> ((1, 0), 1), 1 -> ((0, -1), 2), 2 -> ((-1, 0), 3), 3 -> ((0, 1), 0)),
    1 -> Map(0 -> ((-1, 0), 3), 3 -> ((0, -1), 2), 2 -> ((1, 0), 1), 1 -> ((0, 1), 0)))

  def main(args:Array[String]):Unit = {
    val boardSize = 11
    val gfx = emptyFrame(boardSize).getGraphics
    val draw = drawBoard(gfx, boardSize) _
    def simulation(ant:Ant, board:Seq[Int]):Unit = {
      draw(board, ant)
      Thread.sleep(250)
      val (currentPoint, currentDirection) = ant
      val index = pointToIndex(currentPoint, boardSize)
      val currentColor = board(index)
      val (nextPointDiff, nextDirection) = rules(currentColor)(currentDirection)
      val nextPoint = currentPoint + nextPointDiff
      simulation((nextPoint, nextDirection), board.updated(index, if(currentColor == 0) 1 else 0))
    }
    simulation((centerPoint(boardSize), 0), emptyBoard(boardSize))
  }
}
