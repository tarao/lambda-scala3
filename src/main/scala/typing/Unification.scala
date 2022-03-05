package typing

import util.{:+:, HList, HNil}

trait Eq[T1 <: Type, T2 <: Type]

trait Unified[S <: HList, Ok <: Boolean]

type Unify[Eqn <: HList] <: Unified[_, _] = Eqn match {
  case HNil =>
    Unified[HNil, true]
  case Eq[Var[i], t] :+: rest =>
    Type.Equals[Var[i], t] match {
      case true => Unify[rest]
      case _    => Type.NotIn[i, t] match {
        case true => Unify[Subst.Eqn[Subst[Var[i], t] :+: HNil, rest]] match {
          case Unified[subst, ok] => Unified[Subst[Var[i], t] :+: subst, ok]
        }
        case _    => Unified[HNil, false]
      }
    }
  case Eq[Fun[s, t], Var[i]] :+: rest =>
    Unify[Eq[Var[i], Fun[s, t]] :+: rest]
  case Eq[Fun[s1, t1], Fun[s2, t2]] :+: rest =>
    Unify[Eq[s1, s2] :+: Eq[t1, t2] :+: rest]
  case Subst[s, t] :+: rest => Unify[Eq[s, t] :+: rest]
}
