/**
 * This file is part of the AMEE Java Client Library.
 *
 * Copyright (c) 2008 AMEE UK Ltd. (http://www.amee.com)
 *
 * The AMEE Java Client Library is free software, released under the MIT
 * license. See mit-license.txt for details.
 */

package com.amee.client;

public class AmeeException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 2203333319209151761L;

	public AmeeException() {
		super();
	}

	public AmeeException(final String message) {
		super(message);
	}

	public AmeeException(final String message, final Throwable cause) {
		super(message, cause);
	}

	public AmeeException(final Throwable cause) {
		super(cause);
	}
}
