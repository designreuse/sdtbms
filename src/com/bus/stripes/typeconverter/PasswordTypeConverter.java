package com.bus.stripes.typeconverter;

import java.util.Collection;
import java.util.Locale;

import com.bus.util.HRUtil;

import net.sourceforge.stripes.validation.TypeConverter;
import net.sourceforge.stripes.validation.ValidationError;

public class PasswordTypeConverter implements TypeConverter<String> {

	@Override
	public String convert(String input, Class<? extends String> arg1,
			Collection<ValidationError> arg2) {
		return HRUtil.getStringMD5(input);
	}

	@Override
	public void setLocale(Locale arg0) {
	}

}
