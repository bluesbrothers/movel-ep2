package fefzjon.ep2.bandejao.utils;

public class BandexComment {
	private String	commenter;
	private String	message;

	public String getCommenter() {
		return this.commenter;
	}

	public void setCommenter(final String commenter) {
		this.commenter = commenter;
	}

	public String getMessage() {
		return this.message;
	}

	public void setMessage(final String message) {
		this.message = message;
	}

	@Override
	public String toString() {
		return this.commenter + ": " + this.message;
	}
}
