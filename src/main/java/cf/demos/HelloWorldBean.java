package cf.demos;

public class HelloWorldBean {
	private String message;
	private String value;

	public HelloWorldBean(String message, String value) {
		super();
		this.message = message;
		this.value = value;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	@Override
	public String toString() {
		return "HelloWorldBean [message=" + message + ", value=" + value + "]";
	}
}
