package ch.viascom.lusio.module;

import ch.viascom.lusio.entity.User;
import lombok.Data;

@Data
public class AccountModel {
	private String id;
	private String firstname;
	private String lastname;
	private String email;
	private double credit;

	public AccountModel(User user) {
		id = user.getUser_ID();
		firstname = user.getFirst_Name();
		lastname = user.getLast_Name();
		email = user.getEMail_Address();
	}
}
