package lambda

object Eval1Spec {
  def eval1: Eval.Eval1[DeBruijn.Of[App[Abs["x", Abs["y", Var["x"]]], Abs["y", Var["y"]]]]] =
    (DeBruijn.Abs("y", DeBruijn.Abs("y", DeBruijn.Var(1, "y"))), true)

  def eval2: Eval.Eval1[DeBruijn.Of[App[Abs["x", App[Var["x"], Abs["y", Var["x"]]]], Abs["y", Var["y"]]]]] =
    (DeBruijn.App(DeBruijn.Abs("y", DeBruijn.Var(1, "y")), DeBruijn.Abs("y", DeBruijn.Abs("y", DeBruijn.Var(1, "y")))), true)

  def eval3: Eval.Eval1[DeBruijn.Of[Abs["y", App[Abs["x", Abs["y", Var["x"]]], Abs["z", Var["y"]]]]]] =
    (DeBruijn.Abs("y", DeBruijn.Abs("y", DeBruijn.Abs("z", DeBruijn.Var(3, "y")))), true)

  def eval4: Eval.Eval1[DeBruijn.Of[Abs["z", App[Abs["x", App[Var["z"], Abs["y", Var["x"]]]], Abs["y", Var["y"]]]]]] =
    (DeBruijn.Abs("z", DeBruijn.App(DeBruijn.Var(1, "z"), DeBruijn.Abs("y", DeBruijn.Abs("y", DeBruijn.Var(1, "y"))))), true)
}

object EvalSpec {
  def eval1: Show[Eval[App[App[Abs["x", Abs["y", Abs["z", App[App[Var["x"], Var["z"]], App[Var["y"], Var["z"]]]]]], Abs["x", Abs["y", Var["x"]]]], Abs["x", Abs["y", Var["x"]]]]]] =
    "λz.z"
}
