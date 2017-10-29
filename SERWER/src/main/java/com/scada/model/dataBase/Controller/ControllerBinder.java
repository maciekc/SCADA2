package com.scada.model.dataBase.Controller;

import org.skife.jdbi.v2.SQLStatement;
import org.skife.jdbi.v2.sqlobject.Binder;
import org.skife.jdbi.v2.sqlobject.BinderFactory;
import org.skife.jdbi.v2.sqlobject.BindingAnnotation;

import java.lang.annotation.*;

@BindingAnnotation(ControllerBinder.ControlerBinderFactory.class)
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.PARAMETER})
public @interface ControllerBinder {

    public static class ControlerBinderFactory implements BinderFactory {
        @Override
        public Binder build(Annotation annotation) {
            return new Binder<ControllerBinder, Controller>() {
                public void bind(SQLStatement q, ControllerBinder bind, Controller arg) {

                    q.bind("id", arg.getId());
                    q.bind("variableStateTag", arg.getVariableStateTag());
                    q.bind("stateSpaceTag", arg.getStateSpaceTag());
                    q.bind("value", arg.getValue());
                    q.bind("date", arg.getDate());
                }
            };
        }
    }

}
