package org.graalvm.demos

import org.openjdk.jmh.annotations.*
import org.openjdk.jmh.infra.Blackhole
import java.util.*
import java.util.concurrent.TimeUnit

@Warmup(iterations = 20, time = 100, timeUnit = TimeUnit.MILLISECONDS)
@Measurement(iterations = 20, time = 100, timeUnit = TimeUnit.MILLISECONDS)
@BenchmarkMode(Mode.AverageTime)
@OutputTimeUnit(TimeUnit.NANOSECONDS)
@Fork
@State(Scope.Benchmark)
open class KotlinBemchmark {

    @Param("0", "1", "10", "100", "1000", "10000")
    var arrSz: Int = 0

    private lateinit var values: IntArray
    private val random = Random()

    @Setup(Level.Iteration)
    fun setup() {
        values = IntArray(arrSz)
        for (i in 0 until arrSz) {
            values[i] = random.nextInt()
        }
    }

    @Benchmark
    fun sequenceBenchmark(b: Blackhole) {
        val result = values
                .asSequence()
                .map { it + 1 }
                .map { it * 2 }
                .map { it + 2 }
                .sum()
        b.consume(result)
    }

    @Benchmark
    fun noSequenceBenchmark(b: Blackhole) {
        val result = values
                .map { it + 1 }
                .map { it * 2 }
                .map { it + 2 }
                .sum()
        b.consume(result)
    }


}
