package com.xaxc.teqin.common.model;

import com.auth0.jwt.interfaces.Claim;
import com.fasterxml.jackson.annotation.JsonAlias;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.Field;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * A custom claim for storing custom information in the token.
 * @author sean.zhou
 * @date 2021/11/16
 * @version 0.1
 */
@AllArgsConstructor
@NoArgsConstructor
@Data
@Slf4j
public class CustomClaim {

    /**
     * The id of the account.
     */
    private String id;

    private String userName;

    private String realName;

    private String password;

    /**
     * Convert the custom claim data type to the Map type.
     * @return map
     */
    public ConcurrentHashMap<String, String> convertToMap() {
        ConcurrentHashMap<String, String> map = new ConcurrentHashMap<>(7);
        try {
            Field[] declaredFields = this.getClass().getDeclaredFields();
            for (Field field : declaredFields) {
                JsonAlias annotation = field.getAnnotation(JsonAlias.class);
                field.setAccessible(true);
                // The value of key is named underscore.
                map.put(annotation != null ? annotation.value()[0] : field.getName(),
                        field.get(this).toString());
            }
        } catch (IllegalAccessException e) {
            log.info("CustomClaim converts failed. {}", this.toString());
            e.printStackTrace();
        }
        return map;
    }

    /**
     * Convert the data in Map into a custom claim object.
     * @param claimMap
     */
    public CustomClaim (Map<String, Claim> claimMap) {
        Field[] declaredFields = this.getClass().getDeclaredFields();
        for (Field field : declaredFields) {
            field.setAccessible(true);
            JsonAlias annotation = field.getAnnotation(JsonAlias.class);

            Claim value = claimMap.get(annotation == null ? field.getName() : annotation.value()[0]);
            try {
                Class<?> type = field.getType();
                if (Integer.class.equals(type)) {
                    field.set(this, Integer.valueOf(value.asString()));
                } else if (String.class.equals(type)) {
                    field.set(this, value.asString());
                }
            } catch (IllegalAccessException e) {
                log.info("Claim parses failed. {}", claimMap.toString());
                e.printStackTrace();
            }
        }
    }
}
