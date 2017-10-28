package com.scada.model.dataBase.Andon;

import org.skife.jdbi.v2.SQLStatement;
import org.skife.jdbi.v2.sqlobject.Binder;
import org.skife.jdbi.v2.sqlobject.BinderFactory;
import org.skife.jdbi.v2.sqlobject.BindingAnnotation;

import java.lang.annotation.*;

@BindingAnnotation(AndonBinder.AndonBinderFactory.class)
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.PARAMETER})
public @interface AndonBinder {

    public static class AndonBinderFactory implements BinderFactory {
        @Override
        public Binder build(Annotation annotation) {
            return new Binder<AndonBinder, Andon>() {
                public void bind(SQLStatement q, AndonBinder bind, Andon arg) {

                    q.bind("id", arg.getId());
                    q.bind("limitTag", arg.getLimitName());
                    q.bind("stateSpaceTag", arg.getStateSpaceName());
                    q.bind("value", arg.getValue());
                    q.bind("date", arg.getDate());
                }
            };
        }
    }

}
