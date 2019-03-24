<#import "parts/common.ftl" as c>

<@c.page>

<a class="btn btn-primary" data-toggle="collapse" href="#collapseExample" role="button" aria-expanded="false"
   aria-controls="collapseExample">
    Add new File
</a>
<div class="collapse" id="collapseExample">
    <div class="form-group mt-3">
        <form method="post" enctype="multipart/form-data">
            <div class="form-group">
                <div class="custom-file">
                    <input type="file" name="file" id="customFile">
                    <label class="custom-file-label" for="customFile">Choose file</label>
                </div>
            </div>
            <input type="hidden" name="_csrf" value="${_csrf.token}"/>
            <div class="form-group">
                <button type="submit" class="btn btn-primary">Добавить</button>
            </div>
        </form>
    </div>
</div>

    <#include "parts/security.ftl">
        <#if numberOfRequests?? && numberOfRequests gt 0>
        <div>
            <span><a href="/accessAndRequestPage/">Открытый доступ к моим файлам (${numberOfRequests} новых запросов)</a></span>
        </div>
        <#else>
        <div>
            <span><a href="/accessAndRequestPage/">Открытый доступ к моим файлам</a></span>
        </div>
        </#if>

        <#list files as file>
            <#if file.owner.id == currentUserId>
                <div class="card my-3">
                    <#if file??>
                        <div class="card">
                            <div class="card-body">
                                <p>${file.fileName}</p>
                                <span><a href="/download-file/${file.id}">Download</a></span>
                                <span><a href="/delete-file/${file.id}">Delete</a></span>
                            </div>
                        </div>
                    </#if>
                </div>
            </#if>
        <#else>
            No files
        </#list>

</@c.page>