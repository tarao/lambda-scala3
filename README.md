Type-level lambda calculus in Scala 3
=====================================

This repository demonstrates compile-time lambda calculus parser, evalator, and type
checker in Scala 3.  It is heavily depending on [match types][] feature.

Components
----------

### Parser

`Parse[_]` returns a type representing an abstract syntax tree of the parsed term.

```scala
import lambda.{Parse, Var, Abs, App}

summon[Parse["λx.x"] =:= Abs["x", Var["x"]]]

summon[Parse["λxy.x"] =:= Abs["x", Abs["y", Var["x"]]]]

summon[Parse["x y"] =:= App[Var["x"], Var["y"]]]
```

### Evaluator

`Eval[_]` returns beta-normal form of the specified term.  The evaluation strategy is
leftmost-outermost.

```scala
import lambda.{Show, Eval, Parse}

summon[Show[Eval[Parse["(λxy.x) a b"]]] =:= "a"]
```

You can also use `ReadEvalPrint[_]` to `Parse` and `Show` together at once.

```scala
import lambda.ReadEvalPrint

summon[ReadEvalPrint["(λxy.x) a b"] =:= "a"]
```

### Type checker

`Type.Infer[_]` returns a (Scala) type representing an abstract syntax tree of the type
(of simply typed lambda calculus) of the specified term.

```scala
import lambda.Parse
import typing.{Show, Type}

summon[Show[Type.Infer[Parse["λx.x"]]] =:= "a -> a"]
```

You can also use `Type.Check[_]` (returns a boolean literal type) to determine whether a
term is typeable.

Related Work
------------

- [tarao/lambda-scala: Type level lambda calculus in Scala](https://github.com/tarao/lambda-scala)
  - Scala 2 implmentation of type-level lambda calculus
  - It has no type checker
- [About type-level lambda calculus in C++03 templates](https://tarao.hatenablog.com/entry/20111101/1320143278) (in Japanese)
  - [source code](https://gist.github.com/tarao/1330110)
  - lambda-scala3 is a translation from this implementation (except the parser)
- [About match types and compile-time string literals](https://xuwei-k.hatenablog.com/entry/2022/03/02/081401) by @xuwei-k (in Japanese)
  - [source code 1](https://gist.github.com/xuwei-k/dca46ea655c0a1666891901af80b6790)
  - [source code 2](https://gist.github.com/xuwei-k/521638aa17ebc839c8d8519bcdfdc7ae)
  - This taught me how to write a compile-time parser

[match types]: https://dotty.epfl.ch/docs/reference/new-types/match-types.html
