package ch.viascom.lusio.module;

import lombok.Data;

@Data
public class AccountModel {
	private String id;
	private String firstname;
	private String lastname;
	private String email;
	private double credit;

}
