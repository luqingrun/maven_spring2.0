<%--
  Created by IntelliJ IDEA.
  User: wk
  Date: 2016/3/26
  Time: 11:33
  To change this template use File | Settings | File Templates.
--%>
<%@ page language="java" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>

<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>oss上传测试</title>

    <%--<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/js/webuploader/webuploader.css">--%>

    <%--<script type="text/javascript" src="${pageContext.request.contextPath}/js/jquery-1.11.0.min.js"></script>--%>
    <%--<!--引入JS-->--%>
    <%--<script type="text/javascript" src="${pageContext.request.contextPath}/js/webuploader/webuploader.min.js"></script>--%>

    <%--<script>--%>
        <%--var contextPath = "${pageContext.request.contextPath}";--%>
        <%--$(function () {--%>
            <%--var uploader = WebUploader.create({--%>
                <%--auto: true,--%>
                <%--// swf文件路径--%>
                <%--swf: contextPath + '/js/webuploader/Uploader.swf',--%>

                <%--// 文件接收服务端。--%>
                <%--server: contextPath + '/oss/upload/',--%>

                <%--// 选择文件的按钮。可选。--%>
                <%--// 内部根据当前运行是创建，可能是input元素，也可能是flash.--%>
                <%--pick: '#filePicker',--%>

                <%--// 不压缩image, 默认如果是jpeg，文件上传前会压缩一把再上传！--%>
                <%--resize: false,--%>
                <%--// 只允许选择图片文件。--%>
                <%--accept: {--%>
                    <%--title: 'images',--%>
                    <%--extensions: 'gif,jpg,jpeg,bmp,png',--%>
                    <%--mimeTypes: 'image/*'--%>
                <%--}--%>
            <%--}).on( 'uploadSuccess', function(file, res) {--%>
                <%--alert(res.id);--%>
                <%--$("#picName").text((res.fileName || ""));--%>
                <%--$("#img").attr("src", (res.url || "")).show();--%>
            <%--}).on( 'uploadError', function(res) {--%>
                <%--// 避免重复创建--%>
                <%--alert('上传失败');--%>
            <%--});--%>
        <%--});--%>
    <%--</script>--%>
</head>
<body 123123213>
    <%--<img src="${pageContext.request.contextPath}/captcha/?randomKey=${randomKey}">--%>
    <span>上传</span>
    <form action="${pageContext.request.contextPath}/oss/upload" enctype="multipart/form-data" target="_blank" method="post">
        <input type="file"name="file" />
        <input type="submit" value="提交" />
    </form>

    <div style="height: 50px"></div>

    <%--<img src="img/loading.gif" id="loading" style="display: none;">--%>
    <br />

    <%--<div id="uploader-demo">--%>
        <%--<!--用来存放item-->--%>
        <%--<div id="fileList" class="uploader-list"></div>--%>
        <%--<div id="filePicker">选择图片</div>--%>
        <%--<span id="picName"></span>--%>
        <%--<br>--%>
        <%--<img id="img" src="" width="200" style="display: none" />--%>
    <%--</div>--%>

    <span>上传并入库</span>
    <form action="${pageContext.request.contextPath}/oss/uploadAndSave" enctype="multipart/form-data" target="_blank" method="post">
        <input type="file"name="file" />
        <input type="hidden" name="tabName" value="so_pay" />
        <input type="submit" value="提交" />
    </form>

</body>
</html>
