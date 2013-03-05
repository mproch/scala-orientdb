package scala.orientdb.utils

case class Lens[Whole, Part](get: Whole => Part, set: (Whole, Part) => Whole) {

  def mod(a:Whole, f: Part => Part) = set(a, f(get(a)))

  def compose[C](that: Lens[C, Whole]) = Lens[C, Part](
    c => get(that.get(c)),
    (c, b) => that.mod(c, set(_, b))
  )
}
