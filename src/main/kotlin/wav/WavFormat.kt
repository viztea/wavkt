package wav

import com.soywiz.korio.stream.SyncStream

sealed class WavFormat(val code: Int) {
    companion object {
        fun find(code: Int, bytes: SyncStream): WavFormat {
            return when (code) {
                WAVE_FORMAT_PCM -> PCM(bytes.readWORD())
                else -> Unknown(code, bytes)
            }
        }
    }

    data class PCM(val bitsPerSample: Int) : WavFormat(WAVE_FORMAT_PCM)

    class Unknown(code: Int, val stream: SyncStream) : WavFormat(code) {
        override fun toString(): String = "UnknownWavFormat(code=$code)"
    }
}
