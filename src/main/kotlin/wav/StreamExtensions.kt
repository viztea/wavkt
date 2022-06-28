package wav

import com.soywiz.korio.stream.*

fun SyncStream.readFOURCC(): String = readBytes(4).decodeToString()
fun SyncStream.readCHAR():      Int = readS8()
fun SyncStream.readBYTE():      Int = readU8()
fun SyncStream.readINT():       Int = readS16LE()
fun SyncStream.readWORD():      Int = readU16LE()
fun SyncStream.readLONG():      Int = readS32LE()
fun SyncStream.readDWORD():    Long = readU32LE()
fun SyncStream.readFLOAT():   Float = readF32BE()
fun SyncStream.readDOUBLE(): Double = readF64BE()
fun SyncStream.readSTR():    String = readString(readDWORD().toInt())
fun SyncStream.readZSTR():   String = readStringz(readDWORD().toInt())
fun SyncStream.readBSTR():   String = TODO("TBD")
