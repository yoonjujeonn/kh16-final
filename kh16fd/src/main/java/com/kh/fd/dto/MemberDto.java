package com.kh.fd.dto;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data @Builder @NoArgsConstructor @AllArgsConstructor
@ToString(exclude = {"memberPw"})
@JsonIgnoreProperties(ignoreUnknown = true)
public class MemberDto {
	
	@NotBlank
	@Pattern(regexp = "^[a-z][a-z0-9]{4,19}$")
	private String memberId;
	
	@NotBlank
	@Pattern(regexp = "(?=.*?[A-Z])(?=.*?[a-z])(?=.*?[0-9])(?=.*?[\\\\!\\\\@\\\\#\\\\$])[A-Za-z0-9\\\\!\\\\@\\\\#\\\\$]{8,16}$")
	private String memberPw;

	
	@NotBlank
	@Pattern(regexp = "^[가-힣0-9]{2,10}$")
	private String memberNickname;
	
	@Pattern(regexp = "^(19[0-9]{2}|20[0-9]{2})-((02-(0[1-9]|1[0-9]|2[0-9]))|((0[469]|11)-(0[1-9]|1[0-9]|2[0-9]|30))|((0[13578]|1[02])-(0[1-9]|1[0-9]|2[0-9]|3[01])))$")
	private String memberBirth;
	
	@Pattern(regexp = "^010[1-9][0-9]{7}$")
	private String memberContact;
	
	@NotBlank
	@Email
	private String memberEmail;
	
	private String memberLevel;

	
	@Pattern(regexp = "^[0-9]{5,6}$")
	private String memberPost;
	//나머지 주소는 조건이 없음
	@Size(max = 100)
	private String memberAddress1;
	@Size(max = 100)
	private String memberAddress2;
	//Timestamp 대신 LocalDateTime을 써도 무방하다 (mybatis가 자동변환)
	
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss.SSS")
	private LocalDateTime memberJoin;
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss.SSS")
	private LocalDateTime memberLogin;
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss.SSS")
	private LocalDateTime memberChange;	
	
	private Integer memberPoint;
}
