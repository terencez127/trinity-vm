package vm

import scala.collection.mutable.ArrayBuffer
import vm.VirtualMachine.Instruction

abstract class Closure {
  def getByteCodes: ArrayBuffer[Instruction] = {
    new ArrayBuffer[Instruction]
  }
}

