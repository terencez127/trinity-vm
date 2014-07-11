package vm

import scala.collection.mutable.ArrayBuffer
import vm.VirtualMachine.{CallEnv, Instruction, recordFlag, recordedStates}
import scala.util.control.Breaks.{break, breakable}
import scala.collection.mutable

object VirtualMachine {
  // Static closure runtime map for convenience, need a better way to do it.
  var closureMap: mutable.HashMap[Int, ClosureEnv] = new mutable.HashMap[Int, ClosureEnv]()

  // Basically used as enum variable, can't figure out how to use enumeration class properly.
  class Opcode()
  object PUSH extends Opcode {override def toString: String = "PUSH"}
  object ADD extends Opcode {override def toString: String = "ADD"}
  object SUB extends Opcode {override def toString: String = "SUB"}
  object MUL extends Opcode {override def toString: String = "MUL"}
  object DIV extends Opcode {override def toString: String = "DIV"}
  object GT extends Opcode {override def toString: String = "GT"}
  object GE extends Opcode {override def toString: String = "GE"}
  object EQ extends Opcode {override def toString: String = "EQ"}
  object GOTO extends Opcode {override def toString: String = "GOTO"}
  object IFTRUE extends Opcode {override def toString: String = "IFTRUE"}
  object CALLCC extends Opcode {override def toString: String = "CALLCC"}
  object CCRETURN extends Opcode {override def toString: String = "CCRETURN"}
  object CALL extends Opcode {override def toString: String = "CALL"}
  object RETURN extends Opcode {override def toString: String = "RETURN"}
  object LIST extends Opcode {override def toString: String = "LIST"}
  object NULL extends Opcode {override def toString: String = "NULL"}
  object CAR extends Opcode {override def toString: String = "CAR"}
  object CDR extends Opcode {override def toString: String = "CDR"}
  object STORE extends Opcode {override def toString: String = "STORE"}
  object LOAD extends Opcode {override def toString: String = "LOAD"}
  object STOREL extends Opcode {override def toString: String = "STOREL"}
  object BIND extends Opcode {override def toString: String = "BIND"}


  var recordFlag: Boolean = false
  val recordedStates: ArrayBuffer[VirtualMachine] = new ArrayBuffer[VirtualMachine]()

  /**
   * Store environment variables of a method call.
   */
  class CallEnv {
    def this(cIndex: Int, sIndex: Int, stack: ArrayBuffer[AnyRef], curCall: Int, preCall: Int) {
      this()
      codeIndex = cIndex
      stackIndex = sIndex
      curCallIndex = curCall
      preCallIndex = preCall
    }

    var codeIndex: Int = 0                    // ip value before the call
    var stackIndex: Int = 0                   // sp value before the call
    var curCallIndex: Int = 0                 // Index of ongoing method call in the method call stack
    var preCallIndex: Int = 0                 // index of outer method call in the method call stack
  }

  /**
   * Store the environment variables of a lambda call
   */
  class ClosureEnv {
    def this(m: VirtualMachine, curCall: Int, preCall: Int) {
      this()
      curCallIndex = curCall
      preCallIndex = preCall
      machine = m
    }

    var curCallIndex: Int = 0           // Index of ongoing method call in the method call stack
    var preCallIndex: Int = 0           // index of outer method call in the method call stack
    var machine: VirtualMachine = null  // Machine state when the closure was created
  }

  /**
   * Machine instruction class
   */
  class Instruction {
    def this(opcode: Opcode, arg: ArrayBuffer[AnyRef]) {
      this()
      this.opcode = opcode
      this.args = arg
    }

    var opcode: Opcode = null
    var args: ArrayBuffer[AnyRef] = null  //  Arguments of the instruction
  }

