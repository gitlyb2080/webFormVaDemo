<!DOCTYPE html>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<html lang="en">

<body>
    <h1>tes--form--submit</h1>
    <!-- 这个session版的表单提交地址，如果想尝试redis版，就把这个注释掉，把下面的redis版的注释放开 -->
    <form action="http://localhost:8080/vaSubmiit" onsubmit="return dosubmit()" method="post"> 
    <!-- redis版 表单提交地址 -->
	    <!-- <form action="http://localhost:8080/vaRedisSubmiit" onsubmit="return dosubmit()" method="post"> -->
	    <%--使用EL表达式取出存储在session中的token，实际情况下，请用 hidden属性 隐藏--%>
	    <input type="text" name="formToken" value="${formToken}"/>
	    
	    userName:<input type="text" name="username">
	    <input type="submit" value="提交" id="submit">
    </form>


</body>

</html>