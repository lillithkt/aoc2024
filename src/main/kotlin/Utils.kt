package gay.lilyy.aoc2024

// List.mapNotException
fun <T, R> List<T>.mapNotException(transform: (T) -> R): List<R> {
    val result = mutableListOf<R>()
    for (item in this) {
        try {
            result.add(transform(item))
        } catch (_: Exception) {
            continue
        }
    }
    return result
}