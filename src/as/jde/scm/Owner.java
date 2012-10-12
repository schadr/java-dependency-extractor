package as.jde.scm;

public class Owner
{
	String email;
	float ownership;

	public Owner() {
	}

	public Owner(String email, float ownership) {
		this.email = email;
		this.ownership = ownership;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public float getOwnership() {
		return ownership;
	}

	public void setOwnership(float ownership) {
		this.ownership = ownership;
	}
}
