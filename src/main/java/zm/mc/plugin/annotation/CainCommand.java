package zm.mc.plugin.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.bukkit.permissions.PermissionDefault;


@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public  @interface CainCommand {
    String name();
    PermissionDefault permissionDefault();
    String permisstionDescription() default "";
}
