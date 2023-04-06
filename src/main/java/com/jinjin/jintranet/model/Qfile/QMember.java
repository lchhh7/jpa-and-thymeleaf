package com.jinjin.jintranet.model.Qfile;

import com.jinjin.jintranet.model.QBaseEntity;
import com.jinjin.jintranet.model.*;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.PathMetadata;
import com.querydsl.core.types.dsl.*;

import javax.annotation.processing.Generated;

import static com.querydsl.core.types.PathMetadataFactory.forVariable;


/**
 * QMember is a Querydsl query type for Member
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QMember extends EntityPathBase<Member> {

    private static final long serialVersionUID = -1141035419L;

    public static final QMember member = new QMember("member1");

    public final com.jinjin.jintranet.model.QBaseEntity _super = new QBaseEntity(this);

    public final ListPath<CommutingRequest, QCommutingRequest> commutingRequests = this.<CommutingRequest, QCommutingRequest>createList("commutingRequests", CommutingRequest.class, QCommutingRequest.class, PathInits.DIRECT2);

    //inherited
    public final StringPath createdBy = _super.createdBy;

    //inherited
    public final DateTimePath<java.time.LocalDateTime> crtDt = _super.crtDt;

    //inherited
    public final StringPath deletedBy = _super.deletedBy;

    public final EnumPath<DepartmentType> department = createEnum("department", DepartmentType.class);

    public final NumberPath<Integer> id = createNumber("id", Integer.class);

    public final StringPath memberId = createString("memberId");

    public final StringPath mobileNo = createString("mobileNo");

    //inherited
    public final StringPath ModifiedBy = _super.ModifiedBy;

    //inherited
    public final StringPath modifiedBy = _super.modifiedBy;

    public final StringPath name = createString("name");

    public final StringPath oauthId = createString("oauthId");

    public final StringPath password = createString("password");

    public final StringPath phoneNo = createString("phoneNo");

    public final EnumPath<PositionType> position = createEnum("position", PositionType.class);

    public final EnumPath<RoleType> role = createEnum("role", RoleType.class);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> udtDt = _super.udtDt;

    public final StringPath useColor = createString("useColor");

    public QMember(String variable) {
        super(Member.class, forVariable(variable));
    }

    public QMember(Path<? extends Member> path) {
        super(path.getType(), path.getMetadata());
    }

    public QMember(PathMetadata metadata) {
        super(Member.class, metadata);
    }

}

