package typing

import scala.compiletime.ops.any
import util.{:+:, HList, HNil}

object Environment {
  sealed trait Ty[Var <: String, T <: Type]

  type Lookup[Var <: String, Env <: HList] <: Type = Env match {
    case HNil              => None
    case Ty[v, t] :+: rest => any.==[v, Var] match {
      case true  => t
      case false => Lookup[Var, rest]
    }
  }
}
