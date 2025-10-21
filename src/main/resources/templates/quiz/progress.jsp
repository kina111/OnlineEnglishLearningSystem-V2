<!DOCTYPE html>
<html xmlns:th="http://www.thymeleaf.org">
<head>
    <meta charset="UTF-8">
    <title>Result Page</title>
</head>
<body>
    <h1>Results</h1>
    <div th:each= "question,questNum : ${questions}">
        <button onclick="quiz/${quiz.id}/${question.id}" class="">${questNum}</button>
    </div>

</body>
</html>
