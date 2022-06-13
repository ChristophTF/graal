package com.oracle.graal.pointsto.flow;

import com.oracle.graal.pointsto.PointsToAnalysis;
import com.oracle.graal.pointsto.meta.AnalysisType;

public final class AllEscapedTypeFlow extends TypeFlow<AnalysisType> {

    public AllEscapedTypeFlow(AnalysisType declaredType, boolean canBeNull) {
        super(declaredType, declaredType, canBeNull);
    }

    @Override
    public TypeFlow<AnalysisType> copy(PointsToAnalysis bb, MethodFlowsGraph methodFlows) {
        return this;
    }

    @Override
    public boolean canSaturate() {
        return false;
    }

    @Override
    public String toString() {
        return "AllEscaped" + super.toString();
    }
}
