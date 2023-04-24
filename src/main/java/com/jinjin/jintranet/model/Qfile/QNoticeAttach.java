package com.jinjin.jintranet.model.Qfile;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.jinjin.jintranet.model.NoticeAttach;
import com.jinjin.jintranet.model.QBaseEntity;
import com.jinjin.jintranet.model.QNotice;
import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QNoticeAttach is a Querydsl query type for NoticeAttach
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QNoticeAttach extends EntityPathBase<NoticeAttach> {

    private static final long serialVersionUID = -766882104L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QNoticeAttach noticeAttach = new QNoticeAttach("noticeAttach");

    public final com.jinjin.jintranet.model.QBaseEntity _super = new QBaseEntity(this);

    //inherited
    public final StringPath createdBy = _super.createdBy;

    //inherited
    public final DateTimePath<java.time.LocalDateTime> crtDt = _super.crtDt;

    //inherited
    public final StringPath deletedBy = _super.deletedBy;

    public final NumberPath<Long> fileSize = createNumber("fileSize", Long.class);

    public final NumberPath<Integer> id = createNumber("id", Integer.class);

    //inherited
    public final StringPath ModifiedBy = _super.ModifiedBy;

    //inherited
    public final StringPath modifiedBy = _super.modifiedBy;

    public final com.jinjin.jintranet.model.QNotice notice;

    public final StringPath originalFileName = createString("originalFileName");

    public final StringPath path = createString("path");

    public final StringPath storedFileName = createString("storedFileName");

    //inherited
    public final DateTimePath<java.time.LocalDateTime> udtDt = _super.udtDt;

    public QNoticeAttach(String variable) {
        this(NoticeAttach.class, forVariable(variable), INITS);
    }

    public QNoticeAttach(Path<? extends NoticeAttach> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QNoticeAttach(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QNoticeAttach(PathMetadata metadata, PathInits inits) {
        this(NoticeAttach.class, metadata, inits);
    }

    public QNoticeAttach(Class<? extends NoticeAttach> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.notice = inits.isInitialized("notice") ? new QNotice(forProperty("notice"), inits.get("notice")) : null;
    }

}

