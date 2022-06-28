package wav

sealed class WavSample {
    data class Wave(val data: Int) : WavSample()

    data class Silence(val samples: Long) : WavSample()
}
