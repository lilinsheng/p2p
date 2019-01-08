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

			});
		</script>

		<style type="text/css">
			.orderTime{
				width: 110px;
				height: 60px;
                border:1px solid #e0e0e0;
				margin: 5px;
				float: left;
				text-align: center;
				border-radius:5px;
				padding-top: 5px;
			}
			.orderBtn{
				border: 1px solid #e0e0e0;
				padding: 2px 5px;
                border-radius:5px;
				display: inline-block;
				margin-top: 1px;
                text-decoration:none !important;
				cursor: pointer;
			}
			.bg-info,.bg-success{
				padding: 10px;
				font-size: 16px;
			}
		</style>
		<script type="text/javascript">
			$(function(){
			  $(".orderBtn").click(function () {
			      	var timeId = $(this).data("timeid");
					$("#orderTimeId").val(timeId);

					$("#searchForm").ajaxSubmit(function (data) {
					    if(data.success){
					        $.messager.confirm("提示","恭喜您，约会成功",function () {
								window.location.href = "/videoAuditOrder";
                            });
						}else{
					        $.messager.alert("提示",data.msg);
						}
                    });
              });

			});


		</script>

	</head>
	<body>
	
		<!-- 网页顶部导航 -->
		<#include "common/head-tpl.ftl" />
		<!-- 网页导航 -->
		<#assign currentNav="personal" />
		<#include "common/navbar-tpl.ftl" />
		
		<div class="container">
			<div class="row">
				<!--导航菜单-->
				<div class="col-sm-3">
					<#assign currentMenu="ipLog" />
					<#include "common/leftmenu-tpl.ftl" />		
				</div>
				<!-- 功能页面 -->
				<div class="col-sm-9">
               		<p class="bg-info">视频认证审核时间预约</p>
					<#if videoSuccess??>
                        <p class="bg-success">您已经通过视频认证
                        </p>
					<#elseif videoAuditOrder??>
                    <p class="bg-success">预约成功：客服 [${videoAuditOrder.auditor.username}]
						,日期：[${videoAuditOrder.orderDate?string('yyyy-MM-dd')} ${videoAuditOrder.orderTime.begin} - ${videoAuditOrder.orderTime.end} ]
						<a>撤销预约</a>
					</p>

					<#else>
					<form action="/saveVideoAuditOrder" name="searchForm" id="searchForm" class="form-inline" method="post">
						<div class="form-group">
						    <label>预约客服</label>
						    <select class="form-control" name="auditor.id" id="auditor">
								<#list auditors as auditor>
						    		<option value="${auditor.id}"> ${auditor.username} </option>
								</#list>
						    </select>
						</div>
						&emsp;
						<div class="form-group">
						    <label>预约日期</label>
						    <select class="form-control" name="orderDate" id="orderDate">
								<#list orderDates as orderDate>
						    		<option value="${orderDate?string("yyyy-MM-dd")}">${orderDate?string("yyyy-MM-dd")}</option>
								</#list>
						    </select>
						</div>
						<input type="hidden" name="orderTime.id" id="orderTimeId" />
					</form>

					<div class="panel panel-default" style="margin-top: 20px;height: 400px">
						<div class="panel-heading">
							预约时间
						</div>
						<div>
							<#list orderTimes as orderTime>
							    <div class="orderTime">
									<div>
										${orderTime.begin} - ${orderTime.end}
									</div>
									<div>
										<a class="orderBtn" data-timeid="${orderTime.id}">点击预约</a>
									</div>

								</div>
							</#list>
						</div>
						<div style="text-align: center;">
                            <ul id="pagination" class="pagination-sm"></ul>
						</div>
					</div>


					</#if>

				</div>
			</div>
		</div>		
						
		<#include "common/footer-tpl.ftl" />
	</body>
</html>