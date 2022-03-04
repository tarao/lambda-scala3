package lambda

object TermSpec {
  summon[Show[Var["a"]] =:= "a"]

  summon[Show[Abs["a", Var["a"]]] =:= "λa.a"]

  summon[Show[App[Abs["a", Var["a"]], Var["b"]]] =:= "(λa.a) b"]

  summon[Show[Abs["x", Abs["y", Var["x"]]]] =:= "λxy.x"]

  summon[Show[Abs["x", Abs["y", Var["x"]]]] =:= "λxy.x"]

  summon[Show[Abs["x", Abs["y", Abs["z", App[App[Var["x"], Var["z"]], App[Var["y"], Var["z"]]]]]]] =:= "λxyz.x z (y z)"]

  summon[Show[App[App[Abs["x", Abs["y", Abs["z", App[App[Var["x"], Var["z"]], App[Var["y"], Var["z"]]]]]], Abs["x", Abs["y", Var["x"]]]], Abs["x", Abs["y", Var["x"]]]]] =:= "(λxyz.x z (y z)) (λxy.x) (λxy.x)"]
}
