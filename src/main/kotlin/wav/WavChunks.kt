package wav

import com.soywiz.korio.stream.SyncStream
import wav.riff.RiffChunk

data class WavFmtChunk(
    val format: WavFormat,
    val channels: Int,
    val sampleRate: Long,
    val bytesPerSecond: Long,
    val blockAlign: Int,
) : RiffChunk("fmt ")

data class WavCueChunk(
    val pointCount: Long,
    val points: List<Point>
) : RiffChunk("cue ") {
    data class Point(
        val name: Long,
        val position: Long,
        val dataChunk: String,
        val dataChunkStart: Long,
        val blockStart: Long,
        val sampleOffset: Long,
    )
}

data class WavFactChunk(val sampleCount: Long) : RiffChunk("fact")

data class WavDataChunk(
    val size: Long,
    val startOffset: Long,
    val data: SyncStream
) : RiffChunk("data") {
    fun getBlocks(fmt: WavFmtChunk): List<DataBlock> {
        val blocks = listOf<DataBlock>()

        val blockCount = size / fmt.blockAlign
        println(blockCount)

        val blockPadding = fmt.blockAlign - fmt.channels * ((fmt.format as WavFormat.PCM).bitsPerSample shr 3)
        val padding = if (blockPadding > 0) blockPadding / 2 else null

//        val buffer: ShortArray? = if (padding != null) {
//            ShortArray(fmt.channels * 4096)
//        } else {
//            null
//        }
//
//        var blockPosition: Int
//        while (true) {
//            val endOffset = startOffset + fmt.blockAlign * blockCount
//            blockPosition = min((endOffset - data.position) / fmt.blockAlign, 4096L).toIntClamp()
//
//            if (blockPosition <= 0) {
//                break
//            }
//
//            if (padding != null) {
//                val sampleCount = blockCount * fmt.channels
//                var indexInBlock = 0
//                for (i in 0 until sampleCount) {
//                    buffer!![i.toIntClamp()] = data.readS16LE().toShortClamped()
//                    if (++indexInBlock == fmt.channels) {
//                        data.skip(padding)
//                        indexInBlock = 0
//                    }
//                }
//
//                DataBlock(buffer!!.take((blockCount * fmt.channels).toIntClamp()).toShortArray())
//            } else {
//                DataBlock(data.readShortArrayLE((blockCount * fmt.blockAlign).toIntClamp().coerceAtMost(data.length.toInt() - 1)))
//            }
//        }

        return blocks
    }

    data class DataBlock(val data: ShortArray)
}
