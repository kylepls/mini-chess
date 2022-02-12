package `in`.kyle.chess.epd

data class Operation(
    val key: Epd.OperationKey,
    val value: String
) {

    companion object {
        fun parse(epd: String): List<Operation> {
            val split = epd.split(" ").drop(4).joinToString(" ")
            val operations = split.split(";").map { it.trim().replace("\"", "") }
            return operations.map { operation ->
                val splitOperation = operation.split(" ")
                val key =
                    Epd.OperationKey.values().find { splitOperation[0].startsWith(it.prefix) }
                        ?: return@map null
                val value = splitOperation[1]
                Operation(key, value)
            }.filterNotNull()
        }
    }
}
