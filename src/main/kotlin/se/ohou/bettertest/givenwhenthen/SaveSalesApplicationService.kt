package se.ohou.bettertest.givenwhenthen

class SaveSalesApplicationService {

    fun updateSalesApplication(command: UpdateCommand) {}

    data class UpdateCommand(
        val id: Long
    )
}
