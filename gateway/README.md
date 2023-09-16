# GATEWAY

В обязанности МС входит:

Перенаправлять запросы в другие МСы. 
Главная задача этого МС - инкапсулировать сложную логику всей внутренней системы, предоставив клиенту простой и понятный API.


Ниже приведены примеры запросов:


http://localhost:9085/application


{

"amount": 15000,

"term": 12,

"firstName": "First",

"lastName": "Last",

"email": "example@yandex.ru",

"birthdate": "2000-05-05",

"passportSeries": "2000",

"passportNumber": "200000"

}

http://localhost:9085/application/apply

{

"applicationId": 1,

"requestedAmount": 15000,

"totalAmount": 115000,

"term": 12,

"monthlyPayment": 10217.61,

"rate": 12,

"isInsuranceEnabled": true,

"isSalaryClient": false

}
http://localhost:9085/application/registration/{applicationId}

{

"gender": "FEMALE",

"maritalStatus": "MARRIED",

"dependentAmount": 0,

"passportIssueDate": "2012-12-10",

"passportIssueBrach": "someone",

"employment": {

"employmentStatus": "SELF_EMPLOYED",

"employerINN": "123123",

"salary": 40000,

"position": "TOP_MANAGER",

"workExperienceTotal": 12,

"workExperienceCurrent": 12

},

"account": "000023S"

}

http://localhost:9085/document/{applicationId}

http://localhost:9085/document/{applicationId}/sign

http://localhost:9085/document/{applicationId}/sign/code?ses=0000

