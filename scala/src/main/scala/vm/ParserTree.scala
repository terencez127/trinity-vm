package vm

import scala.collection.mutable.ArrayBuffer
import vm.Tree.SexprList

class ParserTree {
  def this(tokens: ArrayBuffer[Token]) {
    this()
    this.tokens = tokens
    inputLength = this.tokens.size
    endOfTokens = false
    position = 0
    curToken = tokens.remove(0)
  }

  def tokenTake {
    position += 1
    curToken = tokens.remove(0)
    if (curToken.tokenType == Token.EOT) {
      endOfTokens = true
    }
  }

  def parse: SexprList = {
    var sexprList: Tree.SexprList = null
    sexprList = parseSexprList
    sexprList
  }

  def parseSexprList: Tree.SexprList = {
    val list: ArrayBuffer[Tree.Sexpr] = new ArrayBuffer[Tree.Sexpr]
    while (curToken.tokenType != Token.EOT && curToken.tokenType != Token.RPAREN) {
      if (curToken.tokenType == Token.LPAREN) {
        tokenTake
        list.append(parseSexprList)
        if (curToken.tokenType == Token.RPAREN) {
          tokenTake
        }
        else {
          throw new ParseError(curToken)
        }
      }
      else {
        list.append(parseSexpr)
      }
    }
    new Tree.SexprList(list)
  }

  def parseSexpr: Tree.Sexpr = {
    var sexpr: Tree.Sexpr = null
    val tType: Token.Type = curToken.tokenType
    val text: String = curToken.getText
    tType match {
      case Token.NUMBER =>
        sexpr = new Tree.SexprNumber(text.toInt)
      case Token.TRUE =>
        sexpr = new Tree.SexprBoolean(true)
      case Token.FALSE =>
        sexpr = new Tree.SexprBoolean(false)
      case Token.IDENT =>
        sexpr = new Tree.SexprIdent(text)
      case b_ =>
        throw new ParseError(curToken)
    }
    tokenTake
    sexpr
  }

  var tokens: ArrayBuffer[Token] = null
  var inputLength: Int = 0
  var endOfTokens: Boolean = false
  var position: Int = 0
  var curToken: Token = null
}