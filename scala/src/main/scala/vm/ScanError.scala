package vm

class ScanError extends Exception {
  def this(token: Token) {
    this()
    this.token = token
  }

  override def toString: String = {
    "Scan Error at " + token.toString
  }

  var token: Token = null
}

