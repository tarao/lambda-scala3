package lambda

import scala.compiletime.ops.{boolean, int, string}
import scala.compiletime.ops.string.{Length, Matches, Substring}
import util.{:+:, HList, HNil}

type Parse[Src <: String] =
  Parser.ParseExp[Src] match {
    case ParsedTerm[t, rest] => rest match {
      case "" => t
      case _  => ParseError["EOF expected"]
    }
    case ParseError[s] => ParseError[s]
  }

sealed trait ParseResult
case class ParsedTerm[T <: Term, R <: String](term: T, rest: R) extends ParseResult
case class ParseError[R <: String](reason: R) extends ParseResult

@annotation.experimental // Length, Matches, Substring
object Parser {
  // e := λf    [ParseExp]
  //      a
  // f := x.e    [ParseAbs]
  //      xf
  // a := p      [ParseApp]
  //      a p
  // p := x      [ParsePrimary]
  //      (e)

  type ParseExp[Src <: String] <: ParseResult =
    SafeSubstring[Src, 0, 1] match {
      case "λ" => ParseAbs[HNil, Substring[Src, 1, Length[Src]]]
      case _    => ParseApp[Src]
    }

  type ParseAbs[Args <: HList, Src <: String] <: ParseResult =
    Matches[Src, "[a-z][.].*"] match {
      case true => ParseExp[Substring[Src, 2, Length[Src]]] match {
        case ParsedTerm[t, rest] => ParsedTerm[MakeAbs[Substring[Src, 0, 1] :+: Args, t], rest]
        case ParseError[s]       => ParseError[s]
      }
      case _ => Matches[SafeSubstring[Src, 0, 1], "[a-z]"] match {
        case true => ParseAbs[Substring[Src, 0, 1] :+: Args, Substring[Src, 1, Length[Src]]]
        case _    => SafeSubstring[Src, 0, 1] match {
          case "" => ParseError["unexpected EOF"]
          case _  => ParseError[string.+["unexpected token: ", Substring[Src, 0, 1]]]
        }
      }
    }

  type ParseApp[Src <: String] <: ParseResult =
    ParsePrimary[Src] match {
      case ParsedTerm[t, rest] => ParseApp1[t, rest]
      case ParseError[s]       => ParseError[s]
    }

  type ParseApp1[Prev <: Term, Src <: String] <: ParseResult =
    SafeSubstring[Src, 0, 1] match {
      case " " => ParsePrimary[Substring[Src, 1, Length[Src]]] match {
        case ParsedTerm[t, rest] => ParseApp1[App[Prev, t], rest]
        case ParseError[s]       => ParseError[s]
      }
      case _ => ParsedTerm[Prev, Src]
    }

  type ParsePrimary[Src <: String] <: ParseResult =
    Matches[SafeSubstring[Src, 0, 1], "[a-z]"] match {
      case true => ParsedTerm[Var[Substring[Src, 0, 1]], Substring[Src, 1, Length[Src]]]
      case _    => SafeSubstring[Src, 0, 1] match {
        case "(" => ParseExp[Substring[Src, 1, Length[Src]]] match {
          case ParsedTerm[t, rest] => Substring[rest, 0, 1] match {
            case ")" => ParsedTerm[t, Substring[rest, 1, Length[rest]]]
            case _   => ParseError["unclosed parenthesis"]
          }
          case ParseError[s] => ParseError[s]
        }
        case _ => ParseError["variable or parenthesis expected"]
      }
    }

  type MakeAbs[Args <: HList, T <: Term] <: Term =
    Args match {
      case v :+: rest => MakeAbs[rest, Abs[v, T]]
      case HNil       => T
    }

  type SafeSubstring[S <: String, IBeg <: Int, IEnd <: Int] <: String =
    boolean.&&[int.>=[Length[S], IBeg], int.>=[Length[S], IEnd]] match {
      case true => Substring[S, IBeg, IEnd]
      case _    => ""
    }
}
