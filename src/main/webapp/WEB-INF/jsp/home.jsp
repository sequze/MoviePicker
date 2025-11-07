<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" language="java" %>
<%@ include file="layout/header.jspf" %>
<div class="p-5 mb-4 bg-light rounded-3">
    <div class="container-fluid py-5">
        <h1 class="display-5 fw-bold">Добро пожаловать в MoviePicker</h1>
        <p class="col-md-8 fs-4">Подберите фильм по жанру, рейтингу и режиссёру или доверьтесь случайному выбору.</p>
        <a class="btn btn-primary btn-lg" href="${pageContext.request.contextPath}/movies">Подобрать фильм</a>
        <c:if test="${sessionScope.user == null}">
            <a class="btn btn-outline-secondary btn-lg" href="${pageContext.request.contextPath}/login">Войти / Зарегистрироваться</a>
        </c:if>
    </div>
</div>
<%@ include file="layout/footer.jspf" %>