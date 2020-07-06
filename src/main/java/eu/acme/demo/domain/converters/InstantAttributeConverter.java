package eu.acme.demo.domain.converters;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;
import java.time.Instant;
import java.util.Date;

/**
 * Created by lambros on 29/9/2018.
 */

@Converter(autoApply = true)
public class InstantAttributeConverter implements AttributeConverter<Instant, Date> {

    @Override
    public Date convertToDatabaseColumn(Instant attribute) {
        if (attribute == null)
            return null;
        return new Date(attribute.toEpochMilli());
    }

    @Override
    public Instant convertToEntityAttribute(Date dbData) {
        if (dbData == null)
            return null;
        return Instant.ofEpochMilli(dbData.getTime());
    }
}
