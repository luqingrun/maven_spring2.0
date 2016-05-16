<%--
  Created by IntelliJ IDEA.
  User: luqingrun
  Date: 16/3/22
  Time: 下午12:48
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Title</title>
    <script type="text/javascript" src="http://www.gongsibao.com/statics/js/home/jquery.min.js"></script>
</head>
<body>
gongsibao-sy
<br /><br /><br />
类型编号：<input type="text" id="type" name="type" /><br /><br />
值：<input type="text" id="value" name="value" style="width: 500px;" /><br /><br />
<input type="button" id="submit" name="submit" value="提交" />
<br /><br />
<div style="width: 680px; padding:10px; border: 1px solid #cccccc;">
    规则说明：<br />
    类型编号是字典中的分类(即type字段)<br />
    值为要添加的字典值，以、(汉字顿号)分隔<br />
    父级必须比子级先添加<br />
    子级添加规则需将父级id添加到字典值之前，以 (英文空格)为分隔<br /><br /><br />
    一级字典添加示例：<br />
    <img src="img/master.png" style=" border: 1px solid #cccccc;" /><br /><br />
    子级字典添加示例：<br />
    <img src="img/child.png" style=" border: 1px solid #cccccc;" />
</div>
<script>
 $("#submit").click(function(){
     if($("#type").val().trim() == "")
     {
         alert("类型编号不能为空");
     }
     if($("#value").val().trim() == "")
     {
         alert("值不能为空");
     }
     $.post("/gongsibao-sys/dict/addbatch",
     {
         "type":$("#type").val().trim(),
         "value":$("#value").val().trim()
     },
     function (data) {
         data = JSON.parse(data);
        alert(data.msg);
     });
 });
</script>
</body>
</html>
