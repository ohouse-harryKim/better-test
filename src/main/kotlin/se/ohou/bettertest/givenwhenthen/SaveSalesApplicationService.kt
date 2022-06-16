package se.ohou.bettertest.givenwhenthen

class SaveSalesApplicationService(
    private val salesApplicationValidator: SalesApplicationValidator,
    private val salesApplicationPort: SalesApplicationPort,
) {

    fun updateSalesApplication(command: UpdateCommand) {}

    data class UpdateCommand(
        val id: Long,
        val content: String,
    )
}

data class SalesApplication(
    val id: Long,
    val content: String,
)

interface SalesApplicationValidator {
    @Throws(IllegalArgumentException::class)
    fun valid(command: SaveSalesApplicationService.UpdateCommand)
}

interface SalesApplicationPort {
    fun findById(id: Long): SalesApplication?
    fun update(salesApplication: SalesApplication): SalesApplication
}
