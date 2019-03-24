<#import "parts/common.ftl" as c>

<@c.page>

<#if requestsToVisible??>
    <h6>
        Запросы на доступ к просмотру файлов
    </h6>
    <#list requestsToVisible as request>
        <div class="card my-3">
            <div class="card">
                <div class="card-body">
                    <p>${request.username}</p>
                    <span><a href="/confirm-visible-access/${request.id}">Подтвердить</a></span>
                    <span><a href="/refuse-visible-access/${request.id}">Отказать</a></span>
                </div>
            </div>
        </div>
    </#list>
</#if>

<#if requestsToDownload??>
<h6>
    Запросы на доступ к скачиванию файлов
</h6>
    <#list requestsToDownload as request>
    <div class="card my-3">
        <div class="card">
            <div class="card-body">
                <p>${request.username}</p>
                <span><a href="/confirm-download-access/${request.id}">Подтвердить</a></span>
                <span><a href="/refuse-download-access/${request.id}">Отказать</a></span>
            </div>
        </div>
    </div>
    </#list>
</#if>

<#if accessToVisible??>
<h6>
    Открыт доступ к промостру файлов пользователям
</h6>
<#list accessToVisible as access>
    <div class="card my-3">
        <div class="card">
            <div class="card-body">
                <p>${access.username}</p>
                <span><a href="/cancel-visible-access/${access.id}">Отменить доступ</a></span>
            </div>
        </div>
    </div>
</#list>
</#if>

<#if accessToDownload??>
<h6>
Открыт доступ к скачиванию файлов пользователям
</h6>
    <#list accessToDownload as access>
<div class="card my-3">
    <div class="card">
        <div class="card-body">
            <p>${access.username}</p>
            <span><a href="/cancel-download-access/${access.id}">Отменить доступ</a></span>
        </div>
    </div>
</div>
    </#list>
</#if>

</@c.page>