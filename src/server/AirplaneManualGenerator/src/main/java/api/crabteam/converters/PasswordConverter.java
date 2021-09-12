package api.crabteam.converters;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

import api.crabteam.utils.Password;


@Converter
public class PasswordConverter implements AttributeConverter<Password, String> {

	@Override
	public String convertToDatabaseColumn(Password attribute) {
		return attribute.toString();
	}

	@Override
	public Password convertToEntityAttribute(String dbData) {
		return new Password(dbData);
	}

}