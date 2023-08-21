# application
<h1 align="center"> Кредитный конвейер </h1>
<h2 align="center"> MVP Level 3. Перенос прескоринга в МС-application </h2>

<h3> Прескоринг </h3>
Правила прескоринга:

1) Имя, Фамилия - от 2 до 30 латинских букв. Отчество, при наличии - от 2 до 30 латинских букв.
2) Сумма кредита - действительно число, большее или равное 10000.
3) Срок кредита - целое число, большее или равное 6.
4) Дата рождения - число в формате гггг-мм-дд, не позднее 18 лет с текущего дня.
5) Email адрес - строка, подходящая под паттерн "^[A-Za-z0-9+_.-]+@(.+)$".
6) Серия паспорта - 4 цифры, номер паспорта - 6 цифр.


<h3> Реализованные API: </h3>
<h3> 1) </h3> POST: /application - Прескоринг + запрос на расчёт возможных условий кредита. Request - LoanApplicationRequestDTO, response List<LoanOfferDTO>.
  
  Логика работы: 
  
  По API приходит LoanApplicationRequestDTO.
  
  На основе LoanApplicationRequestDTO происходит прескоринг.
  
  Отправляется POST-запрос на /deal/application в МС deal через RestTemplate.
  
  Ответ на API - список из 4х LoanOfferDTO от "худшего" к "лучшему".

  3. Ответ на API - список из 4х LoanOfferDTO от "худшего" к "лучшему" (чем меньше итоговая ставка, тем лучше).

Пример работы:

Тело запроса:

![image](https://github.com/Sermjazhko/loan-conveyor/assets/43463670/db2d001e-5489-44f6-bcc5-a7bd58a9cd85)

Результат работы:

На выходе 4 LoanOfferDTO, созданная заявка и клиент.

![image](https://github.com/Sermjazhko/loan-conveyor/assets/43463670/676538e1-f87d-449e-b643-0cfec05914ee)

![image](https://github.com/Sermjazhko/loan-conveyor/assets/43463670/4fa05a33-c43a-4956-be84-f99229967faf)

![image](https://github.com/Sermjazhko/loan-conveyor/assets/43463670/90742f6d-a7c7-40f2-bff4-924d40454604)


<h3> 2) </h3> PUT: /application/offer - Выбор одного из предложений. Request LoanOfferDTO, response void.

Логика работы:

  По API приходит LoanOfferDTO
  
  Отправляется PUT-запрос на /deal/offer в МС deal через RestTemplate.

Пример работы:

Тело запроса:

![image](https://github.com/Sermjazhko/loan-conveyor/assets/43463670/c2fd66a7-7af4-48dd-b1a1-5e385dff6df2)

Результат: 

Обновленный запрос.

![image](https://github.com/Sermjazhko/loan-conveyor/assets/43463670/4122e5ad-46bb-4cef-a0bd-782eabf62a80)
![image](https://github.com/Sermjazhko/loan-conveyor/assets/43463670/0e8f05de-9046-45a2-8192-ea7eff1183c7)


Скрипты миграции БД на все сущности из МС-deal реализованы и таблицы создаются через них:

![image](https://github.com/Sermjazhko/loan-conveyor/assets/43463670/a6a7d79a-9011-4b7c-b702-38ddf595e87d)

