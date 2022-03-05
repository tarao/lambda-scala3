package typing

import lambda.Parse

object TypingSpec {
  import lambda.EvalSpec.{s, k, skk, _0, _1, _2, _3, _4, o, y, z}

  summon[Type.Check[Parse["x"]] =:= false]
  summon[Type.Check[o] =:= false]
  summon[Type.Check[y] =:= false]
  summon[Type.Check[z] =:= false]

  summon[Type.Check[Parse["λx.x"]] =:= true]
  summon[Type.Check[k] =:= true]
  summon[Type.Check[s] =:= true]
  summon[Type.Check[skk] =:= true]
  summon[Type.Check[_1] =:= true]
  summon[Type.Check[_2] =:= true]
  summon[Type.Check[_3] =:= true]
  summon[Type.Check[_4] =:= true]

  summon[Type.Infer[Parse["x"]] =:= None]
  summon[Type.Infer[o] =:= None]
  summon[Type.Infer[y] =:= None]
  summon[Type.Infer[z] =:= None]

  summon[Show[Type.Infer[Parse["λx.x"]]] =:= "a -> a"]
  summon[Show[Type.Infer[k]] =:= "a -> b -> a"]
  summon[Show[Type.Infer[s]] =:= "(a -> b -> c) -> (a -> b) -> a -> c"]
  summon[Show[Type.Infer[skk]] =:= "a -> a"]
  summon[Show[Type.Infer[_1]] =:= "(a -> b) -> a -> b"]
  summon[Show[Type.Infer[_2]] =:= "(a -> a) -> a -> a"]
  summon[Show[Type.Infer[_3]] =:= "(a -> a) -> a -> a"]
  summon[Show[Type.Infer[_4]] =:= "(a -> a) -> a -> a"]
}
