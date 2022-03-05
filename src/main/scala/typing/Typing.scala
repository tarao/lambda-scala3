package typing

import scala.compiletime.ops.{any, boolean, int}
import util.{:+:, HList, HNil}

sealed trait Type
case class Var[I <: Int](i: I) extends Type
case class Fun[S <: Type, T <: Type](s: S, t: T) extends Type
case class None() extends Type

object Type {
  type Check[Term <: lambda.Term] <: Boolean = Infer[Term] match {
    case None => false
    case _    => true
  }

  type Infer[Term <: lambda.Term] <: Type = Infer0[HNil, Term, 0] match {
    case Typed[_, t, _] => t
  }

  trait Typed[S <: HList, T <: Type, Fresh <: Int]

  type Infer0[E <: HList, Term <: lambda.Term, Fresh <: Int] <: Typed[_, _, _] = Term match {
    case lambda.Var[v] =>
      Typed[HNil, Environment.Lookup[v, E], Fresh]
    case lambda.Abs[v, term] =>
      Infer0[Environment.Ty[v, Var[Fresh]] :+: E, term, int.+[Fresh, 1]] match {
        case Typed[subst, None, fresh] => Typed[subst, None, fresh]
        case Typed[subst, t, fresh]    =>
          Typed[subst, Fun[Subst.Ty[subst, Var[Fresh]], t], fresh]
      }
    case lambda.App[t1, t2] =>
      Infer0[E, t1, int.+[Fresh, 1]] match {
        case Typed[eq1, None, fresh1]  => Typed[eq1, None, fresh1]
        case Typed[eq1, type1, fresh1] =>
          Infer0[E, t2, fresh1] match {
            case Typed[eq2, None, fresh2]  => Typed[eq2, None, fresh2]
            case Typed[eq2, type2, fresh2] =>
              Unify[
                HList.Concat[eq1, HList.Concat[eq2, Eq[type1, Fun[type2, Var[Fresh]]] :+: HNil]]
              ] match {
                case Unified[subst, ok] => ok match {
                  case false => Typed[subst, None, fresh2]
                  case _     => Typed[subst, Subst.Ty[subst, Var[Fresh]], fresh2]
                }
              }
          }
      }
  }

  type Equals[T1 <: Type, T2 <: Type] <: Boolean = T1 match {
    case None => T2 match {
      case None => true
      case _    => false
    }
    case Var[i] => T2 match {
      case Var[j] => any.==[i, j]
      case _      => false
    }
    case Fun[s1, t1] => T2 match {
      case Fun[s2, t2] => boolean.&&[Eq[s1, s2], Eq[t1, t2]]
      case _           => false
    }
  }

  type NotIn[I <: Int, T <: Type] <: Boolean = T match {
    case Var[i]    => any.!=[I, i]
    case Fun[s, t] => boolean.&&[NotIn[I, s], NotIn[I, t]]
  }
}
