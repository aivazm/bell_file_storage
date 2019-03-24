<#assign
known = Session.SPRING_SECURITY_CONTEXT??
>

<#if known>
    <#assign
    user = Session.SPRING_SECURITY_CONTEXT.authentication.principal
    name = user.getUsername()
    isAdmin = user.isAdmin()
    isAnalyst = user.isAnalyst()
    currentUserId = user.getId()
    >
<#else>
    <#assign
    <#--user = Session.SPRING_SECURITY_CONTEXT.authentication.principal-->
    name = "unknown"
    isAdmin = false
    isAnalyst = false
    currentUserId = -1
    >
</#if>