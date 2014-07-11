package vm

class ParseError extends Exception {
  def this(token: Token) {
    this()
    this.token = token
  }

  override def toString: String = {
    return "Parse Error at " + token.toString
  }

  var token: Token = null
}

