package wav

import com.soywiz.korio.file.std.applicationVfs

inline fun <reified T> Iterable<*>.firstOf(): T = filterIsInstance<T>().first()

suspend fun main() {
    val wav = WavFileReader.read(applicationVfs["closure.wav"].readAsSyncStream())
        ?: return
    println(wav)

    val fmt = wav.chunks.firstOf<WavFmtChunk>()
    println(fmt)

    val data = wav.chunks.firstOf<WavDataChunk>()
    println(data)

    println(data.getBlocks(fmt))
}
