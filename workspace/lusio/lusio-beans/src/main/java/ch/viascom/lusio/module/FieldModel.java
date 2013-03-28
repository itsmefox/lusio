package ch.viascom.lusio.module;

import ch.viascom.lusio.entity.Field;
import lombok.Data;

@Data
public class FieldModel {
	private String field_ID;

	private String color;

	private String value;
	
	public FieldModel(Field field){
		field_ID = field.getField_ID();
		color = field.getColor();
		value = field.getValue();
	}
}