  /**
   * Static method to create a new instruction
   * @param opcode  the opcode
   * @param args  the arugments
   * @return a created instruction instance
   */
  def CreateInstruction(opcode: Opcode, args: Any*): Instruction = {
    val argList: ArrayBuffer[AnyRef] = new ArrayBuffer[AnyRef]
    for (arg <- args) {
      argList.append(arg.asInstanceOf[AnyRef])
    }

    new Instruction(opcode, argList)
  }

  def main(args: Array[String]) {
    val m = new VirtualMachine()

    val codes: ArrayBuffer[VirtualMachine.Instruction] = new ArrayBuffer()
    codes.append(CreateInstruction(ADD))
    codes.append(CreateInstruction(RETURN, 1))
    m.ip = m.addProcedure("+", codes.hashCode(), codes)

    // Test case 1: (+ 1 (+ 2 3))

//    var call = m.newCall
//    m.addInstruction(STOREL, "+", call)
//    m.addInstruction(STOREL, 1, call)
//    call = m.newCall
//    m.addInstruction(STOREL, "+", call)
//    m.addInstruction(STOREL, 2, call)
//    m.addInstruction(STOREL, 3, call)
//    m.addInstruction(CALL, call, call - 1)
//    m.addInstruction(CALL, call - 1, call - 2)

    // Test case 2: (+ 1 2)
    m.addInstruction(PUSH, 1)
    m.addInstruction(PUSH, 2)
    m.addInstruction(ADD)

    println(m.execute)
  }
}

class VirtualMachine {
  //  Stores the actual keys of predefined procedures, needed to get the procedure's starting line number in the code list
  var ifTag, beginTag, lambdaTag, ccTag: Int = 0

  //  Instruction list
  var code: ArrayBuffer[Instruction] = ArrayBuffer()

  //  Global data stack
  var stack: ArrayBuffer[AnyRef] = ArrayBuffer()

  //  Method call stack, stores necessary environment variables for each method call
  var callStack: ArrayBuffer[CallEnv] = ArrayBuffer()

  //  Method call stack pointer
  var callp: Int = 1

  //  Stack of method call data stacks, stores temporary data stack for each call
  var argStack: ArrayBuffer[ArrayBuffer[AnyRef]] = ArrayBuffer()

  // Data stack for the current call, starts with a default global stack
  var localArgs: ArrayBuffer[AnyRef] = ArrayBuffer()
  argStack.append(localArgs)

  //  Label list, stores label strings
  var strs: ArrayBuffer[String] = ArrayBuffer()

  // Variable map, [variable_name, actual_data/procedure_internal_tag], a variable can be data or procedure,
  // the internal tag is for getting a procedure's starting line number from pMap
  var vMap: mutable.HashMap[String, AnyRef] = mutable.HashMap()

  //  Procedure map, [procedure_internal_tag, starting_line_number]
  var pMap: mutable.HashMap[Int, Int] = mutable.HashMap()

  //  Reference of outer vMap, used for binding continuation parameter inside a closure
  var vBindMap: mutable.HashMap[String, AnyRef] = null

  //  Stack of machine states for nested closure calls
  var closureEnvStack: ArrayBuffer[VirtualMachine] = ArrayBuffer()

  //  Flag for continuation
  var callCc: Boolean = false

  //  The parameter that terminates the current continuation
  var ccRetVar: String = null

  //  Stack of machine states for nested continuation calls
  var ccStack: ArrayBuffer[VirtualMachine] = ArrayBuffer()

  // Stack pointer
  var sp: Int = 0

  // Instruction pointer
  var ip: Int = 0

  //  Total number of instructions
  var icount: Int = 0

  //  String representation of a instruction, used for printing out execution result of each instruction
  var retValue: AnyRef = null

