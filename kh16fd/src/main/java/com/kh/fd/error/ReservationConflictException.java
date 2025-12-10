package com.kh.fd.error;

public class ReservationConflictException extends RuntimeException{
	private static final long serialVersionUID = 1L;

	public ReservationConflictException() {
		super();
	}

	public ReservationConflictException(String message) {
		super(message);
	}

}
