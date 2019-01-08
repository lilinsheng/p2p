<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
		<title>蓝源Eloan-P2P平台</title>
		<#include "common/links-tpl.ftl" />
		<link type="text/css" rel="stylesheet" href="/css/account.css" />
		<script type="text/javascript" src="/js/plugins/jquery.twbsPagination.min.js"></script>
		<script type="text/javascript" src="/js/plugins-override.js"></script>
		<script type="text/javascript" src="/js/My97DatePicker/WdatePicker.js"></script>
		<script type="text/javascript" src="/js/plugins/jquery.form.js"></script>
		<script type="text/javascript">
			$(function(){
                $('#pagination').twbsPagination({
                    totalPages:${pageResult.totalPage}||1,
                    visiblePages: 5,
                    startPage:${pageResult.currentPage},
                    onPageClick:function(event,page){
                        $("#currentPage").val(page);
                        $("#searchForm").submit();
                    }
                });

                //撤消债券标
				$(".cancel").click(function() {
					var bidId = $(this).data("rid");
					$.ajax({
						data:{bidId:bidId},
						url:"cancelMyTrans",
						type:"post",
						success:function(data){
						    if (data.success){
						        $.messager.confirm("提示","撤消成功",function(){
						            window.location.reload();
								})
							}else {
						        $.messager.alert("提示",data.msg);
							}
						}
					})
                });
            });
		</script>
	</head>
	<body>
	
		<!-- 网页顶部导航 -->
		<#include "common/head-tpl.ftl" />
		<!-- 网页导航 -->
		<#assign currentNav="listMyTransferCredit" />
		<#include "common/navbar-tpl.ftl" />
		
		<div class="container">
			<div class="row">
				<!--导航菜单-->
				<div class="col-sm-3">
					<#assign currentMenu="listMyTransferCredit" />
					<#include "common/leftmenu-tpl.ftl" />		
				</div>
				<!-- 功能页面 -->
				<div class="col-sm-9">
					<form action="/listMyTransferCredit" name="searchForm" id="searchForm" class="form-inline" method="post">
						<input type="hidden" id="currentPage" name="currentPage" value="" />
						<div class="form-group">
						</div>
					</form>
					
					<div class="panel panel-default" style="margin-top: 20px;">
						<div class="panel-heading">
							转让中的债权
						</div>
						<table class="table table-striped">
							<thead>
								<tr>
								    <th></th>
									<th>借款</th>
									<th>剩余期数</th>
									<th>年化利率</th>
									<th>回收金额</th>
									<th>最近还款日</th>
									<th>损失利息</th>
									<th></th>
								</tr>
							</thead>
							<tbody>
								<#list pageResult.listData as item>
									<tr>
									    <th></th>
										<th>${item.bidRequestTitle}</th>
										<th>${item.remainMonthIndex}</th>
										<th>${item.currentRate!""}%</th>
										<th>${item.bidRequestAmount}</th>
										<th>${item.closestDeadLine?string("yyyy-MM-dd")}</th>
										<th>${item.remainInterest}</th>
										<th>
										   <a class="btn btn-success cancel" data-rid="${item.id}">撤销债权标</a>
										</th>
									</tr>
								</#list>
							</tbody>
						</table>
						<div style="text-align: center;">
                            <ul id="pagination" class="pagination-sm"></ul>
						</div>
					</div>
				</div>
			</div>
		</div>		
						
		<#include "common/footer-tpl.ftl" />
	</body>
</html>