  def this(m: VirtualMachine) {
    this()
    code = m.code.clone()
    stack = m.stack.clone()
    callStack = m.callStack.clone()
    closureEnvStack = m.closureEnvStack.clone()
    argStack = new ArrayBuffer[ArrayBuffer[AnyRef]]

    for (list <- m.argStack) {
      argStack.append(list.clone())
    }
    localArgs = m.localArgs.clone()
    strs = m.strs.clone()
    callp = m.callp
    vMap = m.vMap.clone()
    pMap = m.pMap.clone()
    sp = m.sp
    ip = m.ip
    icount = m.icount
    ifTag = m.ifTag
    beginTag = m.beginTag
    lambdaTag = m.lambdaTag
    ccTag = m.ccTag
    ccStack = m.ccStack.clone()
    retValue = m.retValue
  }

  /**
   * Update arguments of a instruction
   * @param index index of the instruction in the instruction list
   * @param args  new arguments
   */
  def updateInsArg(index: Int, args: Int*) {
    val ins: Instruction = code(index)
    ins.args.clear()
    for (arg <- args) {
      ins.args.append(arg.asInstanceOf[AnyRef])
    }
  }

  /**
   * Add instruction to the machine instruction list
   * @param opcode
   * @param args
   * @return
   */
  def addInstruction(opcode: VirtualMachine.Opcode, args: Any*): Int = {
    val argList: ArrayBuffer[AnyRef] = new ArrayBuffer[AnyRef]
    for (arg <- args) {
      argList.append(arg.asInstanceOf[AnyRef])
    }
    val inst: Instruction = new Instruction(opcode, argList)
    code.append(inst)
    icount = icount + 1
    icount - 1
  }

  def setRecord(enable: Boolean) = {
    recordFlag = enable
  }

  def resetRecord() {
    recordFlag = false
    recordedStates.clear()
  }

  def execute: String = {
    breakable {
      while (true) {
        while (ip < icount) {
          executeInstruction()
          if (recordFlag) {
            recordedStates.append(new VirtualMachine(this))
          }
        }

        if (!closureEnvStack.isEmpty) {
          restore(stack(sp - 1).asInstanceOf[Int])
        }
        else {
          break()
        }
      }
    }
    if (retValue == null) {
      val rootStack: ArrayBuffer[AnyRef] = argStack(0)
      retValue = rootStack(rootStack.size - 1).toString
    }
    argStack.clear()
    argStack.append(new ArrayBuffer[AnyRef])
    localArgs.clear()
    callp = 1

    if (recordFlag) {
      "Recorded States: " + recordedStates.size + "\n" + retValue.toString
    } else {
      retValue.toString
    }
  }

  def executeInstruction() {
    val inst: Instruction = code(ip)
    inst.opcode match {
      case VirtualMachine.PUSH => executePush(inst.args(0).asInstanceOf[Int])
      case VirtualMachine.CCRETURN => executeCcReturn(inst.args(0).asInstanceOf[Int])
      case VirtualMachine.ADD => executeAdd()
      case VirtualMachine.SUB => executeSub()
      case VirtualMachine.MUL => executeMul()
      case VirtualMachine.DIV => executeDiv()
      case VirtualMachine.GT => executeGt(inst.args(0).asInstanceOf[Int])
      case VirtualMachine.GE => executeGe(inst.args(0).asInstanceOf[Int])
      case VirtualMachine.EQ => executeEq()
      case VirtualMachine.CALL => executeCall(inst.args(0).asInstanceOf[Int], inst.args(1).asInstanceOf[Int])
      case VirtualMachine.RETURN => executeReturn(inst.args(0).asInstanceOf[Int])
      case VirtualMachine.BIND => executeBind(inst.args(0))
      case VirtualMachine.STORE => executeStore(inst.args(0).asInstanceOf[Int])
      case VirtualMachine.LOAD => executeLoad(inst.args(0).asInstanceOf[Int])
      case VirtualMachine.STOREL => executeStoreL(inst.args(0), inst.args(1).asInstanceOf[Int])
      case VirtualMachine.GOTO => executeGoto(inst.args(0).asInstanceOf[Int])
      case VirtualMachine.IFTRUE => executeIfTrue(inst.args(0).asInstanceOf[Int], inst.args(1).asInstanceOf[Int])
      case VirtualMachine.LIST => executeList()
      case VirtualMachine.CAR => executeCar()
      case VirtualMachine.CDR => executeCdr()
      case VirtualMachine.NULL => executeNull()
      case VirtualMachine.CALLCC => executeCallcc(inst.args(0).asInstanceOf[Int], inst.args(1).asInstanceOf[Int], inst.args(2).asInstanceOf[Int])
    }
  }

