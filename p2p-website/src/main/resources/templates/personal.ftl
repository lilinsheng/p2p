<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
		<title>蓝源Eloan-P2P平台</title>
		<!-- 包含一个模板文件,模板文件的路径是从当前路径开始找 -->
		<#include "common/links-tpl.ftl" />
		<script type="text/javascript" src="/js/plugins/jquery.form.js"></script>
		<link type="text/css" rel="stylesheet" href="/css/account.css" />
		
		<script type="text/javascript">
			$(function () {
                $("#rechargeOnline").click(function () {
                    $("#myModel").modal("show");
                });

                $(".btn_recharge").click(function () {
                    $("#rechargeForm").submit();
                });

                //领取体验金
                $("#expGold").click(function () {
					$.post("/expGold",function (data) {
						if (data.success){
						    $.messager.confirm("提示","体验金领取成功",function () {
								window.location.reload();
                            })
						}else {
						    $.messager.alert("提示",data.msg);
						}
                    })
                });
            });

		</script>
	</head>
	<body>
		<#assign currentNav="personal">

		<!-- 网页顶部导航 -->
		<#include "common/head-tpl.ftl" />
		<!-- 网页导航 -->
		<!-- 在当前的freemarker的上下文中,添加一个变量,变量的名字叫nav,变量的值叫personal -->
		<#include "common/navbar-tpl.ftl" />
		
		<div class="container">
			<div class="row">
				<!--导航菜单-->
				<div class="col-sm-3">
					<#include "common/leftmenu-tpl.ftl" />
				</div>
				<!-- 功能页面 -->
				<div class="col-sm-9">
					<div class="panel panel-default">
						<div class="panel-body el-account">
							<div class="el-account-info">
								<div class="pull-left el-head-img">
									<img class="icon" src="/images/ms.png" />
								</div>
								<div class="pull-left el-head">
									<p>用户名：${loginInfo.nickName}</p>
									<p>最后登录时间：</p>
								</div>
								<div class="pull-left" style="text-align: center;width: 400px;margin:30px auto 0px auto;">
									<a class="btn btn-primary btn-lg" href="/recharge">账户充值</a>
									<a class="btn btn-primary btn-lg" id="rechargeOnline">线上充值</a>
									<a class="btn btn-danger btn-lg" href="/moneyWithdraw">账户提现</a>
									<a class="btn btn-danger btn-lg" href="/bind_phone">绑定手机</a>
									<a class="btn btn-danger btn-lg" href="javascript:;" id="expGold">体验金礼包</a>
								</div>
								<div class="clearfix"></div>
							</div>
							
							<div class="row h4 account-info">
								<div class="col-sm-4">
									账户总额：<span class="text-primary">${account.allAmount?string("#0.00")}元</span>
								</div>
								<div class="col-sm-4">
									可用金额：<span class="text-primary">${account.usableAmount?string("#0.00")}元</span>
								</div>
								<div class="col-sm-4">
									冻结金额：<span class="text-primary">${account.freezedAmount?string("#0.00")}元</span>
								</div>
							</div>
							
							<div class="row h4 account-info">
								<div class="col-sm-4">
									待收利息：<span class="text-primary">${account.unReceiveInterest?string("#0.00")}元</span>
								</div>
								<div class="col-sm-4">
									待收本金：<span class="text-primary">${account.unReceivePrincipal?string("#0.00")}元</span>
								</div>
								<div class="col-sm-4">
									待还本息：<span class="text-primary">${account.unReturnAmount?string("#0.00")}元</span>
								</div>
							</div>
							<div class="row h4 account-info">
								<div class="col-sm-4">
									体验金余额：<span class="text-primary">${(expAccount.usableAmount?string("#0.00"))!'0.00'}元</span>
								</div>
								<div class="col-sm-4">
									体验金冻结金额：<span class="text-primary">${(expAccount.freezedAmount?string("#0.00"))!'0.00'}元</span>
								</div>
								<div class="col-sm-4">
									未回收本金：<span class="text-primary">${(expAccount.unReturnExpAmount?string("#0.00"))!'0.00'}元</span>
								</div>
							</div>

							<div class="el-account-info top-margin">
								<div class="row">
									<div class="col-sm-4">
										<div class="el-accoun-auth">
											<div class="el-accoun-auth-left">
												<img src="images/shiming.png" />
											</div>
											<div class="el-accoun-auth-right">
												<h5>实名认证</h5>
												<p>
													未认证
													<a href="/realAuth" id="">立刻绑定</a>
												</p>
											</div>
											<div class="clearfix"></div>
											<p class="info">实名认证之后才能在平台投资</p>
										</div>
									</div>
									
									<div class="col-sm-4">
										<div class="el-accoun-auth">
											<div class="el-accoun-auth-left">
												<img src="images/youxiang.jpg" />
											</div>
											<div class="el-accoun-auth-right">
												<h5>邮箱认证</h5>
										
												<p>
													未绑定
													<a href="javascript:;" id="showBindEmailModal">去绑定</a>
												</p>
												
											</div>
											<div class="clearfix"></div>
											<p class="info">您可以设置邮箱来接收重要信息</p>
										</div>
									</div>
									
									<div class="col-sm-4">
										<div class="el-accoun-auth">
											<div class="el-accoun-auth-left">
												<img src="images/baozhan.jpg" />
											</div>
											<div class="el-accoun-auth-right">
												<h5>VIP会员</h5>
												<p>
													普通用户
													<a href="#">查看</a>
												</p>
											</div>
											<div class="clearfix"></div>
											<p class="info">VIP会员，让你更快捷的投资</p>
										</div>
									</div>
								</div>
								
							</div>
						</div>
					</div>
				</div>
			</div>
		</div>		
		
		<#include "common/footer-tpl.ftl" />

    <div id="myModel" class="modal" tabindex="-1" role="dialog">
        <div class="modal-dialog modal-sm" role="document">
            <div class="modal-content">
                <div class="modal-header">
                    <button type="button" class="close" data-dismiss="modal"
                            aria-label="Close">
                        <span aria-hidden="true">&times;</span>
                    </button>
                    <h4 class="modal-title">线上充值</h4>
                </div>
                <div class="modal-body">
                    <form id="rechargeForm" class="form-horizontal" method="post" action="/rechargeOnline">
                        <div class="form-group">
                            <div class="col-sm-12">
                                <div class="dropdown" id="autocomplate">
                                    金额：<input  class="form-control" id="amount" name="amount" />
                                </div>
                            </div>
                        </div>
                    </form>
                </div>
                <div class="modal-footer">
                    <button type="button" class="btn btn-success btn_recharge">充值</button>
                    <button type="button" class="btn btn-default" data-dismiss="modal">关闭</button>
				</div>
            </div>
        </div>
    </div>
	</body>
</html>