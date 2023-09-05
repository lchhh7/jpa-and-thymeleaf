package com.jinjin.jintranet.notice.service;

import com.jinjin.jintranet.common.FileUtils;
import com.jinjin.jintranet.handler.CustomException;
import com.jinjin.jintranet.handler.ErrorCode;
import com.jinjin.jintranet.handler.ErrorDto;
import com.jinjin.jintranet.holiday.service.HolidayAPIController;
import com.jinjin.jintranet.model.Member;
import com.jinjin.jintranet.model.Notice;
import com.jinjin.jintranet.model.NoticeAttach;
import com.jinjin.jintranet.notice.dto.NoticeSaveDTO;
import com.jinjin.jintranet.notice.dto.NoticeSearchDTO;
import com.jinjin.jintranet.notice.repository.NoticeAttachRepository;
import com.jinjin.jintranet.notice.repository.NoticeDslRepository;
import com.jinjin.jintranet.notice.repository.NoticeRepository;
import com.jinjin.jintranet.security.auth.PrincipalDetail;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.transaction.Transactional;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.List;

import static com.jinjin.jintranet.handler.ErrorCode.INTERNAL_SERVER_ERROR;

@Service
@RequiredArgsConstructor
public class NoticeService {

	private static final Logger LOGGER = LoggerFactory.getLogger(HolidayAPIController.class);
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
	//@Trace
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
			LOGGER.info("notice write encode error : "+e);
		}
		notice.setMember(member);
		notice.setCreatedBy(member.getName());
		//notice.setAttaches(dto.getAttaches().stream().map(m -> m.DtotoEntity()).toList());
		//notice.getAttaches().stream().forEach(m -> m.setNotice(notice));
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
		//notice.setAttaches(dto.getAttaches().stream().map(m -> m.DtotoEntity()).toList());
		//notice.getAttaches().stream().forEach(m -> m.setNotice(notice));
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
	public void download(int id, HttpServletRequest request, HttpServletResponse response) {
		NoticeAttach attach = noticeAttachRepository.findById(id).orElseThrow(() -> {
			return new IllegalArgumentException("공지사항을 찾을 수 없습니다.");
		}); // 영속화

		if (attach != null) {
			FileUtils.download(attach, request, response);
		}
	}

	@Transactional
	public ResponseEntity<String> deleteAttach(int id , Member member) {
			NoticeAttach attach = noticeAttachRepository.findById(id).orElseThrow(() -> {
				return new IllegalArgumentException("공지사항을 찾을 수 없습니다.");
			}); // 영속화
			
			attach.setDeletedBy(member.getName());
			return ResponseEntity.ok().body("첨부파일이 정상적으로 삭제되었습니다.");
	}

}