  def executePush(arg: Int) {
    stack.append(arg.asInstanceOf[AnyRef])
    sp = sp + 1
    ip = ip + 1
    retValue = arg.toString
  }

  def executeIfTrue(conseq: Int, alt: Int) {
    val v: Int = localArgs.remove(localArgs.size - 1).asInstanceOf[Int]
    if (v == 0) {
      ip = alt
    }
    else {
      ip = conseq
    }
  }

  def executeList() {
    val list: ArrayBuffer[AnyRef] = new ArrayBuffer[AnyRef]
    val preSp: Int = callStack(callp - 2).stackIndex
    while (sp != preSp) {
      sp -= 1
      list.insert(0, stack.remove(sp))
    }
    stack.append(list)
    sp += 1
    ip += 1
  }

  def executeCar() {
    sp -= 1
    val list: ArrayBuffer[AnyRef] = ensureList(stack.remove(sp))
    stack.append(list(0))
    sp += 1
    ip += 1
  }

  def executeCdr() {
    sp -= 1
    val list: ArrayBuffer[AnyRef] = ensureList(stack.remove(sp))
    stack.append(list.slice(1, list.size))
    sp += 1
    ip += 1
  }

  def executeNull() {
    sp -= 1
    val list: ArrayBuffer[AnyRef] = ensureList(stack.remove(sp))
    stack.append((if (list.size > 0) 0 else 1).asInstanceOf[AnyRef])
    sp += 1
    ip += 1
  }

  def executeStore(arg: Int) {
    sp -= 1
    var v: AnyRef = stack.remove(sp)
    sp -= 1
    val k: AnyRef = stack.remove(sp)
    v match {
      case key: String if vMap.contains(key) =>
        v = vMap(key)
      case _ =>
    }

    vMap.put(k.asInstanceOf[String], v)
    if (v.isInstanceOf[Int] && VirtualMachine.closureMap.contains(v.asInstanceOf[Int])) {
      VirtualMachine.closureMap(v.asInstanceOf[Int]).machine.vMap.put(k.asInstanceOf[String], v)
      VirtualMachine.closureMap(v.asInstanceOf[Int]).machine.pMap.put(v.asInstanceOf[Int], -1)
      VirtualMachine.closureMap(v.asInstanceOf[Int]).machine.addString(k.asInstanceOf[String])
      VirtualMachine.closureMap.put(v.asInstanceOf[Int],
        new VirtualMachine.ClosureEnv(
          new VirtualMachine(VirtualMachine.closureMap(v.asInstanceOf[Int]).machine),
          VirtualMachine.closureMap(v.asInstanceOf[Int]).curCallIndex,
          VirtualMachine.closureMap(v.asInstanceOf[Int]).preCallIndex))
    }
    ip = ip + 1
    retValue = new String("Value: " + k.toString)
  }

  def executeBind(k: AnyRef) {
    sp -= 1
    var v: AnyRef = stack.remove(sp)
    if (v.isInstanceOf[String]) {
      v = vBindMap(v.asInstanceOf[String])
    }
    vMap.put(k.asInstanceOf[String], v)
    ip = ip + 1
  }

  def executeLoad(arg: Int) {
    val key: String = stack.remove(sp - 1).asInstanceOf[String]
    stack.append(vMap(key))
    ip = ip + 1
  }

  def executeStoreL(arg: AnyRef, curCall: Int) {
    argStack(curCall).append(arg)
    ip += 1
  }

