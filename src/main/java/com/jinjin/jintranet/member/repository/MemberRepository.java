package com.jinjin.jintranet.member.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.jinjin.jintranet.member.dto.MemberInterface;
import com.jinjin.jintranet.model.Member;

public interface MemberRepository extends JpaRepository<Member, Integer>{
	Optional<Member> findByMemberId(String memberId);
	
	@Query(value="SELECT m.id , m.name FROM Member m WHERE role='ADMIN' and m.deletedBy is null", nativeQuery = true)
	List<MemberInterface> approves();
	
	@Query(value="select * from Member m where deletedBy is null" , nativeQuery = true)
	List<Member> findWorkers();
	
	@Query(value="select * from Member m where deletedBy is null and memberId= ?1" , nativeQuery = true)
	Member findOAuthById(String memberId);
}
