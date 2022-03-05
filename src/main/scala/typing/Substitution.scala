package typing

import scala.compiletime.ops.any
import util.{:+:, HList, HNil}

sealed trait Subst[V <: Var[_], T <: Type]

object Subst {
  type Ty[S <: HList, T <: Type] <: Type = S match {
    case HNil => T
    case Subst[Var[j], t] :+: rest => T match {
      case Var[i] => any.==[i, j] match {
        case true => Ty[rest, t]
        case _    => Ty[rest, T]
      }
      case Fun[s, t] => Fun[Ty[S, s], Ty[S, t]]
    }
  }

  type Eqn[S <: HList, E <: HList] <: HList = E match {
    case HNil                   => HNil
    case Eq[t1, t2] :+: rest    => Eq[Ty[S, t1], Ty[S, t2]] :+: Eqn[S, rest]
    case Subst[t1, t2] :+: rest => Eqn[S, Eq[t1, t2] :+: rest]
  }
}
