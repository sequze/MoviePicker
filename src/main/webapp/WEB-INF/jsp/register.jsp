<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ include file="layout/header.jspf" %>
<h2>Регистрация</h2>
<c:if test="${not empty error}">
    <div class="alert alert-danger">${error}</div>
</c:if>
<form method="post" class="col-md-6" id="registerForm">
    <div class="mb-3">
        <label class="form-label">Логин</label>
        <input name="login" class="form-control" required minlength="3">
    </div>
    <div class="mb-3">
        <label class="form-label">Email</label>
        <input type="email" name="email" class="form-control" required>
    </div>
    <div class="mb-3">
        <label class="form-label">Пароль</label>
        <input type="password" name="password" class="form-control" required minlength="6">
    </div>
    <button class="btn btn-success">Зарегистрироваться</button>
</form>
<%@ include file="layout/footer.jspf" %>