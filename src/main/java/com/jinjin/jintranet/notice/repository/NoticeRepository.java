package com.jinjin.jintranet.notice.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.jinjin.jintranet.model.Notice;

public interface NoticeRepository extends JpaRepository<Notice, Integer>{
}
