package typing

object ShowSpec {
  summon[Show[Var[0]] =:= "a"]
  summon[Show[Var[1]] =:= "a"]
  summon[Show[Var[100]] =:= "a"]

  summon[Show[Fun[Var[1], Var[2]]] =:= "a -> b"]
  summon[Show[Fun[Var[1], Var[1]]] =:= "a -> a"]
  summon[Show[Fun[Var[1], Fun[Var[2], Var[3]]]] =:= "a -> b -> c"]
  summon[Show[Fun[Var[1], Fun[Var[2], Var[1]]]] =:= "a -> b -> a"]
  summon[Show[
    Fun[
      Fun[Var[1], Fun[Var[2], Var[3]]],
      Fun[
        Fun[Var[1], Var[2]],
        Fun[Var[1], Var[3]]
      ]
    ]
  ] =:= "(a -> b -> c) -> (a -> b) -> a -> c"]
}
