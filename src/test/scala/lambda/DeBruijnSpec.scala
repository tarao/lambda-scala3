package lambda

object DeBruijnSpec {
  def debruijn1: DeBruijn.Of[Abs["x", Abs["y", Var["x"]]]] =
    DeBruijn.Abs("x", DeBruijn.Abs("y", DeBruijn.Var(2, "x")))

  def debruijn2: DeBruijn.Of[App[Abs["x", Abs["y", Var["x"]]], Var["b"]]] =
    DeBruijn.App(
      DeBruijn.Abs("x", DeBruijn.Abs("y", DeBruijn.Var(2, "x"))),
      DeBruijn.Var(0, "b"),
    )
}
