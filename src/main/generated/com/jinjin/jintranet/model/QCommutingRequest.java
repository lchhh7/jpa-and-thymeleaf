package com.jinjin.jintranet.model;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QCommutingRequest is a Querydsl query type for CommutingRequest
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QCommutingRequest extends EntityPathBase<CommutingRequest> {

    private static final long serialVersionUID = 1763861315L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QCommutingRequest commutingRequest = new QCommutingRequest("commutingRequest");

    public final QBaseEntity _super = new QBaseEntity(this);

    public final QMember approve;

    public final DateTimePath<java.time.LocalDateTime> approveDt = createDateTime("approveDt", java.time.LocalDateTime.class);

    public final StringPath content = createString("content");

    //inherited
    public final StringPath createdBy = _super.createdBy;

    //inherited
    public final DateTimePath<java.time.LocalDateTime> crtDt = _super.crtDt;

    //inherited
    public final StringPath deletedBy = _super.deletedBy;

    public final NumberPath<Integer> id = createNumber("id", Integer.class);

    public final QMember member;

    //inherited
    public final StringPath ModifiedBy = _super.ModifiedBy;

    //inherited
    public final StringPath modifiedBy = _super.modifiedBy;

    public final StringPath requestDt = createString("requestDt");

    public final StringPath requestTm = createString("requestTm");

    public final StringPath status = createString("status");

    public final StringPath type = createString("type");

    //inherited
    public final DateTimePath<java.time.LocalDateTime> udtDt = _super.udtDt;

    public QCommutingRequest(String variable) {
        this(CommutingRequest.class, forVariable(variable), INITS);
    }

    public QCommutingRequest(Path<? extends CommutingRequest> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QCommutingRequest(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QCommutingRequest(PathMetadata metadata, PathInits inits) {
        this(CommutingRequest.class, metadata, inits);
    }

    public QCommutingRequest(Class<? extends CommutingRequest> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.approve = inits.isInitialized("approve") ? new QMember(forProperty("approve")) : null;
        this.member = inits.isInitialized("member") ? new QMember(forProperty("member")) : null;
    }

}

