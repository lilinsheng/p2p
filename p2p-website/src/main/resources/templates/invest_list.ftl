<thead id="gridHead">
<tr>
    <th>借款人</th>
    <th width="180px">借款标题</th>
    <th>年利率</th>
    <th>金额</th>
    <th>还款方式</th>
    <th>进度</th>
    <th width="80px">操作</th>
</tr>
</thead>
<tbody id="gridBody">
    <#if pageResult.listData?size &gt; 0 >
        <#list pageResult.listData as data>
            <tr>
                <td>${data.createUser.username }</td>
                <td>${data.title}</td>
                <td class="text-info">${data.currentRate}%</td>
                <td class="text-info">${data.bidRequestAmount}</td>
                <td>${data.returnTypeDisplay}</td>
                <td>
                    <div class="">
                        ${data.persent} %
                    </div>
                </td>
                <td><a class="btn btn-danger btn-sm"
                       href="/borrow_info?id=${data.id}">查看</a></td>
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
    $(function () {
        $("#page_container").empty().append($('<ul id="pagination" class="pagination"></ul>'));
        $("#pagination").twbsPagination({
            totalPages:${pageResult.totalPage},
            startPage:${pageResult.currentPage},
            initiateStartPageClick: false,
            onPageClick: function (event, page) {
                $("#currentPage").val(page);
                $("#searchForm").submit();
            }
        });
    });
</script>