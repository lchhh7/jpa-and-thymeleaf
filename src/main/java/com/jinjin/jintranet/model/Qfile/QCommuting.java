package com.jinjin.jintranet.model.Qfile;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.jinjin.jintranet.model.Commuting;
import com.jinjin.jintranet.model.QMember;
import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QCommuting is a Querydsl query type for Commuting
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QCommuting extends EntityPathBase<Commuting> {

    private static final long serialVersionUID = -742216276L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QCommuting commuting = new QCommuting("commuting");

    public final StringPath attendYn = createString("attendYn");

    public final DateTimePath<java.time.LocalDateTime> commutingTm = createDateTime("commutingTm", java.time.LocalDateTime.class);

    public final NumberPath<Integer> id = createNumber("id", Integer.class);

    public final QMember member;

    public QCommuting(String variable) {
        this(Commuting.class, forVariable(variable), INITS);
    }

    public QCommuting(Path<? extends Commuting> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QCommuting(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QCommuting(PathMetadata metadata, PathInits inits) {
        this(Commuting.class, metadata, inits);
    }

    public QCommuting(Class<? extends Commuting> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.member = inits.isInitialized("member") ? new QMember(forProperty("member")) : null;
    }

}

