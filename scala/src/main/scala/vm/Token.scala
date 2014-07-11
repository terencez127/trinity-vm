package vm

import vm.Token.Type

object Token {
  class Type
  object EOT extends Type
  object IDENT extends Type
  object NUMBER extends Type
  object LPAREN extends Type
  object RPAREN extends Type
  object TRUE extends Type
  object FALSE extends Type
  object NONE extends Type

//  val EOT = "EOT"
//  val IDENT = "IDENT"
//  val NUMBER = "NUMBER"
//  val LPAREN = "LPAREN"
//  val RPAREN = "RPAREN"
//  val TRUE = "TRUE"
//  val FALSE = "FALSE"
//  val NONE = "NONE"
}

class Token {

  def this(pos: Int, tType: Type, str: String) {
    this()
    position = pos
    tokenType = tType
    text = str
  }

//  def getType: String = {
////    tokenType match {
////
////    }
//  }

  def getText: String = {
    text
  }

  override def toString: String = {
    "(" + position + ", " + tokenType + ", " + text + ")"
  }

  var position: Int = 0
  var tokenType: Type = null
  var text: String = null
}