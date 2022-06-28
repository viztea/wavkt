package wav

import wav.riff.RiffChunk

data class WavFile(
    val chunks: MutableList<RiffChunk>,
)
