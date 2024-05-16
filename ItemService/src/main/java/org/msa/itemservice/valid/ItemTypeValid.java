package org.msa.itemservice.valid;

import jakarta.validation.Constraint;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import jakarta.validation.Payload;
import org.msa.itemservice.dto.constant.ItemType;

import java.lang.annotation.*;

@Constraint(validatedBy = ItemTypeValid.ItemTypeValidator.class)
@Documented
@Target({ElementType.METHOD, ElementType.FIELD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
public @interface ItemTypeValid {

    String message() default "허용되지 않은 물품 유형입니다.";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};

    class ItemTypeValidator implements ConstraintValidator<ItemTypeValid, String> {
        @Override
        public boolean isValid(String cd, ConstraintValidatorContext context) {
            boolean hasItemType = false;
            ItemType[] itemTypes = ItemType.values();

            for(ItemType item : itemTypes) {
                hasItemType = item.hasItemCd(cd);

                if(hasItemType) break;
            }

            return hasItemType;
        }
    }

}