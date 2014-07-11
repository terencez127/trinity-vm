package vm

import scala.util.parsing.combinator._

class Imp extends JavaTokenParsers {
  def stats: Parser[String] = (
    "{" ~> rep1(stats) <~ "}" ^^ {
      case list =>
        if (list.size > 1)
          "(begin " + list.reduceLeft { (a, b) => a + " " + b} + ")"
        else list.reduceLeft { (a, b) => a + " " + b}
    }
      | "{" ~> rep1(stat) <~ "}" ^^ {
      case list => if (list.size > 1)
        "(begin " + list.reduceLeft { (a, b) => a + " " + b} + ")"
      else list.reduceLeft { (a, b) => a + " " + b}
    }
      | stat
    )

  def stat: Parser[String] = ifstat | define | declaration | func | boolstat

  def func: Parser[String] = expr


  def call: Parser[String] = ident ~ list ^^ {
    case (name ~ l) => "(" + name + " " + l + ")"
  }

  def expr: Parser[String] = term ~ rep("+" ~ term | "-" ~ term) ^^ {
    case (num ~ list) => list.foldLeft(num) {
      case (x, ("+" ~ y)) => "(+ " + x + " " + y + ")"
      case (x, ("-" ~ y)) => "(- " + x + " " + y + ")"
    }
  }

  def term: Parser[String] = factor ~ rep("*" ~ factor | "/" ~ factor) ^^ {
    case (num ~ list) => list.foldLeft(num) {
      case (x, ("*" ~ y)) => "(* " + x + " " + y + ")"
      case (x, ("/" ~ y)) => "(/ " + x + " " + y + ")"
    }
  }

  def factor: Parser[String] = (
    call
      | wholeNumber
      | ident
      | "(" ~> expr <~ ")"
    )

  def define: Parser[String] = "def" ~ ident ~ list ~ stats ^^ {
    case ("def" ~ a ~ b ~ c) => "(" + "define " + a + " (lambda " + "(" + b + ")" + " " + c + "))"
  }

  def list: Parser[String] = "(" ~> repsep(expr, ",") <~ ")" ^^ {
    case list => list.reduceLeft { (a, b) => a + " " + b}
  }

  def declaration: Parser[String] = "int" ~ ident ~ "=" ~ expr ^^ {
    case ("int" ~ name ~ "=" ~ value) => "(define " + name + " " + value + ")"
  }

  def boolstat: Parser[String] = expr ~ opt("<=" | ">=" | ">" | "<" | "==") ~ expr ^^ {
    case (a ~ Some("<=") ~ b) => "(<= " + a + " " + b + ")"
    case (a ~ Some(">=") ~ b) => "(>= " + a + " " + b + ")"
    case (a ~ Some(">") ~ b) => "(> " + a + " " + b + ")"
    case (a ~ Some("<") ~ b) => "(< " + a + " " + b + ")"
    case (a ~ Some("==") ~ b) => "(equal? " + a + " " + b + ")"
  }

  def ifstat: Parser[String] = "if" ~ "(" ~> boolstat ~ ")" ~ stats ~ ifalt ^^ {
    case (test ~ ")" ~ seq ~ alt) => "(if " + test + " " + seq + alt + ")"
  }

  def ifalt: Parser[String] = opt("else" ~> stats) ^^ {
    case Some(s) => " " + s
    case None => ""
  }
}

object Imp extends Imp {
  def main(args: Array[String]) {
    println("input : " + args(0))
    println(parseAll(stats, args(0)))
  }
}
