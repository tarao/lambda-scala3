package lambda

import scala.compiletime.ops.{any, int}

// leftmost-outermost strategy
object Eval {
  type Shift[T <: DeBruijn.Term, I <: Int, D <: Int] <: DeBruijn.Term =
    T match {
      case DeBruijn.Var[i, v] =>
        DeBruijn.Var[int.>[i, D] match {
          case true  => int.+[i, I]
          case false => i
        }, v]
      case DeBruijn.Abs[v, t] =>
        DeBruijn.Abs[v, Shift[t, I, int.+[D, 1]]]
      case DeBruijn.App[t1, t2] =>
        DeBruijn.App[Shift[t1, I, D], Shift[t2, I, D]]
    }

  // T1[I/T2] (replace I in T1 by T2)
  type Subst[T1 <: DeBruijn.Term, T2 <: DeBruijn.Term, I <: Int] <: DeBruijn.Term =
    T1 match {
      case DeBruijn.Var[i, v] =>
        any.==[i, I] match {
          case true  => Shift[T2, int.+[i, -1], 0]
          case false =>
            DeBruijn.Var[int.>[i, I] match {
              case true  => int.+[i, -1]
              case false => i
            }, v]
        }
      case DeBruijn.Abs[v, t] =>
        DeBruijn.Abs[v, Subst[t, T2, int.+[I, 1]]]
      case DeBruijn.App[t1, t2] =>
        DeBruijn.App[Subst[t1, T2, I], Subst[t2, T2, I]]
    }


  // single-step beta-reduction
  type Eval1[T <: DeBruijn.Term] <: (DeBruijn.Term, Boolean) =
    T match {
      case DeBruijn.App[DeBruijn.Abs[v, t1], t2] => // beta-redex
        (Subst[t1, t2, 1], true)
      case DeBruijn.App[t1, t2] =>
        Eval1[t1] match {
          case (_, false) =>
            Eval1[t2] match {
              case (t3, true)  => (DeBruijn.App[t1, t3], true)
              case (t3, false) => (DeBruijn.App[t1, t3], false)
            }
          case (t3, true) => (DeBruijn.App[t3, t2], true)
        }
      case DeBruijn.Abs[v, t] =>
        Eval1[t] match {
          case (t2, true)  => (DeBruijn.Abs[v, t2], true)
          case (t2, false) => (DeBruijn.Abs[v, t2], false)
        }
      case _ => (T, false)
    }

  // multi-step beta-reduction
  type EvalM[T <: DeBruijn.Term] <: DeBruijn.Term =
    Eval1[T] match {
      case (t, true)  => EvalM[t]
      case (_, false) => T
    }
}
