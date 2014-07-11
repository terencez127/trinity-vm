package vm

import vm.VirtualMachine._
import scala.collection.mutable.ArrayBuffer

object CodeGenTree {

  class Plus extends Closure {
    override def getByteCodes: ArrayBuffer[Instruction] = {
      val codes: ArrayBuffer[Instruction] = new ArrayBuffer[Instruction]
      codes.append(VirtualMachine.CreateInstruction(ADD, 0))
      codes.append(VirtualMachine.CreateInstruction(RETURN, 1))
      codes
    }
  }

  class Minus extends Closure {
    override def getByteCodes: ArrayBuffer[Instruction] = {
      val codes: ArrayBuffer[Instruction] = new ArrayBuffer[Instruction]
      codes.append(CreateInstruction(SUB, 0))
      codes.append(CreateInstruction(RETURN, 1))
      codes
    }
  }

  class Multiply extends Closure {
    override def getByteCodes: ArrayBuffer[Instruction] = {
      val codes: ArrayBuffer[Instruction] = new ArrayBuffer[Instruction]
      codes.append(VirtualMachine.CreateInstruction(MUL, 0))
      codes.append(VirtualMachine.CreateInstruction(RETURN, 1))
      codes
    }
  }

  class Divide extends Closure {
    override def getByteCodes: ArrayBuffer[Instruction] = {
      val codes: ArrayBuffer[Instruction] = new ArrayBuffer[Instruction]
      codes.append(VirtualMachine.CreateInstruction(DIV, 0))
      codes.append(VirtualMachine.CreateInstruction(RETURN, 1))
      codes
    }
  }


  class Greater extends Closure {
    override def getByteCodes: ArrayBuffer[Instruction] = {
      val codes: ArrayBuffer[Instruction] = new ArrayBuffer[Instruction]
      codes.append(VirtualMachine.CreateInstruction(GT, 0))
      codes.append(VirtualMachine.CreateInstruction(RETURN, 1))
      codes
    }
  }

  class Less extends Closure {
    override def getByteCodes: ArrayBuffer[Instruction] = {
      val codes: ArrayBuffer[Instruction] = new ArrayBuffer[Instruction]
      codes.append(VirtualMachine.CreateInstruction(GT, 1))
      codes.append(VirtualMachine.CreateInstruction(RETURN, 1))
      codes
    }
  }

  class Equal extends Closure {
    override def getByteCodes: ArrayBuffer[Instruction] = {
      val codes: ArrayBuffer[Instruction] = new ArrayBuffer[Instruction]
      codes.append(VirtualMachine.CreateInstruction(EQ, 0))
      codes.append(VirtualMachine.CreateInstruction(RETURN, 1))
      codes
    }
  }

  class GreaterEqual extends Closure {
    override def getByteCodes: ArrayBuffer[Instruction] = {
      val codes: ArrayBuffer[Instruction] = new ArrayBuffer[Instruction]
      codes.append(VirtualMachine.CreateInstruction(GE, 0))
      codes.append(VirtualMachine.CreateInstruction(RETURN, 1))
      codes
    }
  }

  class LessEqual extends Closure {
    override def getByteCodes: ArrayBuffer[Instruction] = {
      val codes: ArrayBuffer[Instruction] = new ArrayBuffer[Instruction]
      codes.append(CreateInstruction(GE, 1))
      codes.append(CreateInstruction(RETURN, 1))
      codes
    }
  }

  class Define extends Closure {
    override def getByteCodes: ArrayBuffer[Instruction] = {
      val codes: ArrayBuffer[Instruction] = new ArrayBuffer[Instruction]
      codes.append(CreateInstruction(STORE, 0))
      codes.append(CreateInstruction(RETURN, 0))
      codes
    }
  }

  class Conditional extends Closure {
    override def getByteCodes: ArrayBuffer[Instruction] = {
      val codes: ArrayBuffer[Instruction] = new ArrayBuffer[Instruction]
      codes.append(CreateInstruction(IFTRUE, 0))
      codes.append(CreateInstruction(RETURN, 0))
      codes
    }
  }

  class Lambda extends Closure {
  }

  class List extends Closure {
    override def getByteCodes: ArrayBuffer[Instruction] = {
      val codes: ArrayBuffer[Instruction] = new ArrayBuffer[Instruction]
      codes.append(CreateInstruction(LIST, 0))
      codes.append(CreateInstruction(RETURN, 1))
      codes
    }
  }

  class Begin extends Closure {
  }

  class Car extends Closure {
    override def getByteCodes: ArrayBuffer[Instruction] = {
      val codes: ArrayBuffer[Instruction] = new ArrayBuffer[Instruction]
      codes.append(CreateInstruction(CAR, 0))
      codes.append(CreateInstruction(RETURN, 1))
      codes
    }
  }

  class Cdr extends Closure {
    override def getByteCodes: ArrayBuffer[Instruction] = {
      val codes: ArrayBuffer[Instruction] = new ArrayBuffer[Instruction]
      codes.append(CreateInstruction(CDR, 0))
      codes.append(CreateInstruction(RETURN, 1))
      codes
    }
  }

  class Null extends Closure {
    override def getByteCodes: ArrayBuffer[Instruction] = {
      val codes: ArrayBuffer[Instruction] = new ArrayBuffer[Instruction]
      codes.append(CreateInstruction(NULL, 0))
      codes.append(CreateInstruction(RETURN, 1))
      codes
    }
  }

  class Callcc extends Closure {
    override def getByteCodes: ArrayBuffer[Instruction] = {
      val codes: ArrayBuffer[Instruction] = new ArrayBuffer[Instruction]
      codes.append(CreateInstruction(CCRETURN, 1))
      codes
    }
  }

}

