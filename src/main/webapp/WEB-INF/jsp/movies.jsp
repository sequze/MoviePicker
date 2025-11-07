<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ include file="layout/header.jspf" %>
<h2>Список фильмов</h2>

<form class="container mb-4" id="filterForm"
      action="${pageContext.request.contextPath}/movies" method="get">

    <div class="row g-3 align-items-center mb-3">
        <div class="col-md-3">
            <input class="form-control" name="genre" placeholder="Жанр" value="${param.genre}">
        </div>
        <div class="col-md-3">
            <input class="form-control" name="director" placeholder="Режиссёр" value="${param.director}">
        </div>
        <div class="col-md-3">
            <input class="form-control" name="minRating" placeholder="Мин. рейтинг" value="${param.minRating}">
        </div>
        <div class="col-md-auto">
            <button class="btn btn-outline-primary w-100">Фильтровать</button>
        </div>
    </div>

    <c:if test="${sessionScope.user != null}">
        <div class="row g-3">
            <div class="col-md-auto">
                <button type="button" class="btn btn-primary" id="randomBtn">Случайный фильм</button>
            </div>
            <div class="col-md-auto">
                <a class="btn btn-primary" href="${pageContext.request.contextPath}/user/watched">Мои просмотренные</a>
            </div>
        </div>
    </c:if>

</form>



<div id="randomResult" class="mb-4" style="display:none;">
    <div class="card">
        <div class="row g-0">
            <div class="col-md-3">
                <img id="randomPoster" class="img-fluid rounded-start" alt="poster">
            </div>
            <div class="col-md-9">
                <div class="card-body">
                    <h5 class="card-title" id="randomTitle"></h5>
                    <p class="card-text"><small class="text-muted" id="randomMeta"></small></p>
                    <a id="randomDetails" class="btn btn-sm btn-outline-primary">Подробнее</a>
                </div>
            </div>
        </div>
    </div>
</div>

<div class="row row-cols-1 row-cols-md-3 g-4">
    <c:forEach var="m" items="${movies}">
        <div class="col">
            <div class="card h-100">
                <c:if test="${not empty m.posterUrl}">
                    <img src="${m.posterUrl}" class="card-img-top" style="height: 460px; width: 100%; display: block;" alt="${m.name}">
                </c:if>
                <div class="card-body">
                    <h5 class="card-title">
                            ${m.name}
                        <c:if test="${not empty watchedIds and watchedIds.contains(m.id)}">
                            <span class="badge bg-success ms-2">Просмотрено</span>
                        </c:if>
                    </h5>
                    <p class="card-text">
                        Рейтинг: <c:out value="${m.rating}"/>
                    </p>
                    <a class="btn btn-primary" href="${pageContext.request.contextPath}/movies/view?id=${m.id}">Подробнее</a>
                </div>
            </div>
        </div>
    </c:forEach>
</div>
<%@ include file="layout/footer.jspf" %>