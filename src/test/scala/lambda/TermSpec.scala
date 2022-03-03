package lambda

object TermSpec {
  def show1: Show[Var["a"]] =
    "a"

  def show2: Show[Abs["a", Var["a"]]] =
    "λa.a"

  def show3: Show[App[Abs["a", Var["a"]], Var["b"]]] =
    "(λa.a) b"

  def show4: Show[Abs["x", Abs["y", Var["x"]]]] =
    "λxy.x"

  def show5: Show[Abs["x", Abs["y", Var["x"]]]] =
    "λxy.x"

  def show6: Show[Abs["x", Abs["y", Abs["z", App[App[Var["x"], Var["z"]], App[Var["y"], Var["z"]]]]]]] =
    "λxyz.x z (y z)"

  def show7: Show[App[App[Abs["x", Abs["y", Abs["z", App[App[Var["x"], Var["z"]], App[Var["y"], Var["z"]]]]]], Abs["x", Abs["y", Var["x"]]]], Abs["x", Abs["y", Var["x"]]]]] =
    "(λxyz.x z (y z)) (λxy.x) (λxy.x)"
}
