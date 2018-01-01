package com.scada.model.dataBase.Controller;

import org.skife.jdbi.v2.SQLStatement;
import org.skife.jdbi.v2.sqlobject.Binder;
import org.skife.jdbi.v2.sqlobject.BinderFactory;
import org.skife.jdbi.v2.sqlobject.BindingAnnotation;

import java.lang.annotation.*;

@BindingAnnotation(ControllerBinder.ControlerBinderFactory.class)
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.PARAMETER})
public @interface ControllerParameterBinder {

    public static class ControlerParameterBinderFactory implements BinderFactory {
        @Override
        public Binder build(Annotation annotation) {
            return new Binder<ControllerParameterBinder, ControllerParameter>() {
                public void bind(SQLStatement q, ControllerParameterBinder bind, ControllerParameter arg) {

                    q.bind("tag", arg.getTag());
                    q.bind("value", arg.getValue());
                }
            };
        }
    }

}