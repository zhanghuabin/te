<#list items as item>
${item.name} for ${item.price}RMB. <#if item.price < 50>low<#else>high</#if>${item.doSomething()}
</#list>
