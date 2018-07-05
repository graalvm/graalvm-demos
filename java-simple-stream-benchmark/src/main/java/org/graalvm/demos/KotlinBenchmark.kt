package org.graalvm.demos

import org.openjdk.jmh.annotations.*
import org.openjdk.jmh.infra.Blackhole
import java.util.*
import java.util.concurrent.TimeUnit
import kotlin.properties.Delegates

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
    private var result: Int by Delegates.notNull()
    @Setup(Level.Iteration)
    fun setup() {
        values = IntArray(arrSz)
        for (i in 0 until arrSz) {
            values[i] = random.nextInt()
        }
    }

    @Benchmark
    fun sequenceBenchmark(b: Blackhole) {
        result = values
                .asSequence()
                .map { it + 1 }
                .map { it * 2 }
                .map { it + 2 }
                .sum()
    }

    @TearDown(Level.Iteration)
    fun check() {
        var result = 0
        for (i in 0 until arrSz) {
            result += values[i] * 2 + 4
        }
        assert(this.result == result) { "result measured wrong" }
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
