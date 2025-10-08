<%@page import="datebase.Works"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>上架或修改信息</title>
</head>
<style>
    @font-face {
        font-family: 'icomoon';
        src: url('fonts/icomoon.eot?4148gq');
        src: url('fonts/icomoon.eot?4148gq#iefix') format('embedded-opentype'),
            url('fonts/icomoon.ttf?4148gq') format('truetype'),
            url('fonts/icomoon.woff?4148gq') format('woff'),
            url('fonts/icomoon.svg?4148gq#icomoon') format('svg');
        font-weight: normal;
        font-style: normal;
        font-display: block;
    }
	* {
		padding:0 0;
		margin: 0 0;
		font-family: "Microsoft YaHei", "微软雅黑", Arial, sans-serif;
	}
	table {
		flaot:left;
		margin-left: auto;
		margin-right: auto;
		margin-top: 15px;
		margin-bottom:20px;
		border: 1px solid rgba(204, 204, 204, 0.5);
		border-collapse: collapse;
	}
	th {
		margin:0 0;
		width:150px;
		height: 50px;
		color: white;
		line-height: 50px;
		text-align:center;
		background-color: rgba(204, 204, 204, 0.5);
	}
	th:nth-child(3) {
		width: 200px;
	}
	tr {
		color: black; 
		border-bottom: 1px solid rgba(204, 204, 204, 0.5);
	}
	td {
		margin:0 0;
		width:150px;
		height: 50px;
		line-height: 50px;
		text-align:center;

	}
	td:nth-child(3) {
		width: 200px;
	}
	h3 {
		display: inline-block;
		width:100px;
		height:20px;
		margin-left:800px;
		margin-top: 200px;
		font-size: 20px;
	}
	.add {
		display: inline-block;
		width: 100px;
		height: 35px;
		color: white;
		margin-left:400px;
		font-weight: 700;
		background-color: blue;
		border-radius: 20px;
		line-height: 35px;
		text-align: center;
	}
	.add:hover {
		transform: translateY(-3px); /* 向上移动3像素 */
        ox-shadow: 0 4px 8px rgba(0, 0, 0, 0.2); /* 添加阴影增强上浮效果 */
	}
	.add_new_good {
		position: absolute;
		width:500px;
		height:700px;
		top: 50%;
  		left: 50%;
 		transform: translate(-50%, -50%);
 		border-radius: 50px;
 		border: 1px solid rgba(204, 204, 204, 0.5);
 		display:none;
 		background-color: white;
 		box-shadow: 0 4px 12px rgba(0, 0, 0, 0.1);
 		z-index: 1;
	}
	.modify_good {
		position: absolute;
		width:500px;
		height:700px;
		top: 50%;
  		left: 50%;
 		transform: translate(-50%, -50%);
 		border-radius: 50px;
 		border: 1px solid rgba(204, 204, 204, 0.5);
 		display:none;
 		background-color: white;
 		box-shadow: 0 4px 12px rgba(0, 0, 0, 0.1);
 		z-index: 1;
	}
	img {
		width:70%;
		height: 60%;
		margin-left:auto;
		margin-top:20px; 
	}
	tr td:nth-child(4) {
	font-size:15px;
	line-height: 15px;
}
	.available {
		display:inline-block;
		background-color:red;
		width:100px;
		height: 35px;
		border-radius: 10px;
		line-height: 35px;
		color: white;
	}
	.frozen {
		display:inline-block;
		background-color: #dee2e6;
		width:100px;
		height: 35px;
		border-radius: 10px;
		line-height: 35px;
		color: white;
	}
	.sold {
		display:inline-block;
		background-color:#495057;
		width:100px;
		height: 35px;
		border-radius: 10px;
		line-height: 35px;
		color: white;
	}

	ul {
		margin-left:30px;
	}
	li {
		list-style: none;
		margin-top:20px;
		padding-left:15px;
		width: 430px;
		height: 65px;
	}
	li input{
		margin-left:10px;
		margin-top:5px;
		width: 300px;
		height: 50px;
		font-size: 30px;
	}
	li:hover{
		background-color:rgba(0, 123, 255, 0.25);
		transform: translateY(-3px); /* 向上移动3像素 */
        ox-shadow: 0 4px 8px rgba(0, 0, 0, 0.2); /* 添加阴影增强上浮效果 */
	}
	li select{
		margin-left:10px;
		margin-top:5px;
		width: 300px;
		height: 50px;
		font-size: 30px;
	}
	.title {
		width:200px;
		height:50px;
		font-size: 40px;
		margin:20px auto;
		text-align: center;
	}
	input[type="submit"] {
		display:inline-block;
		width: 100px;
		height: 30px;
		font-size: 20px;
		text-align:center;
		line-height:30px;
		margin-top:20px;
		margin-left:220px;
		background-color: skyblue;
		border:none;
		border-radius: 10px;
		}
	input[type="buttom"] {
		display:inline-block;
		width: 100px;
		height: 30px;
		text-align:center;
		line-height:30px;
		font-size: 20px;
		margin-top:20px;
		margin-left:20px;
		background-color: skyblue;
		border:none;
		border-radius: 10px;
	}
	input[type="buttom"]:hover {
		outline: none;
  		box-shadow: 0 0 0 3px rgba(0, 123, 255, 0.25);
  		transform: translateY(-3px); /* 向上移动3像素 */
        ox-shadow: 0 4px 8px rgba(0, 0, 0, 0.2); /* 添加阴影增强上浮效果 */
	}
	input[type="submit"]:hover {
		outline: none;
  		box-shadow: 0 0 0 3px rgba(0, 123, 255, 0.25);
  		transform: translateY(-3px); /* 向上移动3像素 */
        ox-shadow: 0 4px 8px rgba(0, 0, 0, 0.2); /* 添加阴影增强上浮效果 */
	}
	.modify {
		display:inline-block;
		margin-left:10px;
		background-color:#87CEEB;
		width:125px;
		height: 35px;
		margin-top:15px;
		border-radius: 10px;
		line-height: 35px;
		color: white;
	}
	.modify:hover {
		transform: translateY(-3px); /* 向上移动3像素 */
        ox-shadow: 0 4px 8px rgba(0, 0, 0, 0.2); /* 添加阴影增强上浮效果 */
	}
	tr:nth-child(n+2):hover {
		background-color: #F0F8FF;
		transform: translateY(-3px); /* 向上移动3像素 */
        ox-shadow: 0 4px 8px rgba(0, 0, 0, 0.2); /* 添加阴影增强上浮效果 */
	}
	.tips {
		font-size: 15px;
		color: grey;
		margin-left: 300px;
	}
	nav {
		width: 100%;
		height: 50px;
		background-color:  #1a3e72;
		color: white;
		font-size: 20px;
	}
	nav span {
		display:inline-block;
		width:200px;
		height:50px;
		line-height: 50px;
		margin-left: 20px;
	}
	nav span a {
		display:inline-block;
		width:150px;
		height:50px;
		line-height: 50px;
		margin-left: 1200px;
		color: inherit;           /* 继承父元素文字颜色 */
		text-decoration: none;    /* 去除下划线 */
		background-color: transparent; /* 去除背景色 */
		outline: none;            /* 去除焦点轮廓 */
		cursor: pointer;          /* 保持手型指针 */
	}
	nav span a:hover {
		transform: translateY(-3px); /* 向上移动3像素 */
        ox-shadow: 0 4px 8px rgba(0, 0, 0, 0.2); /* 添加阴影增强上浮效果 */
	}
