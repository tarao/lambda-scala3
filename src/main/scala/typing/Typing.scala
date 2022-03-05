package typing

sealed trait Type
case class Var[I <: Int](i: I) extends Type
case class Fun[S <: Type, T <: Type](s: S, t: T) extends Type
