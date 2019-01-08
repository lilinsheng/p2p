<thead id="gridHead">
    <tr>
        <th>借款</th>
        <th>剩余期数</th>
        <th>年化利率</th>
        <th>认购金额</th>
        <th>最近还款日</th>
        <th>获取利息</th>
        <th width="80px">操作</th>
    </tr>
</thead>
<tbody id="gridBody">
    <#if pageResult.listData?size &gt; 0 >
        <#list pageResult.listData as item>
            <tr>
                <th>${item.bidRequestTitle}</th>
                <th>${item.remainMonthIndex}</th>
                <th>${item.currentRate!""}%</th>
                <th>${item.bidRequestAmount}</th>
                <th>${item.closestDeadLine?string("yyyy-MM-dd")}</th>
                <th>${item.remainInterest}</th>
                <td><a class="btn btn-danger btn-sm transfer_subscribe" href="javascript:;" data-did="${item.id}">认购</a></td>
            </tr>
        </#list>
    <#else>
        <tr>
            <td colspan="7" align="center">
                <p class="text-danger">目前没有符合要求的标</p>
            </td>
        </tr>
    </#if>
</tbody>

<script type="text/javascript">
    $(function(){
        $("#page_container").empty().append($('<ul id="pagination" class="pagination"></ul>'));
        $("#pagination").twbsPagination({
            totalPages:${pageResult.totalPage},
            startPage:${pageResult.currentPage},
            initiateStartPageClick:false,
            onPageClick : function(event, page) {
                $("#currentPage").val(page);
                $("#searchForm").submit();
            }
        });
        
        $(".transfer_subscribe").click(function(){
        	$.ajax({
        		dataType:"json",
        		data:{id:$(this).data("did")},
        		url:"/subscribe",
        		success:function(data){
        		    if(data.success){
        		        $.messager.confirm("提示","认购成功",function () {
                            $("#searchForm").submit();
                        })
                    }else {
        		        $.messager.alert("提示",data.msg);
                    }
        		}
        	});
        })
    });
</script>