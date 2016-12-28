<#include "../common/header.ftl">




<div class="column50Left">
    <h3>User Details</h3>

    <table class="table table-striped">
        <tr><td>Id</td><td>${userInfo.id}</td></tr>
        <tr><td>Name</td><td>${userInfo.name}</td></tr>
        <#if userInfo.link??>
        <tr><td>Link</td><td><a href="${userInfo.link}" _target="_new">Google Plus</a></td></tr>
        </#if>

        </table>


    </div>
<#include "../common/footer.ftl">