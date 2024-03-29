<#ftl>
<#list selectorList as x>
    (
    ${itemNameToTableColumnMap?api.get(x.item.name)}
    <#switch x.logicType>
        <#case 'IS'>
            =
            <#switch x.dataType>
                <#case 'NUMBER'> ${x.simpleValue} <#break>
                <#case 'STRING'> '${x.simpleValue?replace("'", "\\'")}' <#break>
                <#case 'DATE'> '${x.simpleValue?string['yyyy-MM-dd']}' <#break>
                <#case 'DATETIME'> '${x.simpleValue?string['yyyy-MM-dd HH:MM:ss']}' <#break>
            </#switch>
            <#break>
        <#case 'NOT'>
            <>
            <#switch x.dataType>
                <#case 'NUMBER'> ${x.simpleValue} <#break>
                <#case 'STRING'> '${x.simpleValue?replace("'", "\\'")}' <#break>
                <#case 'DATE'> '${x.simpleValue?string['yyyy-MM-dd']}' <#break>
                <#case 'DATETIME'> '${x.simpleValue?string['yyyy-MM-dd HH:MM:ss']}' <#break>
            </#switch>
            <#break >
        <#case 'IN_RANGE'>
            BETWEEN
            <#switch x.dataType>
                <#case 'NUMBER'> ${x.rangeValue.requiredA} <#break>
                <#case 'STRING'> '${x.rangeValue.requiredA?replace("'", "\\'")}' <#break>
                <#case 'DATE'> '${x.rangeValue.requiredA?string['yyyy-MM-dd']}' <#break>
                <#case 'DATETIME'> '${x.rangeValue.requiredA?string['yyyy-MM-dd HH:MM:ss']}' <#break>
            </#switch>
            AND
            <#switch x.dataType>
                <#case 'NUMBER'> ${x.rangeValue.requiredB} <#break>
                <#case 'STRING'> '${x.rangeValue.requiredB?replace("'", "\\'")}' <#break>
                <#case 'DATE'> '${x.rangeValue.requiredB?string['yyyy-MM-dd']}' <#break>
                <#case 'DATETIME'> '${x.rangeValue.requiredB?string['yyyy-MM-dd HH:MM:ss']}' <#break>
            </#switch>
            <#break >
        <#case 'NOT_IN_RANGE'>
            NOT BETWEEN
            <#switch x.dataType>
                <#case 'NUMBER'> ${x.rangeValue.requiredA} <#break>
                <#case 'STRING'> '${x.rangeValue.requiredA?replace("'", "\\'")}' <#break>
                <#case 'DATE'> '${x.rangeValue.requiredA?string['yyyy-MM-dd']}' <#break>
                <#case 'DATETIME'> '${x.rangeValue.requiredA?string['yyyy-MM-dd HH:MM:ss']}' <#break>
            </#switch>
            AND
            <#switch x.dataType>
                <#case 'NUMBER'> ${x.rangeValue.requiredB} <#break>
                <#case 'STRING'> '${x.rangeValue.requiredB?replace("'", "\\'")}' <#break>
                <#case 'DATE'> '${x.rangeValue.requiredB?string['yyyy-MM-dd']}' <#break>
                <#case 'DATETIME'> '${x.rangeValue.requiredB?string['yyyy-MM-dd HH:MM:ss']}' <#break>
            </#switch>
            <#break >
        <#case 'IN_SET'>
            IN
            (
            <#list x.setValue as e>
                <#switch x.dataType>
                    <#case 'NUMBER'> ${e}<#if e?has_next>, </#if> <#break>
                    <#case 'STRING'> '${e?replace("'", "\\'")}'<#if e?has_next>, </#if> <#break>
                    <#case 'DATE'> '${e?string['yyyy-MM-dd']}'<#if e?has_next>, </#if> <#break>
                    <#case 'DATETIME'> '${e?string['yyyy-MM-dd HH:MM:ss']}'<#if e?has_next>, </#if> <#break>
                </#switch>
            </#list>
            )
            <#break >
        <#case 'NOT_IN_SET'>
            NOT IN
            (
            <#list x.setValue as e>
                <#switch x.dataType>
                    <#case 'NUMBER'> ${e}<#if e?has_next>, </#if> <#break>
                    <#case 'STRING'> '${e?replace("'", "\\'")}'<#if e?has_next>, </#if> <#break>
                    <#case 'DATE'> '${e?string['yyyy-MM-dd']}'<#if e?has_next>, </#if> <#break>
                    <#case 'DATETIME'> '${e?string['yyyy-MM-dd HH:MM:ss']}'<#if e?has_next>, </#if> <#break>
                </#switch>
            </#list>
            )
            <#break >
    </#switch>
    )
    <#if x?has_next> AND </#if>
</#list>