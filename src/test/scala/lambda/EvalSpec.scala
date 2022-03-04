package lambda

import scala.compiletime.ops.int

object Eval1Spec {
  summon[Eval.Eval1[DeBruijn.Of[App[Abs["x", Abs["y", Var["x"]]], Abs["y", Var["y"]]]]] =:=
    (DeBruijn.Abs["y", DeBruijn.Abs["y", DeBruijn.Var[1, "y"]]], true)]

  summon[Eval.Eval1[DeBruijn.Of[App[Abs["x", App[Var["x"], Abs["y", Var["x"]]]], Abs["y", Var["y"]]]]] =:=
    (DeBruijn.App[DeBruijn.Abs["y", DeBruijn.Var[1, "y"]], DeBruijn.Abs["y", DeBruijn.Abs["y", DeBruijn.Var[1, "y"]]]], true)]

  summon[Eval.Eval1[DeBruijn.Of[Abs["y", App[Abs["x", Abs["y", Var["x"]]], Abs["z", Var["y"]]]]]] =:=
    (DeBruijn.Abs["y", DeBruijn.Abs["y", DeBruijn.Abs["z", DeBruijn.Var[3, "y"]]]], true)]

  summon[Eval.Eval1[DeBruijn.Of[Abs["z", App[Abs["x", App[Var["z"], Abs["y", Var["x"]]]], Abs["y", Var["y"]]]]]] =:=
    (DeBruijn.Abs["z", DeBruijn.App[DeBruijn.Var[1, "z"], DeBruijn.Abs["y", DeBruijn.Abs["y", DeBruijn.Var[1, "y"]]]]], true)]
}

object EvalSpec {
  type s = Abs["x", Abs["y", Abs["z", App[App[Var["x"], Var["z"]], App[Var["y"], Var["z"]]]]]]
  type k = Abs["x", Abs["y", Var["x"]]]
  type ks = App[k, s]
  type skk = App[App[s, k], k]
  type skkski = App[App[s, App[App[s, ks], k]], skk]

  type yy = Abs["x", App[Var["f"], App[Var["x"], Var["x"]]]]
  type y = Abs["f", App[yy, yy]]
  type zz = Abs["x", App[Var["f"], Abs["y", App[App[Var["x"], Var["x"]], Var["y"]]]]]
  type z = Abs["f", App[zz, zz]]

  summon[Show[Eval[App[App[s, k], k]]] =:= "λz.z"]

  summon[Show[Eval[App[App[s, k], k]]] =:= "λz.z"]

  summon[Show[Eval[skkski]] =:= "λzz.z (z z)"]

  summon[Show[Eval[App[App[skkski, Var["a"]], Var["b"]]]] =:= "a (a b)"]

  type S[n <: Int] <: Term = n match {
    case 0 => Var["z"]
    case _ => App[Var["s"], S[int.+[n, -1]]]
  }

  type Church[n <: Int] <: Term = n match { case _ => Abs["s", Abs["z", S[n]]] }

  type _0 = Church[0]
  type _1 = Church[1]
  type _2 = Church[2]
  type _3 = Church[3]
  type _4 = Church[4]
  type _5 = Church[5]
  type _6 = Church[6]

  type t = k
  type f = Abs["x", Abs["y", Var["y"]]]
  type if0 = Abs["n", App[App[Var["n"], Abs["x", f]], t]]
  type mul = Abs["m", Abs["n", Abs["s", Abs["z", App[
    App[Var["n"], App[Var["m"], Var["s"]]],
    Var["z"]
  ]]]]]
  type pred = Abs["n", Abs["s", Abs["z", App[
    App[
      App[Var["n"], Abs["f", Abs["g", App[Var["g"], App[Var["f"], Var["s"]]]]]],
      Abs["x", Var["z"]]
    ],
    Abs["x", Var["x"]]
  ]]]]
  type fact = Abs["r", Abs["n",
    App[
      App[
        App[if0, Var["n"]],                // if 0 = n
        _1                                 // then 1
      ],
      App[                                 // otherwise
        App[mul, Var["n"]],                // n *
        App[Var["r"], App[pred, Var["n"]]] // fact(n-1)
      ]
    ]
  ]]

  summon[Show[Eval[App[App[App[if0, _0], Var["a"]], Var["b"]]]] =:= "a"]
  summon[Show[Eval[App[App[App[if0, _1], Var["a"]], Var["b"]]]] =:= "b"]
  summon[Show[Eval[App[App[mul, _2], _2]]] =:= Show[_4]]
  summon[Show[Eval[App[App[mul, _2], _3]]] =:= Show[_6]]
  summon[Show[Eval[App[App[y, fact], _3]]] =:= Show[_6]] // this takes long

  type o = Abs["a", App[Var["a"], Var["a"]]]

  summon[Show[Eval[App[App[k, Var["c"]], App[o, o]]]] =:= "c"] // the strategy is normalizing
}
