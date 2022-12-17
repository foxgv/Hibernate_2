package org.javarush.domain;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;
import java.time.Year;

@Converter(autoApply = true)
public class YearAttributeConverter implements AttributeConverter<Year, Short> {

    @Override
    public Short convertToDatabaseColumn(Year year) {
        if (year != null) {
            return (short) year.getValue();
        }
        return null;
    }

    @Override
    public Year convertToEntityAttribute(Short aShort) {
        if(aShort != null) {
            return Year.of(aShort);
        }
        return null;
    }
}