  def executeLoadL(arg: Int) {
    val key: String = localArgs.remove(localArgs.size - 1).asInstanceOf[String]
    if (vMap.contains(key)) {
      localArgs.append(vMap(key))
    }
    else {
      localArgs.append(pMap(strs.indexOf(key)).asInstanceOf[AnyRef])
    }
    ip = ip + 1
  }

  def executeGoto(arg: Int) {
    ip = arg
  }

  def ensureInt(v: AnyRef): Int = {
    if (v.isInstanceOf[String]) {
      return vMap(v.asInstanceOf[String]).asInstanceOf[Int]
    }
    v.asInstanceOf[Int]
  }

  def ensureList(v: AnyRef): ArrayBuffer[AnyRef] = {
    if (v.isInstanceOf[String]) {
      return vMap(v.asInstanceOf[String]).asInstanceOf[ArrayBuffer[AnyRef]]
    }
    v.asInstanceOf[ArrayBuffer[AnyRef]]
  }

  def executeAdd() {
    var v1: Int = 0
    var v2: Int = 0
    var sum: Int = 0
    sp = sp - 1
    v1 = ensureInt(stack.remove(sp))
    sp = sp - 1
    v2 = ensureInt(stack.remove(sp))
    sum = v1 + v2
    stack.append(sum.asInstanceOf[AnyRef])
    sp = sp + 1
    ip = ip + 1

    retValue = sum.asInstanceOf[AnyRef]
  }

  def executeSub() {
    var v1: Int = 0
    var v2: Int = 0
    var ret: Int = 0
    sp = sp - 1
    v2 = ensureInt(stack.remove(sp))
    sp = sp - 1
    v1 = ensureInt(stack.remove(sp))
    ret = v1 - v2
    stack.append(ret.asInstanceOf[AnyRef])
    sp = sp + 1
    ip = ip + 1

    retValue = ret.asInstanceOf[AnyRef]
  }

  def executeMul() {
    var v1: Int = 0
    var v2: Int = 0
    var ret: Int = 0
    sp = sp - 1
    v1 = ensureInt(stack.remove(sp))
    sp = sp - 1
    v2 = ensureInt(stack.remove(sp))
    ret = v1 * v2
    stack.append(ret.asInstanceOf[AnyRef])
    sp = sp + 1
    ip = ip + 1

    retValue = ret.asInstanceOf[AnyRef]
  }

  def executeDiv() {
    var v1: Int = 0
    var v2: Int = 0
    var ret: Int = 0
    sp = sp - 1
    v2 = ensureInt(stack.remove(sp))
    sp = sp - 1
    v1 = ensureInt(stack.remove(sp))
    ret = v1 / v2
    stack.append(ret.asInstanceOf[AnyRef])
    sp = sp + 1
    ip = ip + 1

    retValue = ret.asInstanceOf[AnyRef]
  }

  def executeGt(reverse: Int) {
    var v1: Int = 0
    var v2: Int = 0
    var ret: Int = 0
    sp = sp - 1
    v2 = ensureInt(stack.remove(sp))
    sp = sp - 1
    v1 = ensureInt(stack.remove(sp))
    if (reverse == 1) {
      ret = if (v2 > v1) 1 else 0
    }
    else {
      ret = if (v1 > v2) 1 else 0
    }
    stack.append(ret.asInstanceOf[AnyRef])
    sp = sp + 1
    ip = ip + 1

    retValue = ret.asInstanceOf[AnyRef]
  }

  def executeGe(reverse: Int) {
    var v1: Int = 0
    var v2: Int = 0
    var ret: Int = 0
    sp = sp - 1
    v2 = ensureInt(stack.remove(sp))
    sp = sp - 1
    v1 = ensureInt(stack.remove(sp))
    if (reverse == 1) {
      ret = if (v2 >= v1) 1 else 0
    }
    else {
      ret = if (v1 >= v2) 1 else 0
    }
    stack.append(ret.asInstanceOf[AnyRef])
    sp = sp + 1
    ip = ip + 1

    retValue = ret.asInstanceOf[AnyRef]
  }

