package se.ohou.bettertest.givenwhenthen

import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.matchers.shouldBe
import io.mockk.clearAllMocks
import io.mockk.every
import io.mockk.just
import io.mockk.mockk
import io.mockk.runs
import io.mockk.slot
import io.mockk.verify
import se.ohou.bettertest.givenwhenthen.SaveSalesApplicationService.UpdateCommand

class SaveSalesApplicationServiceTest : BehaviorSpec({

    val validator = mockk<SalesApplicationValidator>()
    val port = mockk<SalesApplicationPort>()
    val saveSalesApplicationService = SaveSalesApplicationService(
        salesApplicationValidator = validator,
        salesApplicationPort = port,
    )

    afterContainer {
        clearAllMocks()
    }

    Given("validation 을 통과하는 상품판매신청서 등록 Command 가 있다.") {
        val id = 1L
        val validCommand = UpdateCommand(
            id = id,
            content = "new",
        )

        beforeContainer {
            every {
                validator.valid(any())
            } just runs
        }

        And("DB 에서 업데이트를 하기 위한 Record 를 찾을 수 있다") {

            every {
                port.findById(id)
            } returns SalesApplication(id, "old")

            When("상품판매신청서 수정이 실행되면") {

                val slot = slot<SalesApplication>()
                every {
                    port.update(capture(slot))
                } answers { slot.captured }

                saveSalesApplicationService.updateSalesApplication(validCommand)

                Then("DB에 정상적으로 업데이트 된다") {
                    verify(exactly = 1) { port.update(any()) }
                    slot.captured.id shouldBe 1L
                    slot.captured.content shouldBe "new"
                }
            }
        }
        And("And: DB 에서 업데이트를 하기 위한 Record 를 찾을 수 없다.") {

            every {
                port.findById(id)
            } returns null

            When("상품판매신청서 수정이 실행되면") {
                Then("NoSuchElementException 이 발생한다") {
                    shouldThrow<NoSuchElementException> {
                        saveSalesApplicationService.updateSalesApplication(validCommand)
                    }
                    verify(inverse = true) { port.update(any()) }
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

        every {
            validator.valid(any())
        } throws IllegalArgumentException()

        When("상품판매신청서 수정이 실행되면") {
            Then("Validation Error 가 발생한다") {
                shouldThrow<IllegalArgumentException> {
                    saveSalesApplicationService.updateSalesApplication(notValidCommand)
                }
                verify(inverse = true) { port.update(any()) }
            }
        }
    }
})