class CodeGenTree {
  def evalExpr(node: Tree.Node, machine: VirtualMachine, preCall: Int): Tree.Node = {
    node match {
      case list: Tree.SexprList =>
        return evalSexprList(list, machine, preCall)
      case ident: Tree.SexprIdent =>
        machine.addInstruction(STOREL, ident.value, preCall)
      case boolVal: Tree.SexprBoolean =>
        machine.addInstruction(STOREL, if (boolVal.value) 1 else 0, preCall)
        return node
      case number: Tree.SexprNumber =>
        machine.addInstruction(STOREL, number.value, preCall)
        return node
      case _ =>
    }
    null
  }

  def evalSexprList(list: Tree.SexprList, machine: VirtualMachine, preCall: Int): Tree.Node = {
    if (list.sexprList.size != 0) {
      val first: Tree.Node = list.sexprList(0)
      first match {
        case ident: Tree.SexprIdent =>
          machine.vMap.get(ident.value) match {
            case Some(obj) =>
              if (obj != null && obj.isInstanceOf[Int]) {
                val closureTag: Int = obj.asInstanceOf[Int]
                if (machine.ifTag == closureTag) {
                  return ifStat(list, machine, preCall)
                }
                else if (machine.beginTag == closureTag) {
                  return beginStat(list, machine, preCall)
                }
                else if (machine.lambdaTag == closureTag) {
                  return lambdaStat(list, machine, preCall)
                }
                else if (machine.ccTag == closureTag) {
                  return ContinuationStat(list, machine, preCall)
                }
              }
            case None =>
          }

          val curCall: Int = machine.newCall
          machine.addInstruction(STOREL, ident.value, curCall)

          for (i <- 1 to list.sexprList.size - 1) {
            evalExpr(list.sexprList(i), machine, curCall)
          }

          machine.addInstruction(CALL, curCall, preCall)
          null
        case _ => if (first.isInstanceOf[Tree.SexprBoolean] || first.isInstanceOf[Tree.SexprNumber]) {
          return evalExpr(first, machine, preCall)
        }
        else first match {
          case list1: Tree.SexprList =>
            return evalSexprList(list1, machine, preCall)
        }
      }
    }

    null
  }

  private def ifStat(list: Tree.SexprList, machine: VirtualMachine, preCall: Int): Tree.Node = {
    val test: Tree.Node = list.sexprList(1)
    val conseq: Tree.Node = list.sexprList(2)
    var alt: Tree.Node = null
    if (list.sexprList.size == 4) {
      alt = list.sexprList(3)
    }
    evalExpr(test, machine, preCall)
    val insIndex: Int = machine.addInstruction(IFTRUE, 0, 0)
    val conseqIndex: Int = machine.icount
    evalExpr(conseq, machine, preCall)
    val gotoIndex: Int = machine.addInstruction(GOTO, 0)
    val altIndex: Int = machine.icount
    machine.updateInsArg(insIndex, conseqIndex, altIndex)
    if (alt != null) {
      evalExpr(alt, machine, preCall)
    }
    machine.updateInsArg(gotoIndex, machine.icount)
    null
  }

  private def beginStat(list: Tree.SexprList, machine: VirtualMachine, preCall: Int): Tree.Node = {
    val size: Int = list.sexprList.size
    if (size > 0) {
      val curCall: Int = machine.newCall
      var node: Tree.Node = null
      for (i <- 1 to size - 1) {
        node = list.sexprList(i)
        if (node != null) {
          evalExpr(node, machine, curCall)
        }
      }

      node = list.sexprList(size - 1)
      if (node != null) {
        return evalExpr(node, machine, curCall)
      }
    }
    null
  }

  private def lambdaStat(list: Tree.SexprList, machine: VirtualMachine, preCall: Int): Tree.Node = {
    val closureMachine: VirtualMachine = new VirtualMachine(machine)
    closureMachine.ip = closureMachine.icount
    val args: Tree.Node = list.sexprList(1)
    val body: Tree.Node = list.sexprList(2)
    args match {
      case list1: Tree.SexprList =>
        val start: Int = closureMachine.icount
        for (sexpr <- list1.sexprList) {
          closureMachine.addString(sexpr.asInstanceOf[Tree.SexprIdent].value)
          closureMachine.code.insert(start, CreateInstruction(BIND, sexpr.asInstanceOf[Tree.SexprIdent].value))
          closureMachine.icount = closureMachine.icount + 1
          closureMachine.ccRetVar = sexpr.asInstanceOf[Tree.SexprIdent].value
        }
      case _ =>
        val name: String = args.asInstanceOf[Tree.SexprIdent].value
        closureMachine.addString(name)
        closureMachine.code.append(CreateInstruction(BIND, args.asInstanceOf[Tree.SexprIdent].value))
        closureMachine.icount = closureMachine.icount + 1
        closureMachine.ccRetVar = name
    }
    evalExpr(body, closureMachine, preCall)
    machine.addProcedure(null, list.hashCode, new ArrayBuffer[Instruction])
    val curCall: Int = closureMachine.callp - 1
    VirtualMachine.closureMap.put(list.hashCode, new ClosureEnv(closureMachine, curCall, preCall))
    machine.addInstruction(STOREL, list.hashCode, preCall)
    null
  }

  private def ContinuationStat(list: Tree.SexprList, machine: VirtualMachine, preCall: Int): Tree.Node = {
    val lambda: Tree.Node = list.sexprList(1)
    val curCall: Int = machine.newCall
    evalExpr(lambda, machine, curCall)
    val ccIns: Int = machine.addInstruction(CALLCC, 0, curCall, preCall)
    machine.addInstruction(CALL, curCall, preCall)
    machine.addInstruction(CCRETURN, curCall, preCall)
    machine.updateInsArg(ccIns, machine.ip, curCall, preCall)
    null
  }
}