</style>
<body>
	<nav>
		<span>上架/更改商品页面</span>
		<span><a href="merchant_homepage.jsp">返回商家首页</a></span>
	</nav>
		<h3>商品信息</h3>
		<span class="add">
			上架商品
		</span>
 		<div class="add_new_good">
			<form action="insert_goodServlet" method="post" onsubmit="return validateForm()" name="add">
				<div class="title">商品信息</div>
				<ul>
					<li>商品名称:<input type="text" name="work_name"> </li>
					<li>商品价格:<input type="text" name="work_price"> </li>
					<li>商品描述:<input type="text" name="work_description"> </li>
					<li>商品图片:<input type="text" name="work_image"> 
					<li>商品状态:<select name="work_status">
              	  			<option value="frozen">冻结</option>
               		 		<option value="available">可预订</option>
               		 		<option value="sold">售罄</option>
            			</select>
            		</li>
            	</ul>
					<input type="submit" value="确认上架">
					<input type="buttom" class="addbtm" value="取消">
			</form>
		</div> 
 
	<table>
		<th>商品Id</th>
		<th>商品名称</th>
		<th>商品价格</th>
		<th>商品描述</th>
		<th>商品图片</th>
		<th>商品状态</th>
		<th>操作</th>
		<c:forEach items="${sessionScope.wklist}" var="wk" >
		<tr>
		<td>${wk.getId()}</td>
		<td>${wk.getWork_name()}</td>
		<td>￥${wk.getWork_price()}</td>
		<td>${wk.getWork_description()}</td>
		<td><img src="${wk.getWork_image()}"></td>
		<c:if test="${wk.getWork_status() eq 'available'}">
			<td><span class="available">可预订</span></td>
			<td class="modify" name="id" value="${wk.getId()}">更改商品信息</td>
		</c:if>
		<c:if test="${wk.getWork_status() eq 'frozen'}">
			<td><span class="frozen">冻结</span></td>
			<td class="modify" name="id" value="${wk.getId()}" style="background-color: #66BB6A;">更改商品状态</td>
		</c:if>
		<c:if test="${wk.getWork_status() eq 'sold'}">
			<td><span class="sold">售罄</span></td>
			<td class="modify" name="id" value="${wk.getId()}" style="background-color: #66BB6A;">更改商品状态</td>
		</c:if>
		</tr>
		<div class="modify_good">
			<form action="modify_goodStatusServlet" method="post"  onsubmit="return validateFormmodify()">
				<div class="title">商品信息</div>
				<ul>
					<c:if test="${wk.getWork_status() eq 'available'}">
					<li>商品名称:<input type="text" name="work_name1" value="${wk.getWork_name()}"> </li>
					<li>商品价格:<input type="text" name="work_price1" value="${wk.getWork_price()}"> </li>
					<li>商品描述:<input type="text" name="work_description1" value="${wk.getWork_description()}"> </li>
					<li>商品图片:<input type="text" name="work_image1" value="${wk.getWork_image()}"> 
					<li>商品状态:<select name="work_status1">
              	  			<option value="frozen" >冻结</option>
               		 		<option value="available" selected>可预订</option>
               		 		<option value="sold">售罄</option>
            			</select>
            		</li>
            		</c:if>
            		<c:if test="${wk.getWork_status() eq 'frozen'}">
            		<li>商品名称:<input type="text" name="work_name1" readonly value="${wk.getWork_name()}"> </li>
					<li>商品价格:<input type="text" name="work_price1" readonly value="${wk.getWork_price()}"> </li>
					<li>商品描述:<input type="text" name="work_description1" readonly value="${wk.getWork_description()}"> </li>
					<li>商品图片:<input type="text" name="work_image1" readonly  value="${wk.getWork_image()}"> 
					<li>商品状态:<select name="work_status1">
              	  			<option value="frozen" selected>冻结</option>
               		 		<option value="available">可预订</option>
               		 		<option value="sold">售罄</option>
            			</select>
            		</li>
            		</c:if>
            		<c:if test="${wk.getWork_status() eq 'sold'}">
					<li>商品名称:<input type="text" name="work_name1" readonly value="${wk.getWork_name()}"> </li>
					<li>商品价格:<input type="text" name="work_price1" readonly value="${wk.getWork_price()}"> </li>
					<li>商品描述:<input type="text" name="work_description1" readonly value="${wk.getWork_description()}"> </li>
					<li>商品图片:<input type="text" name="work_image1" readonly  value="${wk.getWork_image()}"> 
					<li>商品状态:<select name="work_status1">
              	  			<option value="frozen" >冻结</option>
               		 		<option value="available">可预订</option>
               		 		<option value="sold" selected>售罄</option>
            			</select>
            		</li>
            		</c:if>
            		<input type="hidden" name="work_id" value="${wk.getId()}">
            	</ul>
					<input type="submit" value="确认修改">
					<input type="buttom" class="modifybtm" value="取消">
			</form>
		</div>
	</c:forEach>
	</table>
	<span class="tips">tips:售罄与冻结状态下，商品只允许修改商品状态这一信息，可预订状态下商品信息可随意修改</span>
	<script type="text/javascript">
		const add = document.querySelector('.add');
		const addbtm = document.querySelector('.addbtm');
		const add_new_good = document.querySelector('.add_new_good');
		const modify = document.querySelectorAll('.modify');
		const modify_good = document.querySelectorAll('.modify_good');
		const modify_buttom = document.querySelectorAll('.modifybtm');
		add.addEventListener('click',function(){
			add_new_good.style.display = 'block';
			document.body.style.backgroundColor = '#EEEEEE'; 
		})
		for(let i = 0;i<modify.length;i++){
			modify[i].addEventListener('click',function(e){
				e.preventDefault();
				modify_good[i].style.display = 'block';
				document.body.style.backgroundColor = '#EEEEEE'; 
			})
		}
		for(let i = 0;i<modify.length;i++){
			modify_buttom[i].addEventListener('click',function(){
				modify_good[i].style.display = 'none';
				document.body.style.backgroundColor = 'white'; 
			})
		}
		addbtm.addEventListener('click',function(e){
			e.preventDefault();
			add_new_good.style.display = 'none';
			document.body.style.backgroundColor = 'white'; 
		})
	function validateForm() {
    const form = document.forms['add'];
    const workName = form.work_name.value.trim();
    const workPrice = form.work_price.value.trim();
    const workDescription = form.work_description.value.trim();
    const workImage = form.work_image.value.trim();
    // 非空检验
    if (!workName) {
        alert('商品名称不能为空');
        form.work_name.focus();
        return false;
    }
    
    if (!workPrice) {
        alert('商品价格不能为空');
        form.work_price.focus();
        return false;
    }
    
    if (!workDescription) {
        alert('商品描述不能为空');
        form.work_description.focus();
        return false;
    }
    
    if (!workImage) {
        alert('商品图片不能为空');
        form.work_image.focus();
        return false;
    }
    
    return true;
}
	 function validateFormmodify() {
		 const form = event ? event.target : this.form;
	    const workName = form.work_name1.value.trim();
	    const workPrice = form.work_price1.value.trim();
	    const workDescription = form.work_description1.value.trim();
	    const workImage = form.work_image1.value.trim();
	    // 非空检验
	    if (!workName) {
	        alert('商品名称不能为空');
	        form.work_name1.focus();
	        return false;
	    }
	     
	    if (!workPrice) {
	        alert('商品价格不能为空');
	        form.work_price1.focus();
	        return false;
	    }
	    
	    if (!workDescription) {
	        alert('商品描述不能为空');
	        form.work_description1.focus();
	        return false;
	    }
	    
	    if (!workImage) {
	        alert('商品图片不能为空');
	        form.work_image1.focus();
	        return false;
	    }
	    
	    return true;
	} 
	</script>
</body>
</html>