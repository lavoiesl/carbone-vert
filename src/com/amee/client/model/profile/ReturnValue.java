/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.amee.client.model.profile;

import java.math.BigDecimal;

import com.amee.client.model.base.AmeeConstants;

/**
 * 
 * @author james
 */
public class ReturnValue {

	protected String name;
	protected BigDecimal value;
	protected String unit;
	protected String perUnit;
	protected Boolean defaultvalue;

	public ReturnValue(final String _name, final String _value,
			final String _unit, final String _perUnit,
			final Boolean _defaultvalue) {
		name = _name;
		value = new BigDecimal(_value);
		value = value
				.setScale(AmeeConstants.SCALE, AmeeConstants.ROUNDING_MODE);
		if (value.precision() > AmeeConstants.PRECISION) {
			// TODO: do something
		}
		unit = _unit;
		perUnit = _perUnit;
		defaultvalue = _defaultvalue;
	}

	public ReturnValue(final String _name, final BigDecimal _value,
			final String _unit, final String _perUnit,
			final Boolean _defaultvalue) {
		name = _name;
		value = _value;
		unit = _unit;
		perUnit = _perUnit;
		defaultvalue = _defaultvalue;
	}

	public String getName() {
		return name;
	}

	public BigDecimal getValue() {
		return value;
	}

	public String getUnit() {
		return unit;
	}

	public String getPerUnit() {
		return perUnit;
	}

	public Boolean isDefaultValue() {
		return defaultvalue;
	}

}
