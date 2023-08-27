package com.google.cms.utilities.requests_dto;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class LoginRequest {

	@NotBlank
	private String email;

	@NotBlank
	private String password;
}
