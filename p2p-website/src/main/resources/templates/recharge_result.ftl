<!DOCTYPE html>
<html lang="zh-CN">
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <link rel="stylesheet" href="/css/recharge.css">

    <title>充值</title>
</head>

<style>
    .result {
        height: 400px;
    }

    .page-footer.snccb .tips {
        position: absolute;
        bottom: 0px;
        width: 100%;
    }
</style>
<body>
<header class="page-header navbar navbar-inverse">
    <div class="cline clearfix">
        <span class="c1"></span>
        <span class="c2"></span>
        <span class="c3"></span>
        <span class="c4"></span>
    </div>
    <nav class="container">
        <div class="navbar-header">
            <a class="navbar-toggle collapsed" href="javascript:void(0)" data-toggle="collapse" data-target="#navbar">
                <span class="sr-only"></span>
                <span class="icon-bar"></span>
                <span class="icon-bar"></span>
                <span class="icon-bar"></span>
            </a>
            <a href="javascript:void(0)" class="navbar-brand">
                <img src="/images/logo.png" alt="" style="margin-top: 10px">
            </a>
            <span class="splite"></span>

            <span class="navbar-des">充值结果</span>

        </div>

    </nav>
</header>
<section class="container result">
    <hr>
    <div class="row">
        <h1 class="text-alert">
            <img src="/images/alert.png" alt="">
            <#if ro.state == 0>充值结果处理中,请耐心等待.</#if>
            <#if ro.state == 1>恭喜您充值成功,充值金额：${ro.amount}</#if>
            <#if ro.state == 2>很遗憾充值失败,失败原因:${ro.errorMsg}</#if>
        </h1>

        <p>充值可能会有一定时间的延迟,请稍等片刻</p>
    </div>
    <div class="wave"></div>
</section>

<footer class="page-footer snccb" style="margin-top: 20px;">

    <div class="tips">
        <div class="container">
            <p>
                温馨提示：<a href='http://www.wolfcode.cn' target="_blank">叩丁狼</a>股份有限公司仅提供资金存管服务，此为测试项目,不做任何商业用途
            </p>
            <p>
                <span> 版权所有：广州狼码教育科技有限公司</span>
            </p>
        </div>
    </div>
</footer>
</body>
</html>