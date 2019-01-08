<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>蓝源Eloan-P2P平台(系统管理平台)</title>
<#include "../common/header.ftl"/>
<script type="text/javascript" src="/js/plugins/jquery.form.js"></script>
<script type="text/javascript" src="/js/plugins/jquery.twbsPagination.min.js"></script>
<script type="text/javascript" src="/js/My97DatePicker/WdatePicker.js" ></script>

<script type="text/javascript">
	$(function() {

		$('#pagination').twbsPagination({
			totalPages : ${pageResult.totalPage}||1,
			startPage : ${pageResult.currentPage},
			visiblePages : 5,
			first : "首页",
			prev : "上一页",
			next : "下一页",
			last : "最后一页",
			onPageClick : function(event, page) {
				$("#currentPage").val(page);
				$("#searchForm").submit();
			}
		});
		
		$("#query").click(function(){
			$("#currentPage").val(1);
			$("#searchForm").submit();
		});
		
	});
</script>
</head>
<body>
	<div class="container">
		<#include "../common/top.ftl"/>
		<div class="row">
			<div class="col-sm-3">
				<#assign currentMenu="bidrequest_audit1_list" />
				<#include "../common/menu.ftl" />
			</div>
			<div class="col-sm-9">
				<div class="page-header">
					<h3>体验金管理</h3>
				</div>
				<form id="searchForm" class="form-inline" method="post" action="/expBidRequest_list">
					<input type="hidden" id="currentPage" name="currentPage" value=""/>
					<div class="form-group">
					    <label>状态</label>
					     <select class="form-control" name="bidRequestState" id="bidRequestState">

					    	<option value="-1">全部</option>
					    	<option value="0">待发布</option>
					    	<option value="1">招标中</option>
					    	<option value="2">已撤销</option>
					    	<option value="3">流标</option>
					    	<option value="4">满标1审</option>
					    	<option value="5">满标2审</option>
					    	<option value="6">满标审核被拒绝</option>
					    	<option value="7">还款中</option>
					    	<option value="8">已还清</option>
					    	<option value="9">逾期</option>
					    	<option value="10">发标审核拒绝状态</option>
					    	<option value="11">发标待审</option>
					    </select>
					    <script type="text/javascript">
					    	$("#bidRequestState option[value=${qo.bidRequestState}]").attr("selected",true);
					    </script>
					</div>
					<div class="form-group">
					    <label>发布时间</label>
					    <input class="form-control beginDate" type="text" name="beginDate" value='${(qo.beginDate?string("yyyy-MM-dd"))!""}'/>到
					    <input class="form-control endDate" type="text" name="endDate" value='${(qo.endDate?string("yyyy-MM-dd"))!""}'/>
					</div>
                    <div class="form-group">
                        <button id="query" type="submit" class="btn btn-success"><i class="icon-search"></i> 查询</button>
                    </div>
				</form>
				<div class="panel panel-default">
					<table class="table">
						<thead>
							<tr>
								<th>标题</th>
								<th>发标时间</th>
								<th>发布人</th>
								<th>体验标金额(元)</th>
								<th>期限</th>
								<th>利率</th>
								<th>总利息</th>
								<th>状态</th>
							</tr>
						</thead>
						<tbody>
						<#list pageResult.listData as data>
							<tr>
								<td>
									<a target="_blank" href="/borrow_info.do?id=${data.id}">${data.title}</a>&emsp;<span class="label label-primary">体</span>
								</td>
								<td>${(data.publishTime?string("yyyy-MM-dd HH:mm:SS"))!'未发布'}</td>
								<td>${data.bidRequestAmount}</td>
								<td>${data.createUser.username}</td>
								<td>${data.returnMonthes}月</td>
								<td>${data.currentRate}%</td>
								<td>${data.totalRewardAmount}</td>
								<td>${data.bidRequestStateDisplay}</td>
							</tr>
						</#list>
						</tbody>
					</table>
					<div style="text-align: center;">
						<ul id="pagination" class="pagination"></ul>
					</div>
				</div>
			</div>
		</div>
	</div>
</body>
</html>