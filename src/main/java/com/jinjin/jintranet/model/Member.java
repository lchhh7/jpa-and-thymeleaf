package com.jinjin.jintranet.model;

import lombok.*;

import javax.persistence.*;
import java.io.Serializable;


@Entity
@Getter @Setter
@NoArgsConstructor(access = AccessLevel.PUBLIC)
@ToString(of={"id" , "memberId" , "name"})
public class Member extends BaseEntity implements Serializable {
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Integer id;
	
	@Column(nullable = false, length =30 , unique = true)
	private String memberId;

	@Column(unique = true)
	private String oauthId;

	private String password;
	
	private String name;
	
	private String phoneNo;
	private String mobileNo;

	@Enumerated(EnumType.STRING)
	private PositionType position;
	
	@Enumerated(EnumType.STRING)
	private DepartmentType department;

	private String useColor;
	
	// 시큐리티 ROLE 용
	@Enumerated(EnumType.STRING)
	private RoleType role;

	/*@JsonIgnore
	@OneToMany(mappedBy = "member" , fetch = FetchType.LAZY)
	private List<CommutingRequest> commutingRequests;*/

	
	@Builder
	public Member(String memberId, String password, String name, String phoneNo, String mobileNo,
			PositionType position, DepartmentType department, String useColor, RoleType role) {
		this.memberId = memberId;
		this.password = password;
		this.name = name;
		this.phoneNo = phoneNo;
		this.mobileNo = mobileNo;
		this.position = position;
		this.department = department;
		this.useColor = useColor;
		this.role = role;
	}

	/*public void add(CommutingRequest commutingRequest) {
		commutingRequest.setMember(this);
		this.getCommutingRequests().add(commutingRequest);
	}
	public void delete(int id) {
		this.getCommutingRequests().stream().filter(m ->m.getId() ==id)
				.collect(Collectors.toList())
				.forEach(li -> this.getCommutingRequests().remove(li));
	}
	public void approve(int id, CommuteApproveDTO approveDTO) {
		this.getCommutingRequests().stream().filter(m -> m.getId() == id).forEach(m -> m.setStatus(approveDTO.getStatus()));
	}*/
}