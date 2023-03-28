package com.jinjin.jintranet.model.Qfile;

import static com.querydsl.core.types.PathMetadataFactory.forVariable;

import javax.annotation.processing.Generated;

import com.jinjin.jintranet.model.Holiday;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.PathMetadata;
import com.querydsl.core.types.dsl.DatePath;
import com.querydsl.core.types.dsl.EntityPathBase;
import com.querydsl.core.types.dsl.NumberPath;
import com.querydsl.core.types.dsl.StringPath;


/**
 * QHoliday is a Querydsl query type for Holiday
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QHoliday extends EntityPathBase<Holiday> {

    private static final long serialVersionUID = -869335571L;

    public static final QHoliday holiday = new QHoliday("holiday");

    public final DatePath<java.time.LocalDate> holidayDt = createDate("holidayDt", java.time.LocalDate.class);

    public final NumberPath<Integer> id = createNumber("id", Integer.class);

    public final StringPath title = createString("title");

    public QHoliday(String variable) {
        super(Holiday.class, forVariable(variable));
    }

    public QHoliday(Path<? extends Holiday> path) {
        super(path.getType(), path.getMetadata());
    }

    public QHoliday(PathMetadata metadata) {
        super(Holiday.class, metadata);
    }

}

