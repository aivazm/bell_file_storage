<#import "parts/common.ftl" as c>

<@c.page>

<div>
    <#include "parts/security.ftl">

    <#if isAnalyst>
        <div>
            <h7>
                Всего пользователей в системе: ${numberOfUsers}
            </h7>
        </div>
    </#if>

    <#list users as user, numberOfFiles>
        <div class="card my-3">
                <#if user??>
                    <div class="card">
                        <div class="card-body">
                            <span><a href="/user-files/${user.id}">${user.username}</a></span>
                            <#if isAnalyst>
                                <p>Количество файлов у пользователя: ${numberOfFiles}</p>
                            </#if>
                        </div>
                        <div>
                            <#if currentUserId != user.id && !isAnalyst && !isAdmin>
                                <a href="/request-visible-access/${user.id}">Request Visible Access</a>
                            </#if>
                        </div>
                        <div>
                            <#if currentUserId != user.id && !isAdmin>
                                <a href="/request-download-access/${user.id}">Request Download Access</a>
                            </#if>
                        </div>
                    </div>
                </#if>
        </div>
    <#else>
        No users
</#list>
</div>
</@c.page>