  def executeEq() {
    var v1: Int = 0
    var v2: Int = 0
    var ret: Int = 0
    sp = sp - 1
    v2 = ensureInt(stack.remove(sp))
    sp = sp - 1
    v1 = ensureInt(stack.remove(sp))
    ret = if (v1 == v2) 1 else 0
    stack.append(ret.asInstanceOf[AnyRef])
    sp = sp + 1
    ip = ip + 1

    retValue = ret.asInstanceOf[AnyRef]
  }

  /**
   * Need to be called before each method call, initialize envrionment for it.
   * Maybe it can be replaced by a instruction which does the job automatically.
   * @return
   */
  def newCall: Int = {
    localArgs = new ArrayBuffer[AnyRef]
    argStack.append(localArgs)
    callStack.append(new VirtualMachine.CallEnv(ip + 1, sp, stack, 0, 0))
    callp += 1
    callp - 1
  }

  /**
   * Add a string to the used string list.
   * @param s
   * @return
   */
  def addString(s: String): Int = {
    strs.append(s)
    strs.size - 1
  }

  def executeCall(curCall: Int, preCall: Int) {
    var pos: Int = 0
    var tag: AnyRef = null
    var ccVar: String = null
    if (callCc) {
      sp -= 1
      tag = stack.remove(sp)
      pos = -1
      ccVar = VirtualMachine.closureMap(tag.asInstanceOf[Int]).machine.ccRetVar + tag.hashCode
      vMap.put(ccVar, ccTag.asInstanceOf[AnyRef])
    }
    else {
      localArgs = argStack(curCall)
      val name: String = localArgs(0).asInstanceOf[String]
      tag = vMap(name)
      pos = pMap(tag.asInstanceOf[Int])
    }
    val env: VirtualMachine.CallEnv = callStack(callStack.size - 1)
    env.codeIndex = ip + 1
    env.curCallIndex = curCall
    env.preCallIndex = preCall
    stack = stack.clone()
    if (pos == -1) {
      val closueEnv: VirtualMachine.ClosureEnv = VirtualMachine.closureMap(tag.asInstanceOf[Int])
      val m: VirtualMachine = new VirtualMachine(closueEnv.machine)
      m.vBindMap = vMap
      for (i <- 1 to localArgs.size - 1) {
        val arg: AnyRef = localArgs(i)
        m.stack.append(arg)
        m.sp += 1
      }

      if (callCc) {
        m.stack.append(ccVar)
        m.sp += 1
        callCc = false
      }

      callClosure(m)
    }
    else {
      for (i <- 1 to localArgs.size - 1) {
        val arg: AnyRef = localArgs(i)
        stack.append(arg)
        sp += 1
      }
      ip = pos
    }
  }

  /**
   * @param arg 1 for returning value, 0 for no value.
   */
  def executeReturn(arg: Int) {
    val env: VirtualMachine.CallEnv = callStack.remove(callStack.size - 1)
    localArgs = argStack(env.preCallIndex)
    ip = env.codeIndex
    if (arg == 1) {
      val ret: AnyRef = stack(stack.size - 1)
      argStack(env.preCallIndex).append(ret)
      retValue = String.valueOf(ret)
    }
  }

  def executeCallcc(endP: Int, curCall: Int, preCall: Int) {
    val m: VirtualMachine = new VirtualMachine(this)
    ccStack.append(m)
    callCc = true
    localArgs = argStack(curCall)
    val env: VirtualMachine.CallEnv = callStack(callStack.size - 1)
    env.codeIndex = endP + 1
    env.curCallIndex = curCall
    env.preCallIndex = preCall
    stack = stack.clone()
    stack.append(localArgs(0))
    sp += 1
    ip += 1
  }

