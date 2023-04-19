package com.jinjin.jintranet.member.repository;

import com.jinjin.jintranet.member.dto.MemberInterface;
import com.jinjin.jintranet.model.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member, Integer>{
	Optional<Member> findByMemberId(String memberId);


	@Query(value="SELECT m.id , m.name FROM Member m WHERE role='ADMIN' and m.deletedBy is null", nativeQuery = true)
	List<MemberInterface> approves();


	@Query(value="select * from Member m where m.deletedBy is null" , nativeQuery = true)
	List<Member> findWorkers();


	@Query(value="select * from Member m where m.deletedBy is null and m.memberId= :memberId" , nativeQuery = true)
	Member findOAuthById(@Param("memberId") String memberId);
}
