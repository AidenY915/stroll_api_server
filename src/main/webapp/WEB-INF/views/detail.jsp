<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<html lang="en">
<head>
<meta charset="UTF-8" />
<meta name="viewport" content="width=device-width, initial-scale=1.0" />
<title>Detail</title>
<link rel="icon" href="pages/images/favicon.png" />
<!-- splider css -->
<link
	href="
https://cdn.jsdelivr.net/npm/@splidejs/splide@4.1.4/dist/css/splide.min.css
"
	rel="stylesheet" />
<link rel="stylesheet" href="pages/css/frameCss.css" />
<link rel="stylesheet" href="pages/css/detail/detail.css" />
<!-- jQuery -->
<script src="https://code.jquery.com/jquery-3.7.1.min.js"
	integrity="sha256-/JqT3SQfawRcv/BIHPThkBvs0OEvtFFmqPF/lYI/Cxo="
	crossorigin="anonymous"></script>
<!-- jQuery UI -->
<script src="https://code.jquery.com/ui/1.13.2/jquery-ui.min.js"
	integrity="sha256-lSjKY0/srUM9BE3dPm+c4fBo1dky2v27Gdjm2uoZaL0="
	crossorigin="anonymous"></script>
<!-- splider -->
<script
	src="
https://cdn.jsdelivr.net/npm/@splidejs/splide@4.1.4/dist/js/splide.min.js
"></script>
<script src="pages/js/footer.js"></script>
<script src="pages/js/detail/splide.js"></script>
<script src="pages/js/detail/detail.js"></script>
</head>
<body>
	<%@include file="gnb.jsp"%>
	<div class="container">
		<section class="about">
			<div class="imgSlider">
				<img class="displayedImg" />
				<section class="splide" aria-label="Splide Basic HTML Example">
					<div class="splide__track">
						<ul class="splide__list">
							<c:forEach var="img" items="${imgs}">
								<li class="splide__slide"><img src="${img}" alt="장소 이미지" /></li>
							</c:forEach>
						</ul>
					</div>
				</section>
			</div>
			<article>
				<h2>${place.title}<c:if test="${id != null}">
						<button class="wishBtn">
							<img
								src=<c:choose>
						<c:when test = "${isWishedPlace eq 'true'}">"pages/images/wished.png"</c:when>
						<c:otherwise>"pages/images/beforeWish.png"</c:otherwise>
						</c:choose>>
						</button>
					</c:if>
				</h2>
				<p>★ ${place.star}</p>
				<p>
					<span>${place.guAddress} ${place.afterGuAddress} ${place.detailAddress}</span>
				</p>
				<p>${place.content}</p>
				<c:if test="${id == place.userId}">
					<button class="deletePlaceBtn" onclick="location.assign('deletePlace?no=${param.no}')">장소 삭제</button>
				</c:if>
			</article>
		</section>
		<section class="detailInfo"></section>
		<section class="reviewSection">
			<c:if test="${sessionScope.id != null}">
				<form class="commentWritingForm" method="post" action="insertReply">
					<select name="star">
						<option value="1">★</option>
						<option value="2">★★</option>
						<option value="3" selected>★★★</option>
						<option value="4">★★★★</option>
						<option value="5">★★★★★</option>
					</select>
					<textarea name="content"></textarea>
					<button class="replyBtn">등록</button>
					<input type='hidden' name='placeNo' value="${param.no}">
				</form>
			</c:if>
			<ul class="reviews">
				<c:forEach var="reply" items="${replies}">
					<li class='review'>
						<p class='writer'>${reply.userNickname}</p>
						<p class='score'>
							<c:forEach begin="1" end="${reply.star}">★</c:forEach>
						</p>
						<p class='content'>${reply.content}</p>
						<p class='createdDate'>${reply.writtenDate}</p> <c:if
							test="${reply.userId == sessionScope.id}">
							<a href="deleteReply?no=${reply.no}&placeNo=${param.no}">
								<button class="replyBtn">삭제</button>
							</a>
						</c:if>
					</li>
				</c:forEach>
			</ul>
		</section>
	</div>
</body>
</html>
