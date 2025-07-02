package io.github.iamrenny.ruleflow.evaluators;

import io.github.iamrenny.ruleflow.RuleFlowLanguageParser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class GeoOperationContext {
    private static final Logger logger = LoggerFactory.getLogger(GeoOperationContext.class);

    public Object evaluate(RuleFlowLanguageParser.GeoOperationContext ctx, io.github.iamrenny.ruleflow.visitors.Visitor visitor) {
        Object result;
        if (ctx.geoExpr() instanceof io.github.iamrenny.ruleflow.RuleFlowLanguageParser.GeohashEncodeContext) {
            result = evaluateGeohashEncode((io.github.iamrenny.ruleflow.RuleFlowLanguageParser.GeohashEncodeContext) ctx.geoExpr(), visitor);
        } else if (ctx.geoExpr() instanceof io.github.iamrenny.ruleflow.RuleFlowLanguageParser.GeohashDecodeContext) {
            result = evaluateGeohashDecode((io.github.iamrenny.ruleflow.RuleFlowLanguageParser.GeohashDecodeContext) ctx.geoExpr(), visitor);
        } else if (ctx.geoExpr() instanceof io.github.iamrenny.ruleflow.RuleFlowLanguageParser.DistanceContext) {
            result = evaluateDistance((io.github.iamrenny.ruleflow.RuleFlowLanguageParser.DistanceContext) ctx.geoExpr(), visitor);
        } else if (ctx.geoExpr() instanceof io.github.iamrenny.ruleflow.RuleFlowLanguageParser.DistanceGeohashContext) {
            result = evaluateDistanceGeohash((io.github.iamrenny.ruleflow.RuleFlowLanguageParser.DistanceGeohashContext) ctx.geoExpr(), visitor);
        } else if (ctx.geoExpr() instanceof io.github.iamrenny.ruleflow.RuleFlowLanguageParser.WithinRadiusContext) {
            result = evaluateWithinRadius((io.github.iamrenny.ruleflow.RuleFlowLanguageParser.WithinRadiusContext) ctx.geoExpr(), visitor);
        } else {
            throw new IllegalArgumentException("Unknown geo operation: " + ctx.getText());
        }
        logger.debug("GeoOperation: ctx={}, result={}", ctx.getText(), result);
        return result;
    }

    private Object evaluateGeohashEncode(io.github.iamrenny.ruleflow.RuleFlowLanguageParser.GeohashEncodeContext ctx, io.github.iamrenny.ruleflow.visitors.Visitor visitor) {
        double lat = ((Number) visitor.visit(ctx.lat)).doubleValue();
        double lon = ((Number) visitor.visit(ctx.lon)).doubleValue();
        int precision = ctx.precision != null ? ((Number) visitor.visit(ctx.precision)).intValue() : 12;
        String result = io.github.iamrenny.ruleflow.utils.GeoUtils.encodeGeohash(lat, lon, precision);
        logger.debug("geohash_encode({}, {}, {}) -> {}", lat, lon, precision, result);
        return result;
    }

    private Object evaluateGeohashDecode(io.github.iamrenny.ruleflow.RuleFlowLanguageParser.GeohashDecodeContext ctx, io.github.iamrenny.ruleflow.visitors.Visitor visitor) {
        String geohash = visitor.visit(ctx.geohash).toString();
        double[] result = io.github.iamrenny.ruleflow.utils.GeoUtils.decodeGeohash(geohash);
        logger.debug("geohash_decode({}) -> [{}, {}]", geohash, result[0], result[1]);
        return result;
    }

    private Object evaluateDistance(io.github.iamrenny.ruleflow.RuleFlowLanguageParser.DistanceContext ctx, io.github.iamrenny.ruleflow.visitors.Visitor visitor) {
        double lat1 = ((Number) visitor.visit(ctx.lat1)).doubleValue();
        double lon1 = ((Number) visitor.visit(ctx.lon1)).doubleValue();
        double lat2 = ((Number) visitor.visit(ctx.lat2)).doubleValue();
        double lon2 = ((Number) visitor.visit(ctx.lon2)).doubleValue();
        double result = io.github.iamrenny.ruleflow.utils.GeoUtils.distance(lat1, lon1, lat2, lon2);
        logger.debug("distance({}, {}, {}, {}) -> {}", lat1, lon1, lat2, lon2, result);
        return result;
    }

    private Object evaluateDistanceGeohash(io.github.iamrenny.ruleflow.RuleFlowLanguageParser.DistanceGeohashContext ctx, io.github.iamrenny.ruleflow.visitors.Visitor visitor) {
        String geohash1 = visitor.visit(ctx.geohash1).toString();
        String geohash2 = visitor.visit(ctx.geohash2).toString();
        double result = io.github.iamrenny.ruleflow.utils.GeoUtils.distance(geohash1, geohash2);
        logger.debug("distance({}, {}) -> {}", geohash1, geohash2, result);
        return result;
    }

    private Object evaluateWithinRadius(io.github.iamrenny.ruleflow.RuleFlowLanguageParser.WithinRadiusContext ctx, io.github.iamrenny.ruleflow.visitors.Visitor visitor) {
        double lat1 = ((Number) visitor.visit(ctx.lat1)).doubleValue();
        double lon1 = ((Number) visitor.visit(ctx.lon1)).doubleValue();
        double lat2 = ((Number) visitor.visit(ctx.lat2)).doubleValue();
        double lon2 = ((Number) visitor.visit(ctx.lon2)).doubleValue();
        double radius = ((Number) visitor.visit(ctx.radius)).doubleValue();
        boolean result = io.github.iamrenny.ruleflow.utils.GeoUtils.withinRadius(lat1, lon1, lat2, lon2, radius);
        logger.debug("within_radius({}, {}, {}, {}, {}) -> {}", lat1, lon1, lat2, lon2, radius, result);
        return result;
    }
} 