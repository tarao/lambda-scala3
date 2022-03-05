package lambda

import scala.compiletime.ops.string

sealed trait Term
case class Var[V <: String](v: V) extends Term
case class Abs[V <: String, T <: Term](v: V, t: T) extends Term
case class App[T1 <: Term, T2 <: Term](t1: T1, t2: T2) extends Term

type Eval[T <: Term] <: Term =
  T match {
    case _ => Term.Of[Eval.EvalM[DeBruijn.Of[T]]]
  }

type Show[T <: Term] <: String =
  T match {
    case Var[v]      => v
    case Abs[_, _]   => string.+["λ", Show.AbsBody[T]]
    case App[t1, t2] => string.+[Show.AppL[t1], string.+[" ", Show.AppR[t2]]]
  }
object Show {
  type AbsBody[T <: Term] <: String =
    T match {
      case Var[_]    => string.+[".", Show[T]]
      case Abs[v, t] => string.+[v, AbsBody[t]]
      case App[_, _] => string.+[".", Show[T]]
    }

  type AppL[T <: Term] <: String =
    T match {
      case Var[_]      => Show[T]
      case Abs[_, _]   => string.+["(", string.+[Show[T], ")"]]
      case App[t1, t2] => Show[T]
    }

  type AppR[T <: Term] <: String =
    T match {
      case Var[_]    => Show[T]
      case Abs[_, _] => string.+["(", string.+[Show[T], ")"]]
      case App[_, _] => string.+["(", string.+[Show[T], ")"]]
    }
}

type ReadEvalPrint[Src <: String] <: String =
  Src match {
    case _ => Show[Eval[Parse[Src]]]
  }

object Term {
  type Of[T <: DeBruijn.Term] <: Term =
    T match {
      case DeBruijn.Var[_, v]   => Var[v]
      case DeBruijn.Abs[v, t]   => Abs[v, Of[t]]
      case DeBruijn.App[t1, t2] => App[Of[t1], Of[t2]]
    }
}