  def executeCcReturn(arg: Int) {
    val m: VirtualMachine = ccStack.remove(ccStack.size - 1)
    val ret: AnyRef = stack(stack.size - 1)
    code = m.code
    stack = m.stack
    callStack = m.callStack
    argStack = m.argStack
    localArgs = m.localArgs
    strs = m.strs
    callp = m.callp
    vMap = m.vMap
    pMap = m.pMap
    vBindMap = m.vBindMap
    sp = m.sp
    ip = m.ip
    icount = m.icount
    ifTag = m.ifTag
    beginTag = m.beginTag
    lambdaTag = m.lambdaTag
    closureEnvStack = m.closureEnvStack
    val env: VirtualMachine.CallEnv = callStack.remove(callStack.size - 1)
    localArgs = argStack(env.preCallIndex)
    ip = env.codeIndex + 1
    argStack(env.preCallIndex).append(ret)
    retValue = String.valueOf(ret)
  }

  /**
   * Add a procedure to the machine's instruction list.
   * @param name
   * @param label
   * @param insts
   * @return
   */
  def addProcedure(name: String, label: Int, insts: ArrayBuffer[Instruction]): Int = {
    if (name != null) {
      strs.append(name)
      vMap.put(name, label.asInstanceOf[AnyRef])
      pMap.put(label, icount)
    }
    else {
      pMap.put(label, -1)
    }
    for (ins <- insts) {
      code.append(ins)
      icount += 1
    }
    icount
  }

  def callClosure(m: VirtualMachine) {
    closureEnvStack.append(new VirtualMachine(this))
    code = m.code
    stack = m.stack
    callStack = m.callStack
    argStack = m.argStack
    localArgs = m.localArgs
    strs = m.strs
    callp = m.callp
    vMap = m.vMap
    pMap = m.pMap
    vBindMap = m.vBindMap
    sp = m.sp
    ip = m.ip
    icount = m.icount
    ifTag = m.ifTag
    beginTag = m.beginTag
    lambdaTag = m.lambdaTag
    retValue = m.retValue

  }

  /**
   * Return from a closure call
   * @param arg
   */
  def restore(arg: Int) {
    val m: VirtualMachine = closureEnvStack.remove(closureEnvStack.size - 1)
    code = m.code
    stack = m.stack
    callStack = m.callStack
    argStack = m.argStack
    localArgs = m.localArgs
    strs = m.strs
    callp = m.callp
    vMap = m.vMap
    pMap = m.pMap
    sp = m.sp
    ip = m.ip
    icount = m.icount
    ifTag = m.ifTag
    beginTag = m.beginTag
    lambdaTag = m.lambdaTag
    val env: VirtualMachine.CallEnv = callStack.remove(callStack.size - 1)
    localArgs = argStack(env.preCallIndex)
    ip = env.codeIndex
    localArgs.append(arg.asInstanceOf[AnyRef])
  }

  override def toString: String = {
    val builder1: mutable.StringBuilder = new mutable.StringBuilder()
    val builder2: mutable.StringBuilder = new mutable.StringBuilder()

    builder1.append("Functions:")
    builder2.append("Variables:")
    for (k <- vMap.keySet) {
      if (pMap.contains(vMap(k).asInstanceOf[Int])) {
        builder1.append(" \"" + k + "\"")
      } else {
        builder2.append(" \"" + k + "\"")
      }
    }

    builder2.append("\n")
    builder2.append(recordedStates.size.toString+"\n")
    val inst: Instruction = code(ip)
    builder2.append("Current Instruction: " + ip.toString + ":" + inst.opcode.toString + " " + (if (inst.args.size > 1) inst.args.reduceLeft {(a,b) => a.toString + " " + b.toString}) + "\n")
    builder2.append("Current Stack: " + (if (stack.size > 1) stack.reduceLeft { (a, b) => a.toString + ", " + b.toString}))
    builder1.toString() + "\n" + builder2.toString()
  }
}
