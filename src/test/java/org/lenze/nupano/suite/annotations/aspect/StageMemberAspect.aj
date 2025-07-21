package org.lenze.nupano.suite.annotations.aspect;

import org.aspectj.lang.JoinPoint;
import org.lenze.nupano.suite.annotations.StageMember;
import org.lenze.nupano.suite.helper.SuiteProperties;
import org.lenze.nupano.suite.helper.SuiteStage;

public privileged aspect StageMemberAspect extends SuiteStage {

    pointcut stageMemberAnnotatedMethod() :
        execution(@org.lenze.nupano.suite.annotations.StageMember * *(..));

    before() : stageMemberAnnotatedMethod() {
        SuiteProperties.activeStage = stageUI();
        SuiteProperties.activeStage.shineSpotlightOn("Derek");
    }
}