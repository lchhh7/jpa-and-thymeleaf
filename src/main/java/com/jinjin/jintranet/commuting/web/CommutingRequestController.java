package com.jinjin.jintranet.commuting.web;


import com.jinjin.jintranet.commuting.dto.AdminCommuteRequestViewDTO;
import com.jinjin.jintranet.commuting.dto.CommuteRequestViewDTO;
import com.jinjin.jintranet.commuting.service.CommutingRequestService;
import com.jinjin.jintranet.security.auth.PrincipalDetail;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.Comparator;
import java.util.List;


@Controller
@RequiredArgsConstructor
public class CommutingRequestController {

	private final CommutingRequestService commutingRequestService;

	@GetMapping("/commuting/searching.do")
	public ResponseEntity<List<CommuteRequestViewDTO>> searching (
			@RequestParam(value ="st", required = false , defaultValue = "") String st,
			@RequestParam(value ="y", required = false , defaultValue ="") String y,
			@AuthenticationPrincipal PrincipalDetail principal) {
		List<CommuteRequestViewDTO> list = commutingRequestService.commutingRequestSearching(principal.getMember() , st , y)
				.stream().sorted(Comparator.comparing(CommuteRequestViewDTO::getRequestDt , Comparator.reverseOrder())
						.thenComparing(CommuteRequestViewDTO::getCrtDt)).toList();

		return ResponseEntity.ok().body(list);
	}

	@GetMapping("/commuting/request/{id}.do")
	public ResponseEntity<AdminCommuteRequestViewDTO> requesting(@PathVariable("id") int id) {
		return ResponseEntity.ok().body(commutingRequestService.findById(id));
	}

	@PutMapping(value="/commuting/editRequest/{id}.do")
	public ResponseEntity<String> editRequest(
			@PathVariable("id") int id ,
			@RequestBody AdminCommuteRequestViewDTO dto) {
			commutingRequestService.EditRequest(id,dto);
			return ResponseEntity.ok().body("정상적으로 처리되었습니다.");
	}

	@DeleteMapping(value="/commuting/deleteRequest/{id}.do")
	public ResponseEntity<String> deleteRequest(
			@PathVariable("id") int id ,
			@AuthenticationPrincipal PrincipalDetail principal) {
			commutingRequestService.DeleteRequest(id , principal.getMember());
			return ResponseEntity.ok().body("정상적으로 처리되었습니다.");
	}
}
