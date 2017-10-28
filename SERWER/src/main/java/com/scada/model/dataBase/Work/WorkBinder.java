package com.scada.model.dataBase.Work;

import org.skife.jdbi.v2.SQLStatement;
import org.skife.jdbi.v2.sqlobject.Binder;
import org.skife.jdbi.v2.sqlobject.BinderFactory;
import org.skife.jdbi.v2.sqlobject.BindingAnnotation;

import java.lang.annotation.*;

@BindingAnnotation(WorkBinder.WorkBinderFactory.class)
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.PARAMETER})
public @interface WorkBinder {

    public static class WorkBinderFactory implements BinderFactory {
        @Override
        public Binder build(Annotation annotation) {
            return new Binder<WorkBinder, Work>() {
                public void bind(SQLStatement q, WorkBinder bind, Work arg) {

                    q.bind("id", arg.getId());
                    q.bind("stateSpaceTag", arg.getStateSpaceName());
                    q.bind("value", arg.getValue());
                    q.bind("date", arg.getDate());
                }
            };
        }
    }

}