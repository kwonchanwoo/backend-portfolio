package com.example.module.converter;

import com.example.module.entity.Board;
import com.example.module.entity.Member;
import com.example.module.repository.MemberCoreRepository;
import com.example.module.util.CommonException;
import com.example.module.util._Enum.ErrorCode;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.type.TypeFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;


@Component
public class MemberConverter implements
        Converter<String, Member>,
        com.fasterxml.jackson.databind.util.Converter<String, Member> {
    private final MemberCoreRepository repository;

    public MemberConverter(@Qualifier("memberCoreRepository") MemberCoreRepository repository) {
        this.repository = repository;
    }

    @Override
    public Member convert(String id) {
        return repository.findById(Long.parseLong(id)).orElseThrow(() -> new CommonException(ErrorCode.ACCESS_DENIED));
    }

    @Override
    public JavaType getInputType(TypeFactory typeFactory) {
        return typeFactory.constructType(String.class);
    }

    @Override
    public JavaType getOutputType(TypeFactory typeFactory) {
        return typeFactory.constructType(Board.class);
    }
}
