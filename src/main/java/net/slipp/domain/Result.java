package net.slipp.domain;

public class Result {
	private boolean valid;
	
	private String errorMessage;

	public String getErrorMessage() {
		return errorMessage;
	}

	private Result(boolean valid,String errorMessage) {
		this.valid=valid;
		this.errorMessage = errorMessage;
	}
	
	public boolean isValid() {
		return valid;
	}
	
	public static Result ok() {
		return new Result(true,null);
	}
	
	public static Result fail(String errorMessage) {
		return new Result(false,errorMessage);
	}
}
