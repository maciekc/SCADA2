package com.scada.model.dataBase.ChangeParameterValue;

import org.skife.jdbi.v2.SQLStatement;
import org.skife.jdbi.v2.sqlobject.Binder;
import org.skife.jdbi.v2.sqlobject.BinderFactory;
import org.skife.jdbi.v2.sqlobject.BindingAnnotation;

import java.lang.annotation.*;

@BindingAnnotation(ChangeParameterValueBinder.ChangeParameterValueBinderFactory.class)
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.PARAMETER})

public @interface ChangeParameterValueBinder {

    public static class ChangeParameterValueBinderFactory implements BinderFactory {
        @Override
        public Binder build(Annotation annotation) {
            return new Binder<ChangeParameterValueBinder, ChangeParameterValue>() {
                public void bind(SQLStatement q, ChangeParameterValueBinder bind, ChangeParameterValue arg) {

                    q.bind("id", arg.getId());
                    q.bind("systemParameterTag", arg.getSystemParameterName());
                    q.bind("value", arg.getValue());
                    q.bind("date", arg.getDate());
                }
            };
        }
    }

}
