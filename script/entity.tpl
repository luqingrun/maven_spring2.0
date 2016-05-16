package com.gongsibao.module.{{module_name}}.{{pack_name}}.entity;

import java.util.Date;


public class {{class_name}} extends com.gongsibao.common.db.BaseEntity {

    private static final long serialVersionUID = -1L;

    {% for column in columns %}{% if 'PRI' not in column.COLUMN_KEY %}
    /** {{ column.f_comment }} */
    private {{ column.f_type }} {{ column.f_name }};
    {% endif %}{% endfor %}

    {% for column in columns %}{% if 'PRI' not in column.COLUMN_KEY %}
    public {{ column.f_type }} get{{ column.f_setget_name }}() {
        return {{ column.f_name }};
    }

    public void set{{ column.f_setget_name }}({{ column.f_type }} {{ column.f_name }}) {
        this.{{ column.f_name }} = {{ column.f_name }};
    }
    {% endif %}{% endfor %}

}