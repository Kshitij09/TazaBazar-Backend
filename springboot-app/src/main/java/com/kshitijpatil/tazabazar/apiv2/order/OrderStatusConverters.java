package com.kshitijpatil.tazabazar.apiv2.order;

import org.springframework.context.annotation.Configuration;
import org.springframework.core.convert.converter.Converter;
import org.springframework.data.convert.ReadingConverter;
import org.springframework.data.convert.WritingConverter;
import org.springframework.data.jdbc.core.convert.JdbcCustomConversions;
import org.springframework.data.jdbc.repository.config.AbstractJdbcConfiguration;

import java.util.List;
import java.util.Locale;

@Configuration
public class OrderStatusConverters extends AbstractJdbcConfiguration {

    @Override
    public JdbcCustomConversions jdbcCustomConversions() {
        return new JdbcCustomConversions(List.of(OrderStatusToString.INSTANCE, StringToOrderStatus.INSTANCE));
    }

    @WritingConverter
    enum OrderStatusToString implements Converter<OrderStatus, String> {
        INSTANCE;

        @Override
        public String convert(OrderStatus source) {
            return source.getLabel();
        }
    }

    @ReadingConverter
    enum StringToOrderStatus implements Converter<String, OrderStatus> {
        INSTANCE;

        private String labelToName(String label) {
            return label.toUpperCase(Locale.ROOT);
        }

        @Override
        public OrderStatus convert(String source) {
            return OrderStatus.valueOf(labelToName(source));
        }
    }
}
