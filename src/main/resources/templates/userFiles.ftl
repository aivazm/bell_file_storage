<#import "parts/common.ftl" as c>

<@c.page>
    <#include "parts/security.ftl">
    <#list files as file, downloadCount>

        <#if isCurrentUserOrAdmin>
            <div class="card my-3">
                <#if file??>
                    <div class="card">
                        <div class="card-body">
                            <p>${file.fileName}</p>
                            <#if isAnalyst>
                                <p>Количество скачиваний файла: ${downloadCount}</p>
                            </#if>
                            <span><a href="/download-file/${file.id}" download>Download</a></span>
                            <span><a href="/delete-file/${file.id}">Delete</a></span>
                        </div>
                    </div>
                </#if>
            </div>
        <#elseif isDownloadAccess>
            <div class="card my-3">
                <#if file??>
                    <div class="card">
                        <div class="card-body">
                            <p>${file.fileName}</p>
                            <#if isAnalyst>
                                <p>Количество скачиваний файла: ${downloadCount}</p>
                            </#if>
                            <span><a href="/download-file/${file.id}" download>Download</a></span>
                        </div>
                    </div>
                </#if>
            </div>
        <#elseif isVisibleAccessOrAnalyst>
            <div class="card my-3">
                <#if file??>
                    <div class="card">
                        <div class="card-body">
                            <p>${file.fileName}</p>
                            <#if isAnalyst>
                                <p>Количество скачиваний файла: ${downloadCount}</p>
                            </#if>
                        </div>
                    </div>
                </#if>
            </div>
        </#if>

    <#else>
        No files
    </#list>
</@c.page>