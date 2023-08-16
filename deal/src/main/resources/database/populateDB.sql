/*примеры запросов:*/

/*
POST
http://localhost:9080/deal/application

{
    "amount": 100000,
    "term": 12,
    "firstName": "First",
    "lastName": "Last",
    "email": "sel@mail.ru",
    "birthdate": "2000-05-05",
    "passportSeries": "2000",
    "passportNumber": "200000"
}

PUT
http://localhost:9080/deal/offer
{
        "applicationId": 1,
        "requestedAmount": 100000,
        "totalAmount": 100000,
        "term": 12,
        "monthlyPayment": 9025.83,
        "rate": 15,
        "isInsuranceEnabled": false,
        "isSalaryClient": false
}

PUT
http://localhost:9080/deal/calculate/1
{
        "gender": "MALE",
        "maritalStatus": "MARRIED",
        "dependentAmount": 0,
        "passportIssueDate": "2012-12-10",
        "passportIssueBrach": "someone",
        "employment": {
            "employmentId": 12,
            "employmentStatus": "SELF_EMPLOYED",
            "employerInn": "123123",
            "salary": 40000,
            "position": "TOP_MANAGER",
            "workExperienceTotal": 12,
            "workExperienceCurrent": 12
        },
        "account": "000023S"
}
*/