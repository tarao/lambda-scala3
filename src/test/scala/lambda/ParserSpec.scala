package lambda

object ParserSpec {
  summon[Parse["x"] =:= Var["x"]]

  summon[Parse["λx.x"] =:= Abs["x", Var["x"]]]

  summon[Parse["x y"] =:= App[Var["x"], Var["y"]]]

  summon[Parse["(x)"] =:= Var["x"]]

  summon[Parse["((x))"] =:= Var["x"]]

  summon[Parse["(λx.x)"] =:= Abs["x", Var["x"]]]

  summon[Parse["((λx.x))"] =:= Abs["x", Var["x"]]]

  summon[Parse["λx.(x)"] =:= Abs["x", Var["x"]]]

  summon[Parse["(λx.(x))"] =:= Abs["x", Var["x"]]]

  summon[Parse["(x y)"] =:= App[Var["x"], Var["y"]]]

  summon[Parse["x (y)"] =:= App[Var["x"], Var["y"]]]

  summon[Parse["(x) y"] =:= App[Var["x"], Var["y"]]]

  summon[Parse["(x (y))"] =:= App[Var["x"], Var["y"]]]

  summon[Parse["((x) y)"] =:= App[Var["x"], Var["y"]]]

  summon[Parse["λx.x y"] =:= Abs["x", App[Var["x"], Var["y"]]]]

  summon[Parse["λx.λy.x"] =:= Abs["x", Abs["y", Var["x"]]]]

  summon[Parse["λxy.x"] =:= Abs["x", Abs["y", Var["x"]]]]

  summon[Parse["λx.(λy.x)"] =:= Abs["x", Abs["y", Var["x"]]]]

  summon[Parse["x y z"] =:= App[App[Var["x"], Var["y"]], Var["z"]]]

  summon[Parse["x (y z)"] =:= App[Var["x"], App[Var["y"], Var["z"]]]]

  summon[Parse["λxyz.x z (y z)"] =:= Abs["x", Abs["y", Abs["z", App[App[Var["x"], Var["z"]], App[Var["y"], Var["z"]]]]]]]

  summon[Parse["λx.xy"] =:= ParseError["EOF expected"]]

  summon[Parse["λ"] =:= ParseError["unexpected EOF"]]

  summon[Parse["λ.x"] =:= ParseError["unexpected token: ."]]

  summon[Parse["λx.x λx.x"] =:= ParseError["variable or parenthesis expected"]]

  summon[Parse["(x."] =:= ParseError["unclosed parenthesis"]]

  summon[Parse["(x "] =:= ParseError["variable or parenthesis expected"]]
}
