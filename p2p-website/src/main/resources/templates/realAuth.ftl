<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
		<title>蓝源Eloan-P2P平台</title>
		<#include "common/links-tpl.ftl" />
		<link type="text/css" rel="stylesheet" href="/css/account.css" />
		<link type="text/css" rel="stylesheet" href="/js/plugins/uploadifive/uploadifive.css" />
		<script type="text/javascript" src="/js/plugins/jquery.form.js"></script>
        <script type="text/javascript" src="/js/My97DatePicker/WdatePicker.js"></script>
        <script type="text/javascript" src="/js/plugins/uploadifive/jquery.uploadifive.min.js"></script>
        <script type="text/javascript" src="/js/plugins/jquery-validation/jquery.validate.js"></script>
		<style type="text/css">
			#realAuthForm input ,#realAuthForm select{
				width: 260px;
			}
			.idCardItem{
				width: 160px;
				height: 150px;
				float:left;
				margin-right:10px;
				border: 1px solid #e3e3e3;
				text-align: center;
				padding: 5px;
			}
			.uploadImg{
				width: 120px;
				height: 100px;
				margin-top: 5px;
			}

			#viewExample{
				position: relative;
				left: 50px;
				top: 60px;
			}
			.uploadExample{
				width: 200px;
				height: 130px;
				margin-bottom: 20px;
			}
			.uploadifive-button{
				width: 100% !important;
                border-radius:5 !important;
			}
			.uploadifive-queue{
				display: none;
			}
		</style>
		<script type="text/javascript">
		
		$(function(){

            $("#uploadBtn1").uploadifive({
                'auto' : true,		//自动上传
                'uploadScript' : '/uploadPhoto',	//上传地址
                'fileObjName' : 'file',	//文件名
                'buttonText' : '上传照片',	//按钮文字
                'fileType' : 'image',	//上传的文件的类型
                'multi' : false,		//多文件上传
                'fileSizeLimit'   : 5242880,	//文件大小
                'uploadLimit' : 10,		//一次可以上传的最大文件数
                'onUploadComplete' : function(file, data) {	//上传成功后

                    //1.处理回显
                    $("#uploadImg1").attr("src",data);

                    //2.设置图片地址到隐藏域
                    $("#uploadImage1").val(data);
                },
                onFallback : function() {
                    $.messager.alert("提示","该浏览器无法使用");
                },
                onUpload : function(file) {
                    document.getElementById("realAuthForm").disabled = true;//当开始上传文件，要防止上传未完成而表单被提交
                }
            });

            $("#uploadBtn2").uploadifive({
                'auto' : true,		//自动上传
                'uploadScript' : '/uploadPhoto',	//上传地址
                'fileObjName' : 'file',	//文件名
                'buttonText' : '上传照片',	//按钮文字
                'fileType' : 'image',	//上传的文件的类型
                'multi' : false,		//多文件上传
                'fileSizeLimit'   : 5242880,	//文件大小
                'uploadLimit' : 10,		//一次可以上传的最大文件数
                'onUploadComplete' : function(file, data) {	//上传成功后

                    //1.处理回显
                    $("#uploadImg2").attr("src",data);

                    //2.设置图片地址到隐藏域
                    $("#uploadImage2").val(data);
                },
                onFallback : function() {
                    $.messager.alert("提示","该浏览器无法使用");
                },
                onUpload : function(file) {
                    document.getElementById("realAuthForm").disabled = true;//当开始上传文件，要防止上传未完成而表单被提交
                }
            });
            

			$("#viewExample").popover({
				html:true,
				content:'正面<img src="/images/upload_source_2.jpg" class="uploadExample"/><br/>反面<img src="/images/upload_source_2_1.jpg" class="uploadExample"/>',
				trigger:"hover",
				placement:"top"
			});


    

            /**
             * 添加自定义的校验规则
             */
            jQuery.validator.addMethod("isIdNumber", function(value, element) {
                var reg = /(^\d{15}$)|(^\d{18}$)|(^\d{17}(\d|X|x)$)/;
                return reg.test(value);
            }, "无效的身份证号码") ;

            jQuery.validator.addMethod("isRealName", function(value, element) {
                var reg = /^[\u4E00-\u9FA5A-Za-z]+$/;
                return reg.test(value);
            }, "这是你的姓名???") ;

            $("#realAuthForm").validate({
                //当表单所有元素验证通过 ,触发该函数 ,form就是验证的表单,
                //该方法会阻止默认的表单提交 ,我们需要手动的在这个回调函数中,使用ajax提交表单
                submitHandler: function(form) {
                    $(form).ajaxSubmit(function(data){
                        if(data.success){
                            $.messager.confirm("提示" ,"提交成功",function () {
                                window.location.href = "/realAuth?time="+new Date().getTime();
                            })
                        }else{
                            $.messager.alert("提示",data.msg) ;
                        }
                    });
                },
                //highlight :当某个元素验证码不通过时,触发该函数
                // ,element 就是当前验证不通过的元素,errorClass就是错误的样式
                highlight: function(element, errorClass) {
                    $(element).fadeOut(function() {
                        $(element).fadeIn();
                    });

                    $(element).parent().parent().addClass("has-error").removeClass("has-success");

                },
                //unhighlight:当某一个表单元素验证通过,触发该函数
                unhighlight: function(element, errorClass) {
                    $(element).closest("div.form-group").addClass("has-success").removeClass("has-error");
                },
                //errorClass:修改默认的错误样式
                errorClass:"text-danger",
                rules: {
                    realName: {
                        required:true,
                        rangelength:[2,4],
                        'isRealName':'isRealName'
                    },
                    idNumber: {
                        required:true,
						'isIdNumber':'isIdNumber'
                    },
                    sex: {
                        required:true,
                    },
                    bornDate: {
                        required:true,
                        date:true
                    },
                    address: {
                        required:true
                    },
                    image1: {
                        required:true
                    },
                    image2: {
                        required:true
                    }
                },
                messages: {
                    realName: {
                        required:"姓名不可为空",
                        rangelength:"姓名{0}-{1} 位"
                    },
                    idNumber: {
                        required:"身份证不可为空",
                    },
                    sex: {
                        required:"不可为空",
                    },
                    bornDate: {
                        required:"不可为空",
                        date:"无效的日期格式"
                    },
                    address: {
                        required:"不可为空"
                    },
                    image1: {
                        required:"不可为空"
                    },
                    image2: {
                        required:"不可为空"
                    }
                }
            });



		});
		</script>
	</head>
	<body>
		<!-- 网页顶部导航 -->
		<#include "common/head-tpl.ftl"/>
		<#assign currentNav="personal" />
		<#include "common/navbar-tpl.ftl" />

		<div class="container">
			<div class="row">
				<!--导航菜单-->
				<div class="col-sm-3">
					<#assign currentMenu="realAuth"/>
					<#include "common/leftmenu-tpl.ftl" />
				</div>
				<!-- 功能页面 -->
				<div class="col-sm-9">
					<div class="panel panel-default">
						<div class="panel-heading">
							实名认证
						</div>
							<form class="form-horizontal" id="realAuthForm" method="post" action="/realAuth_save" novalidate="novalidate">
							<fieldset>
								<div class="form-group">
									<p class="text-center text-danger">为保护您账户安全，实名认证成功之后不能修改信息，请认真填写！</p>
								</div>
								<div class="form-group">
						        	<label class="col-sm-4 control-label ">用户名</label>
					        		<div class="col-sm-8">
						        		<p class="form-control-static">${loginInfo.username }</p>
						        	</div>
						        </div>
								<div class="form-group">
						        	<label class="col-sm-4 control-label" for="realName">姓名</label>
					        		<div class="col-sm-8">
						        		<input id="realName" name="realName" class="form-control" type="text" value="">
						        	</div>
						        </div>
						        <div class="form-group">
						        	<label class="col-sm-4  control-label" for="sex">性别</label>
					        		<div class="col-sm-8">
						        		<select id="sex" class="form-control" name="sex" size="1">
											<option value="0">男</option>
											<option value="1">女</option>
										</select>
						        	</div>
						        </div>
						        <div class="form-group">
						        	<label class="col-sm-4  control-label" for="idnumber">证件号码</label>
					        		<div class="col-sm-8">
						        		<input id="idNumber" class="form-control" name="idNumber"  type="text" value="">
						        	</div>
						        </div>
						        <div class="form-group">
						        	<label class="col-sm-4  control-label" for="bornDate">出生日期</label>
					        		<div class="col-sm-8">
						        		<input id="birthDate" onclick="WdatePicker()" class="form-control" name="bornDate" type="text">
						        	</div>
						        </div>
						        <div class="form-group">
						        	<label class="col-sm-4  control-label" for="address">证件地址</label>
					        		<div class="col-sm-8">
						        		<input id="address" class="form-control" name="address"  type="text" style="max-width: 100%;width:500px;">
						        	</div>
						        </div>
						        
						        <div class="form-group">
						        	<label class="col-sm-4  control-label" for="address">身份证照片</label>
					        		<div class="col-sm-8">
					        			<p class="text-help text-primary">请点击“选择图片”,选择证件的正反两面照片。</p>
					        			<a href="javascript:;" id="viewExample">查看样板</a>
					        			<div class="idCardItem">
                                            <div>
                                                <a href="javascript:;" id="uploadBtn1" >上传正面</a>
                                            </div>
					        				<img alt="" src="" class="uploadImg" id="uploadImg1" />
					        				<input type="hidden" name="image1" id="uploadImage1" />
					        			</div>
					        			<div class="idCardItem">
					        				<div>
					        					<a href="javascript:;" id="uploadBtn2" >上传反面</a>
					        				</div>
					        				<img alt="" src="" class="uploadImg" id="uploadImg2"/>
					        				<input type="hidden" name="image2" id="uploadImage2" />
					        			</div>
					        			<div class="clearfix"></div>
						        	</div>
						        </div>
						        <div class="form-group">
						        	<button type="submit" id="asubmit" class="btn btn-primary col-sm-offset-4" data-loading-text="正在提交"><i class="icon-ok"></i> 提交认证</button>
						        </div>
							</fieldset>
						</form>
					</div>
				</div>
			</div>
		</div>		
		<#include "common/footer-tpl.ftl" />		
	</body>
</html>