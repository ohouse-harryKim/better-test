---
marp: true
---

# 좋은 테스트를 작성하는 방법 1

---

1. bdd 테스트를 작성하는 예시
    - Given When Then
    - Describe Context It
2. 소소한 팁들

---

## Given - When - Then

---

[User Story & Acceptance Criteria](https://wiki.dailyhou.se/pages/viewpage.action?pageId=62968415)

Given: 주어진 상황
When: 어떤 Action이 발생했을때
Then: 결과는 이러해야 한다

---

## User Story

상품판매신청서 수정요청이 오면 validation을하고 상품판매신청서를 DB에 업데이트 한다

---

### 분석

- "상품판매신청서 수정요청" -> 상품판매신청서 등록 `Command` 가 있다
- validation을 한다
- 상품판매신청서를 DB에 업데이트

### Application 흐름의 분기점

1. application 수행 시 validation 을 먼저 하고, DB 에 업데이트를 한다
2. validation 하다가 실패한 경우?
3. DB에서 업데이트 하기 위한 record 를 찾을 수 없는 경우?
    (업데이트 도중 실패하는 경우는 논외로)

---

### 일반적으로 함수를 짜고 코드 흐름대로 테스트 코드를 짜려고 접근 하면..

```
Given: 상품판매신청서 등록 `Command` 가 있다
    When: 상품판매신청서 수정이 실행될 때
        - Validation 을 통과한 경우
            - DB 에서 업데이트를 하기 위한 Record 를 성공적으로 찾은 경우
                Then: Update를 수행해야 한다
            - DB 에서 업데이트를 하기 위한 Record 를 찾지 못한 경우
                Then: NoSuchElementException 이 발생한다
        - Validation 을 통과하지 못한 경우
            Then: Validation Error 가 발생한다
```
When 이 여러 상황으로 갈라져버리고 중첩에 중첩을 해야 하는 상황이 발생함

---

뭔가 낯이 익는데...?

---

### 사실 이건 Describe - Context - It 패턴이었음

---
```
Describe: 상품판매 신청서 수정 function 테스트
    Context: Command 가 validation 을 통과하고
        Context: DB 에서 업데이트를 하기 위한 Record 를 성공적으로 찾는다면
            It: Update 를 수행한다
        Context: DB 에서 업데이트를 하기 위한 Record 를 찾지 못한다면
            It: NoSuchElementException 이 발생한다
    Context: Command 가 validation 을 통과하지 못한다면
        Context: Validation Error 가 발생한다
```
---

### 아까 짜려다 실패한 테스트 케이스

Given When Then 을 단어만 바꿔주면 거의 맞아 떨어짐

---

Describe - Context - It 패턴에 대한 부분은 [기계인간 블로그](https://johngrib.github.io/wiki/junit5-nested) 에 잘 설명 돼있음

---

**코드의 행동을 설명하는 테스트 코드를 작성** 하는 패턴

### Describe - Context - It 패턴 장점

- "코드"의 흐름에 따라 작성할 수 있기 때문에 작성하기가 매우 쉽다
- 테스트 코드를 계층 구조로 만들수 있다
- 테스트 코드를 추가하거나 읽을 때 스코프 범위만 신경쓰면 된다.
- "빠뜨린" 테스트 코드를 찾기 쉽다.
- 높은 테스트 커버리지가 필요한 경우 큰 도움이 된다.

---

Point. 문장을 쭉 읽었을 때 올바른 문장이 되는지 확인하자

→ 상품판매 신청서 수정 function 이 실행될 때 / Command 가 validation 을 통과하고 / DB 에서 업데이트를 하기 위한 Record 를 성공적으로 찾는다면 / Update 를 수행한다

---

### Given-When-Then 에 대한 좀 더 올바른 접근

---

여러가지 발생할 수 있는 상황들을 미리 생각하고 이런 부분들을 모두 Given에 기술
(Given 상황을 중첩해서 작업할 수 있긴 하나 code 작성하는 흐름에 비해 테스트 케이스는 다소 평면적임)

---

```
Given: 상품판매신청서 등록 Command 가 있다.
       이 Command 는 validation 을 통과할 수 있다
       DB 에서 업데이트를 하기 위한 Record 를 찾을 수 있다
 When: 상품판매신청서 수정이 실행되면
 Then: DB에 정상적으로 업데이트 된다


Given: 상품판매신청서 등록 Command 가 있다.
       이 Command 는 validation 을 통과할 수 있다
       DB 에서 업데이트를 하기 위한 Record 를 찾을 수 없다.
 When: 상품판매신청서 수정이 실행되면
 Then: NoSuchElementException 이 발생한다


Given: 상품판매신청서 등록 Command 가 있다.
       이 Command 는 validation 을 통과할 수 없다
 When: 상품판매신청서 수정이 실행되면
 Then: Validation Error 가 발생한다
```

---

```
Given: validation 을 통과하는 상품판매신청서 등록 Command 가 있다.
    And: DB 에서 업데이트를 하기 위한 Record 를 찾을 수 있다
        When: 상품판매신청서 수정이 실행되면
        Then: DB에 정상적으로 업데이트 된다

    And: DB 에서 업데이트를 하기 위한 Record 를 찾을 수 없다.
        When: 상품판매신청서 수정이 실행되면
        Then: NoSuchElementException 이 발생한다


Given: validation 을 통과하지 못하는 상품판매신청서 등록 Command 가 있다.
    And: DB 에서 업데이트를 하기 위한 Record 를 찾을 수 있다.
        When: 상품판매신청서 수정이 실행되면
        Then: Validation Error 가 발생한다
    
```
---

하나의 User Story 에서 발생할 수 있는 여러 흐름을 적절히 분리해서
이를 Given 절에 잘 녹여내는것이 중요

- Application 에서 분기가 많아질 경우, Given 이 점점 거대해짐

---

# 코드로 옮기면

https://github.com/ohouse-harryKim/better-test/commits/main

---

# Tips 

---

1. IntelliJ build tool 설정에서 gradle > run test using > IntelliJ Idea로 할 것

---

2. 테스트 중간에 분기가 너무 많은 경우
→ 한 클래스 또는 함수가 너무 많은 일을 하고 있는 것을 의미

---

3. Dependency 가 너무 많이 걸려 있어서 Mocking을 너무 많이 해야 하는 경우
→ 이것 또한 한 클래스 또는 함수가 너무 많은 일을 하고 있는 것을 의미

---

4. private function 은 어떻게 테스트?
→ reflection 을 사용해 테스트 할수도 있으나 추천하지 않음
→ 해당 private function 을 사용하는 function 전체의 입/출력 값을 테스트

---

5. 부수작용이 없는 순수함수 위주로 작성하고, 이를 테스트
→ 함수 내부에서 결정할 수 없는 값 (i.g random, datetime 등)이 함수 내부에서 사용된다면 테스트하기 어렵다.

---

6. 클린 코드
→ 클린 코드에서 제시하는 여러 원칙들이 있는데 이를 지키면 테스트 작성도 쉬워짐
→ 3장, 함수에 대한 내용

    - 작게 만들어라
    - 한 가지만 해라
    - 함수당 추상화 수준은 하나로
    - 부수 효과를 일으키지 마라
    - Etc...

---

7. Spy 도 잘 활용하면 좋아요
→ mapstruct 의 mapper 같은것 사용할 때..

---

추가사항: "구글 엔지니어는 이렇게 일한다" 챕터 11~14 에도 테스트에 대한 내용이 나옴
→ 어떤게 짜는게 테스트인가? 에 대한 고민이 필요

사실 오늘 설명한 부분은 좋은 테스트 작성이라기 보다는 그냥 정리되지 않은 팁 정도..?

---

# to be continued..

- 테스트 할때 데이터는 어떻게 해요?
- Controller Test 는 어떻게 해요?
