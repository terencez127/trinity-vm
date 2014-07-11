package vm

import scala.io.Source
import scala.collection.mutable.ArrayBuffer
import vm.VirtualMachine._


object Main {
  def main(args: Array[String]) {
    val IMPERATIVE = 0
    val FUNCTIONAL = 1
    val BYTECODE = 3

    var mode: Int = 0

    init(machine)
    val impParser: Imp = new Imp

    if (args.length > 0) {
      //        val sc: Scanner = new Scanner(new FileReader(args(0)))
      val file = Source.fromFile(args(0))
      val it = file.getLines()
      while (it.hasNext) {
        eval(it.next())
      }
    }
    else {
      var rawCommand, command = ""
      while (true) {
        try {
          System.out.print("input# ")
          val queryPattern = "query \\d+".r
          rawCommand = Console.readLine().trim
          rawCommand match {
            case "list" => {
              println("Avaiable commands:")
              println("States related: record, stop, query, reset\n")
              println("Mode changing: functional, imp, bytecode\n")
              println("Language related:")
              mode match {
                case IMPERATIVE => {
                  println("""
Arthmetic: 1+2,  a*b
Function Declaration: 	def add(a, b) {a + b}
Assignment: 			int a = 3
Function call: 			add(a, 4)
Conditional:			if (a > 10) {a + 3} else {a -3}""")
          }

                case BYTECODE => {
                  println("PUSH, ADD, SUB, MUL, DIV")
                }

                case _  =>
              }
            }
            case "record" => {
              machine.setRecord(true)
              println("State recording started.")
            }
            case "stop" => {
              machine.setRecord(false)
              println("State recording stopped. " + recordedStates.size + " states have been stored.")
            }
            case "reset" => {
              machine.resetRecord()
              println("Stored states have been cleared.")
            }
            case "functional" => mode = FUNCTIONAL; println("Functional mode!")
            case "bytecode" => mode = BYTECODE; println("Bytecode mode!")
            case "imp" => mode = IMPERATIVE; println("Imperative mode!")
            case queryPattern() => {
              "\\d+".r.findFirstIn(rawCommand) match {
                case Some(num) => {
                  println(recordedStates(num.toInt).toString)
                }
                case None =>
              }
            }
            case _ => {
              mode match {
                case IMPERATIVE => {
                  command = impParser.parseAll(impParser.stats, rawCommand).get
                  println("Scheme code: " + command)
                  "Result: " + eval(command)
                }
                case FUNCTIONAL => eval(rawCommand)
                case BYTECODE => {
                  val strs: Array[String] = rawCommand.split(" ")
                  strs(0) match {
                    case "push" => machine.addInstruction(PUSH, strs(1).toInt)
                    case "add" => machine.addInstruction(ADD)
                    case "sub" => machine.addInstruction(SUB)
                    case "mul" => machine.addInstruction(MUL)
                    case "div" => machine.addInstruction(DIV)
                  }
                  println(machine.execute)
                }
              }
            }
          }
        } catch {
          case e: Exception => e.printStackTrace()
        }
      }
    }
    System.exit(0)
  }

  private def eval(content: String) {
    var scanner: Scanner = null
    var tokens: ArrayBuffer[Token] = null
    var parserTree: ParserTree = null
    var sexprs: Tree.SexprList = null
    scanner = new Scanner(content)
    try {
      tokens = scanner.scan
    }
    catch {
      case e: ScanError => {
        System.out.println("Scan error: " + e.toString)
        return
      }
    }
    parserTree = new ParserTree(tokens)
    try {
      sexprs = parserTree.parse
    }
    catch {
      case e: ParseError => {
        System.out.println(e)
      }
    }
    val ret: Tree.Node = null
    codeGenTree.evalSexprList(sexprs, machine, 0)
    println(machine.execute)
  }

  private def addClosure(name: String, closure: Closure, paramCount: Int, m: VirtualMachine) {
    m.ip = m.addProcedure(name, closure.hashCode, closure.getByteCodes)
  }

  private def init(m: VirtualMachine) {
    val plus: Closure = new CodeGenTree.Plus
    addClosure("+", plus, 2, machine)
    val minus: Closure = new CodeGenTree.Minus
    addClosure("-", minus, 2, machine)
    val multiply: Closure = new CodeGenTree.Multiply
    addClosure("*", multiply, 2, machine)
    val divide: Closure = new CodeGenTree.Divide
    addClosure("/", divide, 2, machine)
    val greater: Closure = new CodeGenTree.Greater
    addClosure(">", greater, 2, machine)
    val less: Closure = new CodeGenTree.Less
    addClosure("<", less, 2, machine)
    val equal: Closure = new CodeGenTree.Equal
    addClosure("equal?", equal, 2, machine)
    val greaterEqual: Closure = new CodeGenTree.GreaterEqual
    addClosure(">=", greaterEqual, 2, machine)
    val lessEqual: Closure = new CodeGenTree.LessEqual
    addClosure("<=", lessEqual, 2, machine)
    val conditional: Closure = new CodeGenTree.Conditional
    addClosure("if", conditional, 3, machine)
    val define: Closure = new CodeGenTree.Define
    addClosure("define", define, 2, machine)
    val closureList: Closure = new CodeGenTree.List
    addClosure("list", closureList, 1, machine)
    val begin: Closure = new CodeGenTree.Begin
    addClosure("begin", begin, 1, machine)
    val lambda: Closure = new CodeGenTree.Lambda
    addClosure("lambda", lambda, 2, machine)
    val car: Closure = new CodeGenTree.Car
    addClosure("car", car, 1, machine)
    val cdr: Closure = new CodeGenTree.Cdr
    addClosure("cdr", cdr, 1, machine)
    val nullClosure: Closure = new CodeGenTree.Null
    addClosure("null?", nullClosure, 1, machine)
    val ccClosure: Closure = new CodeGenTree.Callcc
    addClosure("callcc", ccClosure, 1, machine)
    machine.ifTag = machine.vMap("if").asInstanceOf[Int]
    machine.beginTag = machine.vMap("begin").asInstanceOf[Int]
    machine.lambdaTag = machine.vMap("lambda").asInstanceOf[Int]
    machine.ccTag = machine.vMap("callcc").asInstanceOf[Int]
  }

  var codeGenTree: CodeGenTree = new CodeGenTree
  var machine: VirtualMachine = new VirtualMachine()
}


