package dam.ad.dto.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface RowField {
    int columnLength() default 0;
    String label() default "";
    String numericFormat() default "";
    String expression() default "";
    boolean asComplex() default false;
}
