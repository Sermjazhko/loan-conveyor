# dealMVP

# dealMVP

<h1>Сущности для базы данных</h1>

![image](https://github.com/Sermjazhko/dealMVP/assets/43463670/aa21de4c-5b04-4af6-b608-ba479300e24f)


Цвета:
- Зеленый: сущность реализована в виде отдельного отношения.
- Желтый: сущность реализована в виде поля типа jsonb.
- Синий: сущность реализована в виде java enum, сохранена в бд как varchar.
- 

![image](https://github.com/Sermjazhko/dealMVP/assets/43463670/df7c9366-f71e-4643-b929-0697d8b34361)


<h1>Реализованные API: </h1>
1.

 ![image](https://github.com/Sermjazhko/dealMVP/assets/43463670/fb4cffb5-c1b4-4822-825a-c781304c7964)



(Использовался RestTemplate)

Пример работы (в populateDB.sql в resourse можно найти тела запросов). Фоном запускался второй проект conveyor (внесены небольшие изменения в enum-сущности для согласования с новым заданием): 

Тело запроса:

![image](https://github.com/Sermjazhko/dealMVP/assets/43463670/8c8c6952-a86a-499e-8340-d4a64f55f0bf)


Результат: 

![image](https://github.com/Sermjazhko/dealMVP/assets/43463670/de71083e-bda1-4bbe-88b5-9f65468093b9)


В БД добавился клиент (заглушки, ибо нужную информацию из тела запроса не вытащить): 

![image](https://github.com/Sermjazhko/dealMVP/assets/43463670/1251ce95-c344-4406-8890-a4cee1114a0b)


В БД добавилась заявка с привязкой на клиента и в теле запроса (выше) можно увидеть, что id соответствует текущей заявке: 

![image](https://github.com/Sermjazhko/dealMVP/assets/43463670/04627d61-0047-4aa4-a5d7-c275c9561410)


Статус: 
![image](https://github.com/Sermjazhko/dealMVP/assets/43463670/331377b0-cf25-4ffe-9f3e-a1ed0adb4fa9)


2. 

![image](https://github.com/Sermjazhko/dealMVP/assets/43463670/c61ff496-3f2d-402a-a347-26bc29df4ee3)


Тело запроса:

![image](https://github.com/Sermjazhko/dealMVP/assets/43463670/58beb37c-1a2b-47a2-904a-31e95f7ef817)


Обновленный статус в БД: 

![image](https://github.com/Sermjazhko/dealMVP/assets/43463670/3d1ca583-1855-4c29-9dcf-f8a6b75bbd1b)


Обновленное поле offer:

![image](https://github.com/Sermjazhko/dealMVP/assets/43463670/d8bdd6eb-df20-4b42-acee-a5d96f9882aa)


3.

![image](https://github.com/Sermjazhko/dealMVP/assets/43463670/e488155f-cf39-4aab-894e-c0474571e5d8)


Тело запроса:

![image](https://github.com/Sermjazhko/dealMVP/assets/43463670/b98e9467-d081-4522-bd03-765ee78be364)


Результат (моя вольность, в задании этого нет...): 

Обновляется в заявке id кредита:

![image](https://github.com/Sermjazhko/dealMVP/assets/43463670/81b7f89c-823f-47a6-aff0-8cb281808699)


Появляется кредит в БД: 

![image](https://github.com/Sermjazhko/dealMVP/assets/43463670/82f502b6-945f-4f1c-a8e1-f21074e159e0)



