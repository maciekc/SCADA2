package com.scada.model.dataBase.Notification;
import org.skife.jdbi.v2.SQLStatement;
import org.skife.jdbi.v2.sqlobject.Binder;
import org.skife.jdbi.v2.sqlobject.BinderFactory;
import org.skife.jdbi.v2.sqlobject.BindingAnnotation;

import java.lang.annotation.*;

@BindingAnnotation(NotificationBinder.NotificationBinderFactory.class)
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.PARAMETER})
public @interface NotificationBinder {

    public static class NotificationBinderFactory implements BinderFactory {
        @Override
        public Binder build(Annotation annotation) {
            return new Binder<NotificationBinder, Notification>() {
                public void bind(SQLStatement q, NotificationBinder bind, Notification arg) {

                    q.bind("id", arg.getId());
                    q.bind("name", arg.getName());
                    q.bind("limit_name", arg.getLimitName());
                    q.bind("value", arg.getValue());
                    q.bind("date", arg.getDate());
                    q.bind("type", arg.getType());
                }
            };
        }
    }

}