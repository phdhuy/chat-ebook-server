package com.chatebook.ai.payload.response;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.*;

import java.util.List;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class GenerateMindMapResponse {

    private List<Node> nodes;
    private List<Edge> edges;

    /**
     * Represents each JSON object under "nodes": e.g.
     * {
     *   "data": { "label": "Deployment", "page": null },
     *   "id": "root",
     *   "position": { "x": 0, "y": 0 },
     *   "type": "input"
     * }
     */
    @Getter
    @Setter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
    public static class Node {
        private Data data;
        private String id;
        private Position position;
        private String type;
    }

    /**
     * Nested inside Node: holds "label" and "page":
     * {
     *   "label": "Deployment",
     *   "page": 8
     * }
     */
    @Getter
    @Setter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
    public static class Data {
        private String label;
        private Integer page;
    }

    /**
     * Nested inside Node: holds "x" and "y":
     * {
     *   "x": 0,
     *   "y": 0
     * }
     */
    @Getter
    @Setter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
    public static class Position {
        private Integer x;
        private Integer y;
    }

    /**
     * Represents each JSON object under "edges": e.g.
     * {
     *   "animated": false,
     *   "id": "eroot-node1",
     *   "source": "root",
     *   "target": "node1",
     *   "type": "smoothstep"
     * }
     */
    @Getter
    @Setter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
    public static class Edge {
        private Boolean animated;
        private String id;
        private String source;
        private String target;
        private String type;
    }
}
