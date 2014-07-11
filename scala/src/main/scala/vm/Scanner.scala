package vm

import scala.collection.mutable.ArrayBuffer

class Scanner {
  def this(inputString: String) {
    this()
    this.inputString = inputString
    inputLength = inputString.length
    endOfText = false
    position = 0
    curChar = inputString.charAt(position)
  }

  def charTake {
    position += 1
    if (position < inputLength) {
      curChar = inputString.charAt(position)
    }
    else {
      curChar = 0
      endOfText = true
    }
  }

  def scan: ArrayBuffer[Token] = {
    var token: Token = null
    val tokens: ArrayBuffer[Token] = new ArrayBuffer[Token]
    val tokenStrs: Array[String] = inputString.replace("(", " ( ").replace(")", " ) ").trim.split("\\s")
    if (tokenStrs == null || tokenStrs.length == 0) {
      throw new ScanError(new Token(position, Token.NONE, ""))
    }
    for (tokenStr <- tokenStrs) {
      if (tokenStr != "") {
        token = scanToken(tokenStr)
        tokens.append(token)
      }
    }
    tokens.append(new Token(position, Token.EOT, "EOT"))
    tokens
  }

  def scanToken(str: String): Token = {
    val pattern  = "[+-]?\\d+".r
    var token: Token = null
    str match {
      case pattern() => token = new Token(position, Token.NUMBER, str)
      case "(" => token = new Token(position, Token.LPAREN, str)
      case ")" => token = new Token(position, Token.RPAREN, str)
      case "#t" => token = new Token(position, Token.TRUE, str)
      case "#f"=> token = new Token(position, Token.FALSE, str)
      case _ => token = new Token(position, Token.IDENT, str)
    }

    token
  }


  var inputString: String = null
  var inputLength: Int = 0
  var endOfText: Boolean = false
  var position: Int = 0
  var curChar: Char = 0

  def printTokens(tokens: ArrayBuffer[Token]) {
    for (t <- tokens) {
      System.out.println(t)
    }
  }
}