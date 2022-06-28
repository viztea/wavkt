package wav.riff

import com.soywiz.korio.stream.SyncStream

open class RiffChunk(val identifier: String) {
    override fun toString(): String = "RiffChunk(identifier=$identifier)"

    class Unknown(type: String, val size: Long, val stream: SyncStream? = null) : RiffChunk(type) {
        override fun toString(): String = "UnknownRiffChunk(identifier=$identifier, size=$size)"
    }
}

open class ListRiffChunk(val type: String) : RiffChunk("LIST") {
    class Unknown(type: String, val size: Long, val stream: SyncStream?) : ListRiffChunk(type) {
        override fun toString(): String = "UnknownListRiffChunk(type=$type, size=$size)"
    }

    data class Info(val chunks: List<Chunk>) : ListRiffChunk("INFO") {
        override fun toString(): String = "InfoListRiffChunk(chunks=$chunks)"

        data class Chunk(val type: String, val value: String)
    }
}
