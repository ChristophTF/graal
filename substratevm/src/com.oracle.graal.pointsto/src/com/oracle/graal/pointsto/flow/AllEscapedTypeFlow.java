package com.oracle.graal.pointsto.flow;

import com.oracle.graal.pointsto.PointsToAnalysis;
import com.oracle.graal.pointsto.meta.AnalysisType;
import com.oracle.graal.pointsto.typestate.TypeState;

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
    public boolean addState(PointsToAnalysis bb, TypeState add, boolean postFlow) {
        boolean anyany = false;

        for (AnalysisType type : add.types(bb)) {
            TypeState typeState = TypeState.forExactType(bb, type, true);
            TypeState typeStateNonNull = TypeState.forExactType(bb, type, false);

            var ref = new Object() {
                boolean any = false;
            };

            type.forAllSuperTypes(t -> {
                ref.any |= t.escapedTypes.superAddState(bb, typeState);
                ref.any |= t.escapedTypesNonNull.superAddState(bb, typeStateNonNull);
            });

            anyany |= ref.any;
        }

        return anyany;
    }

    boolean superAddState(PointsToAnalysis bb, TypeState add) {
        return super.addState(bb, add, true);
    }
}
