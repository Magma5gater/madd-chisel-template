package prefetcher

import chisel3._
import chisel3.util._
import chisel3.stage.{ChiselStage, ChiselGeneratorAnnotation}

// TODO: update this module to implement stride prefetching.
class StridePrefetcher(val addressWidth: Int, val pcWidth: Int) extends Module {
  val io = IO(new Bundle {
    val pc = Input(UInt(pcWidth.W))
    val address = Input(UInt(addressWidth.W))
    val prefetch = Output(UInt(addressWidth.W))
    val prefetch_valid = Output(Bool())
  })
  val depth = 8.U
  val pcSheet = Reg(Vec(depth, UInt(pcWidth.W)))
  val addressSheet = Reg(Vec(depth, UInt(addressWidth.W)))
  val strideSheet = Reg(Vec(depth, UInt(addressWidth.W)))
  val Areg = RegInit(0.U(depth.W))
  val len = RegInit(0.U(depth.W))
  val find = RegInit(false.B)
  find := false.B
  for (i <- 0 until len) {
    if(pc===pcSheet((i+Areg)%depth){
      if(address - addressSheet((i+Areg)%depth)===strideSheet((i+Areg)%depth)){
        io.out.prefetch := address + strideSheet((i+Areg)%depth)
        io.out.prefetch_valid := true.B
        find := true.B
        break
      }
    }
  }
  if(find){
    Areg := Areg + 1.U
    
  }
}
