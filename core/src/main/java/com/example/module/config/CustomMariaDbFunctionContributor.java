package com.example.module.config;

import org.hibernate.boot.model.FunctionContributions;
import org.hibernate.boot.model.FunctionContributor;

/**
 * querydsl custom function 정의 (spring 3에서 작동)
 */
public class CustomMariaDbFunctionContributor implements FunctionContributor {

    @Override
    public void contributeFunctions(FunctionContributions functionContributions) {
//        BasicType<String> resultType = functionContributions
//                .getTypeConfiguration()
//                .getBasicTypeRegistry()
//                .resolve(StandardBasicTypes.STRING);

        functionContributions
                .getFunctionRegistry()
                .registerPattern("GROUP_CONCAT_CUSTOM", "GROUP_CONCAT(?1 SEPARATOR ?2)");
    }
}
