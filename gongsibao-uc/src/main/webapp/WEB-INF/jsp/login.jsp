<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<!DOCTYPE html>
<html lang="zh-CN">
<head>
    <meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title>登录</title>

    <!-- Bootstrap core CSS -->
    <link href="${pageContext.request.contextPath}/bootstrap/css/bootstrap.min.css" rel="stylesheet">
    <style>
        body {
            padding-top: 40px;
            padding-bottom: 40px;
            background-color: #eee;
        }

        .form-signin {
            max-width: 330px;
            padding: 15px;
            margin: 0 auto;
        }
        .form-signin .form-signin-heading,
        .form-signin .checkbox {
            margin-bottom: 10px;
        }
        .form-signin .checkbox {
            font-weight: normal;
        }
        .form-signin .form-control {
            position: relative;
            height: auto;
            -webkit-box-sizing: border-box;
            -moz-box-sizing: border-box;
            box-sizing: border-box;
            padding: 10px;
            font-size: 16px;
        }
        .form-signin .form-control:focus {
            z-index: 2;
        }
        .form-signin input {
            margin-bottom: 10px;
            border-top-left-radius: 0;
            border-top-right-radius: 0;
        }
        .form-signin .log_yzm  {height:60px;}
        .form-signin .log_yzm .lbox {float:left;width:50%;}
        .form-signin .log_yzm .rbox {float:right;width:40%;}
        .form-signin .log_yzm .rbox img {diaplay:block;width:120px;height:44px;}
    </style>

    <!-- Just for debugging purposes. Don't actually copy these 2 lines! -->
    <!--[if lt IE 9]><script src="${pageContext.request.contextPath}/js/ie8-responsive-file-warning.js"></script><![endif]-->
    <script src="${pageContext.request.contextPath}/js/ie-emulation-modes-warning.js"></script>

    <!-- HTML5 shim and Respond.js for IE8 support of HTML5 elements and media queries -->
    <!--[if lt IE 9]>
    <script src="//cdn.bootcss.com/html5shiv/3.7.2/html5shiv.min.js"></script>
    <script src="//cdn.bootcss.com/respond.js/1.4.2/respond.min.js"></script>
    <![endif]-->
    <script type="text/javascript">
        var ctx_sys = "${ctx_sys}";
        function refreshcode(obj) {
            obj.src = ctx_sys + "/captcha/?randomKey=${randomKey}&t=" + (new Date()).getTime();
        }
    </script>
</head>

<body>

<div class="container">
    <c:if test="${not empty msg}">
    <div class="alert alert-danger alert-dismissible" role="alert">
        <strong>错误!</strong> ${msg}
    </div>
    </c:if>

    <form class="form-signin" method="post" action="${pageContext.request.contextPath}/ucuser/login">
        <h2 class="form-signin-heading">用户登录</h2>
        <div class="topbox">
            <input type="text" name="loginName" value="${loginName}" class="form-control" placeholder="手机号" required autofocus>
            <input type="password" name="passwd" class="form-control" placeholder="密码" required>
            <input name="redirectUrl" type="hidden" value="${redirectUrl}" >
        </div>
        <c:if test="${requestScope.need_captcha}">
            <div class="log_yzm">
                <div class="lbox"><input type="text" name="captcha" class="form-control" placeholder="验证码" required></div>
                <div class="rbox"><img src="${ctx_sys}/captcha/?randomKey=${randomKey}" title="看不清？换一张" onclick="refreshcode(this);return false;" style="cursor: pointer;"/></div>
                <input name="randomKey" type="hidden" value="${randomKey}" >
            </div>
        </c:if>
        <button class="btn btn-lg btn-primary btn-block" type="submit">登录</button>
    </form>
</div> <!-- /container -->


<!-- IE10 viewport hack for Surface/desktop Windows 8 bug -->
<script src="${pageContext.request.contextPath}/js/ie10-viewport-bug-workaround.js"></script>
</body>
</html>
