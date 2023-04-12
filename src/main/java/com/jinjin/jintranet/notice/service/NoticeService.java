package com.jinjin.jintranet.notice.service;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.transaction.Transactional;

import com.jinjin.jintranet.aop.Trace;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.jinjin.jintranet.common.FileUtils;
import com.jinjin.jintranet.model.Member;
import com.jinjin.jintranet.model.Notice;
import com.jinjin.jintranet.model.NoticeAttach;
import com.jinjin.jintranet.notice.dto.NoticeSaveDTO;
import com.jinjin.jintranet.notice.dto.NoticeSearchDTO;
import com.jinjin.jintranet.notice.repository.NoticeAttachRepository;
import com.jinjin.jintranet.notice.repository.NoticeDslRepository;
import com.jinjin.jintranet.notice.repository.NoticeRepository;
import com.jinjin.jintranet.security.auth.PrincipalDetail;

@Service
@RequiredArgsConstructor
public class NoticeService {
	private final NoticeDslRepository noticeDslRepository;

	private final NoticeRepository noticeRepository;

	private final NoticeAttachRepository noticeAttachRepository;

	@Transactional
	public List<Notice> findAll() {
		return noticeRepository.findAll();
	}

	@Transactional
	public Notice findById(Integer id) {
		return noticeRepository.findById(id).orElseThrow(() -> {
			return new IllegalArgumentException("해당 공지사항을 조회하는중 오류가 발생했습니다");
		});
	}

	@Transactional
	@Trace
	public Page<NoticeSearchDTO> findNotices(Pageable pabeable , String keyword , String searchType) {
		return noticeDslRepository.findNotices(pabeable , keyword , searchType);
	}

	@Transactional
	public String write(NoticeSaveDTO dto, Member member) {
		Notice notice = new Notice();
		try {
			notice.setContent(URLDecoder.decode(dto.getContent(), "UTF-8"));
			notice.setTitle(URLDecoder.decode(dto.getTitle(), "UTF-8"));
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		notice.setMember(member);
		notice.setCreatedBy(member.getName());
		notice.setAttaches(dto.getAttaches().stream().map(m -> m.DtotoEntity()).toList());
		notice.getAttaches().stream().forEach(m -> m.setNotice(notice));
		noticeRepository.save(notice);
		return String.valueOf(notice.getId());
	}

	@Transactional
	public String edit(NoticeSaveDTO dto, PrincipalDetail principal) {
		Notice notice = noticeRepository.findById(dto.getId()).orElseThrow(() -> {
			return new IllegalArgumentException("공지사항을 찾을 수 없습니다.");
		}); // 영속화

		notice.setTitle(dto.getTitle());
		notice.setContent(dto.getContent());
		notice.setModifiedBy(principal.getMember().getName());
		notice.setAttaches(dto.getAttaches().stream().map(m -> m.DtotoEntity()).toList());
		notice.getAttaches().stream().forEach(m -> m.setNotice(notice));
		return String.valueOf(dto.getId());
	}

	@Transactional
	public void delete(NoticeSaveDTO dto, Member member) {
		Notice notice = noticeRepository.findById(dto.getId()).orElseThrow(() -> {
			return new IllegalArgumentException("공지사항을 찾을 수 없습니다.");
		}); // 영속화
		notice.setDeletedBy(member.getName());
	}

	@Transactional
	public void download(int id, HttpServletRequest request, HttpServletResponse response) throws Exception {
		NoticeAttach attach = noticeAttachRepository.findById(id).orElseThrow(() -> {
			return new IllegalArgumentException("공지사항을 찾을 수 없습니다.");
		}); // 영속화

		if (attach != null) {
			FileUtils.download(attach, request, response);
		}
	}

	@Transactional
	public ResponseEntity<String> deleteAttach(int id , Member member) throws Exception {
		try {
			NoticeAttach attach = noticeAttachRepository.findById(id).orElseThrow(() -> {
				return new IllegalArgumentException("공지사항을 찾을 수 없습니다.");
			}); // 영속화
			
			attach.setDeletedBy(member.getName());
			return new ResponseEntity<>("첨부파일이 정상적으로 삭제되었습니다." ,HttpStatus.OK);
		} catch (Exception e) {
			e.printStackTrace();
			return new ResponseEntity<>("첨부파일 삭제 중 오류가 발생했습니다.", HttpStatus.CONFLICT);
		}
	}

}
