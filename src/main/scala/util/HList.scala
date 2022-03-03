package util

import scala.compiletime.ops.{any, int}

sealed trait HList {
  def :+:[T](t: T): T :+: this.type = new :+:(t, this)
}
object HList {
  type Index[A, L <: HList] <: (Int, Boolean) =
    L match {
      case h :+: t =>
        any.==[h, A] match {
          case true  => (1, true)
          case false =>
            Index[A, t] match {
              case (i, true)  => (int.+[i, 1], true)
              case (_, false) => (0, false)
            }
        }
      case HNil => (0, false)
    }
}

sealed trait HNil extends HList
case object HNil extends HNil

final case class :+:[H, +T <: HList](h: H, t: T) extends HList
