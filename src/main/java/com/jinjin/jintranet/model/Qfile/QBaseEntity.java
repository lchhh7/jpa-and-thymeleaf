package com.jinjin.jintranet.model.Qfile;

import static com.querydsl.core.types.PathMetadataFactory.forVariable;

import javax.annotation.processing.Generated;

import com.jinjin.jintranet.model.BaseEntity;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.PathMetadata;
import com.querydsl.core.types.dsl.DateTimePath;
import com.querydsl.core.types.dsl.EntityPathBase;
import com.querydsl.core.types.dsl.StringPath;


/**
 * QBaseEntity is a Querydsl query type for BaseEntity
 */
@Generated("com.querydsl.codegen.DefaultSupertypeSerializer")
public class QBaseEntity extends EntityPathBase<BaseEntity> {

    private static final long serialVersionUID = 174619743L;

    public static final QBaseEntity baseEntity = new QBaseEntity("baseEntity");

    public final StringPath createdBy = createString("createdBy");

    public final DateTimePath<java.time.LocalDateTime> crtDt = createDateTime("crtDt", java.time.LocalDateTime.class);

    public final StringPath deletedBy = createString("deletedBy");

    public final StringPath ModifiedBy = createString("ModifiedBy");

    public final StringPath modifiedBy = createString("modifiedBy");

    public final DateTimePath<java.time.LocalDateTime> udtDt = createDateTime("udtDt", java.time.LocalDateTime.class);

    public QBaseEntity(String variable) {
        super(BaseEntity.class, forVariable(variable));
    }

    public QBaseEntity(Path<? extends BaseEntity> path) {
        super(path.getType(), path.getMetadata());
    }

    public QBaseEntity(PathMetadata metadata) {
        super(BaseEntity.class, metadata);
    }

}

