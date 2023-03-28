package com.jinjin.jintranet.model.Qfile;

import static com.querydsl.core.types.PathMetadataFactory.forVariable;

import javax.annotation.processing.Generated;

import com.jinjin.jintranet.model.DepartmentType;
import com.jinjin.jintranet.model.Member;
import com.jinjin.jintranet.model.PositionType;
import com.jinjin.jintranet.model.RoleType;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.PathMetadata;
import com.querydsl.core.types.dsl.DateTimePath;
import com.querydsl.core.types.dsl.EntityPathBase;
import com.querydsl.core.types.dsl.EnumPath;
import com.querydsl.core.types.dsl.NumberPath;
import com.querydsl.core.types.dsl.StringPath;


/**
 * QMember is a Querydsl query type for Member
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QMember extends EntityPathBase<Member> {

    private static final long serialVersionUID = -1141035419L;

    public static final QMember member = new QMember("member1");

    public final QBaseEntity _super = new QBaseEntity(this);

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

