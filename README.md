# conveyor
<h1 align="center"> Кредитный конвейер </h1>
<h2 align="center"> MVP Level 1 Реализация кредитного конвейера </h2>

Кредитный калькулятор, который использовался для проверки результатов (скрины приведены): <a> https://calculator-credit.ru/</a>.

<h3> Прескоринг </h3>
Правила прескоринга:

1) Имя, Фамилия - от 2 до 30 латинских букв. Отчество, при наличии - от 2 до 30 латинских букв.
2) Сумма кредита - действительно число, большее или равное 10000.
3) Срок кредита - целое число, большее или равное 6.
4) Дата рождения - число в формате гггг-мм-дд, не позднее 18 лет с текущего дня.
5) Email адрес - строка, подходящая под паттерн "^[A-Za-z0-9+_.-]+@(.+)$".
6) Серия паспорта - 4 цифры, номер паспорта - 6 цифр.

<h3> Скоринг </h3>
Правила скоринга:

1) Рабочий статус: Безработный → отказ; Самозанятый → ставка увеличивается на 1; Владелец бизнеса → ставка увеличивается на 3
2) Позиция на работе: Менеджер → ставка не меняется; Менеджер среднего звена → ставка уменьшается на 2; Топ-менеджер → ставка уменьшается на 4
3) Сумма займа больше, чем 20 зарплат → отказ
4) Семейное положение: Замужем/женат → ставка уменьшается на 3; Разведен → ставка увеличивается на 1
5) Количество иждивенцев больше 1 → ставка увеличивается на 1
6) Возраст менее 20 или более 60 лет → отказ
7) Пол: Женщина, возраст от 35 до 60 лет → ставка уменьшается на 3; Мужчина, возраст от 30 до 55 лет → ставка уменьшается на 3; Не бинарный → ставка увеличивается на 3
8) Стаж работы: Общий стаж менее 12 месяцев → отказ; Текущий стаж менее 3 месяцев → отказ

<h3> Реализованные API: </h3>
1) POST: /conveyor/offers - расчёт возможных условий кредита. Request - LoanApplicationRequestDTO, response - List<LoanOfferDTO>.
  Логика работы: 
  1. По API приходит LoanApplicationRequestDTO.
  2. На основании LoanApplicationRequestDTO происходит прескоринг. 
  Создаётся 4 кредитных предложения LoanOfferDTO на основании всех возможных комбинаций булевских полей isInsuranceEnabled и isSalaryClient (false-false, false-true, true-false, true-true). 
  Логика формирования кредитных предложений: в зависимости от страховых услуг уменьшается процентная ставка (на 3) и сумма кредита (на 1). 
  Базовая ставка (15) хардкодится в коде через property файл. 
  Цена страховки 100к (фиксированная, также находится в property файле), ее стоимость добавляется в тело кредита.
  3. Ответ на API - список из 4х LoanOfferDTO от "худшего" к "лучшему" (чем меньше итоговая ставка, тем лучше).

Пример работы:

Тело запроса:


![image](https://github.com/Sermjazhko/conveyor/assets/43463670/0c7c6aed-294b-40a0-8c6a-a188c4881875)

Результат:


![image](https://github.com/Sermjazhko/conveyor/assets/43463670/6117db8c-6551-4ab5-8dd5-7769391e9234)
![image](https://github.com/Sermjazhko/conveyor/assets/43463670/6ab6427d-9634-471b-bf5d-d10339405045)

На основании isInsuranceEnabled прибавляется (если true) фиксированное значение страховки, а ставка уменьшается.
На основании isSalaryClient ставка уменьшается. 
Предложения в порядке от "худшего" к "лучшему" на основании итоговой ставки.

Для первого и последнего случаев результат работы кредитного калькулятора (в кредитном калькуляторе округление до рублей, без копеек): 


![image](https://github.com/Sermjazhko/conveyor/assets/43463670/07567be3-acef-4f56-a865-5b384744ced0)
![image](https://github.com/Sermjazhko/conveyor/assets/43463670/1b1862ef-0c0e-4f05-9651-7d635b183e2d)

  
2) POST: /conveyor/calculation - валидация присланных данных + скоринг данных + полный расчет параметров кредита. Request - ScoringDataDTO, response CreditDTO.
Логика работы: 
1. По API приходит ScoringDataDTO.
2. Происходит скоринг данных, высчитывание ставки(rate), полная стоимость кредита(psk), размер ежемесячного платежа(monthlyPayment), график ежемесячных платежей (List<PaymentScheduleElement>).
3. Логика расчета параметров кредита:
Формула ПСК:

![image](https://github.com/Sermjazhko/conveyor/assets/43463670/3db550a2-727d-43fb-86cd-c7d75e937e6c)

Где S - сумма всех платежей,S0 - сумма от банка и n - количество лет погашения ссуд.
Формула для ежемесячного платежа (в коде эта же формула, просто преобразованная так, чтобы вынести минус из степени):

![image](https://github.com/Sermjazhko/conveyor/assets/43463670/4d4af678-9dff-419a-8558-0dd9d864d1a1)
 
4. Ответ на API - CreditDTO, насыщенный всеми рассчитанными параметрами.

Пример работы:

Тело запроса:

![image](https://github.com/Sermjazhko/conveyor/assets/43463670/f5f78ce9-d9c1-4f0a-8705-8f6726d3d35f)

Результат (часть из 12 месяцев): 

![image](https://github.com/Sermjazhko/conveyor/assets/43463670/5c32b259-b8c5-4617-aa88-120a67a78356)
![image](https://github.com/Sermjazhko/conveyor/assets/43463670/8d00027d-94a0-4800-adab-0016f40ffbb3)
![image](https://github.com/Sermjazhko/conveyor/assets/43463670/3453d5b1-bb33-4eee-96e4-b92f69e520d5)

Результат кредитного калькулятора: 

![image](https://github.com/Sermjazhko/conveyor/assets/43463670/3c800b04-7282-49ee-ab53-ae22a5869f82)
![image](https://github.com/Sermjazhko/conveyor/assets/43463670/47814a22-299b-4d47-8339-bfa8145bbcd4)

При этом пск с кредитным калькулятором может в некоторых случаях не совпадать, так как в кредитном калькуляторе используется сложная формула. 

<h3> Документация API </h3>
Документация всех API производилась через Swagger, ссылка для запуска swagger-ui в браузере: <a>http://localhost:9090/swagger-ui/index.html</a>.

![image](https://github.com/Sermjazhko/conveyor/assets/43463670/6e230b1b-51af-4f52-83e0-1c95e41a7a2d)


<h3> Пометка к тестам </h3>

Тест, который сравнивался с кредитным калькулятором: 

![image](https://github.com/Sermjazhko/conveyor/assets/43463670/529500c9-1592-4eeb-b0de-b8240fdd9882)

![image](https://github.com/Sermjazhko/conveyor/assets/43463670/03b2bd31-4b47-4fce-a6f5-e396ca049055)

Имеются небольшие погрешности округления (иногда в копейках) + в коде реализация идёт так, 
что небольшой остаток в конце последнего месяца дополнительно учитывается в самом платеже (тоже погрешность округления), 
поэтому последний месяц может отличаться по сумме
