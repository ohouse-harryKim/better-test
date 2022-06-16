package se.ohou.bettertest.givenwhenthen

import io.kotest.core.spec.style.BehaviorSpec
import io.mockk.mockk
import se.ohou.bettertest.givenwhenthen.SaveSalesApplicationService.UpdateCommand

class SaveSalesApplicationServiceTest : BehaviorSpec({

    val validator = mockk<SalesApplicationValidator>()
    val port = mockk<SalesApplicationPort>()
    val saveSalesApplicationService = SaveSalesApplicationService(
        salesApplicationValidator = validator,
        salesApplicationPort = port,
    )

    Given("validation 을 통과하는 상품판매신청서 등록 Command 가 있다.") {
        val id = 1L
        val validCommand = UpdateCommand(
            id = id,
            content = "new"
        )

        And("DB 에서 업데이트를 하기 위한 Record 를 찾을 수 있다") {
            When("상품판매신청서 수정이 실행되면") {
                saveSalesApplicationService.updateSalesApplication(validCommand)
                Then("DB에 정상적으로 업데이트 된다") {
                }
            }
        }
        And("And: DB 에서 업데이트를 하기 위한 Record 를 찾을 수 없다.") {
            When("상품판매신청서 수정이 실행되면") {
                saveSalesApplicationService.updateSalesApplication(validCommand)
                Then("NoSuchElementException 이 발생한다") {
                }
            }
        }
    }

    Given("validation 을 통과하지 못하는 상품판매신청서 등록 Command 가 있다.") {
        val id = 1L
        val notValidCommand = UpdateCommand(
            id = id,
            content = "new"
        )
        And("DB 에서 업데이트를 하기 위한 Record 를 찾을 수 있다.") {
            When("상품판매신청서 수정이 실행되면") {
                saveSalesApplicationService.updateSalesApplication(notValidCommand)
                Then("Validation Error 가 발생한다") {
                }
            }
        }
    }
})
