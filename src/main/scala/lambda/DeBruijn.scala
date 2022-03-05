package lambda

import util.{:+:, HList, HNil}

object DeBruijn {
  sealed trait Term
  case class Var[I <: Int, V <: String](i: I, v: V) extends Term
  case class Abs[V <: String, T <: Term](v: V, t: T) extends Term
  case class App[T1 <: Term, T2 <: Term](t1: T1, t2: T2) extends Term

  type Of[T <: lambda.Term] <: Term =
    T match {
      case _ => Of.Transform[T, HNil]
    }
  object Of {

    type Transform[T <: lambda.Term, L <: HList] <: Term =
      T match {
        case lambda.Var[v]      =>
          HList.Index[v, L] match {
            case (i, _) => Var[i, v]
          }
        case lambda.Abs[v, t]   => Abs[v, Transform[t, v :+: L]]
        case lambda.App[t1, t2] => App[Transform[t1, L], Transform[t2, L]]
      }
  }
}
