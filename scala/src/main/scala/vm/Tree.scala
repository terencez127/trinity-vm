package vm

import scala.collection.mutable.ArrayBuffer
import vm.Tree._

object Tree {
  abstract class Node {
    def printValue()
  }

  class Sexpr extends Node {
    override def printValue() {}
  }

  class SexprList extends Sexpr {
    def this(sexprList: ArrayBuffer[Sexpr]) {
      this()
      this.sexprList = sexprList
    }

    def this(sexprs: Sexpr*) {
      this()
      sexprList = new ArrayBuffer[Sexpr]
      for (sexpr <- sexprs) {
        sexprList.append(sexpr)
      }
    }

    override def toString: String = {
      val builder: StringBuilder = new StringBuilder
      builder += '('
      for (i <- 0 to sexprList.size - 1) {
        builder ++= sexprList(i).toString + " "
      }

      if (sexprList.size > 0) {
        builder ++= sexprList(sexprList.size - 1).toString
      }

      builder += ')'
      builder.toString()
    }

    override def printValue {
    }

    var sexprList: ArrayBuffer[Sexpr] = null
  }

  class SexprIdent extends Sexpr {
    def this(value: String) {
      this()
      this.value = value
    }

    override def toString: String = {
      value
    }

    override def printValue {
      print(value)
    }

    var value: String = null
  }

  class SexprNumber extends Sexpr {
    def this(value: Int) {
      this()
      this.value = value
    }

    override def toString: String = {
      String.valueOf(value)
    }

    override def printValue {
      print(value)
    }

    var value: Int = 0
  }

  class SexprBoolean extends Sexpr {
    def this(value: Boolean) {
      this()
      this.value = value
    }

    override def toString: String = {
      value.toString
    }

    override def printValue {
      print(value)
    }

    var value: Boolean = false
  }
}

class Tree {
  def printTree(node: Node) {
    printRecursive(node, 0)
  }

  def printRecursive(node: Node, level: Int) {
    node match {
      case sList: SexprList =>
        printSpace(level)
        System.out.println("(")
        val list: ArrayBuffer[Sexpr] = sList.sexprList
        for (sexpr <- list) {
          printRecursive(sexpr, level + 1)
        }
        printSpace(level)
        System.out.println(")")
      case _ =>
        printSpace(level)
        System.out.println(node)
    }
  }

  def printSpace(level: Int) {
    for (i <- 0 to level) {
      System.out.print("    ")
    }
  }

  def main(args: Array[String]) {
    val t1: Sexpr = new SexprNumber(1)
    println(t1)
    val t2: Sexpr = new SexprList(new SexprIdent("+"))
    println(t2)
  }

}


