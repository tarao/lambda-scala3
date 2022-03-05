package typing

import scala.compiletime.ops.{any, int, string}
import util.{:+:, HList, HNil}

type Show[T <: Type] <: String = Show.AnyType[T, Show.VarMap[HNil, 0]] match {
  case Show.Str[str, _] => str
}

object Show {
  trait Str[+S <: String, +M <: VarMap[_, _]]

  type AnyType[T <: Type, M <: VarMap[_, _]] <: Str[String, VarMap[_, _]] = T match {
    case Var[i] => VarMapGetOrUpdate[i, M] match {
      case LookedUp[j, map] => Str[IntToString[j], map]
    }
    case Fun[s, t] =>
      FunL[s, M] match {
        case Str[str1, map1] =>
          AnyType[t, map1] match {
            case Str[str2, map2] =>
              Str[string.+[str1, string.+[" -> ", str2]], map2]
          }
      }
  }

  type FunL[T <: Type, M <: VarMap[_, _]] <: Str[String, VarMap[_, _]] = T match {
    case Fun[_, _] => AnyType[T, M] match {
      case Str[str, map] =>
        Str[string.+["(", string.+[str, ")"]], map]
    }
    case _ => AnyType[T, M]
  }

  trait ICons[A <: Int, B <: Int]
  trait VarMap[AssocList <: HList, Min <: Int]
  trait LookedUp[+I <: Int, +M <: VarMap[_, _]]

  type VarMapGetOrUpdate[I <: Int, M <: VarMap[_, _]] <: LookedUp[Int, VarMap[_, _]] = M match {
    case VarMap[map, min] => VarMapGet0[I, map] match {
      case ICons[_, i] => int.>=[i, 0] match {
        case true  => LookedUp[i, M]
        case false => LookedUp[min, VarMap[ICons[I, min] :+: map, int.+[min, 1]]]
      }
    }
  }

  type VarMapGet0[I <: Int, Map <: HList] <: ICons[_, _] = Map match {
    case ICons[i, j] :+: rest => any.==[i, I] match {
      case true  => ICons[i, j]
      case false => VarMapGet0[I, rest]
    }
    case HNil => ICons[I, -1]
  }

  type IntToString[I <: Int] <: String =
    int./[I, 26] match {
      case 0 => IntToString0[int.%[I, 26]]
      case _ => string.+[IntToString[int.+[int./[I, 26], -1]], IntToString0[int.%[I, 26]]]
    }

  type IntToString0[I <: Int] <: String = I match {
    case 0  => "a"
    case 1  => "b"
    case 2  => "c"
    case 3  => "d"
    case 4  => "e"
    case 5  => "f"
    case 6  => "g"
    case 7  => "h"
    case 8  => "i"
    case 9  => "j"
    case 10 => "k"
    case 11 => "l"
    case 12 => "m"
    case 13 => "n"
    case 14 => "o"
    case 15 => "p"
    case 16 => "q"
    case 17 => "r"
    case 18 => "s"
    case 19 => "t"
    case 20 => "u"
    case 21 => "v"
    case 22 => "w"
    case 23 => "x"
    case 24 => "y"
    case 25 => "z"
  }
}
