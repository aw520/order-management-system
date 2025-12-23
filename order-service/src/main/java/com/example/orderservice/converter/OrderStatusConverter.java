package com.example.orderservice.converter;

import com.example.orderservice.constant.OrderStatus;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter(autoApply = true)
public class OrderStatusConverter
        implements AttributeConverter<OrderStatus, Integer> {

    @Override
    public Integer convertToDatabaseColumn(OrderStatus status) {
        return status == null ? null : status.getDbValue();
    }

    @Override
    public OrderStatus convertToEntityAttribute(Integer dbValue) {
        return OrderStatus.getFromDbValue(dbValue);
    }
}
