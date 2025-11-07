<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ include file="layout/header.jspf" %>
<link rel="stylesheet" href="${pageContext.request.contextPath}/static/css/movie.css">
<c:set var="m" value="${movie}" />
<div class="container movie-container">
    <div class="card movie-card p-3">
        <div class="row g-3 align-items-start">
            <div class="col-md-4">
                <c:if test="${not empty m.posterUrl}">
                    <img style = "width: 100%;" src="${m.posterUrl}" class="poster" alt="${m.name}">
                </c:if>
            </div>

            <div class="col-md-8">
                <div class="d-flex align-items-start justify-content-between mb-2">
                    <div>
                        <h2 class="h4 mb-1">${m.name}
                            <c:if test="${isWatched}">
                                <span class="badge bg-success status-badge ms-2">Просмотрено</span>
                            </c:if>
                        </h2>
                        <div class="movie-meta text-muted">Жанр: <c:out value="${m.genre}"/> · Режиссёр: ${m.director} · Год: <c:out value="${m.year}"/></div>
                    </div>

                    <div class="text-end">
                        <div class="badge badge-rating px-3 py-2">
                            &#9733; <c:out value="${m.rating}"/>
                        </div>
                    </div>
                </div>

                <p class="mb-3">${m.description}</p>

                <div class="movie-actions d-flex flex-wrap align-items-center">
                    <c:choose>
                        <c:when test="${sessionScope.user != null}">
                            <form method="post" action="${pageContext.request.contextPath}/movies/watched" style="display:inline-block">
                                <input type="hidden" name="movieId" value="${m.id}" />
                                <c:if test="${isWatched}">
                                    <input type="hidden" name="action" value="remove" />
                                    <button class="btn btn-outline-danger">Убрать из просмотренных</button>
                                </c:if>
                                <c:if test="${not isWatched}">
                                    <input type="hidden" name="action" value="add" />
                                    <button class="btn btn-success">Отметить как просмотрено</button>
                                </c:if>
                            </form>
                        </c:when>
                        <c:otherwise>
                            <a class="btn btn-secondary" href="${pageContext.request.contextPath}/login">Войдите, чтобы отметить как просмотренное</a>
                        </c:otherwise>
                    </c:choose>

                    <a class="btn btn-link ms-2" href="${pageContext.request.contextPath}/movies">Назад к списку</a>
                </div>
            </div>
        </div>
    </div>
</div>

<%@ include file="layout/footer.jspf" %>