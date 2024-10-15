package com.example.Mangxahoi.utils;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Component;

import java.util.Locale;

@Component
@Slf4j
public class EbsI18n {
    private static MessageSource source;

    public EbsI18n(MessageSource messageSource) {
        source = messageSource;
    }

    public static String get(Locale locale, String code, @Nullable Object[] args) {
        try {
            return source.getMessage(code, args, locale);
        } catch (Exception var4) {
            log.info(var4.getMessage());
            return code;
        }
    }

    public static String get(String code, @Nullable Object... args) {
        return get(LocaleContextHolder.getLocale(), code, args);
    }
}
