package wav

import com.soywiz.kmem.toIntClamp
import com.soywiz.korio.stream.*
import wav.riff.ListRiffChunk
import wav.riff.RiffChunk

object WavFileReader {
    private val WAVE_RIFF_HEADER: IntArray = intArrayOf('R'.code, 'I'.code, 'F'.code, 'F'.code, -1, -1, -1, -1, 'W'.code, 'A'.code, 'V'.code, 'E'.code)

    private fun checkNextBytes(stream: SyncStream, input: IntArray, rewind: Boolean = true): Boolean {
        if (stream.available < input.size) {
            return false
        }

        val oldPos = stream.position
        var valid = true
        for (byte in input) {
            val `in` = stream.read()
            if (`in` == -1 || (byte != -1 && `in` != byte)) {
                valid = false
            }
        }

        if (rewind) {
            stream.position = oldPos
        }

        return valid
    }

    private fun findRiffChunk(id: String, size: Long, stream: SyncStream): RiffChunk {
        return when (id) {
            "fact" -> WavFactChunk(stream.readDWORD())

            "data" -> WavDataChunk(size, stream.markPos, stream.sliceHere())

            "fmt " -> {
                val fmtTag = stream.readWORD()
                WavFmtChunk(
                    channels = stream.readWORD(),
                    sampleRate = stream.readDWORD(),
                    bytesPerSecond = stream.readDWORD(),
                    blockAlign = stream.readWORD(),
                    format = WavFormat.find(fmtTag, stream),
                )
            }

            "cue " -> {
                val points = stream.readDWORD()
                WavCueChunk(points, List(points.toIntClamp()) {
                    WavCueChunk.Point(
                        stream.readDWORD(),
                        stream.readDWORD(),
                        stream.readFOURCC(),
                        stream.readDWORD(),
                        stream.readDWORD(),
                        stream.readDWORD(),
                    )
                })
            }

            "LIST" -> when (val type = stream.readFOURCC()) {
                "wavl" -> TODO("fuck RIFF i guess")

                "INFO" -> {
                    val chunks = mutableListOf<ListRiffChunk.Info.Chunk>()
                    while (stream.available > 0) {
                        chunks.add(ListRiffChunk.Info.Chunk(stream.readFOURCC(), stream.readZSTR()))
                    }

                    ListRiffChunk.Info(chunks)
                }

                else -> ListRiffChunk.Unknown(type, stream.available, stream)
            }

            else -> RiffChunk.Unknown(id, size, stream)
        }
    }

    fun isWav(stream: SyncStream): Boolean {
        return checkNextBytes(stream, WAVE_RIFF_HEADER, true)
    }

    fun read(stream: SyncStream): WavFile? {
        if (!checkNextBytes(stream, WAVE_RIFF_HEADER, false)) {
            return null
        }

        val chunks = mutableListOf<RiffChunk>()
        while (stream.available > 0) {
            val chunkId = stream.readFOURCC()
            val chunkSize = stream.readDWORD()
            val chunkData = stream.readStream(chunkSize)

            chunkData.markPos = stream.position - chunkSize

            /* check for pad byte */
            if (chunkSize % 2L != 0L) {
                stream.skip(1)
            }

            chunks.add(findRiffChunk(chunkId, chunkSize, chunkData))
        }

        return WavFile(chunks)
    }
}
