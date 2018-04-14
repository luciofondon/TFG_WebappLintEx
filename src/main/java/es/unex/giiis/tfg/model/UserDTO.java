package es.unex.giiis.tfg.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "USER_WEB")
public class UserDTO extends BaseDTO {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "ID")
	private int id;

	@Column(name = "ENABLED")
	private int enabled;

	@Column(name = "LAST_NAME")
	private String lastName;

	@Column(name = "NAME")
	private String name;

	@Column(name = "NICK")
	private String nick;

	@Column(name = "PASSWORD")
	private String password;

	@Column(name = "THEME")
	private String theme;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getEnabled() {
		return enabled;
	}

	public void setEnabled(int enabled) {
		this.enabled = enabled;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getNick() {
		return nick;
	}

	public void setNick(String nick) {
		this.nick = nick;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getTheme() {
		return theme;
	}

	public void setTheme(String theme) {
		this.theme = theme;
	}

	@Override
	public String toString() {
		return "UserDTO [id=" + id + ", enabled=" + enabled + ", lastName=" + lastName + ", name=" + name + ", nick="
				+ nick + ", password=" + password + ", theme=" + theme + "]\n";
	}

}
