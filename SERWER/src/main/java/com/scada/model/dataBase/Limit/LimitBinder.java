package com.scada.model.dataBase.Limit;

import org.skife.jdbi.v2.SQLStatement;
import org.skife.jdbi.v2.sqlobject.Binder;
import org.skife.jdbi.v2.sqlobject.BinderFactory;
import org.skife.jdbi.v2.sqlobject.BindingAnnotation;

import java.lang.annotation.*;

@BindingAnnotation(LimitBinder.LimitBinderFactory.class)
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.PARAMETER})
public @interface LimitBinder {

    public static class LimitBinderFactory implements BinderFactory {
        @Override
        public Binder build(Annotation annotation) {
            return new Binder<LimitBinder, Limit>() {
                public void bind(SQLStatement q, LimitBinder bind, Limit arg) {

                    q.bind("id", arg.getId());
                    q.bind("tag", arg.getTag());
                    q.bind("name", arg.getName());
                    q.bind("stateSpaceTag", arg.getStateSpaceTag());
                    q.bind("value", arg.getValue());
                    q.bind("type", arg.getType());
                }
            };
        }
    }

}